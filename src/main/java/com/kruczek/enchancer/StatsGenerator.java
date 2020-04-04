package com.kruczek.enchancer;

import java.util.Arrays;
import java.util.List;

import com.kruczek.enchancer.processor.global.BestJukes;
import com.kruczek.enchancer.processor.global.BestPerformance;
import com.kruczek.enchancer.processor.global.BestPlayerKillerAndVictim;
import com.kruczek.enchancer.processor.global.WorstPlayer;
import com.kruczek.enchancer.processor.round.RoundEndCauseProcessor;
import com.kruczek.enchancer.processor.global.GlobalQuakeStatsProcessor;
import com.kruczek.enchancer.processor.round.RoundMainTableProcessor;
import com.kruczek.enchancer.processor.round.RoundQuakeStatsProcessor;
import com.kruczek.enchancer.processor.global.SelfKiller;
import com.kruczek.enchancer.processor.global.TopGlobalPlayers;
import com.kruczek.model.AggregatedGameStats;
import com.kruczek.model.GameStats;
import com.kruczek.model.RoundStats;

public class StatsGenerator {

    private final List<RoundQuakeStatsProcessor> roundProcessors;
    private final List<GlobalQuakeStatsProcessor> globalProcessors;

    public StatsGenerator() {
        roundProcessors = Arrays.asList(
                new RoundEndCauseProcessor(),
                new RoundMainTableProcessor());

        globalProcessors = Arrays.asList(
                new TopGlobalPlayers(),
                new BestPlayerKillerAndVictim(),
                new WorstPlayer(),
                new SelfKiller(),
                new BestJukes(),
                new BestPerformance());
    }

    public StatsGenerator(List<RoundQuakeStatsProcessor> roundProcessors, List<GlobalQuakeStatsProcessor> globalProcessors) {
        this.roundProcessors = roundProcessors;
        this.globalProcessors = globalProcessors;
    }

    public String process(GameStats gameStats) {
        StringBuilder statsToPrint = new StringBuilder("TIME IS OVER!!!\nStats:\n\n");

        for (RoundStats roundStat : gameStats.getRoundStats()) {
            roundProcessors.forEach(roundProcessor -> statsToPrint.append(roundProcessor.process(roundStat)));
        }

        statsToPrint.append("\n\nGlobal stats:\n\n");
        AggregatedGameStats aggregatedGameStats = AggregatedGameStats.aggregate(gameStats);
        globalProcessors.forEach(processor -> statsToPrint.append(processor.process(aggregatedGameStats)));

        return statsToPrint.toString();
    }
}
