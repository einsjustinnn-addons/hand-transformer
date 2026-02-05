package de.einsjustin.handtransformer.listener;

import de.einsjustin.handtransformer.HandTransformerAddon;
import de.einsjustin.handtransformer.api.event.HandSwingEvent;
import net.labymod.api.event.Subscribe;

public record HandSwingListener(HandTransformerAddon addon) {

  @SuppressWarnings("unused")
  @Subscribe
  public void onHandSwing(HandSwingEvent event) {

    var configuration = this.addon.configuration();
    var swingConfiguration = configuration.swingSettings();

    if (!swingConfiguration.enabled().get() || !configuration.enabled().get()) {
      return;
    }

    event.setAnimationDuration(swingConfiguration.swingDurationTicks().get());
  }
}
