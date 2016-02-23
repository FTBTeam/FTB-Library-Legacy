package ftb.lib.api.players;

import com.mojang.authlib.GameProfile;
import ftb.lib.FTBLib;
import ftb.lib.api.*;
import ftb.lib.mod.FTBLibMod;
import latmod.lib.LMListUtils;
import latmod.lib.json.UUIDTypeAdapterLM;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.*;

import java.util.*;

/**
 * Created by LatvianModder on 09.02.2016.
 */
public abstract class LMWorld
{
	public final Side side;
	protected GameMode currentMode;
	public final Map<UUID, LMPlayer> playerMap;
	public final Map<String, ForgeWorldData> customData;
	
	public static LMWorld getFrom(Side side)
	{
		if(side == null)
		{
			return getFrom(FTBLib.getEffectiveSide());
		}
		
		return side.isServer() ? LMWorldMP.inst : FTBLibMod.proxy.getClientLMWorld();
	}
	
	LMWorld(Side s)
	{
		side = s;
		currentMode = new GameMode("default");
		playerMap = new HashMap<>();
		
		ForgeWorldDataEvent event = new ForgeWorldDataEvent(this);
		MinecraftForge.EVENT_BUS.post(event);
		customData = Collections.unmodifiableMap(event.getMap());
	}
	
	public abstract World getMCWorld();
	
	public abstract LMWorldMP toWorldMP();
	
	@SideOnly(Side.CLIENT)
	public abstract LMWorldSP toWorldSP();
	
	public GameMode getMode()
	{ return currentMode; }
	
	public LMPlayer getPlayer(Object o)
	{
		if(o == null || o instanceof FakePlayer) return null;
		else if(o.getClass() == UUID.class)
		{
			UUID id = (UUID) o;
			if(id.getLeastSignificantBits() == 0L && id.getMostSignificantBits() == 0L) return null;
			return playerMap.get(id);
		}
		else if(o instanceof LMPlayer) return playerMap.get(((LMPlayer) o).getProfile().getId());
		else if(o instanceof EntityPlayer)
		{
			if(side.isServer())
			{
				for(LMPlayer p : playerMap.values())
				{
					if(p.isOnline() && p.getPlayer() == o)
					{
						return p;
					}
				}
			}
			
			return getPlayer(((EntityPlayer) o).getGameProfile().getId());
		}
		else if(o instanceof GameProfile)
		{
		}
		else if(o instanceof CharSequence)
		{
			String s = o.toString();
			
			if(s == null || s.isEmpty()) return null;
			
			for(LMPlayer p : playerMap.values())
			{
				if(p.getProfile().getName().equalsIgnoreCase(s))
				{
					return p;
				}
			}
			
			return getPlayer(UUIDTypeAdapterLM.getUUID(s));
		}
		
		return null;
	}
	
	public final List<LMPlayer> getOnlinePlayers()
	{
		ArrayList<LMPlayer> l = new ArrayList<>();
		
		for(LMPlayer p : playerMap.values())
		{
			if(p.isOnline()) l.add(p);
		}
		
		return l;
	}
	
	public String[] getAllPlayerNames(boolean online)
	{
		List<LMPlayer> list = online ? getOnlinePlayers() : LMListUtils.clone(playerMap.values());
		
		Collections.sort(list, new Comparator<LMPlayer>()
		{
			public int compare(LMPlayer o1, LMPlayer o2)
			{
				if(o1.isOnline() == o2.isOnline())
					return o1.getProfile().getName().compareToIgnoreCase(o2.getProfile().getName());
				return Boolean.compare(o2.isOnline(), o1.isOnline());
			}
		});
		
		return LMListUtils.toStringArray(list);
	}
	
	/**
	 * 0 = OK, 1 - Mode is invalid, 2 - Mode already set (will be ignored and return 0, if forced == true)
	 */
	public final int setMode(String mode)
	{
		GameMode m = GameModes.getGameModes().modes.get(mode);
		
		if(m == null) return 1;
		if(m.equals(currentMode)) return 2;
		
		currentMode = m;
		
		return 0;
	}
	
	public void onClosed()
	{
		for(ForgeWorldData d : customData.values())
		{
			d.onClosed();
		}
		
		playerMap.clear();
	}
}
