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

import java.util.Arrays;
import java.util.List;

public class ArraySetting extends Setting {

    private int index;
    private final List<String> options;

    public ArraySetting(String displayName, String description, String defaultMode, String... modes) {
        super(displayName, description);
        this.options = Arrays.asList(modes);
        this.index = this.options.indexOf(defaultMode);
    }

    public String get() {
        return options.get(index);
    }

    public void set(String option) {
        index = options.indexOf(option);
    }

    public boolean is(String option) {
        return index == options.indexOf(option);
    }

    public void cycle() {
        if (index < options.size() - 1)
            index++;
        else
            index = 0;
    }
}
