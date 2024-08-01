package de.einsjustin.handtransformer.event;

import net.labymod.api.client.entity.LivingEntity;
import net.labymod.api.client.entity.LivingEntity.HandSide;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.render.model.ModelTransformType;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.event.Phase;
import net.labymod.api.event.client.render.CancellableRenderEvent;
import org.jetbrains.annotations.NotNull;

public class ItemInHandRenderEvent extends CancellableRenderEvent {

  private final LivingEntity livingEntity;
  private final ItemStack itemStack;
  private final ModelTransformType type;
  private final HandSide handSide;

  public ItemInHandRenderEvent(@NotNull Stack stack, @NotNull Phase phase, @NotNull LivingEntity livingEntity, @NotNull ItemStack itemStack, @NotNull ModelTransformType type, HandSide handSide) {
    super(stack, phase);
    this.livingEntity = livingEntity;
    this.itemStack = itemStack;
    this.type = type;
    this.handSide = handSide;
  }

  public LivingEntity livingEntity() {
    return livingEntity;
  }

  public HandSide handSide() {
    return handSide;
  }

  public ModelTransformType type() {
    return type;
  }

  public ItemStack itemStack() {
    return itemStack;
  }
}
