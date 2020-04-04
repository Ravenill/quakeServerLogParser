package com.kruczek.enchancer.processor.global;

import java.util.Map;

import com.kruczek.model.AggregatedGameStats;

public class WorstPlayer implements GlobalQuakeStatsProcessor {

    @Override
    public StringBuilder process(AggregatedGameStats gameStat) {
        StringBuilder statsToPrint = new StringBuilder();

        final Map<String, Integer> playerScoreMap = gameStat.getPlayerNameScoreMapDesc();

        final int toLastRecord = Math.max(playerScoreMap.size() - 1, 0);
        String worstPlayerName = playerScoreMap.keySet().stream().skip(toLastRecord).findFirst().orElse("Gówniak");

        statsToPrint.append(worstPlayerName).append(" coś Ci dzisiaj nie pykło...\nAle luźne gacie - rozgniotłeś/aś ")
                .append(gameStat.getPlayerNameKillsMapDesc().get(worstPlayerName)).append(" biegających robaków pod swoim butem!\n\n");

        return statsToPrint;
    }
}
