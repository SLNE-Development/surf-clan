package dev.slne.surf.essentials.main.utils;

import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.slne.surf.api.SurfApi;
import dev.slne.surf.api.utils.message.SurfColors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.minecraft.ChatFormatting;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public abstract class EssentialsUtil {
    /**
     * An array of {@link Sound} objects representing the sounds that can be played to
     * scare the player.
     */
    public static Sound[] scareSounds = new Sound[]{Sound.ENTITY_LIGHTNING_BOLT_THUNDER, Sound.ENTITY_WOLF_HOWL,
            Sound.ENTITY_BAT_DEATH, Sound.ENTITY_GHAST_SCREAM, Sound.ENTITY_GHAST_HURT};


    /**
     *
     * Check if arg is int.
     *
     * @param s  the string to be checked for an int
     */
    public static boolean isInt(@NotNull String s) {
        int i;
        try {
            i = Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ex) {
            //string is not an integer
            return false;
        }
    }

    /**
     *
     * Simple "No permission" message.
     *
     */
    public static Component NO_PERMISSION(){
        return SurfApi.getPrefix()
                .append(Component.text("You do not have permission to execute this command!", SurfColors.ERROR));
    }

    /**
     *
     * Converts the color from the input string to a gradient.
     *
     * @param input  the string to convert the color from
     * @param firstHex  the first hex color
     * @param secondHex  the second hex color
     */
    public static Component gradientify(@NotNull String input, @NotNull String firstHex, @NotNull String secondHex) {

        TextColor gradientFirst = TextColor.fromHexString(firstHex);
        TextColor gradientSecond = TextColor.fromHexString(secondHex);

        if (gradientFirst == null || gradientSecond == null) {
            return Component.text(input);
        }

        TextComponent.Builder builder = Component.text();
        float step = 1.0f / (input.length() - 1);
        float current = 0.0f;
        for (char c : input.toCharArray()) {
            builder.append(Component.text(c, TextColor.lerp(current, gradientFirst, gradientSecond)));
            current += step;
        }

        return builder.build();
    }

    /**
     *
     * converts ticks in a time format.
     *
     * @param ticks  the ticks to convert
     * @return Time format in string
     */
    public static String ticksToString(int ticks){
        int totalSeconds = ticks / 20;
        int hours, minutes, seconds;
        hours = totalSeconds / 3600;
        minutes = (totalSeconds % 3600) / 60;
        seconds = totalSeconds % 60;
        return String.format("%02dh %02dm %02ds", hours, minutes, seconds);
    }

    /**
     *
     * Sends an error message to the sender.
     *
     * @param sender  the sender
     * @param error  the error
     */
    public static void somethingWentWrongAsync_DE(@NotNull Player sender, @NotNull String error){
        SurfApi.getUser(sender).thenAcceptAsync(user -> user.sendMessage(SurfApi.getPrefix()
                .append(gradientify("Es ist ein Fehler aufgetreten:", "#eb3349", "#f45c43"))
                .append(Component.newline())
                .append(SurfApi.getPrefix())
                .append(gradientify(error, "#EA98DA", "#5B6CF9"))));
    }

    /**
     *
     * sorts the tab completion suggestion
     *
     * @param list  the list
     * @param currentarg  the currentarg
     * @param completions  the completions
     */
    public static void sortedSuggestions(@NotNull List<String> list, @NotNull String currentarg, @NotNull List<String> completions){
        for (String s : list) {
            if (s.toLowerCase().startsWith(currentarg)) {
                completions.add(s);
            }
        }
    }

    /**
     * Returns whether the given player is vanished or not.
     * A player is considered vanished if they have a "vanished" metadata value
     * set to `true`.
     *
     * @param player the player to check
     * @return `true` if the player is vanished, `false` otherwise
     */
    public static boolean isVanished(@NotNull Player player) {
        for (MetadataValue meta : player.getMetadata("vanished")) {
            if (meta.asBoolean()) return true;
        }
        return false;
    }

    /**
     * Suggests all possible color codes to the given {@link SuggestionsBuilder}.
     *
     * @param builder the {@link SuggestionsBuilder} to which the color codes will be added
     * @return a {@link CompletableFuture} containing the {@link Suggestions}
     */
    public static CompletableFuture<Suggestions> suggestAllColorCodes(@NotNull SuggestionsBuilder builder) {
        builder.suggest("&0", net.minecraft.network.chat.Component.literal("Black").withStyle(ChatFormatting.BLACK));
        builder.suggest("&2", net.minecraft.network.chat.Component.literal("Dark Green").withStyle(ChatFormatting.DARK_GREEN));
        builder.suggest("&4", net.minecraft.network.chat.Component.literal("Dark Red").withStyle(ChatFormatting.DARK_RED));
        builder.suggest("&6", net.minecraft.network.chat.Component.literal("Gold").withStyle(ChatFormatting.GOLD));
        builder.suggest("&8", net.minecraft.network.chat.Component.literal("Dark Gray").withStyle(ChatFormatting.DARK_GRAY));
        builder.suggest("&a", net.minecraft.network.chat.Component.literal("Green").withStyle(ChatFormatting.GREEN));
        builder.suggest("&c", net.minecraft.network.chat.Component.literal("Red").withStyle(ChatFormatting.RED));
        builder.suggest("&e", net.minecraft.network.chat.Component.literal("Yellow").withStyle(ChatFormatting.YELLOW));
        builder.suggest("&1", net.minecraft.network.chat.Component.literal("Dark Blue").withStyle(ChatFormatting.DARK_BLUE));
        builder.suggest("&3", net.minecraft.network.chat.Component.literal("Dark Aqua").withStyle(ChatFormatting.DARK_AQUA));
        builder.suggest("&5", net.minecraft.network.chat.Component.literal("Dark Purple").withStyle(ChatFormatting.DARK_PURPLE));
        builder.suggest("&7", net.minecraft.network.chat.Component.literal("Gray").withStyle(ChatFormatting.GRAY));
        builder.suggest("&9", net.minecraft.network.chat.Component.literal("Blue").withStyle(ChatFormatting.BLUE));
        builder.suggest("&b", net.minecraft.network.chat.Component.literal("Aqua").withStyle(ChatFormatting.AQUA));
        builder.suggest("&d", net.minecraft.network.chat.Component.literal("Light Purple").withStyle(ChatFormatting.LIGHT_PURPLE));
        builder.suggest("&f", net.minecraft.network.chat.Component.literal("White").withStyle(ChatFormatting.WHITE));

        builder.suggest("&k", net.minecraft.network.chat.Component.literal("Obfuscated").withStyle(ChatFormatting.OBFUSCATED));
        builder.suggest("&m", net.minecraft.network.chat.Component.literal("Strikethrough").withStyle(ChatFormatting.STRIKETHROUGH));
        builder.suggest("&o", net.minecraft.network.chat.Component.literal("Italic").withStyle(ChatFormatting.ITALIC));
        builder.suggest("&l", net.minecraft.network.chat.Component.literal("Bold").withStyle(ChatFormatting.BOLD));
        builder.suggest("&n", net.minecraft.network.chat.Component.literal("Underline").withStyle(ChatFormatting.UNDERLINE));
        builder.suggest("&r", net.minecraft.network.chat.Component.literal("Reset").withStyle(ChatFormatting.RESET));
        return builder.buildFuture();
    }

    /**
     * Adds a color code suggestion to the given {@link SuggestionsBuilder}.
     *
     * @param builder the {@link SuggestionsBuilder} to add the suggestion to
     * @param colorCode the color code to add as a suggestion
     * @param colorName the name of the color to display in the suggestion
     * @param formatting the {@link ChatFormatting} to apply to the color name in the suggestion
     * @return a {@link CompletableFuture} containing the completed {@link Suggestions} object
     */
    public static CompletableFuture<Suggestions> singleColorCode(@NotNull SuggestionsBuilder builder, @NotNull String colorCode,
                                                                 @NotNull String colorName, @NotNull ChatFormatting formatting) {
        return builder.suggest(colorCode, net.minecraft.network.chat.Component.literal(colorName).withStyle(formatting)).buildFuture();
    }

    /**
     * Plays a random scare sound from the {@link #scareSounds} array for the player.
     *
     * @param player the player to play the sound for
     */
    public static void playScareSound(@NotNull Player player) {
        Random random = new Random();
        int scareIndex = random.nextInt(scareSounds.length - 1);
        Sound scareSound = scareSounds[scareIndex];
        player.playSound(player.getLocation(), scareSound, 1.0F, 1.0F);
    }

    /**
     * Scares the player by playing a random scare sound from the {@link #scareSounds} array and
     * applying a {@link PotionEffectType#DARKNESS} effect to the player for 7 seconds.
     *
     * @param player the player to scare
     */
    public static void scarePlayer(@NotNull Player player) {
        playScareSound(player);
        PotionEffect scareEffect = new PotionEffect(PotionEffectType.DARKNESS, 20*7, 1, false, false, false);
        player.addPotionEffect(scareEffect);
    }
}
