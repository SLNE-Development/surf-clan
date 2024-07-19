package dev.slne.surf.surfessentials.message;

import static net.kyori.adventure.text.Component.translatable;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TranslatableComponent;

public interface Messages {

  interface Message {
    TranslatableComponent message();

    void send(Audience audience, ComponentLike... args);
  }

  interface ErrorMessage extends Message {

    @Override
    default void send(Audience audience, ComponentLike... args) {
      Messages.send(audience, message().colorIfAbsent(Colors.ERROR), args);
    }
  }

  interface SimpleErrorMessage extends ErrorMessage {
    String key();

    @Override
    default TranslatableComponent message() {
      return translatable(key());
    }
  }

  interface InfoMessage extends Message {

    @Override
    default void send(Audience audience, ComponentLike... args) {
      Messages.send(audience, message().colorIfAbsent(Colors.INFO), args);
    }
  }

  interface SimpleInfoMessage extends InfoMessage {
    String key();

    @Override
    default TranslatableComponent message() {
      return translatable(key());
    }
  }

  interface SuccessMessage extends Message {

    @Override
    default void send(Audience audience, ComponentLike... args) {
      Messages.send(audience, message().colorIfAbsent(Colors.SUCCESS), args);
    }
  }

  interface SimpleSuccessMessage extends SuccessMessage {
    String key();

    @Override
    default TranslatableComponent message() {
      return translatable(key());
    }
  }

  private static void send(Audience audience, TranslatableComponent message, ComponentLike... args) {
    audience.sendMessage(Colors.PREFIX.append(message.arguments(args)));
  }
}
