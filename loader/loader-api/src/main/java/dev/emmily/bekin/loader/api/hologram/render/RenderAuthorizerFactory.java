package dev.emmily.bekin.loader.api.hologram.render;

import dev.emmily.bekin.api.hologram.render.RenderAuthorizer;

/**m
 * Represents a factory for {@link RenderAuthorizer} instances that uses {@link RenderAuthorizerTemplate templates}
 * to create custom render authorizers from configuration files.
 */
public interface RenderAuthorizerFactory {
  /**
   * Creates a {@link RenderAuthorizer} based on the given {@code template}.
   *
   * @param template The template to create the authorizer from.
   * @return A new {@link RenderAuthorizer} based on the given {@code template}.
   */
  RenderAuthorizer create(RenderAuthorizerTemplate template);
}
