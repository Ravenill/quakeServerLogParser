package com.kruczek.enchancer;

import com.kruczek.model.RoundStats;

class EndCauseProcessor implements RoundQuakeStatsProcessor {

    @Override
    public void process(RoundStats roundStat, StringBuilder statsToPrint) {
        statsToPrint.append("End cause: ").append(roundStat.getEndCause()).append("\n");
    }
}
