package de.einsjustin.handtransformer.v1_21_5.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import de.einsjustin.handtransformer.api.event.ItemInHandRenderEvent;
import de.einsjustin.handtransformer.api.event.RenderHandEvent;
import net.labymod.api.Laby;
import net.labymod.api.client.entity.LivingEntity.HandSide;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.render.matrix.VanillaStackAccessor;
import net.labymod.api.event.Phase;
import net.labymod.v1_21_5.client.util.MinecraftUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
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
  private ItemRenderer itemRenderer;

  @Shadow
  @Final
  private EntityRenderDispatcher entityRenderDispatcher;

  @Shadow
  @Final
  private Minecraft minecraft;

  @Unique
  private Stack hand_transformer$stack;

  @Redirect(
      method = "renderItem",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;renderStatic(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/level/Level;III)V")
  )
  private void callItemRenderInHandEvent(ItemRenderer instance, LivingEntity livingEntity,
      ItemStack itemStack, ItemDisplayContext itemDisplayContext, PoseStack poseStack,
      MultiBufferSource multiBufferSource, Level level, int i0, int i1, int i2) {
    boolean leftHand = itemDisplayContext.leftHand();
    this.hand_transformer$stack = ((VanillaStackAccessor) poseStack).stack(multiBufferSource);
    var itemInHandRenderEvent = this.hand_transformer$fireItemInHandRenderEvent(Phase.PRE, livingEntity, itemStack, itemDisplayContext, leftHand);
    if (itemInHandRenderEvent.isCancelled()) return;
    this.itemRenderer.renderStatic(livingEntity, itemStack, itemDisplayContext, poseStack, multiBufferSource, level, i0,  i1, i2);
    this.hand_transformer$fireItemInHandRenderEvent(Phase.POST, livingEntity, itemStack, itemDisplayContext, leftHand);
  }

  @Redirect(
      method = "renderPlayerArm",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/player/PlayerRenderer;renderRightHand(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/resources/ResourceLocation;Z)V")
  )
  private void callRenderRightHandEvent(PlayerRenderer instance, PoseStack poseStack,
      MultiBufferSource multiBufferSource, int i0, ResourceLocation resourceLocation, boolean b) {
    this.hand_transformer$stack = ((VanillaStackAccessor) poseStack).stack(multiBufferSource);
    var renderHandEvent = this.hand_transformer$fireRenderHandEvent(Phase.PRE, HandSide.RIGHT);
    if (renderHandEvent.isCancelled()) return;
    var abstractClientPlayer = this.minecraft.player;
    var texture = abstractClientPlayer.getSkin().texture();
    var renderer = (PlayerRenderer) this.entityRenderDispatcher.getRenderer(abstractClientPlayer);
    boolean modelPartShown = abstractClientPlayer.isModelPartShown(PlayerModelPart.RIGHT_SLEEVE);
    renderer.renderRightHand(poseStack, multiBufferSource, i0, texture, modelPartShown);
    this.hand_transformer$fireRenderHandEvent(Phase.POST, HandSide.RIGHT);
  }

  @Redirect(
      method = "renderPlayerArm",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/player/PlayerRenderer;renderLeftHand(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/resources/ResourceLocation;Z)V")
  )
  private void callRenderLeftHandEvent(PlayerRenderer instance, PoseStack poseStack,
      MultiBufferSource multiBufferSource, int i0, ResourceLocation resourceLocation, boolean b) {
    this.hand_transformer$stack = ((VanillaStackAccessor) poseStack).stack(multiBufferSource);
    var renderHandEvent = this.hand_transformer$fireRenderHandEvent(Phase.PRE, HandSide.LEFT);
    if (renderHandEvent.isCancelled()) return;
    var abstractClientPlayer = this.minecraft.player;
    var texture = abstractClientPlayer.getSkin().texture();
    var renderer = (PlayerRenderer) this.entityRenderDispatcher.getRenderer(abstractClientPlayer);
    boolean modelPartShown = abstractClientPlayer.isModelPartShown(PlayerModelPart.LEFT_SLEEVE);
    renderer.renderLeftHand(poseStack, multiBufferSource, i0, texture, modelPartShown);
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
