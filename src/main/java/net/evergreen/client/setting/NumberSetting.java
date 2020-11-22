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

public class NumberSetting extends Setting {

    private Number value;
    private final String prefix;
    private final String suffix;
    private final StoreType type;

    public NumberSetting(Number def, String displayName, String description, StoreType type, String prefix, String suffix) {
        super(displayName, description);
        this.value = def;
        this.prefix = prefix;
        this.suffix = suffix;
        this.type = type;
    }

    @Override
    public String getDisplayName() {
        return prefix + displayName + suffix;
    }

    public double getDouble() {
        return value.doubleValue();
    }

    public float getFloat() {
        return value.floatValue();
    }

    public int getInt() {
        return value.intValue();
    }

    public Number getValue() {
        return this.value;
    }

    public void setValue(Number value) {
        this.value = value;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public StoreType getType() {
        return type;
    }

    public enum StoreType {
        INTEGER,
        FLOAT,
        DOUBLE
    }

}
