package de.einsjustin.handtransformer.v26_3.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import de.einsjustin.handtransformer.api.event.ItemInHandRenderEvent;
import de.einsjustin.handtransformer.api.event.RenderHandEvent;
import net.labymod.api.Laby;
import net.labymod.api.client.entity.LivingEntity.HandSide;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.render.matrix.VanillaStackAccessor;
import net.labymod.api.event.Phase;
import net.labymod.v26_3.client.util.MinecraftUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.PlayerRenderState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FirstPersonHandsAndItemsRenderer.class)
public abstract class FirstPersonHandsAndItemsRendererMixin {

  @Shadow
  @Final
  private Minecraft minecraft;

  @Unique
  private Stack hand_transformer$stack;

  @Inject(
      method = "renderPlayerHand",
      at = @At("HEAD"),
      cancellable = true
  )
  private void hand_transformer$renderPlayerHandPre(PoseStack poseStack,
      SubmitNodeCollector submitNodeCollector, int lightCoords, HumanoidArm arm,
      PlayerRenderState playerState, CallbackInfo callbackInfo) {
    this.hand_transformer$stack = ((VanillaStackAccessor) poseStack).stack(submitNodeCollector);
    HandSide handSide = arm == HumanoidArm.RIGHT ? HandSide.RIGHT : HandSide.LEFT;
    var renderHandEvent = this.hand_transformer$fireRenderHandEvent(Phase.PRE, handSide);
    if (renderHandEvent.isCancelled()) {
      callbackInfo.cancel();
    }
  }

  @Inject(
      method = "renderPlayerHand",
      at = @At("TAIL")
  )
  private void hand_transformer$renderPlayerHandPost(PoseStack poseStack,
      SubmitNodeCollector submitNodeCollector, int lightCoords, HumanoidArm arm,
      PlayerRenderState playerState, CallbackInfo callbackInfo) {
    HandSide handSide = arm == HumanoidArm.RIGHT ? HandSide.RIGHT : HandSide.LEFT;
    this.hand_transformer$fireRenderHandEvent(Phase.POST, handSide);
  }

  @Redirect(
      method = "submitArmWithItem",
      at = @At(
          value = "INVOKE",
          target = "Lnet/minecraft/client/renderer/item/ItemStackRenderState;submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;III)V"
      )
  )
  private void hand_transformer$submitItemInHand(ItemStackRenderState itemStackRenderState,
      PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlay,
      int flags, @Local(argsOnly = true) PlayerRenderState playerState,
      @Local(argsOnly = true) InteractionHand hand, @Local(argsOnly = true) ItemStack itemStack) {
    LivingEntity livingEntity = this.minecraft.player;
    if (livingEntity == null) {
      itemStackRenderState.submit(poseStack, submitNodeCollector, lightCoords, overlay, flags);
      return;
    }

    HumanoidArm mainArm = playerState.avatarRenderState.mainArm;
    HumanoidArm arm = hand == InteractionHand.MAIN_HAND ? mainArm : mainArm.getOpposite();
    boolean rightHand = arm == HumanoidArm.RIGHT;
    ItemDisplayContext itemDisplayContext = rightHand
        ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND
        : ItemDisplayContext.FIRST_PERSON_LEFT_HAND;

    this.hand_transformer$stack = ((VanillaStackAccessor) poseStack).stack(submitNodeCollector);
    var itemInHandRenderEvent = this.hand_transformer$fireItemInHandRenderEvent(
        Phase.PRE, livingEntity, itemStack, itemDisplayContext, !rightHand
    );
    if (itemInHandRenderEvent.isCancelled()) {
      return;
    }

    itemStackRenderState.submit(poseStack, submitNodeCollector, lightCoords, overlay, flags);
    this.hand_transformer$fireItemInHandRenderEvent(
        Phase.POST, livingEntity, itemStack, itemDisplayContext, !rightHand
    );
  }

  @Unique
  private RenderHandEvent hand_transformer$fireRenderHandEvent(Phase phase, HandSide handSide) {
    return Laby.fireEvent(
        new RenderHandEvent(
            this.hand_transformer$stack,
            phase,
            handSide
        )
    );
  }

  @Unique
  private ItemInHandRenderEvent hand_transformer$fireItemInHandRenderEvent(Phase phase,
      LivingEntity livingEntity, ItemStack itemStack, ItemDisplayContext itemDisplayContext,
      boolean leftHand) {
    return Laby.fireEvent(
        new ItemInHandRenderEvent(
            this.hand_transformer$stack,
            phase,
            (net.labymod.api.client.entity.LivingEntity) livingEntity,
            MinecraftUtil.fromMinecraft(itemStack),
            MinecraftUtil.fromMinecraft(itemDisplayContext),
            leftHand ? HandSide.LEFT : HandSide.RIGHT
        )
    );
  }
}
