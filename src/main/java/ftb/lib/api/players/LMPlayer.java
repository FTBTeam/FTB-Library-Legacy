package ftb.lib.api.players;

import com.mojang.authlib.GameProfile;
import ftb.lib.api.ForgePlayerDataEvent;
import latmod.lib.json.UUIDTypeAdapterLM;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.*;

import java.util.*;

/**
 * Created by LatvianModder on 09.02.2016.
 */
public abstract class LMPlayer implements Comparable<LMPlayer>
{
	private GameProfile gameProfile;
	public final List<UUID> friends;
	public final Map<Integer, ItemStack> lastArmor;
	public final Map<String, ForgePlayerData> customData;
	
	LMPlayer(GameProfile p)
	{
		setProfile(p);
		friends = new ArrayList<>();
		lastArmor = new HashMap<>();
		
		ForgePlayerDataEvent event = new ForgePlayerDataEvent(this);
		MinecraftForge.EVENT_BUS.post(event);
		customData = Collections.unmodifiableMap(event.getMap());
	}
	
	public ForgePlayerData getData(String id)
	{ return (id == null || id.isEmpty()) ? null : customData.get(id.toLowerCase()); }
	
	public abstract Side getSide();
	public abstract boolean isOnline();
	public abstract EntityPlayer getPlayer();
	public abstract LMPlayerMP toPlayerMP();
	
	@SideOnly(Side.CLIENT)
	public abstract LMPlayerSP toPlayerSP();
	
	public abstract LMWorld getWorld();
	
	public boolean allowCreativeInteractSecure()
	//{ return getPlayer() != null && getPlayer().capabilities.isCreativeMode && getRank().config.allow_creative_interact_secure.get(); }
	{
		return false;
	}
	
	public final void setProfile(GameProfile p)
	{
		if(p != null)
		{
			gameProfile = new GameProfile(p.getId(), p.getName());
		}
	}
	
	public final GameProfile getProfile()
	{ return gameProfile; }
	
	public final String getStringUUID()
	{ return UUIDTypeAdapterLM.getString(gameProfile.getId()); }
	
	public boolean isFriendRaw(LMPlayer p)
	{ return p != null && (equalsPlayer(this) || friends.contains(p.getProfile().getId())); }
	
	public boolean isFriend(LMPlayer p)
	{ return p != null && isFriendRaw(p) && p.isFriendRaw(this); }
	
	public final int compareTo(LMPlayer o)
	{ return getProfile().getName().compareToIgnoreCase(o.getProfile().getName()); }
	
	public String toString()
	{ return gameProfile.getName(); }
	
	public final int hashCode()
	{ return gameProfile.getId().hashCode(); }
	
	public boolean equals(Object o)
	{
		if(o == null) return false;
		else if(o == this) return true;
		else if(o instanceof UUID) return gameProfile.getId().equals(o);
		else if(o instanceof LMPlayer) return equalsPlayer((LMPlayer) o);
		return false;
	}
	
	public boolean equalsPlayer(LMPlayer p)
	{ return p != null && (p == this || gameProfile.getId().equals(p.gameProfile.getId())); }
	
	public List<LMPlayer> getFriends()
	{
		ArrayList<LMPlayer> list = new ArrayList<>();
		
		for(UUID id : friends)
		{
			LMPlayer p = getWorld().getPlayer(id);
			
			if(p != null)
			{
				list.add(p);
			}
		}
		
		return list;
	}
	
	public List<LMPlayer> getOtherFriends()
	{
		List<LMPlayer> l = new ArrayList<>();
		
		for(LMPlayer p : getWorld().playerMap.values())
		{
			if(!p.equalsPlayer(this) && p.isFriendRaw(this))
			{
				l.add(p);
			}
		}
		
		return l;
	}
	
	public boolean isMCPlayer()
	{ return false; }
	
	public void updateArmor()
	{
		if(getSide().isServer() && isOnline())
		{
			lastArmor.clear();
			EntityPlayer ep = getPlayer();
			for(int i = 0; i < 4; i++)
			{
				if(ep.inventory.armorInventory[i] != null)
				{
					lastArmor.put(i, ep.inventory.armorInventory[i].copy());
				}
			}
			
			if(ep.inventory.getCurrentItem() != null)
			{
				lastArmor.put(4, ep.inventory.getCurrentItem().copy());
			}
		}
	}
	
	public void onLoggedIn(boolean firstTime)
	{
		updateArmor();
		
		for(ForgePlayerData d : customData.values())
		{
			d.onLoggedIn(firstTime);
		}
	}
	
	public void onLoggedOut()
	{
		updateArmor();
		
		for(ForgePlayerData d : customData.values())
		{
			d.onLoggedOut();
		}
	}
	
	public void onDeath()
	{
		updateArmor();
		
		for(ForgePlayerData d : customData.values())
		{
			d.onDeath();
		}
	}
	
	public void sendUpdate()
	{
	}
}