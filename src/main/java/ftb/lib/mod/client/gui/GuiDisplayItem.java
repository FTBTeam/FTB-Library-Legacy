package ftb.lib.mod.client.gui;

import ftb.lib.gui.GuiLM;
import ftb.lib.item.ItemDisplay;
import ftb.lib.mod.FTBLibMod;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.*;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiDisplayItem extends GuiLM
{
	public static final ResourceLocation texture = FTBLibMod.mod.getLocation("textures/gui/displayitem.png");
	
	public ItemDisplay itemDisplay;
	
	public GuiDisplayItem(ItemDisplay i)
	{
		super(null, texture);
		xSize = 144;
		ySize = 182;
		itemDisplay = i;
	}
	
	public void addWidgets()
	{
	}
	
	public void drawBackground()
	{
		super.drawBackground();
		
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.pushMatrix();
		GlStateManager.translate(guiLeft + xSize / 2F, guiTop + ySize / 2F, 0F);
		GlStateManager.scale(itemDisplay.scale, itemDisplay.scale, 1F);
		itemDisplay.item.stackSize = 1;
		drawItem(itemDisplay.item, -8, -8);
		GlStateManager.popMatrix();
	}
	
	public void drawText(List<String> l)
	{
		if(itemDisplay.title != null && !itemDisplay.title.isEmpty())
			drawCenteredString(fontRendererObj, itemDisplay.title, guiLeft + xSize / 2, guiTop + 6, 0xFFFFFFFF);
		if(itemDisplay.desc != null && !itemDisplay.desc.isEmpty()) l.addAll(itemDisplay.desc);
		super.drawText(l);
	}
	
	public boolean handleDragNDrop(GuiContainer g, int x, int y, ItemStack is, int b)
	{
		if(is != null && x > guiLeft && x < guiLeft + xSize && y > guiTop && y < guiTop + ySize)
		{
			itemDisplay = new ItemDisplay(is.copy(), is.getDisplayName(), null, itemDisplay.scale);
			is.stackSize = 0;
			return true;
		}
		
		return false;
	}
}