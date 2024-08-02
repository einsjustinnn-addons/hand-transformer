package de.einsjustin.handtransformer;

import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget.SliderSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingSection;

@ConfigName("settings")
public class HandTransformerConfiguration extends AddonConfig {

  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @SettingSection("item")
  @SliderSetting(min = 0.5F, max = 1.5F, steps = 0.1F)
  private final ConfigProperty<Float> itemSize = new ConfigProperty<>(1.0F);

  @SliderSetting(min = -0.5F, max = 0.5F, steps = 0.1F)
  private final ConfigProperty<Float> itemX = new ConfigProperty<>(1.0F);

  @SliderSetting(min = -0.5F, max = 0.5F, steps = 0.1F)
  private final ConfigProperty<Float> itemY = new ConfigProperty<>(1.0F);

  @SliderSetting(min = -0.5F, max = 0.5F, steps = 0.1F)
  private final ConfigProperty<Float> itemZ = new ConfigProperty<>(1.0F);

  @SettingSection("hand")
  @SliderSetting(min = 0.5F, max = 1.5F, steps = 0.1F)
  private final ConfigProperty<Float> handSize = new ConfigProperty<>(1.0F);

  @SliderSetting(min = -0.5F, max = 0.5F, steps = 0.1F)
  private final ConfigProperty<Float> handX = new ConfigProperty<>(0.0F);

  @SliderSetting(min = -0.5F, max = 0.5F, steps = 0.1F)
  private final ConfigProperty<Float> handY = new ConfigProperty<>(0.0F);

  @SliderSetting(min = -0.5F, max = 0.5F, steps = 0.1F)
  private final ConfigProperty<Float> handZ = new ConfigProperty<>(0.0F);

  @Override
  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }

  public ConfigProperty<Float> handZ() {
    return handZ;
  }

  public ConfigProperty<Float> handY() {
    return handY;
  }

  public ConfigProperty<Float> handX() {
    return handX;
  }

  public ConfigProperty<Float> handSize() {
    return handSize;
  }

  public ConfigProperty<Float> itemZ() {
    return itemZ;
  }

  public ConfigProperty<Float> itemY() {
    return itemY;
  }

  public ConfigProperty<Float> itemX() {
    return itemX;
  }

  public ConfigProperty<Float> itemSize() {
    return itemSize;
  }
}
