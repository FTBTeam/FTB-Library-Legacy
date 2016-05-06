package ftb.lib.api.info.lines;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import ftb.lib.api.info.InfoPage;
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
	public static InfoTextLine get(InfoPage c, JsonElement e)
	{
		if(e == null || e.isJsonNull()) { return null; }
		else if(e.isJsonPrimitive())
		{
			String s = e.getAsString();
			return s.trim().isEmpty() ? null : new InfoTextLine(c, s);
		}
		else
		{
			JsonObject o = e.getAsJsonObject();
			
			InfoExtendedTextLine l;
			
			if(o.has("image"))
			{
				l = new InfoImageLine(c);
			}
			//else if(o.has("recipe"))
			//{
			//	l = new InfoRecipeLine(c);
			//}
			else
			{
				l = new InfoExtendedTextLine(c, null);
			}
			
			l.fromJson(o);
			return l;
		}
	}
	
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
	{ return get(p, getSerializableElement()); }
}