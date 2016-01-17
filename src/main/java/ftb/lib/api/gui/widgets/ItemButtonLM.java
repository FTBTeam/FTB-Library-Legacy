package ftb.lib.api.gui.widgets;

import ftb.lib.TextureCoords;
import ftb.lib.api.gui.GuiLM;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public abstract class ItemButtonLM extends ButtonLM
{
	public ItemStack item;
	
	public ItemButtonLM(GuiLM g, int x, int y, int w, int h, ItemStack is)
	{
		super(g, x, y, w, h);
		item = is;
	}
	
	public ItemButtonLM(GuiLM g, int x, int y, int w, int h)
	{ this(g, x, y, w, h, null); }
	
	public void setItem(ItemStack is)
	{ item = is; }
	
	public void setBackground(TextureCoords t)
	{ background = t; }
	
	public void renderWidget()
	{ if(item != null) gui.drawItem(item, getAX(), getAY()); }
}