package ftb.lib.api;

import com.mojang.authlib.GameProfile;
import ftb.lib.LMNBTUtils;
import ftb.lib.api.client.FTBLibClient;
import ftb.lib.api.events.ForgePlayerSPInfoEvent;
import latmod.lib.LMUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.*;

import java.util.*;

/**
 * Created by LatvianModder on 09.02.2016.
 */
@SideOnly(Side.CLIENT)
public class ForgePlayerSP extends ForgePlayer
{
	public final List<String> clientInfo;
	public boolean isOnline;
	
	public ForgePlayerSP(GameProfile p)
	{
		super(p);
		clientInfo = new ArrayList<>();
		isOnline = false;
	}
	
	public boolean isClientPlayer()
	{ return getProfile().equals(Minecraft.getMinecraft().thePlayer.getGameProfile()); }
	
	public final Side getSide()
	{ return Side.CLIENT; }
	
	public boolean isOnline()
	{ return isOnline; }
	
	public EntityPlayer getPlayer()
	{ return isOnline() ? FTBLibClient.getPlayerSP(getProfile().getId()) : null; }
	
	public final ForgePlayerMP toPlayerMP()
	{ return null; }
	
	public final ForgePlayerSP toPlayerSP()
	{ return this; }
	
	public final ForgeWorld getWorld()
	{ return ForgeWorldSP.inst; }
	
	public ResourceLocation getSkin()
	{ return FTBLibClient.getSkinTexture(getProfile().getName()); }
	
	public ForgePlayerSPSelf toPlayerSPSelf()
	{ return null; }
	
	public boolean isMCPlayer()
	{ return toPlayerSPSelf() != null; }
	
	//public Rank getRank()
	//{ return Ranks.PLAYER; }
	
	public void receiveInfo(List<IChatComponent> info)
	{
		clientInfo.clear();
		
		for(IChatComponent c : info)
		{
			clientInfo.add(c.getFormattedText());
		}
		
		MinecraftForge.EVENT_BUS.post(new ForgePlayerSPInfoEvent(this, clientInfo));
	}
	
	public void readFromNet(NBTTagCompound tag, boolean self)
	{
		isOnline = tag.hasKey("O");
		
		friends.clear();
		
		NBTTagList tagList = (NBTTagList) tag.getTag("F");
		
		if(tagList != null)
		{
			for(int i = 0; i < tagList.tagCount(); i++)
			{
				friends.add(LMUtils.fromString(tagList.getStringTagAt(i)));
			}
		}
		
		List<UUID> otherFriends = new ArrayList<>();
		tagList = (NBTTagList) tag.getTag("OF");
		
		if(tagList != null)
		{
			for(int i = 0; i < tagList.tagCount(); i++)
			{
				otherFriends.add(LMUtils.fromString(tagList.getStringTagAt(i)));
			}
		}
		
		for(ForgePlayer p : getWorld().playerMap.values())
		{
			if(!p.equalsPlayer(this))
			{
				p.friends.clear();
				if(otherFriends.contains(p.getProfile().getId()))
				{
					p.friends.add(getProfile().getId());
					otherFriends.remove(p.getProfile().getId());
				}
			}
		}
		
		if(!customData.isEmpty() && tag.hasKey("C"))
		{
			for(Map.Entry<String, NBTBase> e : LMNBTUtils.entrySet(tag.getCompoundTag("C")))
			{
				ForgePlayerData data = customData.get(e.getKey());
				if(data != null) data.readFromNet((NBTTagCompound) e.getValue(), self);
			}
		}
	}
}
