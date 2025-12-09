package de.einsjustin.handtransformer.v1_21_11.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import de.einsjustin.handtransformer.api.event.ItemInHandRenderEvent;
import de.einsjustin.handtransformer.api.event.RenderHandEvent;
import net.labymod.api.Laby;
import net.labymod.api.client.entity.LivingEntity.HandSide;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.render.matrix.VanillaStackAccessor;
import net.labymod.api.event.Phase;
import net.labymod.v1_21_11.client.util.MinecraftUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {

  @Shadow
  @Final
  private ItemModelResolver itemModelResolver;

  @Shadow
  @Final
  private Minecraft minecraft;

  @Shadow
  @Final
  private EntityRenderDispatcher entityRenderDispatcher;

  @Unique
  private Stack hand_transformer$stack;

  @Redirect(
      method = "renderItem",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/item/ItemModelResolver;updateForTopItem(Lnet/minecraft/client/renderer/item/ItemStackRenderState;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/ItemOwner;I)V")
  )
  private void callItemRenderInHandEvent(ItemModelResolver instance,
      ItemStackRenderState itemStackRenderState, ItemStack itemStack,
      ItemDisplayContext itemDisplayContext, Level level, ItemOwner itemOwner, int i,
      @Local(argsOnly = true) PoseStack poseStack, @Local(argsOnly = true) SubmitNodeCollector submitNodeCollector) {
    boolean leftHand = itemDisplayContext.leftHand();
    LivingEntity livingEntity = itemOwner.asLivingEntity();
    this.hand_transformer$stack = ((VanillaStackAccessor) poseStack).stack(submitNodeCollector);
    var itemInHandRenderEvent = this.hand_transformer$fireItemInHandRenderEvent(Phase.PRE, livingEntity, itemStack, itemDisplayContext, leftHand);
    if (itemInHandRenderEvent.isCancelled()) return;
    this.itemModelResolver.updateForTopItem(itemStackRenderState, itemStack, itemDisplayContext, level, livingEntity, i);
    this.hand_transformer$fireItemInHandRenderEvent(Phase.POST, livingEntity, itemStack, itemDisplayContext, leftHand);
  }

  @Redirect(
      method = "renderPlayerArm",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/player/AvatarRenderer;renderRightHand(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/resources/Identifier;Z)V")
  )
  private void callRenderRightHandEvent(AvatarRenderer instance, PoseStack poseStack,
      SubmitNodeCollector submitNodeCollector, int i0, Identifier identifier, boolean b) {
    this.hand_transformer$stack = ((VanillaStackAccessor) poseStack).stack(submitNodeCollector);
    var renderHandEvent = this.hand_transformer$fireRenderHandEvent(Phase.PRE, HandSide.RIGHT);
    if (renderHandEvent.isCancelled()) return;
    var abstractClientPlayer = this.minecraft.player;
    var texture = abstractClientPlayer.getSkin().body().texturePath();
    var renderer = this.entityRenderDispatcher.getPlayerRenderer(abstractClientPlayer);
    boolean modelPartShown = abstractClientPlayer.isModelPartShown(PlayerModelPart.RIGHT_SLEEVE);
    renderer.renderRightHand(poseStack, submitNodeCollector, i0, texture, modelPartShown);
    this.hand_transformer$fireRenderHandEvent(Phase.POST, HandSide.RIGHT);
  }

  @Redirect(
      method = "renderPlayerArm",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/player/AvatarRenderer;renderLeftHand(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/resources/Identifier;Z)V")
  )
  private void callRenderLeftHandEvent(AvatarRenderer instance, PoseStack poseStack,
      SubmitNodeCollector submitNodeCollector, int i0, Identifier identifier, boolean b) {
    this.hand_transformer$stack = ((VanillaStackAccessor) poseStack).stack(submitNodeCollector);
    var renderHandEvent = this.hand_transformer$fireRenderHandEvent(Phase.PRE, HandSide.LEFT);
    if (renderHandEvent.isCancelled()) return;
    var abstractClientPlayer = this.minecraft.player;
    var texture = abstractClientPlayer.getSkin().body().texturePath();
    var renderer = this.entityRenderDispatcher.getPlayerRenderer(abstractClientPlayer);
    boolean modelPartShown = abstractClientPlayer.isModelPartShown(PlayerModelPart.LEFT_SLEEVE);
    renderer.renderLeftHand(poseStack, submitNodeCollector, i0, texture, modelPartShown);
    this.hand_transformer$fireRenderHandEvent(Phase.POST, HandSide.LEFT);
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
  private ItemInHandRenderEvent hand_transformer$fireItemInHandRenderEvent(Phase phase, LivingEntity livingEntity, ItemStack itemStack, ItemDisplayContext itemDisplayContext, boolean leftHand) {
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
