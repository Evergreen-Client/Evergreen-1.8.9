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

package net.evergreen.client.mod.impl.betterparticles;

import net.evergreen.client.mod.Mod;
import net.evergreen.client.mod.ModMeta;
import net.evergreen.client.setting.NumberSetting;

public class BetterParticles extends Mod {

    @Override
    public ModMeta getMetadata() {
        return new ModMeta("Better Particles", "Improves particle rendering to look much cleaner.", ModMeta.Category.GRAPHIC, null);
    }

    public NumberSetting alphaSpeed;

    @Override
    public void initialise() {
        addSetting(alphaSpeed = new NumberSetting(3, 0, 5, "Opacity Speed", "Changes how fast particles become transparent.", NumberSetting.StoreType.INTEGER, "", ""));
    }
}
