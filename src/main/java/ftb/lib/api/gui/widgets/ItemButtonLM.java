package ftb.lib.api.gui.widgets;

import ftb.lib.api.gui.GuiLM;
import ftb.lib.api.gui.IGuiLM;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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