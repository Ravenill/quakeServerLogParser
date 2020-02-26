package com.kruczek.model;

import java.util.HashMap;
import java.util.Map;

public class PlayerStats {
    private int kills;
    private int deaths;
    private int suicides;

    private int points;

    private Map<Integer, Integer> mapsOfDeaths = new HashMap<>();
    private Map<Integer, Integer> mapsOfKills = new HashMap<>();

    private boolean isActive = true;

    public void incKills(int deadPlayerId) {
        kills++;
        points++;
        mapsOfKills.compute(deadPlayerId, (key, val) -> (val == null) ? 1 : val + 1);
    }

    public void incDeaths(int whoKilledMeId) {
        deaths++;
        mapsOfDeaths.compute(whoKilledMeId, (key, val) -> (val == null) ? 1 : val + 1);
    }

    public void incSuicide() {
        suicides++;
        points--;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getPoints() {
        return points;
    }

    public int getSuicides() {
        return suicides;
    }

    public Map<Integer, Integer> getMapsOfDeaths() {
        return mapsOfDeaths;
    }

    public Map<Integer, Integer> getMapsOfKills() {
        return mapsOfKills;
    }

    public boolean isActive() {
        return isActive;
    }

    public void deactivate() {
        isActive = false;
    }
}
