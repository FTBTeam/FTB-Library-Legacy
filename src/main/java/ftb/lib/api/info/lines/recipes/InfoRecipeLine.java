package ftb.lib.api.info.lines.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ftb.lib.api.info.InfoPage;
import ftb.lib.api.info.lines.InfoExtendedTextLine;
import ftb.lib.mod.client.gui.info.ButtonInfoRecipe;
import ftb.lib.mod.client.gui.info.ButtonInfoTextLine;
import ftb.lib.mod.client.gui.info.GuiInfo;
import net.minecraft.item.ItemStack;

import java.util.Map;

/**
 * Created by LatvianModder on 23.03.2016.
 */
public class InfoRecipeLine extends InfoExtendedTextLine
{
	public String type;
	public Map<Integer, ItemStack> items;
	public ItemStack result;
	
	public InfoRecipeLine(InfoPage c)
	{
		super(c, null);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ButtonInfoTextLine createWidget(GuiInfo gui)
	{ return new ButtonInfoRecipe(gui, this); }
	
	@Override
	public void func_152753_a(JsonElement e)
	{
		super.func_152753_a(e);
		
		JsonObject o = e.getAsJsonObject();
	}
	
	@Override
	public JsonElement getSerializableElement()
	{
		JsonObject o = (JsonObject) super.getSerializableElement();
		
		return o;
	}
}