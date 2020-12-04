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

import net.evergreen.client.Evergreen;
import net.evergreen.client.mod.impl.transparentarmour.TransparentArmour;
import net.evergreen.client.mod.impl.transparentarmour.TransparentArmourMixinHelper;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LayerArmorBase.class)
public abstract class MixinLayerArmorBase<T extends ModelBase> implements LayerRenderer<EntityLivingBase> {

    @Shadow public abstract ItemStack getCurrentArmor(EntityLivingBase entitylivingbaseIn, int armorSlot);
    @Shadow public abstract T getArmorModel(int armorSlot);
    @Shadow @Final private RendererLivingEntity<?> renderer;
    @Shadow protected abstract void setModelPartVisible(T model, int armorSlot);
    @Shadow protected abstract boolean isSlotForLeggings(int armorSlot);
    @Shadow protected abstract ResourceLocation getArmorResource(ItemArmor p_177181_1_, boolean p_177181_2_);
    @Shadow protected abstract ResourceLocation getArmorResource(ItemArmor p_177178_1_, boolean p_177178_2_, String p_177178_3_);
    @Shadow private float colorB;
    @Shadow private float colorG;
    @Shadow private float colorR;
    @Shadow private boolean skipRenderGlint;
    @Shadow protected abstract void renderGlint(EntityLivingBase entitylivingbaseIn, T modelbaseIn, float p_177183_3_, float p_177183_4_, float partialTicks, float p_177183_6_, float p_177183_7_, float p_177183_8_, float scale);

    /**
     * @author isXander
     * @reason Add functionality of {@link TransparentArmour}
     */
    @Overwrite
    private void renderLayer(EntityLivingBase entitylivingbaseIn, float p_177182_2_, float p_177182_3_, float partialTicks, float p_177182_5_, float p_177182_6_, float p_177182_7_, float scale, int armorSlot) {
        TransparentArmour transparentArmour = TransparentArmourMixinHelper.getMod();

        ItemStack itemstack = this.getCurrentArmor(entitylivingbaseIn, armorSlot);

        if (itemstack != null && itemstack.getItem() instanceof ItemArmor) {
            float alpha = transparentArmour.opacity.getFloat() / 100;
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
