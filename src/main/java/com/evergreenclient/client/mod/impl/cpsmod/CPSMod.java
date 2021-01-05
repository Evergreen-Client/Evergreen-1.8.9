/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.client.mod.impl.cpsmod;

import com.evergreenclient.client.Evergreen;
import com.evergreenclient.client.event.EventClientTick;
import com.evergreenclient.client.event.EventRenderGameOverlay;
import com.evergreenclient.client.event.bus.Phase;
import com.evergreenclient.client.event.bus.SubscribeEvent;
import com.evergreenclient.client.mod.HUD;
import com.evergreenclient.client.mod.Mod;
import com.evergreenclient.client.mod.ModMeta;
import com.evergreenclient.client.setting.Setting;
import com.evergreenclient.client.setting.SettingField;
import com.evergreenclient.client.utils.StringUtils;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Mouse;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

@HUD
public class CPSMod extends Mod {

    @Override
    protected Mod getSelf() {
        return this;
    }

    @Override
    public ModMeta getMetadata() {
        return new ModMeta("CPS Mod", "Displays the CPS you are getting on the screen.", ModMeta.Category.GRAPHIC, null);
    }

    @SettingField(type = Setting.PropertyType.COLOR, name = "Text Color", description = "The color of the text.")
    public Color textColor = new Color(255, 255, 255);

    @SettingField(type = Setting.PropertyType.COLOR, name = "Background Color", description = "The color of the background of the text.")
    public Color bgColor = new Color(0, 0, 0, 100);

    @SettingField(type = Setting.PropertyType.BOOLEAN, name = "Text Shadow", description = "Determines the text shadow.")
    public Boolean textShadow = true;

    private final List<Long> leftClicks  = new ArrayList<>();
    private final List<Long> rightClicks = new ArrayList<>();

    private boolean left;
    private boolean right;

    @Override
    public void initialise() {
        Evergreen.EVENT_BUS.register(this);
    }

    @Override
    public void render(EventRenderGameOverlay event) {
        Mouse.poll();
        String text = String.format("%s%s | %s%s", bgColor.getAlpha() == 0 ? "[" : "", getLeft(), getRight(), bgColor.getAlpha() == 0 ? "]" : "");
        int width = mc.fontRendererObj.getStringWidth(text);
        Gui.drawRect(x - (width / 2) - 3,  y - (mc.fontRendererObj.FONT_HEIGHT / 4), x + (width / 2) + 3, y + mc.fontRendererObj.FONT_HEIGHT + (mc.fontRendererObj.FONT_HEIGHT / 4), bgColor.getRGB());
        StringUtils.drawCenteredString(mc.fontRendererObj, text, x, y, textColor.getRGB(), textShadow);
    }

    private int getLeft() {
        long time = System.currentTimeMillis();
        leftClicks.removeIf(o -> o + 1000L < time);
        return leftClicks.size();
    }

    private int getRight() {
        long time = System.currentTimeMillis();
        rightClicks.removeIf(o -> o + 1000L < time);
        return rightClicks.size();
    }

    @SubscribeEvent
    public void onClientTick(EventClientTick event) {
        if (event.phase == Phase.PRE) return;
        Mouse.poll();
        boolean down = Mouse.isButtonDown(mc.gameSettings.keyBindAttack.getKeyCode());
        if (down != left && down) leftClicks.add(System.currentTimeMillis());
        left = down;
        down = Mouse.isButtonDown(mc.gameSettings.keyBindUseItem.getKeyCode());
        if (down != right && down) rightClicks.add(System.currentTimeMillis());
        right = down;
    }
}
