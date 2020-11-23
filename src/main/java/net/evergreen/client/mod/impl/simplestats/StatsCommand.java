package net.evergreen.client.mod.impl.simplestats;

import com.google.gson.JsonObject;
import net.evergreen.client.utils.Multithreading;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.PlayerReply;
import net.hypixel.api.util.ILeveling;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author Nora Cos
 */
public class StatsCommand extends CommandBase {
    private final Minecraft mc = Minecraft.getMinecraft();
    private final String prefix = "\u00a9[\u00a76SS\u00a79]\u00a7f ";

    @Override
    public String getCommandName() {
        return "stats";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/stats [player]";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args != null) {
            Multithreading.runAsync(() -> {
                switch (args.length) {
                    case 0: {
                        err("/stats [player]");
                    }
                    case 1: {
                        String player = args[0];
                        try {
                            HypixelAPI api = new HypixelAPI(UUID.fromString(SimpleStats.API_KEY));
                            PlayerReply playerReply = api.getPlayerByName(player).get();
                            if (playerReply.isSuccess()) {
                                JsonObject obj = playerReply.getPlayer();
                                breakline();
                                put("Network Level: " + ILeveling.getLevel(obj.get("networkExp").getAsDouble()));
                                put("AP: " + obj.get("achievementPoints").getAsInt());
                                put("Karma: " + obj.get("karma").getAsLong());
                                put("Online: " + (obj.get("lastLogin").getAsLong() > obj.get("lastLogout").getAsLong()));
                                put("First Login: " + parseTime(
                                        new BigInteger(
                                                obj.get("_id").getAsString().substring(0, 8),
                                                16
                                        ).longValue() * 1000)
                                );
                                put("Last Login: " + parseTime(obj.get("lastLogin").getAsLong()));
                                String uuid = obj.get("uuid").getAsString();
                                if (modContributors.contains(uuid)) {
                                    put("SimpleStats Contributor: true");
                                } else if (cuties.contains(uuid)) {
                                    put("\u00a7dCutie \u2764\u00a7r: \u00a7dtrue");
                                }
                                breakline();
                            } else {
                                err("Failed to get stats of " + player + ": " + playerReply.getCause());
                            }
                            api.shutdown();
                        } catch (Exception e) {
                            err("An error occurred. See logs for more details");
                            e.printStackTrace();
                        }
                    }
                    default: {
                        err("Not done yet lol");
                        // TODO other games
                    }
                }
            });
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    private void err(String message) {
        put(prefix + "\u00a7c" + message);
    }

    private void put(String msg) {
        mc.thePlayer.addChatMessage(new ChatComponentText(msg));
    }

    private String parseTime(long timestamp) {
        try {
            return new SimpleDateFormat("dd/MM/yyyy").format(new Date(new Timestamp(timestamp).getTime()));
        } catch (Exception ignored) {
            return "N/A";
        }
    }

    private void breakline() {
        StringBuilder dashes = new StringBuilder();
        double dash = Math.floor((280 * mc.gameSettings.chatWidth + 40) / 320 * (1 / mc.gameSettings.chatScale * 53));
        for (int i = 0; i < dash; i++) {
            dashes.append('-');
        }
        put("\u00a79\u00a7m" + dashes.toString());
    }

    private final List<String> cuties = Arrays.asList(
            "8ec7a40981a247feb0421346c1c9d344", // anna
            "3d077bf2be3141e5bc43c70df2747b6d", // caitlin
            "8693c4710fc946cf908fa0f56814e780", // blake
            "936c14678ae8412ba01efadf62197b25", // eva
            "405b843b387f4134a46ba2e9fd538617", // sarah
            "d33a4d925db84c30a28e528239471102", // gus
            "5a2d21179f6143e39ae751ad0c1d145e"  // ajay but its his alt lol
    );

    private final List<String> modContributors = Arrays.asList(
            "e2db3b87ae5c4b91a04f7d6f5ef51e27", // nora
            "346a22e95a954e978243ca0a1839fd12", // waningmatrix
            "a8659452f56d48198fb265903f0ecbff"  // befell
    );
}
