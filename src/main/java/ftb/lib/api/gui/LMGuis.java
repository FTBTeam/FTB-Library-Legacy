package ftb.lib.api.gui;

import ftb.lib.api.gui.callback.*;
import ftb.lib.client.FTBLibClient;
import ftb.lib.mod.client.FTBLibModClient;
import ftb.lib.mod.client.gui.*;
import latmod.lib.PrimitiveType;

public class LMGuis
{
	public static void displayColorSelector(IColorCallback cb, int col, Object id, boolean instant)
	{
		if(FTBLibModClient.open_hsb_cg.get())
			FTBLibClient.mc.displayGuiScreen(new GuiSelectColorHSB(cb, col, id, instant));
		else FTBLibClient.mc.displayGuiScreen(new GuiSelectColorRGB(cb, col, id, instant));
	}
	
	public static void displayFieldSelector(Object id, PrimitiveType typ, Object d, IFieldCallback c)
	{ FTBLibClient.mc.displayGuiScreen(new GuiSelectField(id, typ, String.valueOf(d), c)); }
}