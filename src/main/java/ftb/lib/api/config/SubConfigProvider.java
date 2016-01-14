package ftb.lib.api.config;

import latmod.lib.config.*;

public class SubConfigProvider implements IConfigProvider
{
	public final IConfigProvider parent;
	public final ConfigGroup subGroup;
	
	public SubConfigProvider(IConfigProvider p, Object k)
	{
		parent = p;
		subGroup = p.getGroup().getGroup(k);
	}
	
	public String getGroupTitle(ConfigGroup g)
	{ return parent.getGroupTitle(g); }
	
	public String getEntryTitle(ConfigEntry e)
	{ return parent.getEntryTitle(e); }
	
	public ConfigGroup getGroup()
	{ return subGroup; }
	
	public void save()
	{ parent.save(); }
}