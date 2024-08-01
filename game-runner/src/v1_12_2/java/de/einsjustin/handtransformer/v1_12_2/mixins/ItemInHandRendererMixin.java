package de.einsjustin.handtransformer.v1_12_2.mixins;

import de.einsjustin.handtransformer.event.ItemInHandRenderEvent;
import net.labymod.api.Laby;
import net.labymod.api.client.entity.LivingEntity.HandSide;
import net.labymod.api.event.Phase;
import net.labymod.v1_12_2.client.render.matrix.VersionedStackProvider;
import net.labymod.v1_12_2.client.util.MinecraftUtil;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
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

  @Redirect(
      method = "renderItemSide",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderItem;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;Z)V")
  )
  private void callItemRenderInHandEvent(RenderItem instance, ItemStack itemStack, EntityLivingBase livingBase, ItemCameraTransforms.TransformType transformType, boolean leftHand) {
    ItemInHandRenderEvent itemInHandRenderEvent = hand_transformer$fireItemInHandRenderEvent(Phase.PRE, livingBase, itemStack, transformType, leftHand);
    if (itemInHandRenderEvent.isCancelled()) return;
    itemRenderer.renderItem(itemStack, livingBase, transformType, leftHand);
    hand_transformer$fireItemInHandRenderEvent(Phase.POST, livingBase, itemStack, transformType, leftHand);
  }

  @Unique
  private ItemInHandRenderEvent hand_transformer$fireItemInHandRenderEvent(Phase phase, EntityLivingBase livingEntity, ItemStack itemStack, ItemCameraTransforms.TransformType itemDisplayContext, boolean leftHand) {
    return Laby.fireEvent(
        new ItemInHandRenderEvent(
            VersionedStackProvider.DEFAULT_STACK,
            phase,
            (net.labymod.api.client.entity.LivingEntity) livingEntity,
            MinecraftUtil.fromMinecraft(itemStack),
            MinecraftUtil.fromMinecraft(itemDisplayContext),
            leftHand ? HandSide.LEFT : HandSide.RIGHT
        )
    );
  }
}
