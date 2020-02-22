package com.kruczek;

import java.io.File;

import com.kruczek.enchancer.StatsGenerator;
import com.kruczek.model.GameStats;
import com.kruczek.parser.QuakeLogsStatsParser;
import com.kruczek.printer.Persister;

public class QuakeStatsService {

    private final QuakeLogsStatsParser quakeLogsStatsParser = new QuakeLogsStatsParser();
    private final StatsGenerator statsGenerator = new StatsGenerator();
    private final Persister persister = new Persister();

    public void process(String[] args) {
        if (args == null || args.length > 2) {
            return;
        }

        String logFilePath = args[0];
        File quakeLogsFile = new File(logFilePath);

        GameStats gameStats = quakeLogsStatsParser.parseFileToStats(quakeLogsFile);
        String statsToPrint = statsGenerator.process(gameStats);

        if (args.length == 2) {
            String saveStatsPath = args[1];
            persister.printAndSaveToFile(statsToPrint, saveStatsPath);
        } else {
            persister.printAndSaveToFile(statsToPrint);
        }

    }
}


