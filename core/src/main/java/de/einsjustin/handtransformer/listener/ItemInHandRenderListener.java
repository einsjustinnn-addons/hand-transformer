package de.einsjustin.handtransformer.listener;

import de.einsjustin.handtransformer.HandTransformerAddon;
import de.einsjustin.handtransformer.event.ItemInHandRenderEvent;
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
    var itemConfiguration = configuration.itemSettings();

    if (!itemConfiguration.enabled().get()) return;

    float size = itemConfiguration.itemSize().get();
    float itemX = itemConfiguration.itemX().get();
    float itemY = itemConfiguration.itemY().get();
    float itemZ = itemConfiguration.itemZ().get();

    var stack = event.stack();

    stack.translate(itemX, itemY, itemZ);
    stack.scale(size);
  }
}
