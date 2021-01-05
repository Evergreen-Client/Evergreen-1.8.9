/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.client.gui.screens.resourcepack.utils;

import com.evergreenclient.client.gui.screens.resourcepack.GuiResourcePacks;
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
