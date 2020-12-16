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

package net.evergreen.client.gui.screens.resourcepack.utils;

import net.evergreen.client.gui.screens.resourcepack.GuiResourcePacks;
import net.minecraft.client.resources.ResourcePackListEntryFound;

public abstract class ResourcePackListEntryCustom extends ResourcePackListEntryFound {

    public ResourcePackListEntryCustom(GuiResourcePacks ownerScreen){
        super(ownerScreen,null);
    }

    @Override
    public abstract void func_148313_c();

    @Override
    public abstract String func_148311_a();

    @Override
    public abstract String func_148312_b();

    @Override
    public boolean func_148310_d(){
        return super.func_148310_d();
    }

    @Override
    public boolean func_148307_h(){
        return super.func_148307_h();
    }

    @Override
    public boolean func_148308_f(){
        return super.func_148308_f();
    }

    @Override
    public boolean func_148309_e(){
        return super.func_148309_e();
    }

    @Override
    public boolean func_148314_g(){
        return super.func_148314_g();
    }

}
