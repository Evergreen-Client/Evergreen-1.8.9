/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.client.mixins.inject;

import com.evergreenclient.client.event.EventClientShutdown;
import com.evergreenclient.client.event.EventClientTick;
import com.evergreenclient.client.event.EventGuiOpen;
import com.evergreenclient.client.event.EventRenderTick;
import com.evergreenclient.client.event.bus.Phase;
import com.evergreenclient.client.Evergreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.*;
import net.minecraft.client.main.GameConfiguration;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.Timer;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

    @Shadow @Final public Profiler mcProfiler;
    @Shadow private Timer timer;
    @Shadow private boolean enableGLErrorChecking;
    @Shadow public GuiScreen currentScreen;
    @Shadow public WorldClient theWorld;
    @Shadow public EntityPlayerSP thePlayer;
    @Shadow public GameSettings gameSettings;
    @Shadow public GuiIngame ingameGUI;
    @Shadow public abstract void setIngameFocus();
    @Shadow public abstract void setIngameNotInFocus();
    @Shadow public boolean skipRenderWorld;
    @Shadow private SoundHandler mcSoundHandler;
    @Shadow volatile boolean running;
    @Shadow private static Minecraft theMinecraft;

    @Shadow @Final private DefaultResourcePack mcDefaultResourcePack;

    @Shadow protected abstract ByteBuffer readImageToBuffer(InputStream imageStream) throws IOException;

    @Shadow @Final private static Logger logger;

    @Shadow public abstract IResourceManager getResourceManager();

    @Inject(method = "startGame", at = @At("HEAD"))
    private void injectModPreInit(CallbackInfo ci) {
        Evergreen.getInstance().preInit();
    }

    @Inject(method = "startGame", at = @At("RETURN"))
    private void injectModInit(CallbackInfo ci) {
        Evergreen.getInstance().init();
    }

    /**
     * Injects the pre render event
     *
     * @author isXander
     */
    @Inject(method = "runGameLoop", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;endStartSection(Ljava/lang/String;)V", shift = At.Shift.BEFORE))
    private void injectRenderTickPre(CallbackInfo ci) {
        this.mcProfiler.endStartSection("modGameRenderPre");
        new EventRenderTick(Phase.PRE, this.timer.renderPartialTicks).post();
    }

    /**
     * Injects the post render event
     *
     * @author isXander
     */
    @Inject(method = "runGameLoop", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;endSection()V", shift = At.Shift.AFTER))
    private void injectRenderTickPost(CallbackInfo ci) {
        this.mcProfiler.startSection("modGameRenderPost");
        new EventRenderTick(Phase.POST, this.timer.renderPartialTicks).post();
    }

    /**
     * Injects the pre tick event
     *
     * @author isXander
     */
    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;startSection(Ljava/lang/String;)V", shift = At.Shift.BEFORE))
    private void injectClientTickPre(CallbackInfo ci) {
        this.mcProfiler.startSection("modClientTickPre");
        new EventClientTick(Phase.PRE).post();
        this.mcProfiler.endSection();
    }

    /**
     * Injects the post tick event
     *
     * @author isXander
     */
    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;endSection()V", shift = At.Shift.BEFORE))
    private void injectClientTickPost(CallbackInfo ci) {
        this.mcProfiler.endStartSection("modClientTickPost");
        new EventClientTick(Phase.POST).post();
    }

    /**
     * OpenGL error checking has been known to cause performance issues.
     *
     * @author isXander
     * @reason Performance reasons
     */
    @Inject(method = "<init>", at = @At("RETURN"))
    private void injectNoGLChecking(GameConfiguration gameConfig, CallbackInfo ci) {
        this.enableGLErrorChecking = false;
    }

    /**
     * @author isXander
     * @reason Event hook
     * @param guiScreenIn screen to open
     */
    @Overwrite
    public void displayGuiScreen(GuiScreen guiScreenIn) {
        EventGuiOpen event = new EventGuiOpen(guiScreenIn);
        if (event.post()) return;
        guiScreenIn = event.screen;

        if (this.currentScreen != null) {
            this.currentScreen.onGuiClosed();
        }

        if (guiScreenIn == null && this.theWorld == null) {
            guiScreenIn = new GuiMainMenu();
        }
        else if (guiScreenIn == null && this.thePlayer.getHealth() <= 0.0F) {
            guiScreenIn = new GuiGameOver();
        }

        if (guiScreenIn instanceof GuiMainMenu) {
            this.gameSettings.showDebugInfo = false;
            this.ingameGUI.getChatGUI().clearChatMessages();
        }

        this.currentScreen = guiScreenIn;

        if (guiScreenIn != null) {
            this.setIngameNotInFocus();
            ScaledResolution scaledresolution = new ScaledResolution((Minecraft) (Object) this);
            int i = scaledresolution.getScaledWidth();
            int j = scaledresolution.getScaledHeight();
            guiScreenIn.setWorldAndResolution((Minecraft) (Object) this, i, j);
            this.skipRenderWorld = false;
        } else {
            this.mcSoundHandler.resumeSounds();
            this.setIngameFocus();
        }
    }

    /**
     * @author isXander
     * @reason Event hook {@link EventClientShutdown}
     */
    @Overwrite
    public void shutdown() {
        EventClientShutdown event = new EventClientShutdown();
        if (!event.post()) {
            Evergreen.shutdown();
            this.running = false;
        }
    }

    /**
     * @author isXander
     * @reason Borderless fullscreen
     */
    @Inject(method = "createDisplay", at = @At("RETURN"))
    private void injectWindowTitle(CallbackInfo ci) {
        Display.setTitle("Evergreen " + theMinecraft.getVersion() + "-" + Evergreen.VERSION);
    }

//    /**
//     * @author isXander
//     * @reason Custom icon
//     */
//    @Overwrite
//    private void setWindowIcon() {
//        Util.EnumOS os = Util.getOSType();
//
//        if (os != Util.EnumOS.OSX) {
//            InputStream lowRes = null;
//            InputStream highRes = null;
//
//            try {
//
//                lowRes = this.getResourceManager().getResource(new ResourceLocation("evergreen/misc/icon_16x.png")).getInputStream();
//                highRes = this.getResourceManager().getResource(new ResourceLocation("evergreen/misc/icon_32x.png")).getInputStream();
//
//                if (lowRes != null && highRes != null) {
//                    Display.setIcon(new ByteBuffer[] {this.readImageToBuffer(lowRes), this.readImageToBuffer(highRes)});
//                }
//            }
//            catch (IOException ioexception) {
//                logger.error("Couldn't set icon", ioexception);
//            }
//            finally
//            {
//                IOUtils.closeQuietly(lowRes);
//                IOUtils.closeQuietly(highRes);
//            }
//        }
//    }

}
