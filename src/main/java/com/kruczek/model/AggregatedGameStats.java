package com.kruczek.model;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.kruczek.helper.Helper;

import static java.util.stream.Collectors.toMap;

public class AggregatedGameStats {
    private Map<String, Integer> playerNameIdMap = new HashMap<>();

    private Map<String, Integer> playerNameScoreMapDesc = new HashMap<>();
    private Map<String, Integer> playerNameKillsMapDesc = new HashMap<>();
    private Map<String, Integer> playerNameDeathsMapDesc = new HashMap<>();
    private Map<String, Integer> playerNameSuicidesMapDesc = new HashMap<>();

    private Map<String, Double> playerNameBestPerformanceMapDesc = new HashMap<>();

    private Map<String, Integer> bestPlayerVictimDesc = new HashMap<>();
    private Map<String, Integer> bestPlayerKillerDesc = new HashMap<>();

    private int amountOfRounds;

    private AggregatedGameStats(){
    }

    public static AggregatedGameStats aggregate(GameStats gameStat) {
        AggregatedGameStats aggregatedGameStats = new AggregatedGameStats();
        fillMaps(gameStat, aggregatedGameStats);
        fillOthers(gameStat, aggregatedGameStats);
        sortMaps(aggregatedGameStats);
        return aggregatedGameStats;
    }

    public Map<String, Integer> getPlayerNameIdMap() {
        return playerNameIdMap;
    }

    public Map<String, Integer> getPlayerNameScoreMapDesc() {
        return playerNameScoreMapDesc;
    }

    public Map<String, Integer> getPlayerNameKillsMapDesc() {
        return playerNameKillsMapDesc;
    }

    public Map<String, Integer> getPlayerNameDeathsMapDesc() {
        return playerNameDeathsMapDesc;
    }

    public Map<String, Integer> getPlayerNameSuicidesMapDesc() {
        return playerNameSuicidesMapDesc;
    }

    public Map<String, Double> getPlayerNameBestPerformanceMapDesc() {
        return playerNameBestPerformanceMapDesc;
    }

    public Map<String, Integer> getBestPlayerVictimDesc() {
        return bestPlayerVictimDesc;
    }

    public Map<String, Integer> getBestPlayerKillerDesc() {
        return bestPlayerKillerDesc;
    }

    public int getAmountOfRounds() {
        return amountOfRounds;
    }

    private static void fillMaps(GameStats gameStat, AggregatedGameStats aggregatedGameStats) {
        for (RoundStats roundStat : gameStat.getRoundStats()) {
            roundStat.getPlayersStats().forEach((playerId, playerNameAndStats) -> {
                final String playerName = playerNameAndStats.getLeft();
                final PlayerStats playerStats = playerNameAndStats.getRight();

                aggregatedGameStats.playerNameIdMap.putIfAbsent(playerName, playerId);

                final int score = playerStats.getPoints();
                aggregatedGameStats.playerNameScoreMapDesc.compute(playerName, (key, val) -> val == null ? score : val + score);

                final int kills = playerStats.getKills();
                aggregatedGameStats.playerNameKillsMapDesc.compute(playerName, (key, val) -> val == null ? kills : val + kills);

                final int deaths = playerStats.getDeaths();
                aggregatedGameStats.playerNameDeathsMapDesc.compute(playerName, (key, val) -> val == null ? deaths : val + deaths);

                final int suicides = playerStats.getSuicides();
                aggregatedGameStats.playerNameSuicidesMapDesc.compute(playerName, (key, val) -> val == null ? suicides : val + suicides);

                final double perf = Helper.generateKillToDeathRatio(playerStats);
                aggregatedGameStats.playerNameBestPerformanceMapDesc.compute(playerName, (key, value) -> value == null ? perf : value + perf);
            });
        }

        String bestPlayerName = aggregatedGameStats.playerNameScoreMapDesc.keySet().stream().findFirst().orElse("");
        int bestPlayerId = aggregatedGameStats.playerNameIdMap.getOrDefault(bestPlayerName, -1);

        for (RoundStats roundStat : gameStat.getRoundStats()) {
            roundStat.getPlayersStats().forEach((playerId, playerNameAndStats) -> {
                final String playerName = playerNameAndStats.getLeft();

                final PlayerStats playerStats = playerNameAndStats.getRight();
                Map<Integer, Integer> killMap = playerStats.getMapsOfKills();
                Map<Integer, Integer> deathMap = playerStats.getMapsOfDeaths();

                int deaths = deathMap.getOrDefault(bestPlayerId, 0);
                aggregatedGameStats.bestPlayerVictimDesc.compute(playerName, (key, value) -> value == null ? deaths : value + deaths);

                int kills = killMap.getOrDefault(bestPlayerId, 0);
                aggregatedGameStats.bestPlayerKillerDesc.compute(playerName, (key, value) -> value == null ? kills : value + kills);
            });
        }
    }

    private static void fillOthers(GameStats gameStat, AggregatedGameStats aggregatedGameStats) {
        aggregatedGameStats.amountOfRounds = gameStat.getRoundStats().size();
    }

    private static void sortMaps(AggregatedGameStats aggregatedGameStats) {
        aggregatedGameStats.playerNameScoreMapDesc = aggregatedGameStats.playerNameScoreMapDesc.entrySet().stream()
                .sorted(Comparator.comparingInt(score -> -score.getValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        aggregatedGameStats.playerNameKillsMapDesc = aggregatedGameStats.playerNameKillsMapDesc.entrySet().stream()
                .sorted(Comparator.comparingInt(kill -> -kill.getValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        aggregatedGameStats.playerNameDeathsMapDesc = aggregatedGameStats.playerNameDeathsMapDesc.entrySet().stream()
                .sorted(Comparator.comparingInt(death -> -death.getValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        aggregatedGameStats.playerNameSuicidesMapDesc = aggregatedGameStats.playerNameSuicidesMapDesc.entrySet().stream()
                .sorted(Comparator.comparingInt(suicide -> -suicide.getValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        aggregatedGameStats.playerNameBestPerformanceMapDesc = aggregatedGameStats.playerNameBestPerformanceMapDesc.entrySet().stream()
                .sorted(Comparator.comparingDouble(perf -> -perf.getValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        aggregatedGameStats.bestPlayerVictimDesc = aggregatedGameStats.bestPlayerVictimDesc.entrySet().stream()
                .sorted(Comparator.comparingInt(deaths -> -deaths.getValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        aggregatedGameStats.bestPlayerKillerDesc = aggregatedGameStats.bestPlayerKillerDesc.entrySet().stream()
                .sorted(Comparator.comparingInt(kills -> -kills.getValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
    }
}
