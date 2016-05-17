package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.FTBLibMod;
import com.feed_the_beast.ftbl.api.events.ForgeWorldDataEvent;
import com.feed_the_beast.ftbl.util.FTBLib;
import com.mojang.authlib.GameProfile;
import latmod.lib.LMListUtils;
import latmod.lib.LMUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by LatvianModder on 09.02.2016.
 */
public abstract class ForgeWorld
{
	public final Side side;
	protected UUID worldID;
	protected GameMode currentMode;
	public final Map<UUID, ForgePlayer> playerMap;
	final Map<String, ForgeWorldData> customData;
	
	public static ForgeWorld getFrom(Side side)
	{
		if(side == null)
		{
			return getFrom(FTBLib.getEffectiveSide());
		}
		
		return side.isServer() ? ForgeWorldMP.inst : FTBLibMod.proxy.getClientLMWorld();
	}
	
	ForgeWorld(Side s)
	{
		side = s;
		worldID = null;
		currentMode = new GameMode("default");
		playerMap = new HashMap<>();
		
		ForgeWorldDataEvent event = new ForgeWorldDataEvent(this);
		MinecraftForge.EVENT_BUS.post(event);
		customData = Collections.unmodifiableMap(event.getMap());
	}
	
	public final UUID getID()
	{
		if(worldID == null || (worldID.getLeastSignificantBits() == 0L && worldID.getMostSignificantBits() == 0L))
		{
			worldID = UUID.randomUUID();
		}
		
		return worldID;
	}
	
	public final Collection<ForgeWorldData> customData()
	{ return customData.values(); }
	
	public final ForgeWorldData getData(String id)
	{
		if(id == null || id.isEmpty()) { return null; }
		return customData.get(id);
	}
	
	public void init()
	{
		for(ForgeWorldData d : customData.values())
		{
			d.isLoaded = true;
			d.onLoaded(this);
		}
	}
	
	public abstract World getMCWorld();
	
	public abstract ForgeWorldMP toWorldMP();
	
	@SideOnly(Side.CLIENT)
	public abstract ForgeWorldSP toWorldSP();
	
	public GameMode getMode()
	{ return currentMode; }
	
	public ForgePlayer getPlayer(Object o)
	{
		if(o == null || o instanceof FakePlayer) { return null; }
		else if(o instanceof UUID)
		{
			UUID id = (UUID) o;
			if(id.getLeastSignificantBits() == 0L && id.getMostSignificantBits() == 0L) { return null; }
			return playerMap.get(id);
		}
		else if(o instanceof ForgePlayer) { return getPlayer(((ForgePlayer) o).getProfile().getId()); }
		else if(o instanceof EntityPlayer) { return getPlayer(((EntityPlayer) o).getGameProfile().getId()); }
		else if(o instanceof GameProfile) { return getPlayer(((GameProfile) o).getId()); }
		else if(o instanceof CharSequence)
		{
			String s = o.toString();
			
			if(s == null || s.isEmpty()) { return null; }
			
			for(ForgePlayer p : playerMap.values())
			{
				if(p.getProfile().getName().equalsIgnoreCase(s))
				{
					return p;
				}
			}
			
			return getPlayer(LMUtils.fromString(s));
		}
		
		return null;
	}
	
	public final List<ForgePlayer> getOnlinePlayers()
	{
		ArrayList<ForgePlayer> l = new ArrayList<>();
		
		for(ForgePlayer p : playerMap.values())
		{
			if(p.isOnline()) { l.add(p); }
		}
		
		return l;
	}
	
	public String[] getAllPlayerNames(boolean online)
	{
		List<ForgePlayer> list = online ? getOnlinePlayers() : LMListUtils.clone(playerMap.values());
		
		Collections.sort(list, (o1, o2) -> {
			if(o1.isOnline() == o2.isOnline())
			{
				return o1.getProfile().getName().compareToIgnoreCase(o2.getProfile().getName());
			}
			
			return Boolean.compare(o2.isOnline(), o1.isOnline());
		});
		
		return LMListUtils.toStringArray(list);
	}
	
	/**
	 * 0 = OK, 1 - Mode is invalid, 2 - Mode already set (will be ignored and return 0, if forced == true)
	 */
	public final int setMode(String mode)
	{
		GameMode m = GameModes.instance().modes.get(mode);
		
		if(m == null) { return 1; }
		if(m.equals(currentMode)) { return 2; }
		
		currentMode = m;
		
		return 0;
	}
	
	public void onClosed()
	{
		for(ForgeWorldData d : customData.values())
		{
			d.onClosed();
			d.isLoaded = false;
		}
		
		playerMap.clear();
	}
}
