package ftb.lib.notification;

import ftb.lib.FTBLibClient;
import latmod.lib.*;
import latmod.lib.util.FinalIDObject;
import net.minecraft.client.gui.GuiChat;
import net.minecraftforge.fml.relauncher.*;

import java.io.File;
import java.net.URI;

public abstract class ClickAction extends FinalIDObject
{
	public final PrimitiveType type;
	
	public ClickAction(String s, PrimitiveType t)
	{
		super(s);
		type = t;
	}
	
	@SideOnly(Side.CLIENT)
	public abstract void onClicked(MouseAction c);
	
	// Static //
	
	public static final ClickAction CMD = new ClickAction("cmd", PrimitiveType.STRING)
	{
		@SideOnly(Side.CLIENT)
		public void onClicked(MouseAction c)
		{ FTBLibClient.execClientCommand(c.stringVal()); }
	};
	
	public static final ClickAction SHOW_CMD = new ClickAction("show_cmd", PrimitiveType.STRING)
	{
		@SideOnly(Side.CLIENT)
		public void onClicked(MouseAction c)
		{ FTBLibClient.mc.displayGuiScreen(new GuiChat(c.stringVal())); }
	};
	
	public static final ClickAction URL = new ClickAction("url", PrimitiveType.STRING)
	{
		@SideOnly(Side.CLIENT)
		public void onClicked(MouseAction c)
		{
			try { LMUtils.openURI(new URI(c.stringVal())); }
			catch(Exception ex) { ex.printStackTrace(); }
		}
	};
	
	public static final ClickAction FILE = new ClickAction("file", PrimitiveType.STRING)
	{
		@SideOnly(Side.CLIENT)
		public void onClicked(MouseAction c)
		{
			try { LMUtils.openURI(new File(c.stringVal()).toURI()); }
			catch(Exception ex) { ex.printStackTrace(); }
		}
	};
	
	public static final ClickAction GUI = new ClickAction("gui", PrimitiveType.STRING)
	{
		@SideOnly(Side.CLIENT)
		public void onClicked(MouseAction c)
		{
		}
	};
}