package de.einsjustin.handtransformer.v1_20_6.mixins;

import de.einsjustin.handtransformer.api.event.HandSwingEvent;
import net.labymod.api.Laby;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {

  @Unique
  private int hand_transformer$animTicks;

  @Inject(method = "aiStep", at = @At("TAIL"))
  public void hand_transformer$aiStep(
      CallbackInfo callbackInfo
  ) {

    var event = Laby.fireEvent(new HandSwingEvent(6));

    if (event.getAnimationDuration() == 6)
      return;

    LocalPlayer localPlayer = (LocalPlayer) (Object) this;

    if (this.hand_transformer$animTicks > event.getAnimationDuration())
      this.hand_transformer$animTicks = 0;

    if (this.hand_transformer$animTicks == 0)
      localPlayer.attackAnim = 1f;
    else {
      localPlayer.attackAnim = (this.hand_transformer$animTicks - 1f) / event.getAnimationDuration();
      this.hand_transformer$animTicks++;
    }

  }

  @Inject(method = "swing(Lnet/minecraft/world/InteractionHand;)V", at = @At("HEAD"))
  public void hand_transformer$swing(
      InteractionHand hand,
      CallbackInfo callbackInfo
  ) {
    if (this.hand_transformer$animTicks == 0)
      this.hand_transformer$animTicks = 1;
  }

}

