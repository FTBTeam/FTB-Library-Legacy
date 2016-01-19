package ftb.lib.api.gui;

import ftb.lib.api.client.FTBLibClient;
import ftb.lib.api.gui.callback.*;
import ftb.lib.mod.client.FTBLibModClient;
import ftb.lib.mod.client.gui.*;
import latmod.lib.PrimitiveType;

public class LMGuis
{
	public static void displayColorSelector(IColorCallback cb, int col, Object id, boolean instant)
	{
		if(FTBLibModClient.open_hsb_cg.get()) FTBLibClient.openGui(new GuiSelectColorHSB(cb, col, id, instant));
		else FTBLibClient.openGui(new GuiSelectColorRGB(cb, col, id, instant));
	}
	
	public static void displayFieldSelector(Object id, PrimitiveType typ, Object d, IFieldCallback c)
	{ FTBLibClient.openGui(new GuiSelectField(id, typ, String.valueOf(d), c)); }
}