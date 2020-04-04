package com.kruczek.enchancer.processor.global;

import com.kruczek.model.AggregatedGameStats;

public interface GlobalQuakeStatsProcessor {
    StringBuilder process(AggregatedGameStats gameStat);
}
