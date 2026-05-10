@file:Suppress("UnstableApiUsage")

package dev.slne.clan.paper.commands.subcommands

import com.github.shynixn.mccoroutine.folia.launch
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.clan.update.ClanNameAndTag
import dev.slne.clan.api.clan.updateClanNameAndTag
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.clan.paper.permission.ClanPermissions
import dev.slne.clan.paper.plugin
import dev.slne.surf.api.core.messages.Colors
import dev.slne.surf.api.core.messages.adventure.text
import dev.slne.surf.api.paper.command.executors.playerExecutorSuspend
import dev.slne.surf.api.paper.dialog.*
import dev.slne.surf.clan.core.client.components.Components
import dev.slne.surf.transaction.api.currency.Currency
import dev.slne.surf.transaction.api.transaction.TransactionResult
import dev.slne.surf.transaction.api.transaction.data.TransactionData
import dev.slne.surf.transaction.api.user.transactionUser
import io.papermc.paper.registry.data.dialog.DialogBase
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext

private const val RENAME_NAME_KEY = "rename_name"
private const val RENAME_TAG_KEY = "rename_tag"

fun CommandAPICommand.clanRenameCommand() = subcommand("rename") {
    withPermission(ClanPermissions.CLAN_RENAME_COMMAND)

    playerExecutorSuspend { player, _ ->
        val clan = Clan.byPlayer(player.uniqueId) ?: throw CommandAPI.failWithString("Du bist in keinem Clan.")

        if (!clan.hasMemberPermission(player.uniqueId, ClanPermission.RENAME)) {
            throw CommandAPI.failWithString("Du hast keine Berechtigung, den Clan umzubenennen.")
        }

        // TODO: Increase the price with every rename in the future
        player.showDialog(renameClanDialog(Clan.CLAN_RENAME_COST, clan.name, clan.tag))
    }
}

