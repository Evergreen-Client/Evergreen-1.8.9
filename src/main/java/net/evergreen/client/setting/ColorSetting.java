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

package net.evergreen.client.setting;

import java.awt.*;

public class ColorSetting extends Setting {

    private Color color;
    private boolean allowAlpha;

    public ColorSetting(Color def, String displayName, String description, boolean allowAlpha) {
        super(displayName, description);
        this.color = def;
        this.allowAlpha = allowAlpha;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean allowAlpha() {
        return allowAlpha;
    }

    public void setAllowAlpha(boolean allowAlpha) {
        this.allowAlpha = allowAlpha;
    }
}
