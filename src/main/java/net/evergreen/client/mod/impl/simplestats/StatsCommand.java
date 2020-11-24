package net.evergreen.client.mod.impl.simplestats;

import net.evergreen.client.command.CommandBase;
import net.evergreen.client.utils.Multithreading;
import net.evergreen.client.utils.json.BetterJsonObject;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.PlayerReply;
import net.hypixel.api.util.ILeveling;
import net.minecraft.client.Minecraft;
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
    private final String prefix = "§9[§6SS§9]§f ";

    @Override
    public List<String> getAliases() {
        return Arrays.asList("stats");
    }

    @Override
    public String getCommandUsage() {
        return "/stats [player]";
    }

    @Override
    public void processCommand(List<String> args) {
        if (args != null) {
            Multithreading.runAsync(() -> {
                switch (args.size()) {
                    case 0:
                        err("/stats [player]");
                        break;
                    case 1:
                        String player = args.get(0);
                        try {
                            HypixelAPI api = new HypixelAPI(UUID.fromString(SimpleStats.API_KEY));
                            PlayerReply playerReply = api.getPlayerByName(player).get();
                            if (playerReply.isSuccess()) {
                                BetterJsonObject obj = new BetterJsonObject(playerReply.getPlayer());
                                breakLine();
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
                                breakLine();
                            } else {
                                err("Failed to get stats of " + player + ": " + playerReply.getCause());
                            }
                            api.shutdown();
                        } catch (Exception e) {
                            err("An error occurred. See logs for more details");
                            e.printStackTrace();
                        }
                        break;
                    default:
                        err("This function has not yet been implemented.");
                        // TODO other games

                }
            });
        }
    }

    private void err(String message) {
        put(prefix + "§c" + message);
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

    private void breakLine() {
        StringBuilder dashes = new StringBuilder();
        double dash = Math.floor((280 * mc.gameSettings.chatWidth + 40) / 320 * (1 / mc.gameSettings.chatScale * 53));
        for (int i = 0; i < dash; i++) {
            dashes.append('-');
        }
        put("§9§m" + dashes.toString());
    }

}
