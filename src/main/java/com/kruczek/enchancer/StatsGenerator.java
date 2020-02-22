package com.kruczek.enchancer;

import java.util.Arrays;
import java.util.List;

import com.kruczek.model.GameStats;
import com.kruczek.model.RoundStats;

public class StatsGenerator {

    private final List<RoundQuakeStatsProcessor> roundProcessors;
    private final List<GameQuakeStatsProcessor> globalProcessors;

    public StatsGenerator() {
        roundProcessors = Arrays.asList(
                new EndCauseProcessor(),
                new MainTableProcessor());

        globalProcessors = Arrays.asList(
                new BestPlayerKillerAndVictim());
    }

    public StatsGenerator(List<RoundQuakeStatsProcessor> roundProcessors, List<GameQuakeStatsProcessor> globalProcessors) {
        this.roundProcessors = roundProcessors;
        this.globalProcessors = globalProcessors;
    }

    public String process(GameStats gameStats) {
        StringBuilder statsToPrint = new StringBuilder("TIME IS OVER!!!\nStats:\n\n");

        for (RoundStats roundStat : gameStats.getRoundStats()) {
            roundProcessors.forEach(roundProcessor -> roundProcessor.process(roundStat, statsToPrint));
        }

        statsToPrint.append("\n\nGlobal stats:\n\n");
        globalProcessors.forEach(processor -> processor.process(gameStats, statsToPrint));

        return statsToPrint.toString();
    }
}
