package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.events.ForgePlayerEvent;
import com.feed_the_beast.ftbl.api.events.ForgePlayerSPInfoEvent;
import com.mojang.authlib.GameProfile;
import latmod.lib.LMUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

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
	
	@Override
	public final Side getSide()
	{ return Side.CLIENT; }
	
	@Override
	public boolean isOnline()
	{ return isOnline; }
	
	@Override
	public EntityPlayer getPlayer()
	{ return isOnline() ? FTBLibClient.getPlayerSP(getProfile().getId()) : null; }
	
	@Override
	public final ForgePlayerMP toPlayerMP()
	{ return null; }
	
	@Override
	public final ForgePlayerSP toPlayerSP()
	{ return this; }
	
	@Override
	public final ForgeWorld getWorld()
	{ return ForgeWorldSP.inst; }
	
	public ResourceLocation getSkin()
	{ return FTBLibClient.getSkinTexture(getProfile().getName()); }
	
	public ForgePlayerSPSelf toPlayerSPSelf()
	{ return null; }
	
	@Override
	public boolean isMCPlayer()
	{ return toPlayerSPSelf() != null; }
	
	//public Rank getRank()
	//{ return Ranks.PLAYER; }
	
	public void receiveInfo(List<ITextComponent> info)
	{
		clientInfo.clear();
		
		for(ITextComponent c : info)
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
		
		Collection<UUID> otherFriends = new HashSet<>();
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
		
		MinecraftForge.EVENT_BUS.post(new ForgePlayerEvent.Sync(this, tag.getCompoundTag("SY"), self));
	}
}
