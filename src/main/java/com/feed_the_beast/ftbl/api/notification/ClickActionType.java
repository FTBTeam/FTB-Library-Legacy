package com.feed_the_beast.ftbl.api.notification;

import com.feed_the_beast.ftbl.api.ForgeWorldSP;
import com.feed_the_beast.ftbl.api.PlayerAction;
import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.gui.GuiScreenRegistry;
import com.feed_the_beast.ftbl.api.gui.PlayerActionRegistry;
import com.feed_the_beast.ftbl.net.MessageModifyFriends;
import com.google.gson.JsonElement;
import latmod.lib.LMUtils;
import latmod.lib.util.FinalIDObject;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;
import java.net.URI;

public abstract class ClickActionType extends FinalIDObject
{
	public ClickActionType(String s)
	{
		super(s);
	}
	
	@SideOnly(Side.CLIENT)
	public abstract void onClicked(JsonElement data);
	
	public String getDisplayID()
	{ return "click_action." + getID(); }
	
	// Static //
	
	public static final ClickActionType ACTION = new ClickActionType("action")
	{
		@Override
		@SideOnly(Side.CLIENT)
		public void onClicked(JsonElement data)
		{
			PlayerAction a = PlayerActionRegistry.get(data.getAsString());
			if(a != null && a.type.isSelf())
			{ a.onClicked(ForgeWorldSP.inst.clientPlayer, ForgeWorldSP.inst.clientPlayer); }
		}
	};
	
	public static final ClickActionType CMD = new ClickActionType("cmd")
	{
		@Override
		@SideOnly(Side.CLIENT)
		public void onClicked(JsonElement data)
		{ FTBLibClient.execClientCommand("/" + data.getAsString()); }
	};
	
	public static final ClickActionType SHOW_CMD = new ClickActionType("show_cmd")
	{
		@Override
		@SideOnly(Side.CLIENT)
		public void onClicked(JsonElement data)
		{ FTBLibClient.openGui(new GuiChat(data.getAsString())); }
	};
	
	public static final ClickActionType URL = new ClickActionType("url")
	{
		@Override
		@SideOnly(Side.CLIENT)
		public void onClicked(JsonElement data)
		{
			try { LMUtils.openURI(new URI(data.getAsString())); }
			catch(Exception ex) { ex.printStackTrace(); }
		}
	};
	
	public static final ClickActionType FILE = new ClickActionType("file")
	{
		@Override
		@SideOnly(Side.CLIENT)
		public void onClicked(JsonElement data)
		{
			try { LMUtils.openURI(new File(data.getAsString()).toURI()); }
			catch(Exception ex) { ex.printStackTrace(); }
		}
	};
	
	public static final ClickActionType GUI = new ClickActionType("gui")
	{
		@Override
		@SideOnly(Side.CLIENT)
		public void onClicked(JsonElement data)
		{
			GuiScreen gui = GuiScreenRegistry.openGui(FTBLibClient.mc.thePlayer, data.getAsString());
			if(gui != null) { FTBLibClient.openGui(gui); }
		}
	};
	
	public static final ClickActionType FRIEND_ADD = new ClickActionType("friend_add")
	{
		@Override
		@SideOnly(Side.CLIENT)
		public void onClicked(JsonElement data)
		{ new MessageModifyFriends(MessageModifyFriends.ADD, LMUtils.fromString(data.getAsString())).sendToServer(); }
	};
	
	public static final ClickActionType FRIEND_ADD_ALL = new ClickActionType("friend_add_all")
	{
		@Override
		@SideOnly(Side.CLIENT)
		public void onClicked(JsonElement data)
		{ new MessageModifyFriends(MessageModifyFriends.ADD_ALL, null).sendToServer();}
	};
}