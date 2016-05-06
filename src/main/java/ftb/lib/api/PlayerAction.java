package ftb.lib.api;

import ftb.lib.TextureCoords;
import ftb.lib.api.client.FTBLibClient;
import ftb.lib.api.gui.GuiLM;
import latmod.lib.util.FinalIDObject;
import net.minecraft.client.resources.I18n;

import java.util.List;

public abstract class PlayerAction extends FinalIDObject
{
	public enum Type
	{
		SELF,
		OTHER,
		BOTH;
		
		public boolean isSelf()
		{ return this == SELF || this == BOTH; }
		
		public boolean isOther()
		{ return this == OTHER || this == BOTH; }
		
		public boolean equalsType(Type t)
		{
			if(t == null || t == BOTH) { return true; }
			else if(t == SELF) { return isSelf(); }
			else { return isOther(); }
		}
	}
	
	public final Type type;
	public final int priority;
	public final TextureCoords icon;
	
	public PlayerAction(Type t, String id, int p, TextureCoords c)
	{
		super(id);
		type = (t == null) ? Type.SELF : t;
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