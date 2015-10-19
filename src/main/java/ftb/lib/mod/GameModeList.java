package ftb.lib.mod;

import java.util.*;

import com.google.gson.annotations.SerializedName;

public class GameModeList
{
	@SerializedName("default")
	public String defaultMode;
	public String common;
	public List<String> modes;
	public Map<String, String> custom;
	
	public void setDefaults()
	{
		if(defaultMode == null) defaultMode = "default";
		if(common == null) common = "common";
		if(modes == null) { modes = new ArrayList<String>(); modes.add(defaultMode); }
		if(custom == null) custom = new HashMap<String, String>();
	}
}