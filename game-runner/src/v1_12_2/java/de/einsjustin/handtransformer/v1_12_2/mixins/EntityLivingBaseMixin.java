package de.einsjustin.handtransformer.v1_12_2.mixins;

import de.einsjustin.handtransformer.api.event.HandSwingEvent;
import net.labymod.api.Laby;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityLivingBase.class)
public class EntityLivingBaseMixin {

  @Inject(method = "getArmSwingAnimationEnd()I", at = @At("HEAD"), cancellable = true)
  private void getArmSwingAnimationEnd(CallbackInfoReturnable<Integer> cir) {

    var event = Laby.fireEvent(new HandSwingEvent(6));

    if (event.getAnimationDuration() == 6)
      return;

    cir.setReturnValue(event.getAnimationDuration());

  }

}
