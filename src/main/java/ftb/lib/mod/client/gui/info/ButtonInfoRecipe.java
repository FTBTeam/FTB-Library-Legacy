package ftb.lib.mod.client.gui.info;

import cpw.mods.fml.relauncher.*;
import ftb.lib.TextureCoords;
import ftb.lib.api.MouseButton;
import ftb.lib.api.client.*;
import ftb.lib.api.info.lines.recipes.InfoRecipeLine;
import latmod.lib.util.Pos2I;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.*;

/**
 * Created by LatvianModder on 04.03.2016.
 */
@SideOnly(Side.CLIENT)
public class ButtonInfoRecipe extends ButtonInfoTextLine
{
	public static RenderItem renderItem = new RenderItem();
	
	public TextureCoords texture;
	public Map<Pos2I, ItemStack> items;
	
	public ButtonInfoRecipe(GuiInfo g, InfoRecipeLine l)
	{
		super(g, l);
		
		width = 143;
		height = 80;
		
		items = new HashMap<>();
		texture = new TextureCoords(new ResourceLocation("ftbl", "textures/gui/info/crafting_table.png"), 0, 0, width, height);
		
		items.put(new Pos2I(11, 16), new ItemStack(Items.apple));
		
		items.put(new Pos2I(105, 34), new ItemStack(Items.diamond_sword, 1, 20));
	}
	
	@Override
	public void addMouseOverText(List<String> l)
	{
		if(!items.isEmpty())
		{
			int ax = getAX();
			int ay = getAY();
			
			for(Map.Entry<Pos2I, ItemStack> e : items.entrySet())
			{
				if(gui.mouse().isInside(e.getKey().x + ax, e.getKey().y + ay, 16, 16))
				{
					l.add(e.getValue().getDisplayName());
					e.getValue().getItem().addInformation(e.getValue(), Minecraft.getMinecraft().thePlayer, l, false);
				}
			}
		}
	}
	
	@Override
	public void onClicked(MouseButton button)
	{
	}
	
	@Override
	public void renderWidget()
	{
		int ay = getAY();
		int ax = getAX();
		
		GlStateManager.color(1F, 1F, 1F, 1F);
		render(texture);
		
		boolean uni = guiInfo.getFontRenderer().getUnicodeFlag();
		
		guiInfo.getFontRenderer().setUnicodeFlag(false);
		guiInfo.getFontRenderer().drawString("Shapeless recipe", ax + 11, ay + 5, guiInfo.colorText);
		guiInfo.getFontRenderer().setUnicodeFlag(uni);
		
		if(!items.isEmpty())
		{
			for(Map.Entry<Pos2I, ItemStack> e : items.entrySet())
			{
				FTBLibClient.renderGuiItem(e.getValue(), renderItem, gui.getFontRenderer(), e.getKey().x + ax, e.getKey().y + ay);
			}
		}
	}
}
