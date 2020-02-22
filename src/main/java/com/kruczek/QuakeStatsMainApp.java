package com.kruczek;

public class QuakeStatsMainApp {

    public static void main(String[] args) {
        QuakeStatsService quakeStatsService = new QuakeStatsService();
        quakeStatsService.process(args);
    }
}
