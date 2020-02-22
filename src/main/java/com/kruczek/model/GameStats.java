package com.kruczek.model;

import java.util.ArrayList;
import java.util.List;

public class GameStats {
    private int actualRound = -1;
    List<RoundStats> rounds = new ArrayList<>();

    public void addNewGame() {
        actualRound++;
        rounds.add(new RoundStats());
    }

    public List<RoundStats> getRoundStats() {
        return rounds;
    }

    public RoundStats getActualRoundStats() {
        if (actualRound < 0) {
            throw new RuntimeException("Game not started");
        }

        return rounds.get(actualRound);
    }
}
