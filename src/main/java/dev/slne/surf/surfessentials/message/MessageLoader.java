package dev.slne.surf.surfessentials.message;

import java.util.Arrays;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;

public final class MessageLoader {

  public static final MessageLoader INSTANCE = new MessageLoader();
  private static final Key TRANSLATION_KEY = Key.key("surf-essentials", "messages");

  private TranslationRegistry registry;

  public void load() {
    reset();

    Arrays.stream(Locale.getAvailableLocales())
        .map(locale -> {
          try {
            return ResourceBundle.getBundle("messages.defaults.Bundle",
                locale); // TODO: 13.05.2024 14:56 - Change to url classloader
          } catch (MissingResourceException e) {
            return null;
          }
        })
        .filter(Objects::nonNull)
        .forEach(bundle -> registry.registerAll(bundle.getLocale(), bundle, false));
  }

  private void reset() {
    if (registry != null) {
      GlobalTranslator.translator().removeSource(registry);
    }

    registry = TranslationRegistry.create(TRANSLATION_KEY);
    registry.defaultLocale(Locale.GERMAN);
  }
}
