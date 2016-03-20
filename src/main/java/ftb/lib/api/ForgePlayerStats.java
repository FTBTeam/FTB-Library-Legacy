package ftb.lib.api;

import ftb.lib.mod.FTBLibMod;
import latmod.lib.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.*;
import net.minecraft.util.*;

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
		if(file == null) return;
		
		long ms = LMUtils.millis();
		
		timePlayed = file.readStat(StatList.minutesPlayedStat) * 50L;
		deaths = file.readStat(StatList.deathsStat);
		
		lastSeen = ms;
		if(firstJoined <= 0L) firstJoined = lastSeen;
	}
	
	public void getInfo(ForgePlayerMP owner, List<IChatComponent> info, long ms)
	{
		if(lastSeen > 0L && !owner.isOnline())
			info.add(FTBLibMod.mod.chatComponent("label.last_seen", LMStringUtils.getTimeString(ms - lastSeen)));
		if(firstJoined > 0L)
			info.add(FTBLibMod.mod.chatComponent("label.joined", LMStringUtils.getTimeString(ms - firstJoined)));
		if(deaths > 0) info.add(FTBLibMod.mod.chatComponent("label.deaths", String.valueOf(deaths)));
		if(timePlayed > 0L)
			info.add(new ChatComponentTranslation("stat.playOneMinute").appendSibling(new ChatComponentText(": " + LMStringUtils.getTimeString(timePlayed))));
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
		if(deaths == 0 || timePlayed == 0L) return 0D;
		return (double) deaths / (timePlayed / 3600000D);
	}
	
	public long getLastSeen(ForgePlayerMP p)
	{ return p.isOnline() ? LMUtils.millis() : lastSeen; }
	
	public double getLastSeenDeltaInHours(ForgePlayerMP p)
	{
		if(p.isOnline()) return 0D;
		return (LMUtils.millis() - getLastSeen(p)) / 3600000D;
	}
}