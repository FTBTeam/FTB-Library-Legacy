package ftb.lib.api.gui;

import ftb.lib.api.client.FTBLibClient;
import ftb.lib.api.gui.callback.*;
import ftb.lib.mod.client.gui.*;
import latmod.lib.*;

public class LMGuis
{
	public static void displayColorSelector(IColorCallback cb, LMColor col, Object id, boolean instant)
	{ FTBLibClient.openGui(new GuiSelectColor(cb, col, id, instant)); }
	
	public static void displayFieldSelector(Object id, PrimitiveType typ, Object d, IFieldCallback c)
	{ FTBLibClient.openGui(new GuiSelectField(id, typ, String.valueOf(d), c)); }
}