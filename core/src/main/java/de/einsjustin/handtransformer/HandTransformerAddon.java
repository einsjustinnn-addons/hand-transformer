package de.einsjustin.handtransformer;

import de.einsjustin.handtransformer.configuration.HandTransformerConfiguration;
import de.einsjustin.handtransformer.listener.HandSwingListener;
import de.einsjustin.handtransformer.listener.ItemInHandRenderListener;
import de.einsjustin.handtransformer.listener.RenderHandListener;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonMain;

@AddonMain
public class HandTransformerAddon extends LabyAddon<HandTransformerConfiguration> {

  @Override
  protected void enable() {
    this.registerSettingCategory();

    this.registerListener(new ItemInHandRenderListener(this));
    this.registerListener(new RenderHandListener(this));
    this.registerListener(new HandSwingListener(this));

  }

  @Override
  protected Class<HandTransformerConfiguration> configurationClass() {
    return HandTransformerConfiguration.class;
  }
}
