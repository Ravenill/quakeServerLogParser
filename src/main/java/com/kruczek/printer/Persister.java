package com.kruczek.printer;

import java.io.FileWriter;

import org.apache.commons.lang3.StringUtils;

public class Persister {

    public void printAndSaveToFile(String stats) {
        printAndSaveToFile(stats, null);
    }

    public void printAndSaveToFile(String stats, String saveStatsPath) {
        System.out.println(stats);
        saveToFile(stats, saveStatsPath);
    }

    private void saveToFile(String statsToPrint, String saveStatsPath) {
        String pathToSaveFile = StringUtils.isEmpty(saveStatsPath) ? "/home/ktarczewski/quakeServer/stats/newest" : saveStatsPath;
        System.out.println("Saving file to: " + saveStatsPath);

        try {
            FileWriter myWriter = new FileWriter(pathToSaveFile);
            myWriter.write(statsToPrint);
            myWriter.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
