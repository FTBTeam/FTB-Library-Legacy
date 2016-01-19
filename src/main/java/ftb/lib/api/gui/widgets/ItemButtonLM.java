package ftb.lib.api.gui.widgets;

import cpw.mods.fml.relauncher.*;
import ftb.lib.TextureCoords;
import ftb.lib.api.gui.*;
import net.minecraft.item.ItemStack;

@SideOnly(Side.CLIENT)
public abstract class ItemButtonLM extends ButtonLM
{
	public ItemStack item;
	
	public ItemButtonLM(IGuiLM g, int x, int y, int w, int h, ItemStack is)
	{
		super(g, x, y, w, h);
		item = is;
	}
	
	public ItemButtonLM(IGuiLM g, int x, int y, int w, int h)
	{ this(g, x, y, w, h, null); }
	
	public void setItem(ItemStack is)
	{ item = is; }
	
	public void setBackground(TextureCoords t)
	{ background = t; }
	
	public void renderWidget()
	{ if(item != null) GuiLM.drawItem(gui, item, getAX(), getAY()); }
}