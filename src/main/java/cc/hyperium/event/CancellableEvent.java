package cc.hyperium.event;

public class CancellableEvent extends Event {

    private boolean cancelled;
    private boolean cancellable = true;

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isCancelled() {
        return cancelled && cancellable;
    }

    protected void setCancellable(boolean state) {
        this.cancellable = state;
    }
}
