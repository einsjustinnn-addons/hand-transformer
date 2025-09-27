package de.einsjustin.handtransformer.api.event;

import net.labymod.api.client.entity.LivingEntity.HandSide;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.event.Phase;
import net.labymod.api.event.client.render.CancellableRenderEvent;
import org.jetbrains.annotations.NotNull;

public class RenderHandEvent extends CancellableRenderEvent {

  private final HandSide handSide;

  public RenderHandEvent(@NotNull Stack stack, @NotNull Phase phase, HandSide handSide) {
    super(stack, phase);
    this.handSide = handSide;
  }

  public HandSide getHandSide() {
    return handSide;
  }
}
