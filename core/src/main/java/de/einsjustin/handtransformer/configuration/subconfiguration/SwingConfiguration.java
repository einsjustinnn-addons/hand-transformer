package de.einsjustin.handtransformer.configuration.subconfiguration;

import net.labymod.api.client.gui.screen.widget.widgets.input.SliderWidget.SliderSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.ShowSettingInParent;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.annotation.SpriteTexture;
import net.labymod.api.configuration.loader.property.ConfigProperty;

@SpriteTexture("settings")
public class SwingConfiguration extends Config {

  @SpriteSlot(size = 32)
  @ShowSettingInParent
  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @SliderSetting(min = 1, max = 20, steps = 1)
  private final ConfigProperty<Integer> swingDurationTicks = new ConfigProperty<>(6);

  public ConfigProperty<Integer> swingDurationTicks() {
    return swingDurationTicks;
  }

  public ConfigProperty<Boolean> enabled() {
    return enabled;
  }

}
