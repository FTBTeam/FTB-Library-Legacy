package com.feed_the_beast.ftbl;

import com.feed_the_beast.ftbl.api.LangKey;
import com.feed_the_beast.ftbl.util.StatsLongValue;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatBasic;
import net.minecraft.stats.StatList;
import net.minecraft.stats.StatisticsManagerServer;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Created by LatvianModder on 04.07.2016.
 */
@ParametersAreNonnullByDefault
public class FTBLibStats
{
    public static final StatBase LAST_SEEN = (new StatBasic("ftbl.stat.last_seen", new TextComponentTranslation("ftbl.stat.last_seen")));
    public static final StatBase FIRST_JOINED = (new StatBasic("ftbl.stat.first_joined", new TextComponentTranslation("ftbl.stat.first_joined")));

    public static final LangKey TIME_PLAYED_LANG = new LangKey("ftbl.stat.time_played");

    public static void init()
    {
        LAST_SEEN.registerStat();
        FIRST_JOINED.registerStat();
    }

    public static double getDeathsPerHour(StatisticsManagerServer stats)
    {
        int deaths = stats.readStat(StatList.DEATHS);
        int timePlayed = stats.readStat(StatList.PLAY_ONE_MINUTE);

        if(deaths == 0 || timePlayed == 0L)
        {
            return 0D;
        }
        return (double) deaths / (timePlayed / 72000D);
    }

    public static long getLastSeen(StatisticsManagerServer stats, boolean isOnline)
    {
        if(isOnline)
        {
            return System.currentTimeMillis();
        }

        StatsLongValue v = stats.getProgress(LAST_SEEN);
        return (v == null) ? 0L : v.get();
    }

    public static double getLastSeenDeltaInHours(StatisticsManagerServer stats, boolean isOnline)
    {
        if(isOnline)
        {
            return 0D;
        }

        return (System.currentTimeMillis() - getLastSeen(stats, isOnline)) / 3600000D;
    }

    public static long getFirstJoined(StatisticsManagerServer stats)
    {
        StatsLongValue v = stats.getProgress(FIRST_JOINED);
        return (v == null) ? 0L : v.get();
    }

    public static void updateLastSeen(StatisticsManagerServer stats)
    {
        long ms = System.currentTimeMillis();

        StatsLongValue lv = stats.getProgress(FTBLibStats.LAST_SEEN);

        if(lv == null)
        {
            lv = stats.setProgress(FTBLibStats.LAST_SEEN, new StatsLongValue(0L));
        }

        lv.set(ms);

        lv = stats.getProgress(FTBLibStats.FIRST_JOINED);

        if(lv == null)
        {
            stats.setProgress(FTBLibStats.FIRST_JOINED, new StatsLongValue(ms));
        }
    }
}
