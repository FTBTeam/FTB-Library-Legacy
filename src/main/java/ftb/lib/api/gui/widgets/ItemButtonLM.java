package ftb.lib.api.gui.widgets;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ftb.lib.api.gui.GuiLM;
import ftb.lib.api.gui.IGuiLM;
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
	
	@Override
	public void renderWidget()
	{ if(item != null) GuiLM.drawItem(gui, item, getAX(), getAY()); }
}