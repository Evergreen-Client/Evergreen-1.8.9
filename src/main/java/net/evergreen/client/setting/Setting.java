package net.evergreen.client.setting;

/**
 * Setting class that can be used for GUIs
 *
 * @author isXander
 */
public class Setting {

    protected String displayName;
    protected final String description;

    public Setting(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String value) {
        this.displayName = value;
    }

    public final String getJsonKeyName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}
