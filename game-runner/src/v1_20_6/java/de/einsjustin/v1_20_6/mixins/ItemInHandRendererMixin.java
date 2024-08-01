package de.einsjustin.v1_20_6.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import de.einsjustin.core.event.ItemInHandRenderEvent;
import net.labymod.api.Laby;
import net.labymod.api.client.entity.LivingEntity.HandSide;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.render.matrix.VanillaStackAccessor;
import net.labymod.api.event.Phase;
import net.labymod.v1_20_6.client.util.MinecraftUtil;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.entity.LivingEntity;
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

  @Unique
  private Stack hand_transformer$stack;

  @Redirect(
      method = "renderItem",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;renderStatic(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/level/Level;III)V")
  )
  private void callItemRenderInHandEvent(ItemRenderer instance, LivingEntity livingEntity, ItemStack itemStack,
      ItemDisplayContext itemDisplayContext, boolean leftHand, PoseStack poseStack,
      MultiBufferSource multiBufferSource, Level level, int i0, int i1, int i2) {
    hand_transformer$stack = ((VanillaStackAccessor) poseStack).stack(multiBufferSource);
    ItemInHandRenderEvent itemInHandRenderEvent = hand_transformer$fireItemInHandRenderEvent(Phase.PRE, livingEntity, itemStack, itemDisplayContext, leftHand);
    if (itemInHandRenderEvent.isCancelled()) return;
    itemRenderer.renderStatic(livingEntity, itemStack, itemDisplayContext, leftHand, poseStack, multiBufferSource, level, i0,  i1, i2);
    hand_transformer$fireItemInHandRenderEvent(Phase.POST, livingEntity, itemStack, itemDisplayContext, leftHand);
  }

  @Unique
  private ItemInHandRenderEvent hand_transformer$fireItemInHandRenderEvent(Phase phase, LivingEntity livingEntity, ItemStack itemStack, ItemDisplayContext itemDisplayContext, boolean leftHand) {
    return Laby.fireEvent(
        new ItemInHandRenderEvent(
            hand_transformer$stack,
            phase,
            (net.labymod.api.client.entity.LivingEntity) livingEntity,
            MinecraftUtil.fromMinecraft(itemStack),
            MinecraftUtil.fromMinecraft(itemDisplayContext),
            leftHand ? HandSide.LEFT : HandSide.RIGHT
        )
    );
  }
}
