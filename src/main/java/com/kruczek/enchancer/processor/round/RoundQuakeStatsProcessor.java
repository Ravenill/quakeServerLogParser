package com.kruczek.enchancer.processor.round;

import com.kruczek.model.RoundStats;

public interface RoundQuakeStatsProcessor {
    void processAndFill(RoundStats roundStat, StringBuilder statsToPrint);
}
