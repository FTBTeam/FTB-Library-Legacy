package ftb.lib.notification;

import com.google.gson.*;
import net.minecraft.util.IChatComponent;

import java.lang.reflect.Type;

public class MouseAction
{
	public ClickAction click;
	public JsonElement val;
	public IChatComponent[] hover;
	
	public MouseAction() {}
	
	public MouseAction(ClickAction c, JsonElement v)
	{
		click = c;
		val = (v == null) ? JsonNull.INSTANCE : v;
	}
	
	public static class Serializer implements JsonSerializer<MouseAction>, JsonDeserializer<MouseAction>
	{
		public JsonElement serialize(MouseAction src, Type typeOfSrc, JsonSerializationContext context)
		{
			if(src == null) return null;
			JsonObject o = new JsonObject();
			
			if(src.click != null)
			{
				o.add("click", new JsonPrimitive(src.click.toString()));
				if(src.val != null && !src.val.isJsonNull()) o.add("val", src.val);
			}
			
			if(src.hover != null && src.hover.length > 0)
			{
				JsonArray h = new JsonArray();
				for(int i = 0; i < src.hover.length; i++)
				{ if(src.hover[i] != null) h.add(context.serialize(src.hover[i], IChatComponent.class)); }
				o.add("hover", h);
			}
			
			return o;
		}
		
		public MouseAction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
		{
			if(json.isJsonNull()) return null;
			JsonObject o1 = json.getAsJsonObject();
			
			MouseAction mouse = new MouseAction();
			
			if(o1.has("click"))
			{
				mouse.click = ClickActionRegistry.get(o1.get("click").getAsString());
				if(mouse.click != null && o1.has("val")) mouse.val = o1.get("val");
			}
			
			if(o1.has("hover"))
			{
				JsonArray a = o1.get("hover").getAsJsonArray();
				mouse.hover = new IChatComponent[a.size()];
				for(int i = 0; i < a.size(); i++)
					mouse.hover[i] = context.deserialize(a.get(i), IChatComponent.class);
			}
			
			return mouse;
		}
	}
}