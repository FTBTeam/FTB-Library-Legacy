package ftb.lib.api.info;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import ftb.lib.mod.client.gui.info.ButtonInfoTextLine;
import ftb.lib.mod.client.gui.info.GuiInfo;
import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by LatvianModder on 20.03.2016.
 */
public class InfoTextLine implements IJsonSerializable
{
	public final InfoPage page;
	private String text;
	
	public InfoTextLine(InfoPage c, String s)
	{
		page = c;
		text = s;
	}
	
	public ITextComponent getText()
	{ return new TextComponentString(text); }
	
	@SideOnly(Side.CLIENT)
	public ButtonInfoTextLine createWidget(GuiInfo gui)
	{ return new ButtonInfoTextLine(gui, this); }
	
	@Override
	public void fromJson(JsonElement e)
	{ text = e.getAsString(); }
	
	@Override
	public JsonElement getSerializableElement()
	{ return new JsonPrimitive(text); }
	
	public final InfoTextLine copy(InfoPage p)
	{ return p.createLine(getSerializableElement()); }
}