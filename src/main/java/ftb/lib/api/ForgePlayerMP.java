package ftb.lib.api;

import com.mojang.authlib.GameProfile;
import ftb.lib.*;
import ftb.lib.api.events.ForgePlayerInfoEvent;
import ftb.lib.api.item.LMInvUtils;
import ftb.lib.api.notification.*;
import ftb.lib.mod.FTBLibMod;
import ftb.lib.mod.net.*;
import latmod.lib.LMUtils;
import latmod.lib.json.UUIDTypeAdapterLM;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.*;
import net.minecraftforge.fml.relauncher.*;

import java.util.*;

/**
 * Created by LatvianModder on 09.02.2016.
 */
public class ForgePlayerMP extends ForgePlayer
{
	public final ForgePlayerStats stats;
	public BlockDimPos lastPos, lastDeath;
	private EntityPlayerMP entityPlayer = null;
	
	public static ForgePlayerMP get(Object o) throws CommandException
	{
		ForgePlayerMP p = ForgeWorldMP.inst.getPlayer(o);
		if(p == null || p.isFake()) throw new PlayerNotFoundException();
		return p;
	}
	
	public ForgePlayerMP(GameProfile p)
	{
		super(p);
		stats = new ForgePlayerStats();
	}
	
	public Side getSide()
	{ return Side.SERVER; }
	
	public boolean isOnline()
	{ return getPlayer() != null; }
	
	public EntityPlayerMP getPlayer()
	{ return entityPlayer; }
	
	public void setPlayer(EntityPlayerMP ep)
	{ entityPlayer = ep; }
	
	public final ForgePlayerMP toPlayerMP()
	{ return this; }
	
	@SideOnly(Side.CLIENT)
	public final ForgePlayerSP toPlayerSP()
	{ return null; }
	
	public final ForgeWorld getWorld()
	{ return ForgeWorldMP.inst; }
	
	public boolean isFake()
	{ return getPlayer() instanceof FakePlayer; }
	
	public void sendUpdate()
	{
		//new EventLMPlayerServer.UpdateSent(this).post();
		if(isOnline()) new MessageLMPlayerUpdate(this, true).sendTo(getPlayer());
		for(EntityPlayerMP ep : FTBLib.getAllOnlinePlayers(getPlayer()))
			new MessageLMPlayerUpdate(this, false).sendTo(ep);
	}
	
	public void sendInfoUpdate(ForgePlayerMP p)
	{ new MessageLMPlayerInfo(this, p).sendTo(getPlayer()); }
	
	public boolean isOP()
	{ return FTBLib.isOP(getProfile()); }
	
	public BlockDimPos getPos()
	{
		EntityPlayerMP ep = getPlayer();
		if(ep != null) lastPos = new EntityPos(ep).toBlockDimPos();
		return lastPos;
	}
	
	// Reading / Writing //
	
	public void getInfo(ForgePlayerMP owner, List<IChatComponent> info)
	{
		refreshStats();
		long ms = LMUtils.millis();
		
		if(!equalsPlayer(owner))
		{
			boolean raw1 = isFriendRaw(owner);
			boolean raw2 = owner.isFriendRaw(this);
			
			if(raw1 && raw2)
			{
				IChatComponent c = FTBLibMod.mod.chatComponent("label.friend");
				c.getChatStyle().setColor(EnumChatFormatting.GREEN);
				info.add(c);
			}
			else if(raw1 || raw2)
			{
				IChatComponent c = FTBLibMod.mod.chatComponent("label.pfriend");
				c.getChatStyle().setColor(raw1 ? EnumChatFormatting.GOLD : EnumChatFormatting.BLUE);
				info.add(c);
			}
		}
		
		MinecraftForge.EVENT_BUS.post(new ForgePlayerInfoEvent(this, info));
		
		/*
		if(owner.getRank().config.show_rank.get())
		{
			Rank rank = getRank();
			IChatComponent rankC = new ChatComponentText("[" + rank.ID + "]");
			rankC.getChatStyle().setColor(rank.color.get());
			info.add(rankC);
		}
		*/
		//stats.getInfo(info, ms);
	}
	
	public void refreshStats()
	{
		if(isOnline())
		{
			//stats.refreshStats();
			getPos();
		}
	}
	
	public void readFromServer(NBTTagCompound tag)
	{
		friends.clear();
		
		if(tag.hasKey("Friends"))
		{
			NBTTagList friendsList = tag.getTagList("Friends", Constants.NBT.TAG_LIST);
			for(int i = 0; i < friendsList.tagCount(); i++)
			{
				UUID id = UUIDTypeAdapterLM.getUUID(friendsList.getStringTagAt(i));
				if(id != null) friends.add(id);
			}
		}
		
		lastArmor.clear();
		
		if(tag.hasKey("LastItems"))
		{
			ItemStack[] lastArmorItems = new ItemStack[5];
			LMInvUtils.readItemsFromNBT(lastArmorItems, tag, "LastItems");
			
			for(int i = 0; i < 5; i++)
			{
				if(lastArmorItems[i] != null)
				{
					lastArmor.put(i, lastArmorItems[i]);
				}
			}
		}
		
		stats.load(tag);
		
		lastPos = null;
		if(tag.hasKey("Pos"))
		{
			lastPos = new BlockDimPos(tag.getIntArray("LastPos"));
		}
		
		lastDeath = null;
		if(tag.hasKey("LastDeath"))
		{
			lastDeath = new BlockDimPos(tag.getIntArray("LastDeath"));
		}
		
		NBTTagCompound settingsTag = tag.getCompoundTag("Settings");
	}
	
