package de.einsjustin.handtransformer;

import de.einsjustin.handtransformer.listeners.ItemInHandRenderListener;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonMain;

@AddonMain
public class HandTransformerAddon extends LabyAddon<HandTransformerConfiguration> {

  @Override
  protected void enable() {
    this.registerSettingCategory();

    this.registerListener(new ItemInHandRenderListener(this));
  }

  @Override
  protected Class<HandTransformerConfiguration> configurationClass() {
    return HandTransformerConfiguration.class;
  }
}
