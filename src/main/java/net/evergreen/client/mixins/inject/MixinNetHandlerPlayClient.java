/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package net.evergreen.client.mixins.inject;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.evergreen.client.Evergreen;
import net.evergreen.client.event.EventChatReceived;
import net.evergreen.client.mod.impl.lunarspoof.LunarSpoof;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public class MixinNetHandlerPlayClient {

    @Shadow @Final private NetworkManager netManager;

    @Shadow private Minecraft gameController;

    /**
     * @reason Tell servers we are on Evergreen
     * @author isXander
     */
    @Inject(method = "handleJoinGame", at = @At("RETURN"))
    public void injectRegisterPacket(S01PacketJoinGame packetIn, CallbackInfo ci) {
        LunarSpoof lunarSpoof = Evergreen.getInstance().getModManager().getMod(LunarSpoof.class);
        final ByteBuf message = Unpooled.buffer();
        message.writeBytes((lunarSpoof.isEnabled() ? "Lunar-Client" : "Evergreen").getBytes());
        this.netManager.sendPacket(new C17PacketCustomPayload("REGISTER", new PacketBuffer(message)));
    }

    /**
     * @reason Inject {@link EventChatReceived}
     * @author isXander
     */
    @Overwrite
    public void handleChat(S02PacketChat packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, (NetHandlerPlayClient) (Object) this, this.gameController);
        EventChatReceived event = new EventChatReceived(packetIn.getType(), packetIn.getChatComponent());
        if (!event.post() && event.message != null) {
            if (packetIn.getType() == 2)
                this.gameController.ingameGUI.setRecordPlaying(event.message, false);
            else
                this.gameController.ingameGUI.getChatGUI().printChatMessage(event.message);
        }
    }

}
