package com.kruczek.enchancer;

import com.kruczek.model.RoundStats;

public interface RoundQuakeStatsProcessor {
    void process(RoundStats roundStat, StringBuilder statsToPrint);
}
