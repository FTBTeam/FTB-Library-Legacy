package ftb.lib.api.config;

import latmod.lib.*;
import latmod.lib.config.*;
import net.minecraft.client.resources.I18n;

public class ClientConfigHandler
{
	private static final FastMap<ConfigEntry, ClientConfigHandler> custom = new FastMap<ConfigEntry, ClientConfigHandler>().setWeakIndexing();
	
	public static void addCustom(ClientConfigHandler h)
	{
		if(h == null || h.entry == null || h.entry.parentGroup == null || h.entry.parentGroup.parentList == null) return;
		custom.put(h.entry, h);
	}
	
	public static ClientConfigHandler create(ConfigEntry e)
	{
		ClientConfigHandler h = custom.get(e);
		return (h == null) ? new ClientConfigHandler(e) : h;
	}
	
	public final ConfigEntry entry;
	
	public ClientConfigHandler(ConfigEntry e)
	{ entry = e; }
	
	public void initGui()
	{
	}
	
	public void onClicked()
	{
		if(entry.type == PrimitiveType.NULL) return;
		else if(entry.type == PrimitiveType.BOOLEAN)
		{
			ConfigEntryBool e = (ConfigEntryBool)entry;
			e.set(!e.get());
		}
		else if(entry.type == PrimitiveType.ENUM)
		{
			ConfigEntryEnum<?> e = (ConfigEntryEnum<?>)entry;
			Object[] o = e.enumClass.getEnumConstants();
			if(o != null)
			{
				int i = e.get().ordinal() + 1;
				e.set(o[i % o.length]);
			}
		}
	}
	
	public int getColor(boolean mouseOver)
	{
		if(PrimitiveType.isNull(entry.type))
			return mouseOver ? 0xFFFFFF00 : 0xFFFFAA00;
		else if(entry.type.isEnum)
			return mouseOver ? 0xFF51B6FF : 0xFF0094FF;
		else if(entry.type.isBoolean)
		{
			if(((ConfigEntryBool)entry).get())
				return mouseOver ? 0xFF33D333 : 0xFF339933;
			else
				return mouseOver ? 0xFFD33333 : 0xFF993333;
		}
		else if(entry.type.isArray)
			return mouseOver? 0xFFFF6C59 : 0xFFFF4F34;
		else if(entry.type.isNumber)
			return mouseOver ? 0xFFC364EF : 0xFF933ABC;
		return mouseOver ? 0xFFFFFFFF : 0xFF999999;
	}
	
	public String getTitle()
	{ return I18n.format(entry.getFullID()); }
	
	public String getText()
	{
		if(entry.type == PrimitiveType.NULL) return "edit";
		else if(entry.type == PrimitiveType.BOOLEAN)
			return ((ConfigEntryBool)entry).get() ? "true" : "false";
		else if(entry.type == PrimitiveType.ENUM)
			return ((ConfigEntryEnum<?>)entry).get().name().toLowerCase();
		return entry.getJson().toString();
	}
	
	public void mouseText(FastList<String> l)
	{
	}
}