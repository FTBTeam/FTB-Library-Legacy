package com.feed_the_beast.ftbl;

import com.feed_the_beast.ftbl.api.ForgePlayerMP;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatBasic;
import net.minecraft.stats.StatList;
import net.minecraft.stats.StatisticsManagerServer;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * Created by LatvianModder on 04.07.2016.
 */
public class FTBLibStats
{
    public static final StatBase LAST_SEEN = (new StatBasic("ftbl.stat.last_seen", new TextComponentTranslation("ftbl.stat.last_seen"))).registerStat();

    public static void init()
    {
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

    public static long getLastSeen(ForgePlayerMP p)
    {
        return p.isOnline() ? System.currentTimeMillis() : p.stats().readStat(LAST_SEEN);
    }

    public static double getLastSeenDeltaInHours(ForgePlayerMP p)
    {
        if(p.isOnline())
        {
            return 0D;
        }
        return (System.currentTimeMillis() - getLastSeen(p)) / 3600000D;
    }
}
