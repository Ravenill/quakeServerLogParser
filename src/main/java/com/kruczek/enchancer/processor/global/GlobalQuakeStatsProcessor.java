package com.kruczek.enchancer.processor.global;

import com.kruczek.model.AggregatedGameStats;

public interface GlobalQuakeStatsProcessor {
    void processAndFill(AggregatedGameStats gameStat, StringBuilder statsToPrint);
}
