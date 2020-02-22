package com.kruczek.enchancer;

import com.kruczek.model.GameStats;

public interface GameQuakeStatsProcessor {
    void process(GameStats gameStat, StringBuilder statsToPrint);
}
