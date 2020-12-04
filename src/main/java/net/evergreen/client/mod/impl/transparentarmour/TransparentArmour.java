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

package net.evergreen.client.mod.impl.transparentarmour;

import net.evergreen.client.mod.Mod;
import net.evergreen.client.mod.ModMeta;
import net.evergreen.client.setting.NumberSetting;

public class TransparentArmour extends Mod {

    @Override
    public ModMeta getMetadata() {
        return new ModMeta("Transparent Armour", "Allows you to make armour render with an opacity.", ModMeta.Category.GRAPHIC, null);
    }

    public NumberSetting opacity;

    @Override
    public void initialise() {
        addSetting(opacity = new NumberSetting(100, 0, 100, "Armour Opacity", "Opacity percentage for armour.", NumberSetting.StoreType.INTEGER, "", "%"));
    }
}