	public void writeToServer(NBTTagCompound tag)
	{
		refreshStats();
		
		if(!friends.isEmpty())
		{
			NBTTagList tagList = new NBTTagList();
			
			for(UUID id : friends)
			{
				tagList.appendTag(new NBTTagString(UUIDTypeAdapterLM.getString(id)));
			}
			
			tag.setTag("Friends", tagList);
		}
		
		if(!customData.isEmpty())
		{
			NBTTagCompound tag1 = new NBTTagCompound();
			NBTTagCompound tag2;
			
			for(ForgePlayerData d : customData.values())
			{
				tag2 = new NBTTagCompound();
				d.writeToServer(tag2);
				
				if(!tag2.hasNoTags())
				{
					tag1.setTag(d.getID(), tag2);
				}
			}
			
			if(!tag1.hasNoTags())
			{
				tag.setTag("Custom", tag1);
			}
		}
		
		if(!lastArmor.isEmpty())
		{
			ItemStack[] lastArmorItems = new ItemStack[5];
			for(Map.Entry<Integer, ItemStack> e : lastArmor.entrySet())
			{
				lastArmorItems[e.getKey()] = e.getValue();
			}
			
			LMInvUtils.writeItemsToNBT(lastArmorItems, tag, "LastItems");
		}
		
		stats.save(tag);
		
		if(lastPos != null)
		{
			tag.setIntArray("Pos", lastPos.toIntArray());
		}
		
		if(lastDeath != null)
		{
			tag.setIntArray("LastDeath", lastDeath.toIntArray());
		}
	}
	
	public void writeToNet(NBTTagCompound tag, boolean self)
	{
		refreshStats();
		
		if(!customData.isEmpty())
		{
			NBTTagCompound tag1 = new NBTTagCompound();
			NBTTagCompound tag2;
			
			for(ForgePlayerData d : customData.values())
			{
				tag2 = new NBTTagCompound();
				d.writeToNet(tag2, self);
				
				if(!tag2.hasNoTags())
				{
					tag1.setTag(d.getID(), tag2);
				}
			}
			
			if(!tag1.hasNoTags())
			{
				tag.setTag("C", tag1);
			}
		}
		
		//Rank rank = getRank();
		
		if(isOnline()) tag.setBoolean("O", true);
		
		if(!friends.isEmpty())
		{
			NBTTagList list = new NBTTagList();
			
			for(UUID id : friends)
			{
				list.appendTag(new NBTTagString(UUIDTypeAdapterLM.getString(id)));
			}
			
			tag.setTag("F", list);
		}
		
		List<ForgePlayer> otherFriends = getOtherFriends();
		
		if(!otherFriends.isEmpty())
		{
			NBTTagList list = new NBTTagList();
			
			for(ForgePlayer p : otherFriends)
			{
				list.appendTag(new NBTTagString(p.getStringUUID()));
			}
			
			tag.setTag("OF", list);
		}
	}
	
	public void checkNewFriends()
	{
		if(isOnline())
		{
			ArrayList<String> requests = new ArrayList<>();
			
			for(ForgePlayer p : getWorld().playerMap.values())
			{
				if(p.isFriendRaw(this) && !isFriendRaw(p)) requests.add(p.getProfile().getName());
			}
			
			if(requests.size() > 0)
			{
				IChatComponent cc = FTBLibMod.mod.chatComponent("label.new_friends");
				cc.getChatStyle().setColor(EnumChatFormatting.GREEN);
				Notification n = new Notification("new_friend_requests", cc, 6000);
				n.setDesc(FTBLibMod.mod.chatComponent("label.new_friends_click"));
				
				MouseAction mouse = new MouseAction();
				mouse.click = new ClickAction(ClickActionType.FRIEND_ADD_ALL, null);
				Collections.sort(requests, null);
				
				for(String s : requests) mouse.hover.add(new ChatComponentText(s));
				n.setMouseAction(mouse);
				
				FTBLib.notifyPlayer(getPlayer(), n);
			}
		}
	}
	
	public void onLoggedIn(boolean firstLogin)
	{
		super.onLoggedIn(firstLogin);
		
		EntityPlayerMP ep = getPlayer();
		new MessageLMWorldUpdate(this).sendTo(ep);
		
		new MessageLMPlayerLoggedIn(this, firstLogin, true).sendTo(ep);
		for(EntityPlayerMP ep1 : FTBLib.getAllOnlinePlayers(ep))
			new MessageLMPlayerLoggedIn(this, firstLogin, false).sendTo(ep1);
		
		checkNewFriends();
	}
	
	public void onDeath()
	{
		lastDeath = new EntityPos(getPlayer()).toBlockDimPos();
		stats.refresh(this, false);
		super.onDeath();
		new MessageLMPlayerDied().sendTo(getPlayer());
	}
}