package de.einsjustin.handtransformer.v26_3.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import de.einsjustin.handtransformer.api.event.HandSwingEvent;
import net.labymod.api.Laby;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

  @ModifyExpressionValue(
      method = "swing(Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/component/SwingAnimation;Z)Z",
      at = @At(
          value = "INVOKE",
          target = "Lnet/minecraft/world/entity/LivingEntity;getModifiedSwingDuration(Lnet/minecraft/world/item/component/SwingAnimation;)I"
      )
  )
  private int hand_transformer$modifySwingDuration(int original) {
    if ((Object) this != Minecraft.getInstance().player) {
      return original;
    }

    return Laby.fireEvent(new HandSwingEvent(original)).getAnimationDuration();
  }
}
