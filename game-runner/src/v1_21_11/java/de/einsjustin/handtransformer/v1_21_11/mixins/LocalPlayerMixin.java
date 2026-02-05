package de.einsjustin.handtransformer.v1_21_11.mixins;

import com.mojang.authlib.GameProfile;
import de.einsjustin.handtransformer.HandTransformerAddon;
import de.einsjustin.handtransformer.api.event.HandSwingEvent;
import net.labymod.api.Laby;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin extends AbstractClientPlayer {

  @Unique
  private int hand_transformer$animTicks;

  public LocalPlayerMixin(ClientLevel $$0, GameProfile $$1) {
    super($$0, $$1);
  }

  @Inject(method = "aiStep", at = @At("TAIL"))
  public void hand_transformer$aiStep(CallbackInfo callbackInfo) {

    var event = Laby.fireEvent(new HandSwingEvent(6));

    if (event.getAnimationDuration() == 6) {
      return;
    }

    if (this.hand_transformer$animTicks > event.getAnimationDuration()) {
      this.hand_transformer$animTicks = 0;
    }
    if (this.hand_transformer$animTicks == 0) {
      this.attackAnim = 1f;
    } else {
      this.attackAnim = (this.hand_transformer$animTicks - 1f) / event.getAnimationDuration();
      this.hand_transformer$animTicks++;
    }
  }

  @Inject(method = "swing(Lnet/minecraft/world/InteractionHand;)V", at = @At("HEAD"))
  public void hand_transformer$swing(
      InteractionHand hand,
      CallbackInfo callbackInfo
  ) {
    if (!HandTransformerAddon.INSTANCE.configuration().enabled().get() || !HandTransformerAddon.INSTANCE.configuration().swingSettings().enabled().get()) return;
    if (this.hand_transformer$animTicks == 0) {
      this.hand_transformer$animTicks = 1;
    }
  }
}

