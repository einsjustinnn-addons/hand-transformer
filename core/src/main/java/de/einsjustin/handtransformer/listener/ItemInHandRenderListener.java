package de.einsjustin.handtransformer.listener;

import de.einsjustin.handtransformer.HandTransformerAddon;
import de.einsjustin.handtransformer.api.event.ItemInHandRenderEvent;
import net.labymod.api.Laby;
import net.labymod.api.client.entity.player.ClientPlayer;
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

    var stack = event.stack();

    float size = itemConfiguration.itemSize().get();

    ClientPlayer clientPlayer = Laby.labyAPI().minecraft().getClientPlayer();
    assert clientPlayer != null;
    boolean handActive = clientPlayer.isHandActive();

    if (handActive) {
      stack.scale(size);
      return;
    }

    float itemX = itemConfiguration.itemX().get();
    float itemY = itemConfiguration.itemY().get();
    float itemZ = itemConfiguration.itemZ().get();

    stack.translate(itemX, itemY, itemZ);
    stack.scale(size);
  }
}
