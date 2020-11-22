package cc.hyperium.event;

public enum Phase {
    PRE("Pre"),
    POST("Post");

    public String name;

    Phase(String name) {
        this.name = name;
    }
}
