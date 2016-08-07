package fr.aquazus.rushland.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import fr.aquazus.rushland.api.data.PlayerInfo;

public class PlayerLoadedEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;
    private Player player;
    private PlayerInfo playerInfo;

    public PlayerLoadedEvent(Player player, PlayerInfo playerInfo) {
        this.player = player;
        this.playerInfo = playerInfo;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public PlayerInfo getPlayerInfo() {
        return this.playerInfo;
    }
    
    public Player getPlayer() {
        return this.player;
    }
}
