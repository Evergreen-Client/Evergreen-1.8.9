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

package net.evergreen.client.mod.impl.lowhptint;

import net.evergreen.client.Evergreen;
import net.evergreen.client.event.EventRenderGameOverlay;
import net.evergreen.client.event.bus.SubscribeEvent;
import net.evergreen.client.mod.Mod;
import net.evergreen.client.mod.ModMeta;
import net.evergreen.client.setting.ColorSetting;
import net.evergreen.client.setting.NumberSetting;
import net.evergreen.client.utils.MathUtils;
import net.minecraft.client.Minecraft;
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

    private static final ResourceLocation vignette = new ResourceLocation("evergreen","mods/lowhptint/tintshape.png");

    public NumberSetting health;
    public ColorSetting color;
    public NumberSetting animationSpeed;

    private float prevRed = 1;
    private float prevGreen = 1;
    private float prevBlue = 1;

    @Override
    public void initialise() {
        addSetting(health = new NumberSetting(5, 1, 20, "Health", "When the tint starts to appear.", NumberSetting.StoreType.INTEGER, "", " HP"));
        addSetting(color = new ColorSetting(new Color(255, 0, 0), "Color", "Color of the tint.", false));
        addSetting(animationSpeed = new NumberSetting(10, 1, 20, "Animation Speed", "Determines how fast the tint appears after your health is below the set level.", NumberSetting.StoreType.INTEGER, "", ""));
        Evergreen.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRenderHealth(EventRenderGameOverlay.Pre event) {
        if (event.getType() == EventRenderGameOverlay.ElementType.FOOD) {
            if (isEnabled())
                renderTint(mc.thePlayer.getHealth(), new ScaledResolution(mc), event.getPartialTicks());
        }
    }

    private void renderTint(float currentHealth, ScaledResolution res, float partialTicks) {
        float threshold = health.getFloat();
        if (currentHealth <= threshold) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.depthMask(false);
            GlStateManager.tryBlendFuncSeparate(0, 769, 1, 0);
            float f = (threshold - currentHealth) / threshold + 1.0F / threshold * 2.0F;
            float r = prevRed   = MathUtils.lerp(prevRed,   MathUtils.lerp(MathUtils.getPercent(color.getColor().getRed(),   0, 255), 0.0f, f), partialTicks * (animationSpeed.getFloat() / 1000f));
            float g = prevGreen = MathUtils.lerp(prevGreen, MathUtils.lerp(MathUtils.getPercent(color.getColor().getGreen(), 0, 255), 0.0f, f), partialTicks * (animationSpeed.getFloat() / 1000f));
            float b = prevBlue  = MathUtils.lerp(prevBlue,  MathUtils.lerp(MathUtils.getPercent(color.getColor().getBlue(),  0, 255), 0.0f, f), partialTicks * (animationSpeed.getFloat() / 1000f));
            GlStateManager.color(1.0f - r, 1.0f - g, 1.0f - b, 1.0f);
            Minecraft.getMinecraft().getTextureManager().bindTexture(vignette);
            Tessellator tes = Tessellator.getInstance();
            WorldRenderer wr = tes.getWorldRenderer();
            wr.begin(7, DefaultVertexFormats.POSITION_TEX);
            wr.pos(0.0, res.getScaledHeight_double(), -90.0).tex(0.0, 1.0).endVertex();
            wr.pos(res.getScaledWidth_double(), res.getScaledHeight_double(), -90.0).tex(1.0, 1.0).endVertex();
            wr.pos(res.getScaledWidth_double(), 0.0, -90.0).tex(1.0, 0.0).endVertex();
            wr.pos(0.0, 0.0, -90.0).tex(0.0, 0.0).endVertex();
            tes.draw();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }

    }
}
