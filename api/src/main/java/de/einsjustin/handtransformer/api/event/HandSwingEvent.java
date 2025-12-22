package de.einsjustin.handtransformer.api.event;

import net.labymod.api.event.Event;
import org.jetbrains.annotations.NotNull;

public class HandSwingEvent implements Event {

  private Integer animationDuration;

  public HandSwingEvent(@NotNull Integer animationDuration) {
    this.animationDuration = animationDuration;
  }

  public Integer getAnimationDuration() {
    return this.animationDuration;
  }

  public void setAnimationDuration(Integer animationDuration) {
    this.animationDuration = animationDuration;
  }

}
