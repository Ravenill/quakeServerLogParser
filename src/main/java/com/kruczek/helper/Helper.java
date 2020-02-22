package com.kruczek.helper;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import com.kruczek.model.PlayerStats;

public class Helper {

    public static final double MAX_KD_RATIO = 9999;

    public static double generateKillToDeathRatio(PlayerStats stats) {
        final int kills = stats.getKills();
        final int deathsAndSuicides = stats.getDeaths() + stats.getSuicides();

        if (deathsAndSuicides == 0) {
            return kills == 0 ? 0 : MAX_KD_RATIO;
        }

        return BigDecimal.valueOf((float) kills)
                .divide(BigDecimal.valueOf((float) deathsAndSuicides), new MathContext(2, RoundingMode.HALF_UP))
                .doubleValue();
    }
}
