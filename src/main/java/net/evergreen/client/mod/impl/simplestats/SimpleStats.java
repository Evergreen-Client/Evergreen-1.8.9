package net.evergreen.client.mod.impl.simplestats;

import net.evergreen.client.command.ClientCommandHandler;
import net.evergreen.client.mod.Mod;
import net.evergreen.client.mod.ModMeta;

public class SimpleStats extends Mod {
    // TODO ablility to set API Key in client settings
    public static final String API_KEY = "YOUR-API-KEY-HERE";

    @Override
    public void initialise() {
        ClientCommandHandler.instance.registerCommand(new StatsCommand());
    }

    @Override
    public ModMeta getMetadata() {
        return new ModMeta(
                "SimpleStats",
                "No BS Hypixel stats mod",
                "Nora",
                ModMeta.Category.QOL,
                null
        );
    }
}
