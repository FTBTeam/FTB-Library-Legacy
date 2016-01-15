package ftb.lib.api;

import ftb.lib.client.*;
import ftb.lib.gui.GuiLM;
import latmod.lib.util.FinalIDObject;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.*;

import java.util.List;

public abstract class PlayerAction extends FinalIDObject
{
	public final TextureCoords icon;
	
	public PlayerAction(String id, TextureCoords c)
	{
		super(id);
		icon = c;
	}
	
	public abstract void onClicked(ILMPlayer owner, ILMPlayer player);
	
	public abstract String getTitleKey();
	
	@SideOnly(Side.CLIENT)
	public String getTitle()
	{ return I18n.format(getTitleKey()); }
	
	public void addMouseOverText(List<String> l) { }
	
	public void render(int ax, int ay, double z)
	{
		FTBLibClient.setTexture(icon);
		GuiLM.drawTexturedRectD(ax, ay, z, 16, 16, icon.minU, icon.minV, icon.maxU, icon.maxV);
	}
	
	public void postRender(int ax, int ay, double z)
	{
	}
	
	public int getPriority()
	{ return 0; }
	
	public int compareTo(Object o)
	{ return Integer.compare(((PlayerAction) o).getPriority(), getPriority()); }
	
	public boolean isVisibleFor(ILMPlayer owner, ILMPlayer player)
	{
		return owner.equals(player);
	}
}