package com.kruczek.enchancer.processor.global;

import java.util.Map;

import com.kruczek.model.AggregatedGameStats;

public class SelfKiller implements GlobalQuakeStatsProcessor {
    @Override
    public void processAndFill(AggregatedGameStats gameStat, StringBuilder statsToPrint) {
        final Map<String, Integer> selfKillsMap = gameStat.getPlayerNameSuicidesMapDesc();

        String bestSelfKillerName = selfKillsMap.keySet().stream().findFirst().orElse("Alfons Baltazar");

        statsToPrint.append("Największym samofragiem został(a) ").append(bestSelfKillerName)
                .append(". Zabił(a) się ").append(selfKillsMap.get(bestSelfKillerName)).append(" raz(y) - co za cielak xD\n\n");
    }
}
