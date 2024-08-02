package de.einsjustin.handtransformer.listeners;

import de.einsjustin.handtransformer.HandTransformerAddon;
import de.einsjustin.handtransformer.event.ItemInHandRenderEvent;
import de.einsjustin.handtransformer.event.RenderHandEvent;
import net.labymod.api.client.render.model.ModelTransformType;
import net.labymod.api.event.Phase;
import net.labymod.api.event.Subscribe;

public class ItemInHandRenderListener {

  private final HandTransformerAddon addon;

  public ItemInHandRenderListener(HandTransformerAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onItemInHandRender(ItemInHandRenderEvent event) {
    if (event.phase() != Phase.PRE) return;
    var type = event.type();
    if (!(type == ModelTransformType.FIRST_PERSON_RIGHT_HAND || type == ModelTransformType.FIRST_PERSON_LEFT_HAND)) return;

    var configuration = this.addon.configuration();

    float size = configuration.itemSize().get();
    float itemX = configuration.itemX().get();
    float itemY = configuration.itemY().get();
    float itemZ = configuration.itemZ().get();

    var stack = event.stack();

    stack.translate(itemX, itemY, itemZ);
    stack.scale(size);
  }

  @Subscribe
  public void onRenderHand(RenderHandEvent event) {
    if (event.phase() != Phase.PRE) return;

    var configuration = this.addon.configuration();

    float size = configuration.handSize().get();
    float handX = configuration.handX().get();
    float handY = configuration.handY().get();
    float handZ = configuration.handZ().get();

    var stack = event.stack();

    stack.translate(handX, handY, handZ);
    stack.scale(size);
  }
}
