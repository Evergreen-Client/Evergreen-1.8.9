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
