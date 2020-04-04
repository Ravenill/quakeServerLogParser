package com.kruczek.enchancer.processor.round;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.kruczek.helper.Helper;
import com.kruczek.model.PlayerStats;
import com.kruczek.model.RoundStats;

import static java.util.stream.Collectors.toMap;

public class RoundMainTableProcessor implements RoundQuakeStatsProcessor {

    @Override
    public StringBuilder process(RoundStats roundStat) {
        StringBuilder statsToPrint = new StringBuilder();

        LinkedHashMap<Integer, Pair<String, PlayerStats>> sortedRoundMapByPointsDesc = roundStat.getPlayersStats().entrySet().stream()
                .sorted(Comparator.comparingInt(this::compareByScoreDesc))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        statsToPrint.append(String.format("%20s|%3s|%3s|%3s|%6s|%6s",
                "Name",
                "K",
                "D",
                "S",
                "Score",
                "K/D")).append("\n");

        statsToPrint.append(String.format("%20s|%3s|%3s|%3s|%6s|%6s",
                "--------------------",
                "---",
                "---",
                "---",
                "------",
                "------")).append("\n");

        for (Map.Entry<Integer, Pair<String, PlayerStats>> idToStats : sortedRoundMapByPointsDesc.entrySet()) {
            Pair<String, PlayerStats> nameAndStats = idToStats.getValue();
            String playerName = nameAndStats.getLeft();
            PlayerStats stats = nameAndStats.getRight();

            boolean shouldProcessWithoutCalculatingRatio = stats.getDeaths() == 0 && stats.getSuicides() == 0;

            if (shouldProcessWithoutCalculatingRatio) {
                final boolean isProfessional = stats.getKills() != 0;
                final String kdRatioInfo = isProfessional ? "Inf" : "---";

                statsToPrint.append(String.format("%20s|%3d|%3d|%3d|%6d|%6s",
                        playerName,
                        stats.getKills(),
                        stats.getDeaths(),
                        stats.getSuicides(),
                        stats.getPoints(),
                        kdRatioInfo)).append("\n");
            } else {
                final double v = Helper.generateKillToDeathRatio(stats);
                statsToPrint.append(String.format("%20s|%3d|%3d|%3d|%6d|%6.2f",
                        playerName,
                        stats.getKills(),
                        stats.getDeaths(),
                        stats.getSuicides(),
                        stats.getPoints(),
                        v)).append("\n");
            }
        }

        statsToPrint.append("\n");

        return statsToPrint;
    }

    private int compareByScoreDesc(Map.Entry<Integer, Pair<String, PlayerStats>> playerMap) {
        return -playerMap.getValue().getRight().getPoints();
    }

    private double compareByKillToDeathRatioDesc(Map.Entry<Integer, Pair<String, PlayerStats>> mapPlayerIdWithStats) {
        PlayerStats stats = mapPlayerIdWithStats.getValue().getRight();
        return -Helper.generateKillToDeathRatio(stats);
    }
}
