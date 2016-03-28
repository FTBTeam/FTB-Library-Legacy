package ftb.lib.api.info;

import com.google.gson.*;
import ftb.lib.JsonHelper;
import ftb.lib.api.gui.IClientActionGui;
import ftb.lib.mod.client.gui.info.*;
import ftb.lib.mod.net.MessageDisplayGuide;
import latmod.lib.*;
import latmod.lib.util.FinalIDObject;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.*;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.*;

import java.io.File;
import java.util.*;

public class InfoPage extends FinalIDObject implements IJsonSerializable, IClientActionGui // GuideFile
{
	private static final RemoveFilter<Map.Entry<String, InfoPage>> cleanupFilter = new RemoveFilter<Map.Entry<String, InfoPage>>()
	{
		public boolean remove(Map.Entry<String, InfoPage> entry)
		{ return entry.getValue().childPages.isEmpty() && entry.getValue().getUnformattedText().trim().isEmpty(); }
	};
	
	public InfoPage parent = null;
	private IChatComponent title;
	public final List<InfoTextLine> text;
	public final Map<String, InfoPage> childPages;
	
	public InfoPage(String id)
	{
		super(id);
		text = new ArrayList<>();
		childPages = new LinkedHashMap<>();
	}
	
	public InfoPage setTitle(IChatComponent c)
	{
		title = c;
		return this;
	}
	
	public InfoPage setParent(InfoPage c)
	{
		parent = c;
		return this;
	}
	
	public InfoPage getOwner()
	{
		if(parent == null) return this;
		return parent.getOwner();
	}
	
	public void println(IChatComponent c)
	{ text.add(c == null ? null : new InfoExtendedTextLine(this, c)); }
	
	public void printlnText(String s)
	{ text.add((s == null || s.isEmpty()) ? null : new InfoTextLine(this, s)); }
	
	public String getUnformattedText()
	{
		if(text.isEmpty()) return "";
		StringBuilder sb = new StringBuilder();
		int s = text.size();
		for(int i = 0; i < s; i++)
		{
			InfoTextLine c = text.get(i);
			
			if(c == null || c.getText() == null) sb.append('\n');
			else
			{
				try { sb.append(c.getText().getUnformattedText()); }
				catch(Exception ex) { ex.printStackTrace(); }
			}
			
			if(i != s - 1) sb.append('\n');
		}
		return sb.toString();
	}
	
	public String getFormattedText()
	{
		if(text.isEmpty()) return "";
		StringBuilder sb = new StringBuilder();
		int s = text.size();
		for(int i = 0; i < s; i++)
		{
			InfoTextLine c = text.get(i);
			
			if(c == null || c.getText() == null) sb.append('\n');
			else
			{
				try { sb.append(c.getText().getFormattedText()); }
				catch(Exception ex) { ex.printStackTrace(); }
			}
			
			if(i != s - 1) sb.append('\n');
		}
		return sb.toString();
	}
	
	public void addSub(InfoPage c)
	{
		childPages.put(c.getID(), c);
		c.setParent(this);
	}
	
	public IChatComponent getTitleComponent()
	{ return title == null ? new ChatComponentText(getID()) : title; }
	
	public InfoPage getSub(String id)
	{
		InfoPage c = childPages.get(id);
		if(c == null)
		{
			c = new InfoPage(id);
			c.setParent(this);
			childPages.put(id, c);
		}
		
		return c;
	}
	
	public void clear()
	{
		text.clear();
		childPages.clear();
	}
	
	public void cleanup()
	{
		for(InfoPage c : childPages.values()) c.cleanup();
		LMMapUtils.removeAll(childPages, cleanupFilter);
	}
	
	public void sortAll()
	{
		//TODO: sort
		for(InfoPage c : childPages.values()) c.sortAll();
	}
	
	public void copyFrom(InfoPage c)
	{ for(int i = 0; i < c.childPages.size(); i++) addSub(c.setParent(this)); }
	
	public InfoPage getParentTop()
	{
		if(parent == null) return this;
		return parent.getParentTop();
	}
	
	public JsonElement getSerializableElement()
	{
		JsonObject o = new JsonObject();
		
		if(title != null) o.add("N", JsonHelper.serializeICC(title));
		
		if(!text.isEmpty())
		{
			JsonArray a = new JsonArray();
			for(InfoTextLine c : text)
				a.add(c == null ? JsonNull.INSTANCE : c.getSerializableElement());
			o.add("T", a);
		}
		
		if(!childPages.isEmpty())
		{
			JsonObject o1 = new JsonObject();
			for(InfoPage c : childPages.values())
				o1.add(c.getID(), c.getSerializableElement());
			o.add("S", o1);
		}
		
		return o;
	}
	
	public void fromJson(JsonElement e)
	{
		clear();
		
		if(e == null || !e.isJsonObject()) return;
		JsonObject o = e.getAsJsonObject();
		
		title = o.has("N") ? JsonHelper.deserializeICC(o.get("N")) : null;
		
		if(o.has("T"))
		{
			JsonArray a = o.get("T").getAsJsonArray();
			for(int i = 0; i < a.size(); i++)
				text.add(InfoTextLine.get(this, a.get(i)));
		}
		
		if(o.has("S"))
		{
			JsonObject o1 = o.get("S").getAsJsonObject();
			
			for(Map.Entry<String, JsonElement> entry : o1.entrySet())
			{
				InfoPage c = new InfoPage(entry.getKey());
				c.setParent(this);
				c.fromJson(entry.getValue());
				childPages.put(c.getID(), c);
			}
		}
	}
	
	protected static void loadFromFiles(InfoPage c, File f)
	{
		if(f == null || !f.exists()) return;
		
		if(f.isDirectory())
		{
			File[] f1 = f.listFiles();
			
			if(f1 != null && f1.length > 0)
			{
				Arrays.sort(f1, LMFileUtils.fileComparator);
				InfoPage c1 = c.getSub(f.getName());
				for(File f2 : f1) loadFromFiles(c1, f2);
			}
		}
		else if(f.isFile())
		{
			if(f.getName().endsWith(".txt"))
			{
				try
				{
					InfoPage c1 = c.getSub(LMFileUtils.getRawFileName(f));
					
					for(String s : LMFileUtils.load(f))
					{
						if(s.isEmpty()) c1.text.add(null);
						else if(s.length() > 2 && s.charAt(0) == '{' && s.charAt(s.length() - 1) == '}')
						{
							c1.text.add(InfoTextLine.get(c1, LMJsonUtils.fromJson(s)));
						}
						else c1.text.add(new InfoTextLine(c1, s));
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	public void displayGuide(EntityPlayerMP ep)
	{
		if(ep != null && !(ep instanceof FakePlayer)) new MessageDisplayGuide(this).sendTo(ep);
	}
	
	public LMColor getBackgroundColor()
	{ return (parent == null) ? null : parent.getBackgroundColor(); }
	
	public LMColor getTextColor()
	{ return (parent == null) ? null : parent.getTextColor(); }
	
	public Boolean useUnicodeFont()
	{ return (parent == null) ? null : parent.useUnicodeFont(); }
	
	@SideOnly(Side.CLIENT)
	public ButtonGuidePage createButton(GuiInfo gui)
	{ return new ButtonGuidePage(gui, this); }
	
	public String getFullID()
	{
		if(parent == null) return getID();
		return parent.getFullID() + '.' + getID();
	}
	
	public void onClientDataChanged()
	{
	}
	
	@SideOnly(Side.CLIENT)
	public void initGUI(GuiInfo gui)
	{
	}
}