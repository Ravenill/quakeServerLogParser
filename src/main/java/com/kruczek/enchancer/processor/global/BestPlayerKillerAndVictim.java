package com.kruczek.enchancer.processor.global;

import com.kruczek.model.AggregatedGameStats;

public class BestPlayerKillerAndVictim implements GlobalQuakeStatsProcessor {

    @Override
    public StringBuilder process(AggregatedGameStats gameStat) {
        StringBuilder statsToPrint = new StringBuilder();

        final String bestPlayerName = gameStat.getPlayerNameScoreMapDesc().keySet().stream().findFirst().orElse("Zenon");

        final String bestPlayerKillerName = gameStat.getBestPlayerKillerDesc().keySet().stream().findFirst().orElse("Adyen");
        final String bestPlayerVictimName = gameStat.getBestPlayerVictimDesc().keySet().stream().findFirst().orElse("Żona");

        statsToPrint.append("Oficjalnie ").append(bestPlayerKillerName).append(" stał(a) się dzisiejszym koszmarem króla, który wąchał przez niego/nią ")
                .append(gameStat.getBestPlayerKillerDesc().get(bestPlayerKillerName)).append(" razy kwiatki od spodu xD").append("\n\n");

        statsToPrint.append(bestPlayerVictimName).append(" robił(a) za manekina treningowego dla gracza ").append(bestPlayerName)
                .append(", który zaszlachtował(a) go dzisiaj ").append(gameStat.getBestPlayerVictimDesc().get(bestPlayerVictimName)).append(" raz(y)!\n")
                .append("Przypomina wam to trochę rzeźnię dla świń?\n\n");

        return statsToPrint;
    }
}
