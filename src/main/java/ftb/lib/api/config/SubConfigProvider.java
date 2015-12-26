package ftb.lib.api.config;

import cpw.mods.fml.relauncher.*;
import latmod.lib.config.*;

@SideOnly(Side.CLIENT)
public class SubConfigProvider implements IConfigProvider
{
	public final IConfigProvider parent;
	public final Object key;
	
	public SubConfigProvider(IConfigProvider p, Object k)
	{ parent = p; key = k; }
	
	public String getGroupTitle(ConfigGroup g)
	{ return parent.getGroupTitle(g); }
	
	public String getEntryTitle(ConfigEntry e)
	{ return parent.getEntryTitle(e); }
	
	public ConfigGroup getGroup()
	{ return (ConfigGroup)parent.getGroup().getGroup(key); }
	
	public void save()
	{ parent.save(); }
}