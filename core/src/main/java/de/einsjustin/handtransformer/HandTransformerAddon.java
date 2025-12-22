package de.einsjustin.handtransformer;

import de.einsjustin.handtransformer.configuration.HandTransformerConfiguration;
import de.einsjustin.handtransformer.listener.HandSwingListener;
import de.einsjustin.handtransformer.listener.ItemInHandRenderListener;
import de.einsjustin.handtransformer.listener.RenderHandListener;
import net.labymod.api.Laby;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonMain;
import net.labymod.api.revision.SimpleRevision;
import net.labymod.api.util.version.SemanticVersion;

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

  @Override
  protected void preConfigurationLoad() {
    Laby.references().revisionRegistry().register(new SimpleRevision("hand_transformer", new SemanticVersion(1, 1, 0), "2025-12-22"));
  }
}