private fun renameClanDialog(price: Double, currentName: String, currentTag: String) = dialog {
    base {
        title { primary("Clan umbenennen") }
        afterAction(DialogBase.DialogAfterAction.WAIT_FOR_RESPONSE)

        body {
            plainMessage {
                info("Benenne deinen Clan um oder ändere den Tag.")
                appendNewline()
                appendNewline()
                warning("Egal ob du nur den Namen, nur den Tag oder beides änderst –")
                appendNewline()
                warning("die Kosten bleiben gleich.")
                appendNewline()
                appendNewline()
                warning("Kosten: ")
                append(Currency.default().format(price))
            }

            input {
                text(RENAME_NAME_KEY) {
                    label { primary("Neuer Name") }
                    initial(currentName)
                    maxLength(Clan.MAX_NAME_LENGTH)
                }
                text(RENAME_TAG_KEY) {
                    label { primary("Neuer Tag") }
                    initial(currentTag)
                    maxLength(Clan.MAX_TAG_LENGTH)
                }
            }
        }
    }

    type {
        confirmation {
            no {
                action {
                    showDialog(
                        noticeDialog(
                            text("Abgebrochen", Colors.ERROR),
                            text("Du hast das Umbenennen des Clans abgebrochen.", Colors.INFO)
                        )
                    )
                }
            }

            yes {
                action {
                    customPlayerClick { response, player ->
                        val newName = response.getText(RENAME_NAME_KEY)
                        val newTag = response.getText(RENAME_TAG_KEY)

                        val nameChanged = newName != null && newName != currentName
                        val tagChanged = newTag != null && newTag != currentTag

                        if (!nameChanged && !tagChanged) {
                            player.showDialog(
                                noticeDialog(
                                    text("Keine Änderungen", Colors.ERROR),
                                    text("Du hast weder Namen noch Tag geändert.", Colors.INFO)
                                )
                            )
                            return@customPlayerClick
                        }

                        plugin.launch {
                            val clan = Clan.byPlayer(player.uniqueId) ?: run {
                                player.showDialog(
                                    noticeDialog(
                                        text("Fehler", Colors.ERROR),
                                        text("Du bist nicht mehr in einem Clan.", Colors.INFO)
                                    )
                                )
                                return@launch
                            }

                            var renameSucceeded = false
                            var paymentSucceeded = false

                            try {
                                val updateResult = clan.updateClanNameAndTag {
                                    if (nameChanged) name(newName)
                                    if (tagChanged) tag(newTag)
                                }

                                if (!updateResult.success) {
                                    player.showDialog(buildUpdateErrorDialog(updateResult, newName, newTag))
                                    return@launch
                                }

                                renameSucceeded = true

                                val paymentResult = player.transactionUser().withdraw(
                                    amount = price.toBigDecimal(),
                                    currency = Currency.default(),
                                    additionalData = arrayOf(
                                        TransactionData.of(
                                            "clan_rename",
                                            "From tag '$currentTag' and name '$currentName' to tag '$newTag' and name '$newName'"
                                        )
                                    )
                                )

                                if (!paymentResult.success) {
                                    player.showDialog(buildPaymentErrorDialog(paymentResult, price))
                                    return@launch
                                }

                                paymentSucceeded = true

                                player.showDialog(
                                    buildSuccessDialog(
                                        currentName,
                                        newName,
                                        nameChanged,
                                        currentTag,
                                        newTag,
                                        tagChanged
                                    )
                                )
                            } finally {
                                // Only reset if the renaming was successful but the payment didn't go through
                                if (renameSucceeded && !paymentSucceeded) {
                                    withContext(NonCancellable) {
                                        clan.updateClanNameAndTag {
                                            name(currentName)
                                            tag(currentTag)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun buildUpdateErrorDialog(
    result: ClanNameAndTag.UpdateResult,
    newName: String?,
    newTag: String?
) = noticeDialogWithBuilder(text("Umbenennen fehlgeschlagen", Colors.ERROR)) {
    when (result) {
        ClanNameAndTag.UpdateResult.NameAlreadyTaken -> {
            error("Der Name ")
            variableValue(newName ?: "???")
            error(" ist bereits vergeben.")
        }

        ClanNameAndTag.UpdateResult.TagAlreadyTaken -> {
            error("Der Tag ")
            variableValue(newTag ?: "???")
            error(" ist bereits vergeben.")
        }

        ClanNameAndTag.UpdateResult.TagOrNameAlreadyTaken -> {
            error("Der Name oder der Tag ist bereits vergeben.")
        }

        ClanNameAndTag.UpdateResult.NothingChanged -> {
            error("Es wurde nichts geändert.")
        }

        is ClanNameAndTag.UpdateResult.ValidationFailed -> {
            append(Components.Clan.renderClanValidation(result.result))
        }

        else -> error("Ein unbekannter Fehler ist aufgetreten.")
    }
}

private fun buildPaymentErrorDialog(result: TransactionResult, price: Double) =
    noticeDialogWithBuilder(text("Bezahlung fehlgeschlagen", Colors.ERROR)) {
        if (result == TransactionResult.ReceiverInsufficientFunds) {
            error("Du hast nicht genug Geld.")
            appendNewline()
            info("Benötigt: ")
            append(Currency.default().format(price))
        } else {
            error("Ein interner Fehler bei der Bezahlung ist aufgetreten.")
        }
    }

private fun buildSuccessDialog(
    currentName: String,
    newName: String?,
    nameChanged: Boolean,
    currentTag: String,
    newTag: String?,
    tagChanged: Boolean
) = noticeDialogWithBuilder(text("Clan umbenannt", Colors.INFO)) {
    success("Dein Clan wurde erfolgreich umbenannt.")

    if (nameChanged) {
        appendNewline()
        info("Name: ")
        variableValue(currentName)
        info(" -> ")
        variableValue(newName ?: "???")
    }

    if (tagChanged) {
        appendNewline()
        info("Tag: ")
        variableValue(currentTag)
        info(" -> ")
        variableValue(newTag ?: "???")
    }
}