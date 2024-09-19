package de.einsjustin.handtransformer.configuration.subconfiguration;

import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget.SliderSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.ShowSettingInParent;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.annotation.SpriteTexture;
import net.labymod.api.configuration.loader.property.ConfigProperty;

@SpriteTexture("settings")
public class ItemConfiguration extends Config {

  @SpriteSlot(size = 32)
  @ShowSettingInParent
  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @SliderSetting(min = 0.5F, max = 1.5F, steps = 0.1F)
  private final ConfigProperty<Float> itemSize = new ConfigProperty<>(1.0F);

  @SpriteSlot(size = 32, x = 1)
  @SliderSetting(min = -0.5F, max = 0.5F, steps = 0.1F)
  private final ConfigProperty<Float> itemX = new ConfigProperty<>(0.0F);

  @SpriteSlot(size = 32, x = 2)
  @SliderSetting(min = -0.5F, max = 0.5F, steps = 0.1F)
  private final ConfigProperty<Float> itemY = new ConfigProperty<>(0.0F);

  @SpriteSlot(size = 32, x = 3)
  @SliderSetting(min = -0.5F, max = 0.5F, steps = 0.1F)
  private final ConfigProperty<Float> itemZ = new ConfigProperty<>(0.0F);

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

  public ConfigProperty<Boolean> enabled() {
    return enabled;
  }
}
