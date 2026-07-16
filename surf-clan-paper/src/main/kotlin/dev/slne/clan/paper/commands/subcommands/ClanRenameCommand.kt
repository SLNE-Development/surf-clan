@file:Suppress("UnstableApiUsage")

package dev.slne.clan.paper.commands.subcommands

import com.github.shynixn.mccoroutine.folia.launch
import com.google.common.flogger.StackSize
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
import dev.slne.surf.api.core.util.logger
import dev.slne.surf.api.paper.command.executors.playerExecutorSuspend
import dev.slne.surf.api.paper.dialog.*
import dev.slne.surf.clan.core.client.components.Components
import dev.slne.surf.transaction.api.currency.Currency
import dev.slne.surf.transaction.api.transaction.PendingTransactionResult
import dev.slne.surf.transaction.api.transaction.data.TransactionData
import dev.slne.surf.transaction.api.transactional.PendingExecutionDecision
import dev.slne.surf.transaction.api.transactional.PendingExecutionResult
import dev.slne.surf.transaction.api.transactional.PendingRollbackPolicy
import dev.slne.surf.transaction.api.user.transactionUser
import io.papermc.paper.registry.data.dialog.DialogBase
import org.bukkit.entity.Player

private const val RENAME_NAME_KEY = "rename_name"
private const val RENAME_TAG_KEY = "rename_tag"

private val log = logger()

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
                info("Ändere den Namen, den Tag oder beides für deinen Clan.")
                appendNewline()
                appendNewline()
                warning("Für die Umbenennung fallen immer dieselben Kosten an,")
                appendNewline()
                warning("unabhängig davon, was du änderst.")
                appendNewline()
                appendNewline()
                warning("Kosten: ")
                append(Currency.default().format(price))
            }

            input {
                text(RENAME_NAME_KEY) {
                    label { text("Neuer Name") }
                    initial(currentName)
                    maxLength(Clan.MAX_NAME_LENGTH)
                }
                text(RENAME_TAG_KEY) {
                    label { text("Neuer Tag") }
                    initial(currentTag)
                    maxLength(Clan.MAX_TAG_LENGTH)
                }
            }
        }
    }

    type {
        confirmation {
            no {
                label { error("Abbrechen") }
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
                label { success("Umbenennen") }
                action {
                    customPlayerClick { response, player ->
                        val newName = response.getText(RENAME_NAME_KEY)?.trim()
                        val newTag = response.getText(RENAME_TAG_KEY)?.trim()

                        handleRename(newName, currentName, newTag, currentTag, player, price)
                    }
                }
            }
        }
    }
}

private fun handleRename(
    newName: String?,
    currentName: String,
    newTag: String?,
    currentTag: String,
    player: Player,
    price: Double
) {
    val nameChanged = newName != null && newName != currentName
    val tagChanged = newTag != null && newTag != currentTag

    if (!nameChanged && !tagChanged) {
        player.showDialog(
            noticeDialog(
                text("Keine Änderungen", Colors.ERROR),
                text("Du hast weder Namen noch Tag geändert.", Colors.INFO)
            )
        )
        return
    }

    plugin.launch {
        doRename(player, price, currentTag, currentName, newTag, newName, nameChanged, tagChanged)
    }
}

private suspend fun doRename(
    player: Player,
    price: Double,
    currentTag: String,
    currentName: String,
    newTag: String?,
    newName: String?,
    nameChanged: Boolean,
    tagChanged: Boolean
) {
    val clan = Clan.byPlayer(player.uniqueId) ?: run {
        player.showDialog(
            noticeDialog(
                text("Fehler", Colors.ERROR),
                text("Du bist nicht mehr in einem Clan.", Colors.INFO)
            )
        )
        return
    }

    val executionResult = player.transactionUser().withPendingWithdrawalDecision(
        amount = price.toBigDecimal(),
        currency = Currency.default(),
        additionalData = setOf(
            TransactionData.of(
                "clan_rename",
                "From tag '$currentTag' and name '$currentName' to tag '$newTag' and name '$newName'"
            )
        ),
        rollbackOn = PendingRollbackPolicy.Always
    ) {
        val updateResult = clan.updateClanNameAndTag {
            if (nameChanged) name(newName!!)
            if (tagChanged) tag(newTag!!)
        }

        PendingExecutionDecision.from(updateResult) { it.isSuccess }
    }

    when (executionResult) {
        is PendingExecutionResult.Completed -> player.showDialog(
            buildSuccessDialog(
                currentName,
                newName,
                nameChanged,
                currentTag,
                newTag,
                tagChanged
            )
        )

        is PendingExecutionResult.CommitFailed -> {
            log.atWarning()
                .withStackTrace(StackSize.MEDIUM)
                .log(
                    "Clan rename for ${player.uniqueId} succeeded but the withdrawal could not " +
                            "be committed (transaction ${executionResult.transaction.identifier}). " +
                            "Manual reconciliation required."
                )

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
        }

        is PendingExecutionResult.RolledBack -> player.showDialog(
            buildUpdateErrorDialog(
                executionResult.value,
                newName,
                newTag
            )
        )


        is PendingExecutionResult.RollbackFailed -> {
            log.atWarning()
                .withStackTrace(StackSize.MEDIUM)
                .log(
                    "Clan rename for ${player.uniqueId} failed but the reserved withdrawal could " +
                            "not be rolled back (transaction ${executionResult.transaction.identifier}). " +
                            "Manual reconciliation required."
                )

            player.showDialog(buildUpdateErrorDialog(executionResult.value, newName, newTag))
        }

        is PendingExecutionResult.ReservationFailed -> player.showDialog(
            buildReservationErrorDialog(executionResult.result, price)
        )

        is PendingExecutionResult.ExternalFailureRolledBack -> {
            log.atWarning()
                .withCause(executionResult.cause)
                .log("Clan rename for ${player.uniqueId} threw; the reserved withdrawal was rolled back.")

            player.showDialog(buildInternalErrorDialog())
        }

        is PendingExecutionResult.ExternalFailureRollbackFailed -> {
            log.atSevere()
                .withCause(executionResult.cause)
                .log(
                    "Clan rename for ${player.uniqueId} threw and the reserved withdrawal could " +
                            "not be rolled back (transaction ${executionResult.transaction.identifier}). " +
                            "Manual reconciliation required."
                )

            player.showDialog(buildInternalErrorDialog())
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

private fun buildReservationErrorDialog(result: PendingTransactionResult, price: Double) =
    noticeDialogWithBuilder(text("Bezahlung fehlgeschlagen", Colors.ERROR)) {
        if (result == PendingTransactionResult.ReceiverInsufficientFunds) {
            error("Du hast nicht genug Geld.")
            appendNewline()
            info("Benötigt: ")
            append(Currency.default().format(price))
        } else {
            error("Ein interner Fehler bei der Bezahlung ist aufgetreten.")
        }
    }

private fun buildInternalErrorDialog() = noticeDialog(
    text("Fehler", Colors.ERROR),
    text("Ein interner Fehler ist aufgetreten. Bitte kontaktiere das Team.", Colors.INFO)
)

private fun buildSuccessDialog(
    currentName: String,
    newName: String?,
    nameChanged: Boolean,
    currentTag: String,
    newTag: String?,
    tagChanged: Boolean
) = noticeDialogWithBuilder(text("Clan umbenannt", Colors.INFO)) {
    success("Dein Clan wurde erfolgreich umbenannt.")
    appendNewline()

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