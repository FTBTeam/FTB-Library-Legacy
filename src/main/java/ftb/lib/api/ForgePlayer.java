package ftb.lib.api;

import com.mojang.authlib.GameProfile;
import ftb.lib.api.events.ForgePlayerDataEvent;
import latmod.lib.LMUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.*;

import java.util.*;

/**
 * Created by LatvianModder on 09.02.2016.
 */
public abstract class ForgePlayer implements Comparable<ForgePlayer>
{
	private GameProfile gameProfile;
	public final List<UUID> friends;
	public final Map<Integer, ItemStack> lastArmor;
	Map<String, ForgePlayerData> customData;
	
	ForgePlayer(GameProfile p)
	{
		setProfile(p);
		friends = new ArrayList<>();
		lastArmor = new HashMap<>();
	}
	
	public void init()
	{
		customData = new HashMap<>();
		ForgePlayerDataEvent event = new ForgePlayerDataEvent(this);
		MinecraftForge.EVENT_BUS.post(event);
		customData = Collections.unmodifiableMap(event.getMap());
	}
	
	public final Collection<ForgePlayerData> customData()
	{ return customData.values(); }
	
	public final ForgePlayerData getData(String id)
	{
		if(id == null || id.isEmpty()) return null;
		return customData.get(id);
	}
	
	public abstract Side getSide();
	public abstract boolean isOnline();
	public abstract EntityPlayer getPlayer();
	public abstract ForgePlayerMP toPlayerMP();
	
	@SideOnly(Side.CLIENT)
	public abstract ForgePlayerSP toPlayerSP();
	
	public abstract ForgeWorld getWorld();
	
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
	{ return LMUtils.fromUUID(gameProfile.getId()); }
	
	public boolean isFriendRaw(ForgePlayer p)
	{ return p != null && (equalsPlayer(this) || friends.contains(p.getProfile().getId())); }
	
	public boolean isFriend(ForgePlayer p)
	{ return p != null && isFriendRaw(p) && p.isFriendRaw(this); }
	
	@Override
	public final int compareTo(ForgePlayer o)
	{ return getProfile().getName().compareToIgnoreCase(o.getProfile().getName()); }
	
	@Override
	public final String toString()
	{ return gameProfile.getName(); }
	
	@Override
	public final int hashCode()
	{ return gameProfile.getId().hashCode(); }
	
	@Override
	public boolean equals(Object o)
	{
		if(o == null) return false;
		else if(o == this) return true;
		else if(o instanceof UUID) return gameProfile.getId().equals(o);
		else if(o instanceof ForgePlayer) return equalsPlayer((ForgePlayer) o);
		return false;
	}
	
	public boolean equalsPlayer(ForgePlayer p)
	{ return p != null && (p == this || gameProfile.getId().equals(p.gameProfile.getId())); }
	
	public List<ForgePlayer> getFriends()
	{
		ArrayList<ForgePlayer> list = new ArrayList<>();
		
		for(UUID id : friends)
		{
			ForgePlayer p = getWorld().getPlayer(id);
			
			if(p != null)
			{
				list.add(p);
			}
		}
		
		return list;
	}
	
	public List<ForgePlayer> getOtherFriends()
	{
		List<ForgePlayer> l = new ArrayList<>();
		
		for(ForgePlayer p : getWorld().playerMap.values())
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