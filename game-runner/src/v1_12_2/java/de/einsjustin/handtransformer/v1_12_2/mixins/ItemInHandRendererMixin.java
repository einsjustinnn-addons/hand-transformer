package de.einsjustin.handtransformer.v1_12_2.mixins;

import de.einsjustin.handtransformer.api.event.ItemInHandRenderEvent;
import de.einsjustin.handtransformer.api.event.RenderHandEvent;
import net.labymod.api.Laby;
import net.labymod.api.client.entity.LivingEntity;
import net.labymod.api.client.entity.LivingEntity.HandSide;
import net.labymod.api.event.Phase;
import net.labymod.v1_12_2.client.render.matrix.VersionedStackProvider;
import net.labymod.v1_12_2.client.util.MinecraftUtil;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemRenderer.class)
public abstract class ItemInHandRendererMixin {

  @Shadow
  @Final
  private RenderItem itemRenderer;

  @Shadow
  @Final
  private RenderManager renderManager;

  @Redirect(
      method = "renderItemSide",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderItem;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;Z)V")
  )
  private void callItemRenderInHandEvent(RenderItem instance, ItemStack itemStack,
      EntityLivingBase livingBase, TransformType transformType, boolean leftHand) {
    var itemInHandRenderEvent = hand_transformer$fireItemInHandRenderEvent(Phase.PRE, livingBase, itemStack, transformType, leftHand);
    if (itemInHandRenderEvent.isCancelled()) return;
    this.itemRenderer.renderItem(itemStack, livingBase, transformType, leftHand);
    this.hand_transformer$fireItemInHandRenderEvent(Phase.POST, livingBase, itemStack, transformType, leftHand);
  }

  @Redirect(
      method = "renderArmFirstPerson",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderPlayer;renderRightArm(Lnet/minecraft/client/entity/AbstractClientPlayer;)V")
  )
  private void callRenderRightHandEvent(RenderPlayer instance, AbstractClientPlayer clientPlayer) {
    var renderHandEvent = this.hand_transformer$fireRenderHandEvent(Phase.PRE, HandSide.RIGHT);
    if (renderHandEvent.isCancelled()) return;
    Render<AbstractClientPlayer> entityRenderObject = this.renderManager.getEntityRenderObject(clientPlayer);
    var renderer = (RenderPlayer) entityRenderObject;
    renderer.renderRightArm(clientPlayer);
    this.hand_transformer$fireRenderHandEvent(Phase.POST, HandSide.RIGHT);
  }

  @Redirect(
      method = "renderArmFirstPerson",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderPlayer;renderLeftArm(Lnet/minecraft/client/entity/AbstractClientPlayer;)V")
  )
  private void callRenderLeftHandEvent(RenderPlayer instance, AbstractClientPlayer clientPlayer) {
    var renderHandEvent = this.hand_transformer$fireRenderHandEvent(Phase.PRE, HandSide.LEFT);
    if (renderHandEvent.isCancelled()) return;
    Render<AbstractClientPlayer> entityRenderObject = this.renderManager.getEntityRenderObject(clientPlayer);
    var renderer = (RenderPlayer) entityRenderObject;
    renderer.renderLeftArm(clientPlayer);
    this.hand_transformer$fireRenderHandEvent(Phase.POST, HandSide.LEFT);
  }

  @Unique
  private RenderHandEvent hand_transformer$fireRenderHandEvent(Phase phase, HandSide handSide) {
    return Laby.fireEvent(
        new RenderHandEvent(
            VersionedStackProvider.DEFAULT_STACK,
            phase,
            handSide
        )
    );
  }

  @Unique
  private ItemInHandRenderEvent hand_transformer$fireItemInHandRenderEvent(Phase phase, EntityLivingBase livingEntity, ItemStack itemStack, TransformType itemDisplayContext, boolean leftHand) {
    return Laby.fireEvent(
        new ItemInHandRenderEvent(
            VersionedStackProvider.DEFAULT_STACK,
            phase,
            (LivingEntity) livingEntity,
            MinecraftUtil.fromMinecraft(itemStack),
            MinecraftUtil.fromMinecraft(itemDisplayContext),
            leftHand ? HandSide.LEFT : HandSide.RIGHT
        )
    );
  }
}
