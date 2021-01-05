/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.client.utils;

import com.mojang.authlib.Agent;
import com.mojang.authlib.AuthenticationService;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.util.UUIDTypeAdapter;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

import java.lang.reflect.Field;
import java.util.UUID;

/**
 * Utility class for changing sessions
 *
 * @author isXander
 */
public class SessionChanger {

    private static SessionChanger instance;
    private final UserAuthentication auth;

    public static SessionChanger getInstance() {
        if (instance == null)
            instance = new SessionChanger();
        return instance;
    }

    /**
     * Create new authentication service
     */
    private SessionChanger() {
        UUID notSureWhyINeedThis = UUID.randomUUID(); //Idk, needs a UUID. Seems to be fine making it random
        AuthenticationService authService = new YggdrasilAuthenticationService(Minecraft.getMinecraft().getProxy(), notSureWhyINeedThis.toString());
        auth = authService.createUserAuthentication(Agent.MINECRAFT);
        authService.createMinecraftSessionService();
    }


    /**
     * Online Mode
     *
     * @param email email of user
     * @param password password of user
     */
    public void setUser(String email, String password) {
        if(!Minecraft.getMinecraft().getSession().getUsername().equals(email) || Minecraft.getMinecraft().getSession().getToken().equals("0")){

            this.auth.logOut();
            this.auth.setUsername(email);
            this.auth.setPassword(password);
            try {
                this.auth.logIn();
                Session session = new Session(this.auth.getSelectedProfile().getName(), UUIDTypeAdapter.fromUUID(auth.getSelectedProfile().getId()), this.auth.getAuthenticatedToken(), this.auth.getUserType().getName());
                setSession(session);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Sets session for mc
     * @param session new session
     */
    private void setSession(Session session) {
        try {
            Field f = ReflectionCache.getField("session", "field_71449_j");
            f.setAccessible(true);
            f.set(Minecraft.getMinecraft(), session);
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Offline Mode login session
     * @param username username of player
     */
    public void setUserOffline(String username) {
        this.auth.logOut();
        Session session = new Session(username, username, "0", "legacy");
        setSession(session);
    }

}
