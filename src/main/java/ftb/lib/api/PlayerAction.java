package ftb.lib.api;

import ftb.lib.client.*;
import ftb.lib.gui.GuiLM;
import ftb.lib.mod.client.FTBLibGuiEventHandler;
import latmod.lib.FastList;

public abstract class PlayerAction
{
	public final int ID;
	public final TextureCoords icon;
	
	public PlayerAction(TextureCoords c)
	{
		ID = FTBLibGuiEventHandler.getNextButtonID();
		icon = c;
	}
	
	public int hashCode()
	{ return ID; }
	
	public boolean equals(Object o)
	{ return o == this || o.hashCode() == hashCode(); }
	
	public abstract void onClicked(int playerID);
	public abstract String getTitle();
	
	public void addMouseOverText(FastList<String> l) { }
	
	public void render(int ax, int ay, double z)
	{
		FTBLibClient.setTexture(icon.texture);
		GuiLM.drawTexturedRectD(ax, ay, z, 16, 16, icon.minU, icon.minV, icon.maxU, icon.maxV);
	}
	
	public void postRender(int ax, int ay, double z)
	{
	}
}