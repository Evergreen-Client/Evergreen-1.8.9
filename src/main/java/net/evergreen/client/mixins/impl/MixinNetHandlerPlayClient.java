package net.evergreen.client.mixins.impl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.server.S01PacketJoinGame;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public class MixinNetHandlerPlayClient {

    @Shadow @Final private NetworkManager netManager;

    @Inject(method = "handleJoinGame", at = @At("RETURN"))
    public void injectRegisterPacket(S01PacketJoinGame packetIn, CallbackInfo ci) {
        final ByteBuf message = Unpooled.buffer();
        message.writeBytes("Evergreen".getBytes());
        this.netManager.sendPacket(new C17PacketCustomPayload("REGISTER", new PacketBuffer(message)));
    }

}
