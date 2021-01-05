/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.client.mixins.inject;

import com.evergreenclient.client.Evergreen;
import com.evergreenclient.client.mod.impl.transparentarmour.TransparentArmour;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.*;

@Mixin(LayerArmorBase.class)
public abstract class MixinLayerArmorBase<T extends ModelBase> implements LayerRenderer<EntityLivingBase> {

    @Shadow public abstract ItemStack getCurrentArmor(EntityLivingBase entitylivingbaseIn, int armorSlot);
    @Shadow public abstract T getArmorModel(int armorSlot);
    @Shadow @Final private RendererLivingEntity<?> renderer;
    @Shadow protected abstract void setModelPartVisible(T model, int armorSlot);
    @Shadow protected abstract boolean isSlotForLeggings(int armorSlot);
    @Shadow protected abstract ResourceLocation getArmorResource(ItemArmor p_177181_1_, boolean p_177181_2_);
    @Shadow protected abstract ResourceLocation getArmorResource(ItemArmor p_177178_1_, boolean p_177178_2_, String p_177178_3_);
    @Shadow private float colorR;
    @Shadow private float colorG;
    @Shadow private float colorB;
    @Shadow private boolean skipRenderGlint;
    @Shadow protected abstract void renderGlint(EntityLivingBase entitylivingbaseIn, T modelbaseIn, float p_177183_3_, float p_177183_4_, float partialTicks, float p_177183_6_, float p_177183_7_, float p_177183_8_, float scale);

    /**
     * @author isXander
     * @reason Add functionality of {@link TransparentArmour}
     */
    @Overwrite
    private void renderLayer(EntityLivingBase entitylivingbaseIn, float p_177182_2_, float p_177182_3_, float partialTicks, float p_177182_5_, float p_177182_6_, float p_177182_7_, float scale, int armorSlot) {
        TransparentArmour transparentArmour = Evergreen.getInstance().getModManager().getMod(TransparentArmour.class);

        ItemStack itemstack = this.getCurrentArmor(entitylivingbaseIn, armorSlot);

        if (itemstack != null && itemstack.getItem() instanceof ItemArmor) {
            float alpha = transparentArmour.opacity / 100f;
            ItemArmor itemarmor = (ItemArmor)itemstack.getItem();
            T t = this.getArmorModel(armorSlot);
            t.setModelAttributes(this.renderer.getMainModel());
            t.setLivingAnimations(entitylivingbaseIn, p_177182_2_, p_177182_3_, partialTicks);
            this.setModelPartVisible(t, armorSlot);
            boolean flag = this.isSlotForLeggings(armorSlot);
            this.renderer.bindTexture(this.getArmorResource(itemarmor, flag));
            switch (itemarmor.getArmorMaterial()) {
                case LEATHER:
                    int i = itemarmor.getColor(itemstack);
                    float f = (float)(i >> 16 & 255) / 255.0F;
                    float f1 = (float)(i >> 8 & 255) / 255.0F;
                    float f2 = (float)(i & 255) / 255.0F;
                    GlStateManager.color(this.colorR * f, this.colorG * f1, this.colorB * f2, alpha);
                    t.render(entitylivingbaseIn, p_177182_2_, p_177182_3_, p_177182_5_, p_177182_6_, p_177182_7_, scale);
                    this.renderer.bindTexture(this.getArmorResource(itemarmor, flag, "overlay"));
                case CHAIN:
                case IRON:
                case GOLD:
                case DIAMOND:
                    GlStateManager.color(this.colorR, this.colorG, this.colorB, alpha);
                    t.render(entitylivingbaseIn, p_177182_2_, p_177182_3_, p_177182_5_, p_177182_6_, p_177182_7_, scale);
                default:
                    if (!this.skipRenderGlint && itemstack.isItemEnchanted()) {
                        this.renderGlint(entitylivingbaseIn, t, p_177182_2_, p_177182_3_, partialTicks, p_177182_5_, p_177182_6_, p_177182_7_, scale);
                    }
            }
        }
    }

}
