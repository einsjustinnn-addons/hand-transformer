package de.einsjustin.core.listeners;

import de.einsjustin.core.HandTransformerAddon;
import de.einsjustin.core.event.ItemInHandRenderEvent;
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
    ModelTransformType type = event.type();
    if (!(type == ModelTransformType.FIRST_PERSON_RIGHT_HAND || type == ModelTransformType.FIRST_PERSON_LEFT_HAND)) return;
    float size = this.addon.configuration().itemSize().get();
    var stack = event.stack();
    stack.scale(size);
  }
}
