package com.kruczek.parser;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import com.kruczek.model.GameStats;
import com.kruczek.model.RoundStats;

public class QuakeLogsStatsParser {

    public static final String CONNECTED = "ClientUserinfoChanged:";
    public static final String MAY_BE_CONNECTED = "Clien";
    public static final String START = "InitG";
    public static final String KILL = "Kill:";
    public static final String END = "Exit:";

    public GameStats parseFileToStats(File quakeLogs) {
        GameStats gameStats = new GameStats();

        try (LineIterator lineIterator = FileUtils.lineIterator(quakeLogs, "UTF-8")) {
            while (lineIterator.hasNext()) {
                String line = lineIterator.nextLine();
                processLine(line, gameStats);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return gameStats;
    }

    private void processLine(String line, GameStats gameStats) {
        String action = getAction(line);

        switch (action) {
            case KILL:
                parseKillAction(line, gameStats);
                break;

            case CONNECTED:
                parsePlayer(line, gameStats);
                break;

            case END:
                endTurn(line, gameStats);
                break;

            case START:
                startTurn(line, gameStats);
                break;
        }
    }

    private String getAction(String line) {
        final int lengthOfLine = line.length();

        if (lengthOfLine < 10) {
            return "";
        }

        String action = line.substring(0, Math.min(5, lengthOfLine));
        if (action.equals(MAY_BE_CONNECTED)) {
            action = line.substring(0, Math.min(CONNECTED.length(), lengthOfLine));
        }

        return action;
    }

    //Kill: 1 0 6: UnnamedPlayer killed Zuczek_Gnojarz by MOD_ROCKET
    //Kill: 5 5 7: Gargamel killed Gargamel by MOD_ROCKET_SPLASH
    private void parseKillAction(String line, GameStats gameStats) {
        final RoundStats actualRoundStats = gameStats.getActualRoundStats();

        String[] infoAfterHeader = line.split(" ");
        int killerId = Integer.parseInt(infoAfterHeader[1]);
        int deadId = Integer.parseInt(infoAfterHeader[2]);

        actualRoundStats.registerKill(killerId, deadId);
    }

    //ClientUserinfoChanged: 1 n\UnnamedPlayer\t\1\model\james\hmodel\*james\g_redteam\\g_blueteam\\c1\4\c2\5\hc\100\w\0\l\0\tt\0\tl\0
    private void parsePlayer(String line, GameStats gameStats) {
        final RoundStats actualRoundStats = gameStats.getActualRoundStats();

        String infoAfterHeader = line.substring(line.indexOf(" ") + 1);
        String playerId = infoAfterHeader.substring(0, infoAfterHeader.indexOf(" "));
        String playerName = infoAfterHeader.substring(infoAfterHeader.indexOf("n\\") + 2, infoAfterHeader.indexOf("\\t"));

        actualRoundStats.addPlayer(Integer.parseInt(playerId), playerName);
    }

    //Exit: Fraglimit hit.
    //red:19  blue:25
    //score: 10  ping: 48  client: 2 Majonez Kielecki
    //score: 9  ping: 48  client: 3 NoobMaster
    //score: 8  ping: 48  client: 4 zenek martyniuk
    //score: 5  ping: 48  client: 1 UnnamedPlayer
    //score: 4  ping: 43  client: 7 Kruk
    //score: 3  ping: 43  client: 5 Gargamel
    //score: 3  ping: 48  client: 0 Zuczek_Gnojarz
    //score: 2  ping: 48  client: 6 Antek
    //==== ShutdownGame ====
    private void endTurn(String line, GameStats gameStats) {
        final RoundStats actualRoundStats = gameStats.getActualRoundStats();
        String endOfTurnCause = line.substring(line.indexOf(" ") + 1);
        actualRoundStats.setEndCause(endOfTurnCause);
    }

    //InitGame: \fs_cdn\content.quakejs.com\fs_manifest\linuxq3ademo-1.11-6.x86.gz.sh@857908472@49292197 linuxq3apoint-1.32b-3.x86.run@296843703@30914987...
    private void startTurn(String line, GameStats gameStats) {
        gameStats.addNewGame();
    }
}
