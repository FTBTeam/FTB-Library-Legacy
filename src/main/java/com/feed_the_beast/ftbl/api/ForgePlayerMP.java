package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.api.events.ForgePlayerMPInfoEvent;
import com.feed_the_beast.ftbl.api.item.LMInvUtils;
import com.feed_the_beast.ftbl.api.notification.ClickAction;
import com.feed_the_beast.ftbl.api.notification.ClickActionType;
import com.feed_the_beast.ftbl.api.notification.MouseAction;
import com.feed_the_beast.ftbl.api.notification.Notification;
import com.feed_the_beast.ftbl.net.MessageLMPlayerDied;
import com.feed_the_beast.ftbl.net.MessageLMPlayerInfo;
import com.feed_the_beast.ftbl.net.MessageLMPlayerLoggedIn;
import com.feed_the_beast.ftbl.net.MessageLMPlayerUpdate;
import com.feed_the_beast.ftbl.net.MessageReload;
import com.feed_the_beast.ftbl.util.BlockDimPos;
import com.feed_the_beast.ftbl.util.EntityDimPos;
import com.feed_the_beast.ftbl.util.FTBLib;
import com.feed_the_beast.ftbl.util.ReloadType;
import com.mojang.authlib.GameProfile;
import latmod.lib.LMUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.stats.StatisticsFile;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
		if(p == null || p.isFake()) { throw new PlayerNotFoundException(); }
		return p;
	}
	
	public ForgePlayerMP(GameProfile p)
	{
		super(p);
		stats = new ForgePlayerStats();
	}
	
	@Override
	public Side getSide()
	{ return Side.SERVER; }
	
	@Override
	public boolean isOnline()
	{ return getPlayer() != null; }
	
	@Override
	public EntityPlayerMP getPlayer()
	{ return entityPlayer; }
	
	public void setPlayer(EntityPlayerMP ep)
	{ entityPlayer = ep; }
	
	@Override
	public final ForgePlayerMP toPlayerMP()
	{ return this; }
	
	@Override
	@SideOnly(Side.CLIENT)
	public final ForgePlayerSP toPlayerSP()
	{ return null; }
	
	@Override
	public final ForgeWorld getWorld()
	{ return ForgeWorldMP.inst; }
	
	public boolean isFake()
	{ return getPlayer() instanceof FakePlayer; }
	
	@Override
	public void sendUpdate()
	{
		//new EventLMPlayerServer.UpdateSent(this).post();
		if(isOnline())
		{
			new MessageLMPlayerUpdate(this, true).sendTo(getPlayer());
		}
		
		for(EntityPlayerMP ep : FTBLib.getAllOnlinePlayers(getPlayer()))
		{
			new MessageLMPlayerUpdate(this, false).sendTo(ep);
		}
	}
	
	public void sendInfoUpdate(ForgePlayerMP p)
	{ new MessageLMPlayerInfo(this, p).sendTo(getPlayer()); }
	
	public boolean isOP()
	{ return FTBLib.isOP(getProfile()); }
	
	public BlockDimPos getPos()
	{
		EntityPlayerMP ep = getPlayer();
		if(ep != null) { lastPos = new EntityDimPos(ep).toBlockDimPos(); }
		return lastPos;
	}
	
	// Reading / Writing //
	
	public void getInfo(ForgePlayerMP owner, List<ITextComponent> info)
	{
		refreshStats();
		
		if(!equalsPlayer(owner))
		{
			boolean raw1 = isFriendRaw(owner);
			boolean raw2 = owner.isFriendRaw(this);
			
			if(raw1 && raw2)
			{
				ITextComponent c = GuiLang.label_friend.textComponent();
				c.getStyle().setColor(TextFormatting.GREEN);
				info.add(c);
			}
			else if(raw1 || raw2)
			{
				ITextComponent c = GuiLang.label_friend_pending.textComponent();
				c.getStyle().setColor(raw1 ? TextFormatting.GOLD : TextFormatting.BLUE);
				info.add(c);
			}
		}
		
		MinecraftForge.EVENT_BUS.post(new ForgePlayerMPInfoEvent(this, info));
		
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
			stats.refresh(this, false);
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
				UUID id = LMUtils.fromString(friendsList.getStringTagAt(i));
				if(id != null) { friends.add(id); }
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
				tagList.appendTag(new NBTTagString(LMUtils.fromUUID(id)));
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
		
		if(isOnline()) { tag.setBoolean("O", true); }
		
		if(!friends.isEmpty())
		{
			NBTTagList list = new NBTTagList();
			
			for(UUID id : friends)
			{
				list.appendTag(new NBTTagString(LMUtils.fromUUID(id)));
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
				if(p.isFriendRaw(this) && !isFriendRaw(p)) { requests.add(p.getProfile().getName()); }
			}
			
			if(requests.size() > 0)
			{
				ITextComponent cc = GuiLang.label_friend_new.textComponent();
				cc.getStyle().setColor(TextFormatting.GREEN);
				Notification n = new Notification("new_friend_requests", cc, 6000);
				n.setDesc(GuiLang.label_friend_new_click.textComponent());
				
				MouseAction mouse = new MouseAction();
				mouse.click = new ClickAction(ClickActionType.FRIEND_ADD_ALL, null);
				Collections.sort(requests, null);
				
				for(String s : requests) mouse.hover.add(new TextComponentString(s));
				n.setMouseAction(mouse);
				
				FTBLib.notifyPlayer(getPlayer(), n);
			}
		}
	}
	
	@Override
	public void onLoggedIn(boolean firstLogin)
	{
		super.onLoggedIn(firstLogin);
		
		EntityPlayerMP ep = getPlayer();
		new MessageReload(ReloadType.CLIENT_ONLY, ep, true).sendTo(ep);
		
		new MessageLMPlayerLoggedIn(this, firstLogin, true).sendTo(ep);
		for(EntityPlayerMP ep1 : FTBLib.getAllOnlinePlayers(ep))
			new MessageLMPlayerLoggedIn(this, firstLogin, false).sendTo(ep1);
		
		checkNewFriends();
	}
	
	@Override
	public void onDeath()
	{
		if(!isOnline()) { return; }
		lastDeath = new EntityDimPos(getPlayer()).toBlockDimPos();
		stats.refresh(this, false);
		super.onDeath();
		new MessageLMPlayerDied().sendTo(getPlayer());
	}
	
	public StatisticsFile getStatFile(boolean force)
	{
		if(isOnline()) { return getPlayer().getStatFile(); }
		return force ? FTBLib.getServer().getPlayerList().getPlayerStatsFile(new FakePlayer(FTBLib.getServerWorld(), getProfile())) : null;
	}
}