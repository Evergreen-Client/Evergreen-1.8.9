package net.evergreen.client.setting;

/**
 * Setting that contains boolean value
 * Displayed like a switch
 *
 * @author isXander
 */
public class BooleanSetting extends Setting {

    private boolean value;

    public BooleanSetting(boolean def, String displayName, String description) {
        super(displayName, description);
        this.value = def;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

}
