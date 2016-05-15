package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.gui.GuiLM;
import com.feed_the_beast.ftbl.util.TextureCoords;
import latmod.lib.util.FinalIDObject;
import net.minecraft.client.resources.I18n;

import java.util.List;

public abstract class PlayerAction extends FinalIDObject
{
	public final EnumSelf type;
	public final int priority;
	public final TextureCoords icon;
	
	public PlayerAction(EnumSelf t, String id, int p, TextureCoords c)
	{
		super(id);
		type = (t == null) ? EnumSelf.SELF : t;
		priority = p;
		icon = c;
	}
	
	public abstract void onClicked(ForgePlayer self, ForgePlayer other);
	
	public String getDisplayName()
	{ return I18n.format("player_action." + getID()); }
	
	public void addMouseOverText(List<String> l) { }
	
	public void render(int ax, int ay, double z)
	{
		FTBLibClient.setTexture(icon);
		GuiLM.drawTexturedRectD(ax, ay, z, 16, 16, icon.minU, icon.minV, icon.maxU, icon.maxV);
	}
	
	public void postRender(int ax, int ay, double z)
	{
	}
	
	@Override
	public int compareTo(Object o)
	{
		int i = Integer.compare(((PlayerAction) o).priority, priority);
		return (i == 0) ? super.compareTo(o) : i;
	}
	
	public boolean isVisibleFor(ForgePlayer self, ForgePlayer other)
	{ return true; }
	
	public Boolean configDefault()
	{ return null; }
}