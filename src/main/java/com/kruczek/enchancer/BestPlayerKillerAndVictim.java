package com.kruczek.enchancer;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.kruczek.helper.Helper;
import com.kruczek.model.GameStats;
import com.kruczek.model.PlayerStats;
import com.kruczek.model.RoundStats;

import static java.util.stream.Collectors.toMap;

class BestPlayerKillerAndVictim implements GameQuakeStatsProcessor {

    @Override
    public void process(GameStats gameStat, StringBuilder statsToPrint) {
        Map<String, Integer> playersScore = new HashMap<>();
        Map<String, Integer> playersKills = new HashMap<>();
        Map<String, Integer> playerNameId = new HashMap<>();

        for (RoundStats roundStat : gameStat.getRoundStats()) {
            roundStat.getPlayersStats().forEach((playerId, playerStats) -> {
                final String playerName = playerStats.getLeft();
                final int score = playerStats.getRight().getPoints();
                final int kills = playerStats.getRight().getKills();

                playersScore.compute(playerName, (key, val) -> val == null ? score : val + score);
                playerNameId.putIfAbsent(playerName, playerId);

                playersKills.compute(playerName, (key, val) -> val == null ? kills : val + kills);
            });
        }

        LinkedHashMap<String, Integer> sortedPlayersScoreDesc = playersScore.entrySet().stream()
                .sorted(Comparator.comparingInt(playerScore -> -playerScore.getValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        String bestPlayerName = sortedPlayersScoreDesc.keySet().stream().findFirst().orElse("");
        String secondBestPlayer = sortedPlayersScoreDesc.keySet().stream().skip(1).findFirst().orElse("");
        String thirdBestPlayer = sortedPlayersScoreDesc.keySet().stream().skip(2).findFirst().orElse("");

        int bestPlayerId = playerNameId.getOrDefault(bestPlayerName, -1);

        LinkedHashMap<String, Integer> sortedPlayersScoreAsc = playersScore.entrySet().stream()
                .sorted(Comparator.comparingInt(Map.Entry::getValue))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        String worstPlayerName = sortedPlayersScoreAsc.keySet().stream().findFirst().orElse("");

        Map<String, Integer> bestPlayerVictim = new HashMap<>();
        Map<String, Integer> bestPlayerKiller = new HashMap<>();
        Map<String, Integer> selfKiller = new HashMap<>();
        Map<String, Double> bestPerformance = new HashMap<>();

        for (RoundStats roundStat : gameStat.getRoundStats()) {
            roundStat.getPlayersStats().forEach((playerId, playerNameAndStats) -> {
                final String playerName = playerNameAndStats.getLeft();

                final PlayerStats playerStats = playerNameAndStats.getRight();
                Map<Integer, Integer> killMap = playerStats.getMapsOfKills();
                Map<Integer, Integer> deathMap = playerStats.getMapsOfDeaths();

                int deaths = deathMap.getOrDefault(bestPlayerId, 0);
                bestPlayerVictim.compute(playerName, (key, value) -> value == null ? deaths : value + deaths);

                int kills = killMap.getOrDefault(bestPlayerId, 0);
                bestPlayerKiller.compute(playerName, (key, value) -> value == null ? kills : value + kills);

                final int suicides = playerStats.getSuicides();
                selfKiller.compute(playerName, (key, value) -> value == null ? suicides : value + suicides);

                final double perf = Helper.generateKillToDeathRatio(playerStats);
                bestPerformance.compute(playerName, (key, value) -> value == null ? perf : value + perf);
            });
        }

        LinkedHashMap<String, Integer> sortedBestPlayerVictimDesc = bestPlayerVictim.entrySet().stream()
                .sorted(Comparator.comparingInt(deaths -> -deaths.getValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        LinkedHashMap<String, Integer> sortedBestPlayerKillerDesc = bestPlayerKiller.entrySet().stream()
                .sorted(Comparator.comparingInt(kills -> -kills.getValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        LinkedHashMap<String, Integer> sortedSelfKillerDesc = selfKiller.entrySet().stream()
                .filter(suicides -> suicides.getValue() != 0)
                .sorted(Comparator.comparingInt(suicides -> -suicides.getValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        LinkedHashMap<String, Double> sortedBestPerformanceDesc = bestPerformance.entrySet().stream()
                .sorted(Comparator.comparingDouble(performance -> -performance.getValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        String bestPlayerVictimName = sortedBestPlayerVictimDesc.keySet().stream().findFirst().orElse("");
        String bestPlayerKillerName = sortedBestPlayerKillerDesc.keySet().stream().findFirst().orElse("");
        String bestSelfKillerName = sortedSelfKillerDesc.keySet().stream().findFirst().orElse("");
        String bestPerformanceName = sortedBestPerformanceDesc.keySet().stream().findFirst().orElse("");

        int amountOfRounds = gameStat.getRoundStats().size();
        statsToPrint.append(bestPlayerName).append(" zaorał dzisiaj system!!!\n")
                .append("Rozpieprzył was w drobny mak tak, że nie wiedzieliście nawet kiedy przebrać portki.\nWyrąbał dzisiaj łącznie punktów ")
                .append(playersScore.get(bestPlayerName)).append(" w przeciągu ").append(amountOfRounds).append(" rund(y)!\n\n");

        statsToPrint.append("Zaraz za dzisiejszym rzeźnikiem orał ").append(secondBestPlayer)
                .append(", niczym\nrolnik ziemniaczane pole, myśląc o zimnym bimberku, zebrał plony w punktach równych ")
                .append(playersScore.get(secondBestPlayer)).append(".\n\n");

        statsToPrint.append("Nie najgorzej radził sobie też dzisiaj ").append(thirdBestPlayer)
                .append(", który z zapałem godnym osoby reanimującej\nMarylę Rodowicz przed sylwestrem, napompował punktów ")
                .append(playersScore.get(thirdBestPlayer)).append(" zamykając dzisiejsze małe kółeczko koniobijców.\n\n");

        statsToPrint.append("Oficjalnie ").append(bestPlayerKillerName).append(" stał się dzisiejszym koszmarem króla, który przez niego wąchał ")
                .append(bestPlayerKiller.get(bestPlayerKillerName)).append(" razy kwiatki od spodu xD").append("\n\n");

        statsToPrint.append(bestPlayerVictimName).append(" robił za manekina treningowego dla gracza ").append(bestPlayerName)
                .append(", który zaszlachtował go dzisiaj ").append(bestPlayerVictim.get(bestPlayerVictimName)).append(" raz(y)!\n")
                .append("Przypomina wam to trochę rzeźnię dla świń?\n\n");

        statsToPrint.append(worstPlayerName).append(" coś Ci dzisiaj nie pykło...\nAle luźne gacie - rozgniotłeś ")
                .append(playersKills.get(worstPlayerName)).append(" biegających robaków pod swoim butem").append("\n\n");

        statsToPrint.append("Największym samofragiem został ").append(bestSelfKillerName)
                .append(". Zabił się ").append(selfKiller.get(bestSelfKillerName)).append(" raz(y) - co za cielak xD").append("\n\n");

        final boolean shouldCountRatio = bestPerformance.get(bestPerformanceName) != null && amountOfRounds != 0;
        final boolean isPerformanceInfinite = shouldCountRatio && (bestPerformance.get(bestPerformanceName) >= Helper.MAX_KD_RATIO);

        final double bestPerformanceRatio = shouldCountRatio ? bestPerformance.get(bestPerformanceName) / amountOfRounds : 0;
        statsToPrint.append("Prawdziwą klasę pokazał jednak ").append(bestPerformanceName).append(" odstawiając niemałe, jebane szoł...\n")
                .append("Zakończył grę ze średnim K/D ratio wynoszącym ");

        if (isPerformanceInfinite) {
            statsToPrint.append("tyle, że się wam w pałach to nie zmieści... Po prostu was rozpierdolił xD");
        } else {
            statsToPrint.append(String.format("%-5.2f\n", bestPerformanceRatio));
        }
    }
}
