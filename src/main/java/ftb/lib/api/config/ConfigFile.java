package ftb.lib.api.config;

import com.google.gson.JsonElement;
import latmod.lib.*;

import java.io.File;

/**
 * Created by LatvianModder on 26.03.2016.
 */
public class ConfigFile extends ConfigGroup
{
	private File file;
	private String displayName;
	
	public ConfigFile(String id)
	{
		super(id);
	}
	
	public void setFile(File f)
	{ file = LMFileUtils.newFile(f); }
	
	public File getFile()
	{ return file; }
	
	public void load()
	{
		JsonElement e = LMJsonUtils.fromJson(file);
		
		if(e.isJsonObject())
		{
			ConfigGroup g = new ConfigGroup(getID());
			g.fromJson(e.getAsJsonObject());
			loadFromGroup(g, false);
		}
	}
	
	public void save()
	{ if(file != null) LMJsonUtils.toJson(file, getSerializableElement()); }
	
	public void setDisplayName(String s)
	{ displayName = s; }
	
	public String getDisplayName()
	{ return displayName == null ? getID() : displayName; }
	
	public void addGroup(String id, Class<?> c)
	{ add(new ConfigGroup(id).addAll(c, null)); }
}