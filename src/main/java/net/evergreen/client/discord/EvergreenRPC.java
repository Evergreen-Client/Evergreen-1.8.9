/*
 * Copyright (C) [2020] [Evergreen]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under certain conditions
 */

package net.evergreen.client.discord;

import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.RichPresence;
import net.evergreen.client.Evergreen;
import net.evergreen.client.utils.Multithreading;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.OffsetDateTime;

public class EvergreenRPC {

    private static final Logger logger = LogManager.getLogger("Discord RPC");

    public static boolean isConnected = false;
    RichPresence.Builder builder = new RichPresence.Builder();
    IPCClient client = new IPCClient(784773930355261470L);

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    /**
     * @author Hot Tutorials | Hot Tutorials#8262
     */
    public void init() {
        client.setListener(new IPCListener() {
            @Override
            public void onReady(IPCClient client) {
                new Thread(() -> {
                    Multithreading.sleep(5000);
                    while (Evergreen.isRunning()) {
                        setPresence(Minecraft.getMinecraft().getSession().getUsername() + " is getting " + (Minecraft.getDebugFPS() == 0 ? "?" : Minecraft.getDebugFPS()) + " fps", "https://evergreenclient.com/");
                        Multithreading.sleep(60000);
                    }
                }).start();
            }
        });
        try {
            client.connect();
            builder.setStartTimestamp(OffsetDateTime.now());
            client.sendRichPresence(builder.build());
            logger.info("Connected to RPC Client!");
            this.setConnected(true);
        }
        catch (Exception e) {
            e.printStackTrace();
            this.setConnected(false);
        }
        if (isConnected)
            logger.info("Connected to Discord RPC");
        else
            logger.info("Failed to connect to Discord RPC");
    }


    /**
     * @param firstLine  is the firstLine
     * @param secondLine is the secondLine
     * @author Hot Tutorials | Hot Tutorials#8262
     */
    public void setPresence(String firstLine, String secondLine) {
        builder.setDetails(firstLine)
                .setState(secondLine);
        client.sendRichPresence(builder.build());
    }

    /**
     * @param firstLine  is the firstLine
     * @param secondLine is the secondLine
     * @param largeImage is the large image that shows
     * @author Hot Tutorials | Hot Tutorials#8262
     */
    public void setPresence(String firstLine, String secondLine, String largeImage) {
        builder.setDetails(firstLine)
                .setState(secondLine)
                .setLargeImage(largeImage);
        client.sendRichPresence(builder.build());
    }

    /**
     * @param firstLine  is the firstLine
     * @param secondLine is the secondLine
     * @param largeImage is the large image that shows
     * @param smallImage is the image that shows in the bottom right of the largeImage
     * @author Hot Tutorials | Hot Tutorials#8262
     * @since b0.1
     */
    public void setPresence(String firstLine, String secondLine, String largeImage, String smallImage) {
        builder.setDetails(firstLine)
                .setState(secondLine)
                .setLargeImage(largeImage)
                .setSmallImage(smallImage);
        client.sendRichPresence(builder.build());
    }

}
