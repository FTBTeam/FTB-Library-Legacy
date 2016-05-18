package com.feed_the_beast.ftbl.api;

import latmod.lib.LMStringUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.stats.StatisticsFile;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.List;

/**
 * Created by LatvianModder on 11.02.2016.
 */
public class ForgePlayerStats
{
    public int deaths;
    public long firstJoined;
    public long lastSeen;
    public long timePlayed;
    
    public void refresh(ForgePlayerMP player, boolean force)
    {
        StatisticsFile file = player.getStatFile(force);
        if(file == null) { return; }
        
        long ms = System.currentTimeMillis();
        
        timePlayed = file.readStat(StatList.PLAY_ONE_MINUTE) * 50L;
        deaths = file.readStat(StatList.DEATHS);
        
        lastSeen = ms;
        if(firstJoined <= 0L) { firstJoined = lastSeen; }
    }
    
    public void getInfo(ForgePlayerMP owner, List<ITextComponent> info, long ms)
    {
        if(lastSeen > 0L && !owner.isOnline())
        {
            info.add(GuiLang.label_friend_last_seen.textComponent(LMStringUtils.getTimeString(ms - lastSeen)));
        }
        
        if(firstJoined > 0L)
        {
            info.add(GuiLang.label_friend_joined.textComponent(LMStringUtils.getTimeString(ms - firstJoined)));
        }
        
        if(deaths > 0)
        {
            info.add(GuiLang.label_friend_deaths.textComponent(String.valueOf(deaths)));
        }
        
        if(timePlayed > 0L)
        {
            info.add(new TextComponentTranslation("stat.playOneMinute").appendSibling(new TextComponentString(": " + LMStringUtils.getTimeString(timePlayed))));
        }
    }
    
    public void save(NBTTagCompound tag)
    {
        NBTTagCompound stats = new NBTTagCompound();
        
        stats.setInteger("Deaths", deaths);
        stats.setLong("LastSeen", lastSeen);
        stats.setLong("Joined", firstJoined);
        stats.setLong("TimePlayed", timePlayed);
        
        tag.setTag("Stats", stats);
    }
    
    public void load(NBTTagCompound tag)
    {
        NBTTagCompound stats = tag.getCompoundTag("Stats");
        
        deaths = stats.getInteger("Deaths");
        lastSeen = stats.getLong("LastSeen");
        firstJoined = stats.getLong("Joined");
        timePlayed = stats.getLong("TimePlayed");
    }
    
    public double getDeathsPerHour()
    {
        if(deaths == 0 || timePlayed == 0L) { return 0D; }
        return (double) deaths / (timePlayed / 3600000D);
    }
    
    public long getLastSeen(ForgePlayerMP p)
    { return p.isOnline() ? System.currentTimeMillis() : lastSeen; }
    
    public double getLastSeenDeltaInHours(ForgePlayerMP p)
    {
        if(p.isOnline()) { return 0D; }
        return (System.currentTimeMillis() - getLastSeen(p)) / 3600000D;
    }
}