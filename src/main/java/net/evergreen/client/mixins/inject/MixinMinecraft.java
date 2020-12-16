/*
 * Copyright [2020] [Evergreen]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.evergreen.client.mixins.inject;

import net.evergreen.client.event.*;
import net.evergreen.client.event.bus.Phase;
import net.evergreen.client.Evergreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.*;
import net.minecraft.client.main.GameConfiguration;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.Timer;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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

    @Shadow private volatile boolean running;

    @Inject(method = "startGame", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;mcLanguageManager:Lnet/minecraft/client/resources/LanguageManager;", shift = At.Shift.AFTER))
    private void injectModPreInit(CallbackInfo ci) {
        Evergreen.getInstance().preInit();
        new EventModInitialization.Pre().post();
    }

    @Inject(method = "startGame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;checkGLError(Ljava/lang/String;)V", shift = At.Shift.BEFORE))
    private void injectModInit(CallbackInfo ci) {
        Evergreen.getInstance().init();
        new EventModInitialization.Post().post();
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
        if (!event.post())
            this.running = false;
    }

}
