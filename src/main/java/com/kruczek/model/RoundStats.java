package com.kruczek.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

public class RoundStats {
    private Map<Integer, Pair<String, PlayerStats>> playersStats = new HashMap<>();
    private String endCause = "Server timeout. END OF THE GAME";

    public void addPlayer(int noPlayer, String name) {
        playersStats.putIfAbsent(noPlayer, Pair.of(name, new PlayerStats()));
    }

    public void registerKill(int killerId, int deadId) {
        if (killerId == deadId || killerId > 512) {
            playersStats.get(deadId).getRight().incSuicide();
        } else {
            playersStats.get(killerId).getRight().incKills(deadId);
            playersStats.get(deadId).getRight().incDeaths(killerId);
        }
    }

    public Map<Integer, Pair<String, PlayerStats>> getPlayersStats() {
        return playersStats;
    }

    public void setEndCause(String endCause) {
        this.endCause = endCause;
    }

    public String getEndCause() {
        return endCause;
    }
}
