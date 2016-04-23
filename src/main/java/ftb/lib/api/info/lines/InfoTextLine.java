package ftb.lib.api.info.lines;

import com.google.gson.*;
import cpw.mods.fml.relauncher.*;
import ftb.lib.api.info.InfoPage;
import ftb.lib.api.info.lines.recipes.InfoRecipeLine;
import ftb.lib.mod.client.gui.info.*;
import net.minecraft.util.*;

/**
 * Created by LatvianModder on 20.03.2016.
 */
public class InfoTextLine implements IJsonSerializable
{
	public static InfoTextLine get(InfoPage c, JsonElement e)
	{
		if(e == null || e.isJsonNull()) return null;
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
			else if(o.has("recipe"))
			{
				l = new InfoRecipeLine(c);
			}
			else
			{
				l = new InfoExtendedTextLine(c, null);
			}
			
			l.func_152753_a(o);
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
	
	public IChatComponent getText()
	{ return new ChatComponentText(text); }
	
	@SideOnly(Side.CLIENT)
	public ButtonInfoTextLine createWidget(GuiInfo gui)
	{ return new ButtonInfoTextLine(gui, this); }
	
	@Override
	public void func_152753_a(JsonElement e)
	{ text = e.getAsString(); }
	
	@Override
	public JsonElement getSerializableElement()
	{ return new JsonPrimitive(text); }
	
	public final InfoTextLine copy(InfoPage p)
	{ return get(p, getSerializableElement()); }
}