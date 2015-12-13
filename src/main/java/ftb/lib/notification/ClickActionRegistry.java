package ftb.lib.notification;

import latmod.lib.*;

public class ClickActionRegistry
{
	private static final FastList<ClickAction> list = new FastList<ClickAction>();
	
	static
	{
		add(ClickAction.CMD);
		add(ClickAction.SHOW_CMD);
		add(ClickAction.URL);
		add(ClickAction.FILE);
		add(ClickAction.GUI);
	}
	
	public static void add(ClickAction a)
	{
		if(a != null && a.type != null)
		{
			if(LMStringUtils.isValid(a.ID) && !list.contains(a.ID))
				list.add(a);
		}
	}
	
	public static ClickAction get(String s)
	{ return list.getObj(s); }
}