package ftb.lib.api;

import cpw.mods.fml.relauncher.*;
import ftb.lib.client.*;
import ftb.lib.gui.GuiLM;
import latmod.lib.util.FinalIDObject;
import net.minecraft.client.resources.I18n;

import java.util.List;

public abstract class PlayerAction extends FinalIDObject
{
	public final TextureCoords icon;
	
	public PlayerAction(String id, TextureCoords c)
	{
		super(id);
		icon = c;
	}

	public abstract void onClicked(int playerID);

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
}