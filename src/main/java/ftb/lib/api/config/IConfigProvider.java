package ftb.lib.api.config;

import cpw.mods.fml.relauncher.*;
import latmod.lib.config.*;

@SideOnly(Side.CLIENT)
public interface IConfigProvider
{
	public String getGroupTitle(ConfigGroup g);
	public String getEntryTitle(ConfigEntry e);
	public ConfigGroup getGroup();
	public void save();
}