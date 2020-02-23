package com.kruczek.enchancer.processor.global;

import java.util.Map;

import com.kruczek.model.AggregatedGameStats;

public class TopGlobalPlayers implements GlobalQuakeStatsProcessor {
    @Override
    public void processAndFill(AggregatedGameStats gameStat, StringBuilder statsToPrint) {
        final int amountOfRounds = gameStat.getAmountOfRounds();
        final Map<String, Integer> scoreMap = gameStat.getPlayerNameScoreMapDesc();

        String bestPlayerName = scoreMap.keySet().stream().findFirst().orElse("Twoja Stara");
        String secondBestPlayer = scoreMap.keySet().stream().skip(1).findFirst().orElse("Pan Wiesiu");
        String thirdBestPlayer = scoreMap.keySet().stream().skip(2).findFirst().orElse("Kotek Wrocek");

        statsToPrint.append(bestPlayerName).append(" zaorał(a) dzisiaj system!!!\n")
                .append("Rozpieprzył(a) was w drobny mak tak, że nie wiedzieliście nawet kiedy przebrać portki.\nWyrąbał(a) dzisiaj łącznie punktów ")
                .append(scoreMap.get(bestPlayerName)).append(" w przeciągu ").append(amountOfRounds).append(" rund(y)!\n\n");

        statsToPrint.append("Zaraz za dzisiejszym rzeźnikiem orał(a) ").append(secondBestPlayer)
                .append(", niczym\nrolnik ziemniaczane pole, myśląc o zimnym bimberku, zebrał(a) plony w punktach równych ")
                .append(scoreMap.get(secondBestPlayer)).append(".\n\n");

        statsToPrint.append("Nie najgorzej radził(a) sobie też dzisiaj ").append(thirdBestPlayer)
                .append(", który/a z zapałem godnym osoby rozmrażającej\nMarylę Rodowicz przed sylwestrem, zgarnął/ęła punktów ")
                .append(scoreMap.get(thirdBestPlayer)).append(" zamykając dzisiejsze małe kółeczko koniobijców.\n\n");

    }
}
