package main.dto;

import java.io.Serializable;

public class PlayerData implements Serializable {
    private static final long serialVersionUID = 110L;
    private int playerId;
    private String playerName;

    public PlayerData(String playerName) {
        this.playerName = playerName;
    }

    public PlayerData(int playerId) {
        this.playerId = playerId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public String getPlayerName() {
        return playerName;
    }
}
