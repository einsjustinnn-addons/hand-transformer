package de.einsjustin.handtransformer.listener;

import de.einsjustin.handtransformer.HandTransformerAddon;
import de.einsjustin.handtransformer.api.event.RenderHandEvent;
import net.labymod.api.event.Phase;
import net.labymod.api.event.Subscribe;

public class RenderHandListener {

  private final HandTransformerAddon addon;

  public RenderHandListener(HandTransformerAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onRenderHand(RenderHandEvent event) {
    if (event.phase() != Phase.PRE) return;

    var configuration = this.addon.configuration();
    var handConfiguration = configuration.handSettings();

    if (!handConfiguration.enabled().get()) return;

    float size = handConfiguration.handSize().get();
    float handX = handConfiguration.handX().get();
    float handY = handConfiguration.handY().get();
    float handZ = handConfiguration.handZ().get();

    var stack = event.stack();

    stack.translate(handX, handY, handZ);
    stack.scale(size);
  }
}
