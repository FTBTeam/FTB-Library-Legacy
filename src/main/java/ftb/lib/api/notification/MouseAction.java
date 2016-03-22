package ftb.lib.api.notification;

import com.google.gson.*;
import ftb.lib.JsonHelper;
import latmod.lib.json.IJsonObject;
import net.minecraft.util.IChatComponent;

import java.util.*;

public class MouseAction implements IJsonObject
{
	public ClickAction click;
	public final List<IChatComponent> hover;
	
	public MouseAction()
	{
		hover = new ArrayList<>();
	}
	
	public JsonElement getJson()
	{
		JsonObject o = new JsonObject();
		
		if(click != null) o.add("click", click.getJson());
		
		if(!hover.isEmpty())
		{
			JsonArray h = new JsonArray();
			for(IChatComponent c : hover)
				h.add(JsonHelper.serializeICC(c));
			o.add("hover", h);
		}
		
		return o;
	}
	
	public void setJson(JsonElement e)
	{
		if(e == null || !e.isJsonObject()) return;
		
		JsonObject o1 = e.getAsJsonObject();
		
		if(o1.has("click"))
		{
			click = new ClickAction();
			click.setJson(o1.get("click"));
		}
		else click = null;
		
		if(o1.has("hover"))
		{
			JsonArray a = o1.get("hover").getAsJsonArray();
			hover.clear();
			for(int i = 0; i < a.size(); i++)
				hover.add(JsonHelper.deserializeICC(a.get(i)));
		}
	}
	
	public void addHoverText(List<String> l)
	{
		for(IChatComponent c : hover)
		{
			if(c != null) l.add(c.getFormattedText());
			else l.add("");
		}
	}
}