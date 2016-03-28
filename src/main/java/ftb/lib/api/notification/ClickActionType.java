package ftb.lib.api.notification;

import com.google.gson.JsonElement;
import ftb.lib.api.*;
import ftb.lib.api.client.FTBLibClient;
import ftb.lib.api.gui.*;
import ftb.lib.mod.FTBLibMod;
import ftb.lib.mod.net.MessageModifyFriends;
import latmod.lib.LMUtils;
import latmod.lib.util.FinalIDObject;
import net.minecraft.client.gui.*;
import net.minecraftforge.fml.relauncher.*;

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
	
	public String getDisplayName()
	{ return FTBLibMod.proxy.translate("click_action." + getID()); }
	
	// Static //
	
	public static final ClickActionType ACTION = new ClickActionType("action")
	{
		@SideOnly(Side.CLIENT)
		public void onClicked(JsonElement data)
		{
			PlayerAction a = PlayerActionRegistry.get(data.getAsString());
			if(a != null && a.type.isSelf())
				a.onClicked(ForgeWorldSP.inst.clientPlayer, ForgeWorldSP.inst.clientPlayer);
		}
	};
	
	public static final ClickActionType CMD = new ClickActionType("cmd")
	{
		@SideOnly(Side.CLIENT)
		public void onClicked(JsonElement data)
		{ FTBLibClient.execClientCommand("/" + data.getAsString()); }
	};
	
	public static final ClickActionType SHOW_CMD = new ClickActionType("show_cmd")
	{
		@SideOnly(Side.CLIENT)
		public void onClicked(JsonElement data)
		{ FTBLibClient.openGui(new GuiChat(data.getAsString())); }
	};
	
	public static final ClickActionType URL = new ClickActionType("url")
	{
		@SideOnly(Side.CLIENT)
		public void onClicked(JsonElement data)
		{
			try { LMUtils.openURI(new URI(data.getAsString())); }
			catch(Exception ex) { ex.printStackTrace(); }
		}
	};
	
	public static final ClickActionType FILE = new ClickActionType("file")
	{
		@SideOnly(Side.CLIENT)
		public void onClicked(JsonElement data)
		{
			try { LMUtils.openURI(new File(data.getAsString()).toURI()); }
			catch(Exception ex) { ex.printStackTrace(); }
		}
	};
	
	public static final ClickActionType GUI = new ClickActionType("gui")
	{
		@SideOnly(Side.CLIENT)
		public void onClicked(JsonElement data)
		{
			GuiScreen gui = GuiScreenRegistry.openGui(FTBLibClient.mc.thePlayer, data.getAsString());
			if(gui != null) FTBLibClient.openGui(gui);
		}
	};
	
	public static final ClickActionType FRIEND_ADD = new ClickActionType("friend_add")
	{
		@SideOnly(Side.CLIENT)
		public void onClicked(JsonElement data)
		{ new MessageModifyFriends(MessageModifyFriends.ADD, LMUtils.fromString(data.getAsString())).sendToServer(); }
	};
	
	public static final ClickActionType FRIEND_ADD_ALL = new ClickActionType("friend_add_all")
	{
		@SideOnly(Side.CLIENT)
		public void onClicked(JsonElement data)
		{ new MessageModifyFriends(MessageModifyFriends.ADD_ALL, null).sendToServer();}
	};
}