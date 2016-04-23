package ftb.lib.api.info.lines;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ftb.lib.JsonHelper;
import ftb.lib.api.MouseButton;
import ftb.lib.api.client.FTBLibClient;
import ftb.lib.api.info.InfoPage;
import ftb.lib.api.notification.ClickAction;
import ftb.lib.mod.client.gui.info.ButtonInfoExtendedTextLine;
import ftb.lib.mod.client.gui.info.ButtonInfoTextLine;
import ftb.lib.mod.client.gui.info.GuiInfo;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.IChatComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by LatvianModder on 20.03.2016.
 */
public class InfoExtendedTextLine extends InfoTextLine
{
	protected IChatComponent text;
	private ClickAction clickAction;
	private List<IChatComponent> hover;
	
	public InfoExtendedTextLine(InfoPage c, IChatComponent cc)
	{
		super(c, null);
		text = cc;
		
		if(text != null)
		{
			ClickEvent clickEvent = text.getChatStyle().getChatClickEvent();
			if(clickEvent != null)
			{
				clickAction = ClickAction.from(clickEvent);
			}
			
			HoverEvent hoverEvent = text.getChatStyle().getChatHoverEvent();
			if(hoverEvent != null && hoverEvent.getAction() == HoverEvent.Action.SHOW_TEXT)
			{
				hover = Collections.singletonList(hoverEvent.getValue());
			}
		}
	}
	
	@Override
	public IChatComponent getText()
	{ return text; }
	
	@Override
	@SideOnly(Side.CLIENT)
	public ButtonInfoTextLine createWidget(GuiInfo gui)
	{ return new ButtonInfoExtendedTextLine(gui, this); }
	
	public List<IChatComponent> getHover()
	{ return hover; }
	
	@SideOnly(Side.CLIENT)
	public boolean hasClickAction()
	{ return clickAction != null; }
	
	@SideOnly(Side.CLIENT)
	public void onClicked(MouseButton button)
	{
		if(clickAction != null)
		{
			FTBLibClient.playClickSound();
			clickAction.onClicked();
		}
	}
	
	@Override
	public void func_152753_a(JsonElement e)
	{
		JsonObject o = e.getAsJsonObject();
		
		text = o.has("text") ? JsonHelper.deserializeICC(o.get("text")) : null;
		
		if(o.has("click"))
		{
			clickAction = new ClickAction();
			clickAction.func_152753_a(o.get("click"));
		}
		else clickAction = null;
		
		if(o.has("hover"))
		{
			hover = new ArrayList<>();
			
			JsonElement e1 = o.get("hover");
			
			if(e1.isJsonPrimitive()) hover.add(JsonHelper.deserializeICC(e1));
			else
			{
				for(JsonElement e2 : o.get("hover").getAsJsonArray())
				{
					hover.add(JsonHelper.deserializeICC(e2));
				}
			}
			
			if(hover.isEmpty()) hover = null;
		}
		else hover = null;
	}
	
	@Override
	public JsonElement getSerializableElement()
	{
		JsonObject o = new JsonObject();
		if(text != null) o.add("text", JsonHelper.serializeICC(text));
		
		if(clickAction != null)
		{
			o.add("click", clickAction.getSerializableElement());
		}
		
		if(hover != null && !hover.isEmpty())
		{
			if(hover.size() == 1)
			{
				o.add("hover", JsonHelper.serializeICC(hover.get(0)));
			}
			else
			{
				JsonArray a = new JsonArray();
				for(IChatComponent c : hover)
				{
					a.add(JsonHelper.serializeICC(c));
				}
				
				o.add("hover", a);
			}
		}
		
		return o;
	}
	
	public void setClickAction(ClickAction a)
	{ clickAction = a; }
	
	public void setHover(List<IChatComponent> h)
	{
		if(h == null || h.isEmpty()) hover = null;
		else
		{
			hover = new ArrayList<>(h.size());
			hover.addAll(h);
		}
	}
}