package com.kruczek.enchancer.processor.round;

import com.kruczek.model.RoundStats;

public class RoundEndCauseProcessor implements RoundQuakeStatsProcessor {

    @Override
    public StringBuilder process(RoundStats roundStat) {
        StringBuilder statsToPrint = new StringBuilder();
        statsToPrint.append("End cause: ").append(roundStat.getEndCause()).append("\n");
        return statsToPrint;
    }
}
