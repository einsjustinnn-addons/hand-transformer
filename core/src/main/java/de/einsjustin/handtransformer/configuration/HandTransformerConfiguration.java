package de.einsjustin.handtransformer.configuration;

import de.einsjustin.handtransformer.configuration.subconfiguration.HandConfiguration;
import de.einsjustin.handtransformer.configuration.subconfiguration.ItemConfiguration;
import de.einsjustin.handtransformer.configuration.subconfiguration.SwingConfiguration;
import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.annotation.IntroducedIn;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.annotation.SpriteTexture;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingSection;

@SpriteTexture("settings")
@ConfigName("settings")
public class HandTransformerConfiguration extends AddonConfig {

  @SpriteSlot(size = 32)
  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @SettingSection("settings")
  private final ItemConfiguration itemSettings = new ItemConfiguration();

  private final HandConfiguration handSettings = new HandConfiguration();

  @IntroducedIn(namespace = "hand_transformer", value = "1.1")
  private final SwingConfiguration swingSettings = new SwingConfiguration();

  @Override
  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }

  public ItemConfiguration itemSettings() {
    return itemSettings;
  }

  public HandConfiguration handSettings() {
    return handSettings;
  }

  public SwingConfiguration swingSettings() {
    return swingSettings;
  }

}
