package com.kruczek.enchancer.processor.global;

import java.util.Map;

import com.kruczek.model.AggregatedGameStats;

public class BestJukes implements GlobalQuakeStatsProcessor {
    @Override
    public void processAndFill(AggregatedGameStats gameStat, StringBuilder statsToPrint) {
        final Map<String, Integer> playerDeathsMap = gameStat.getPlayerNameDeathsMapDesc();

        final int toLastRecord = Math.max(playerDeathsMap.size() - 1, 0);
        String bestJukesPlayerName = playerDeathsMap.keySet().stream().skip(toLastRecord).findFirst().orElse("Zając");

        statsToPrint.append("Zającem dzisiejszego meczu - tym zwinnym pajacem, który oszukiwał przeznaczenie raz po raz jest ")
                .append(bestJukesPlayerName).append(".\nPrzez całą grę zaszlachtowano go/ją tylko ")
                .append(playerDeathsMap.get(bestJukesPlayerName)).append(" raz(y). To skill w zwinności, czy zwykły fart nooba?\n\n");
    }
}
