package de.einsjustin.handtransformer.v26_3.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.player.FirstPersonHandsAndItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FirstPersonHandsAndItems.class)
public abstract class FirstPersonHandsAndItemsMixin {

  @ModifyExpressionValue(
      method = "tick(Lnet/minecraft/client/player/LocalPlayer;)V",
      at = @At(
          value = "INVOKE",
          target = "Lnet/minecraft/client/player/LocalPlayer;getItemSwapScale(F)F"
      )
  )
  public float hand_transformer$tick(float original) {
    return 1f;
  }
}
