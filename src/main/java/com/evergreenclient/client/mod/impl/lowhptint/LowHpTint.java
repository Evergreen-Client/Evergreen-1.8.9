/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.client.mod.impl.lowhptint;

import com.evergreenclient.client.event.EventRenderGameOverlay;
import com.evergreenclient.client.event.bus.SubscribeEvent;
import com.evergreenclient.client.setting.SettingField;
import com.evergreenclient.client.utils.MathUtils;
import com.evergreenclient.client.Evergreen;
import com.evergreenclient.client.mod.Mod;
import com.evergreenclient.client.mod.ModMeta;
import com.evergreenclient.client.setting.Setting;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class LowHpTint extends Mod {

    @Override
    public ModMeta getMetadata() {
        return new ModMeta("Low Hp Tint", "Adds vignette to your screen when your health gets low.", ModMeta.Category.GRAPHIC, null);
    }

    private static final ResourceLocation vignette = new ResourceLocation("evergreen/mods/lowhptint/tintshape.png");

    @SettingField(
            type = Setting.PropertyType.INTEGER,
            name = "Health",
            description = "When the tint starts to appear.",
            min = 1, max = 20
    )
    public Integer health = 5;

    @SettingField(
            type = Setting.PropertyType.COLOR,
            name = "Color",
            description = "Color of the tint."
    )
    public Color color = new Color(255, 0, 0);

    @SettingField(
            type = Setting.PropertyType.INTEGER,
            name = "Animation Speed",
            description = "Determines how fast the tint appears after your health is below the set level.",
            min = 1, max = 20
    )
    public Integer animationSpeed = 10;

    private float prevRed = 1;
    private float prevGreen = 1;
    private float prevBlue = 1;

    @Override
    public void initialise() {
        Evergreen.EVENT_BUS.register(this);
    }

    @Override
    protected Mod getSelf() {
        return this;
    }

    @SubscribeEvent
    public void onRenderHealth(EventRenderGameOverlay.Pre event) {
        if (event.getType() == EventRenderGameOverlay.ElementType.FOOD) {
            if (isEnabled())
                renderTint(mc.thePlayer.getHealth(), new ScaledResolution(mc), event.getPartialTicks());
        }
    }

    private void renderTint(float currentHealth, ScaledResolution res, float partialTicks) {
        float threshold = health;
        if (currentHealth <= threshold) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.depthMask(false);
            GlStateManager.tryBlendFuncSeparate(0, 769, 1, 0);
            float f = (threshold - currentHealth) / threshold + 1.0F / threshold * 2.0F;
            float r = prevRed   = MathUtils.lerp(prevRed,   MathUtils.lerp(MathUtils.getPercent(color.getRed(),   0, 255), 0.0f, f), partialTicks * (animationSpeed / 1000f));
            float g = prevGreen = MathUtils.lerp(prevGreen, MathUtils.lerp(MathUtils.getPercent(color.getGreen(), 0, 255), 0.0f, f), partialTicks * (animationSpeed / 1000f));
            float b = prevBlue  = MathUtils.lerp(prevBlue,  MathUtils.lerp(MathUtils.getPercent(color.getBlue(),  0, 255), 0.0f, f), partialTicks * (animationSpeed / 1000f));
            GlStateManager.color(1.0f - r, 1.0f - g, 1.0f - b, 1.0f);
            mc.getTextureManager().bindTexture(vignette);
            Tessellator tes = Tessellator.getInstance();
            WorldRenderer wr = tes.getWorldRenderer();
            wr.begin(7, DefaultVertexFormats.POSITION_TEX);
            wr.pos(0.0, res.getScaledHeight_double(), -90.0).tex(0.0, 1.0).endVertex();
            wr.pos(res.getScaledWidth_double(), res.getScaledHeight_double(), -90.0).tex(1.0, 1.0).endVertex();
            wr.pos(res.getScaledWidth_double(), 0.0, -90.0).tex(1.0, 0.0).endVertex();
            wr.pos(0.0, 0.0, -90.0).tex(0.0, 0.0).endVertex();
            tes.draw();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }

    }
}
