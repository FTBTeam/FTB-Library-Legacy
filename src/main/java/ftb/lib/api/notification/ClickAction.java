package ftb.lib.api.notification;

import com.google.gson.*;
import latmod.lib.json.IJsonObject;
import net.minecraftforge.fml.relauncher.*;

/**
 * Created by LatvianModder on 23.01.2016.
 */
public class ClickAction implements IJsonObject
{
	public ClickActionType type;
	public JsonElement data;
	
	public ClickAction() {}
	
	public ClickAction(ClickActionType t, JsonElement d)
	{
		type = t;
		data = d;
	}
	
	public JsonElement getJson()
	{
		JsonObject o = new JsonObject();
		o.add("type", new JsonPrimitive(type.ID));
		if(data != null) o.add("data", data);
		return o;
	}
	
	public void setJson(JsonElement e)
	{
		JsonObject o = e.getAsJsonObject();
		type = ClickActionRegistry.get(o.get("type").getAsString());
		data = o.get("data");
	}
	
	@SideOnly(Side.CLIENT)
	public void onClicked()
	{ if(type != null) type.onClicked(data); }
}