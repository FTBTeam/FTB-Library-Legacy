package ftb.lib.api.friends;

import latmod.lib.LMUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.*;

/**
 * Created by LatvianModder on 11.02.2016.
 */
public class ForgePlayerStats
{
	public int deaths;
	public long firstJoined;
	public long lastSeen;
	public long timePlayed;
	
	public void refresh(LMPlayerMP player, boolean force)
	{
		if(!force && !player.isOnline()) return;
		StatisticsFile file = player.getPlayer().getStatFile();
		
		long ms = LMUtils.millis();
		
		timePlayed = file.readStat(StatList.minutesPlayedStat) * 50L;
		deaths = file.readStat(StatList.deathsStat);
		
		lastSeen = ms;
		if(firstJoined <= 0L) firstJoined = lastSeen;
	}
	
	public void save(NBTTagCompound tag)
	{
		tag.setInteger("Deaths", deaths);
		tag.setLong("LastSeen", lastSeen);
		tag.setLong("Joined", firstJoined);
		tag.setLong("TimePlayed", timePlayed);
	}
	
	public void load(NBTTagCompound tag)
	{
		deaths = tag.getInteger("Deaths");
		lastSeen = tag.getLong("LastSeen");
		firstJoined = tag.getLong("Joined");
		timePlayed = tag.getLong("TimePlayed");
	}
	
	public double getDeathsPerHour()
	{
		if(deaths == 0 || timePlayed == 0L) return 0D;
		return (double) deaths / (timePlayed / 3600000D);
	}
	
	public long getLastSeen(LMPlayer p)
	{ return p.isOnline() ? LMUtils.millis() : lastSeen; }
	
	public double getLastSeenDeltaInHours(LMPlayer p)
	{
		if(p.isOnline()) return 0D;
		return (LMUtils.millis() - getLastSeen(p)) / 3600000D;
	}
}