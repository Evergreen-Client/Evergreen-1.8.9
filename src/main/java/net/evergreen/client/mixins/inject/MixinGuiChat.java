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

import net.evergreen.client.command.ClientCommandHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(GuiChat.class)
public abstract class MixinGuiChat {

    @Shadow
    private boolean waitingOnAutocomplete;

    @Shadow
    protected GuiTextField inputField;

    @Shadow
    private List<String> foundPlayerNames;

    @Shadow
    private boolean playerNamesFound;

    @Shadow
    public abstract void autocompletePlayerNames();

    /**
     * @author isXander
     * @reason {@link ClientCommandHandler} autocompletion
     */
    @Overwrite
    private void sendAutocompleteRequest(String leftOfCursor, String p_146405_2_) {
        Minecraft mc = Minecraft.getMinecraft();

        if (leftOfCursor.length() >= 1) {
            ClientCommandHandler.instance.autoComplete(leftOfCursor);
            BlockPos blockpos = null;

            if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                blockpos = mc.objectMouseOver.getBlockPos();
            }

            mc.thePlayer.sendQueue.addToSendQueue(new C14PacketTabComplete(leftOfCursor, blockpos));
            this.waitingOnAutocomplete = true;
        }
    }

    /**
     * @author isXander
     * @reason {@link ClientCommandHandler} autocompletion
     */
    @Overwrite
    public void onAutocompleteResponse(String[] p_146406_1_) {
        if (this.waitingOnAutocomplete) {
            this.playerNamesFound = false;
            this.foundPlayerNames.clear();

            String[] complete = ClientCommandHandler.instance.latestAutoComplete;
            if (complete != null) {
                p_146406_1_ = com.google.common.collect.ObjectArrays.concat(complete, p_146406_1_, String.class);
            }

            for (String s : p_146406_1_) {
                if (s.length() > 0) {
                    this.foundPlayerNames.add(s);
                }
            }

            String s1 = this.inputField.getText().substring(this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false));
            String s2 = StringUtils.getCommonPrefix(p_146406_1_);
            s2 = net.minecraft.util.EnumChatFormatting.getTextWithoutFormattingCodes(s2);

            if (s2.length() > 0 && !s1.equalsIgnoreCase(s2)) {
                this.inputField.deleteFromCursor(this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false) - this.inputField.getCursorPosition());
                this.inputField.writeText(s2);
            } else if (this.foundPlayerNames.size() > 0) {
                this.playerNamesFound = true;
                this.autocompletePlayerNames();
            }
        }
    }

}
