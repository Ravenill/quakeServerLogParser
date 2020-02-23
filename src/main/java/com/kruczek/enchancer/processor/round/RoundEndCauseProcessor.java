package com.kruczek.enchancer.processor.round;

import com.kruczek.model.RoundStats;

public class RoundEndCauseProcessor implements RoundQuakeStatsProcessor {

    @Override
    public void processAndFill(RoundStats roundStat, StringBuilder statsToPrint) {
        statsToPrint.append("End cause: ").append(roundStat.getEndCause()).append("\n");
    }
}
