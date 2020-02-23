package com.kruczek.enchancer.processor.global;

import java.util.Map;

import com.kruczek.helper.Helper;
import com.kruczek.model.AggregatedGameStats;

public class BestPerformance implements GlobalQuakeStatsProcessor {
    @Override
    public void processAndFill(AggregatedGameStats gameStat, StringBuilder statsToPrint) {
        final Map<String, Double> bestPerformanceMap = gameStat.getPlayerNameBestPerformanceMapDesc();
        final int amountOfRounds = gameStat.getAmountOfRounds();

        final String bestPerformancePlayerName = bestPerformanceMap.keySet().stream().findFirst().orElse("Rozpierdzielator");
        final boolean shouldCountRatio = bestPerformanceMap.get(bestPerformancePlayerName) != null && amountOfRounds != 0;
        final boolean isPerformanceInfinite = shouldCountRatio && (bestPerformanceMap.get(bestPerformancePlayerName) >= Helper.MAX_KD_RATIO);

        final double bestPerformanceRatio = shouldCountRatio ? bestPerformanceMap.get(bestPerformancePlayerName) / amountOfRounds : 0;
        statsToPrint.append("Prawdziwą klasę pokazał(a) jednak ").append(bestPerformancePlayerName).append(" odstawiając niemałe, jebane szoł...\n")
                .append("Zakończył(a) grę ze średnim K/D ratio wynoszącym ");

        if (isPerformanceInfinite) {
            statsToPrint.append("tyle, że się wam w pałach to nie zmieści... Po prostu was rozpierdolił(a) xD");
        } else {
            statsToPrint.append(String.format("%-5.2f\n\n", bestPerformanceRatio));
        }
    }
}
