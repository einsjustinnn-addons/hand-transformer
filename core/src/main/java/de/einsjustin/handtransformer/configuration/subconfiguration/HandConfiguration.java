package de.einsjustin.handtransformer.configuration.subconfiguration;

import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget.SliderSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.ShowSettingInParent;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.annotation.SpriteTexture;
import net.labymod.api.configuration.loader.property.ConfigProperty;

@SpriteTexture("settings")
public class HandConfiguration extends Config {

  @SpriteSlot(size = 32)
  @ShowSettingInParent
  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @SliderSetting(min = 0.5F, max = 1.5F, steps = 0.1F)
  private final ConfigProperty<Float> handSize = new ConfigProperty<>(1.0F);

  @SpriteSlot(size = 32, x = 1)
  @SliderSetting(min = -0.5F, max = 0.5F, steps = 0.1F)
  private final ConfigProperty<Float> handX = new ConfigProperty<>(0.0F);

  @SpriteSlot(size = 32, x = 2)
  @SliderSetting(min = -0.5F, max = 0.5F, steps = 0.1F)
  private final ConfigProperty<Float> handY = new ConfigProperty<>(0.0F);

  @SpriteSlot(size = 32, x = 3)
  @SliderSetting(min = -0.5F, max = 0.5F, steps = 0.1F)
  private final ConfigProperty<Float> handZ = new ConfigProperty<>(0.0F);

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

  public ConfigProperty<Boolean> enabled() {
    return enabled;
  }
}
