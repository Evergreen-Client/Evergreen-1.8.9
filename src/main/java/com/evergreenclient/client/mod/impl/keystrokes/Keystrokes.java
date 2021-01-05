/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.client.mod.impl.keystrokes;

import com.evergreenclient.client.Evergreen;
import com.evergreenclient.client.event.EventRenderGameOverlay;
import com.evergreenclient.client.event.bus.SubscribeEvent;
import com.evergreenclient.client.mod.HUD;
import com.evergreenclient.client.mod.Mod;
import com.evergreenclient.client.mod.ModMeta;
import com.evergreenclient.client.mod.impl.keystrokes.util.KeystrokesKey;
import com.evergreenclient.client.setting.SettingField;
import com.evergreenclient.client.utils.StringUtils;
import com.evergreenclient.client.setting.Setting;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@HUD
public class Keystrokes extends Mod {

    @Override
    public ModMeta getMetadata() {
        return new ModMeta("Keystrokes", "Displays your keypresses on screen.", ModMeta.Category.GRAPHIC, null);
    }

    @Override
    protected Mod getSelf() {
        return this;
    }

    @SettingField(type = Setting.PropertyType.COLOR, name = "Background Color", description = "Determines the color of the background of the keys.")
    public Color backColor = new Color(0, 0, 0, 100);

    @SettingField(type = Setting.PropertyType.COLOR, name = "Background Color Pressed", description = "Determines the color the background of the keys when they are pressed.")
    public Color backColorPressed = new Color(255, 255, 255, 100);

    @SettingField(type = Setting.PropertyType.COLOR, name = "Text Color", description = "Determines the color of the text in a key.")
    public Color textColor = new Color(255, 255, 255, 255);

    @SettingField(type = Setting.PropertyType.COLOR, name = "Text Color Pressed", description = "Determines the color of the text in a key when it is pressed.")
    public Color textColorPressed = new Color(0, 220, 0, 255);

    @SettingField(type = Setting.PropertyType.BOOLEAN, name = "Literal Keys", description = "Shows the actual key names rather than their abbreviations.")
    public Boolean literalKeys = false;

    public List<KeystrokesKey> keys = new ArrayList<>();

    @Override
    public void initialise() {
        keys.add(new KeystrokesKey(mc.gameSettings.keyBindForward.getKeyCode(), 25, 25, 25 + 2, 0));
        keys.add(new KeystrokesKey(mc.gameSettings.keyBindLeft.getKeyCode(), 25, 25, 0, 25 + 2));
        keys.add(new KeystrokesKey(mc.gameSettings.keyBindRight.getKeyCode(), 25, 25, 50 + 2 + 2, 25 + 2));
        keys.add(new KeystrokesKey(mc.gameSettings.keyBindJump.getKeyCode(), 81, 20, 0, 75 + 2 + 2 + 2));

        Evergreen.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRender(EventRenderGameOverlay event) {
        if (!isEnabled()) return;

        for (KeystrokesKey key : keys) {
            boolean pressed = isButtonDown(key.key);
            Gui.drawRect(x + key.x, y + key.y, x + key.x + key.width, y + key.y + key.height, (pressed ? backColorPressed.getRGB() : backColor.getRGB()));
            StringUtils.drawCenteredString(mc.fontRendererObj, getKeyOrMouseName(key.key), x + key.x - (key.width / 2f), y + key.y - (key.height / 2f) - (mc.fontRendererObj.FONT_HEIGHT / 2f), (pressed ? textColorPressed.getRGB() : textColor.getRGB()), true);
        }
    }

    private boolean isButtonDown(int code) {
        if (code < 0)
            return Mouse.isButtonDown(code + 100);
        return code > 0 && Keyboard.isKeyDown(code);
    }

    private String getKeyOrMouseName(int keyCode) {
        if (keyCode < 0) {
            String glName = Mouse.getButtonName(keyCode + 100);
            if (glName != null) {
                if (glName.equalsIgnoreCase("button0"))
                    return "LMB";
                if (glName.equalsIgnoreCase("button1"))
                    return "RMB";
            }
            return glName;
        }
        if (literalKeys) {
            switch (keyCode) {
                case 41: {
                    return "~";
                }
                case 12:
                case 74: {
                    return "-";
                }
                case 40: {
                    return "'";
                }
                case 26: {
                    return "[";
                }
                case 27: {
                    return "]";
                }
                case 43: {
                    return "\\";
                }
                case 53:
                case 181: {
                    return "/";
                }
                case 51: {
                    return ",";
                }
                case 52: {
                    return ".";
                }
                case 39: {
                    return ";";
                }
                case 13: {
                    return "=";
                }
                case 200: {
                    return "\u25b2";
                }
                case 208: {
                    return "\u25bc";
                }
                case 203: {
                    return "\u25c0";
                }
                case 205: {
                    return "\u25b6";
                }
                case 55: {
                    return "*";
                }
                case 78: {
                    return "+";
                }
            }
        }
        return Keyboard.getKeyName(keyCode);
    }
}
