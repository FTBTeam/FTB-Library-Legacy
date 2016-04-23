package ftb.lib.api.gui.widgets;

import cpw.mods.fml.relauncher.*;
import ftb.lib.api.*;
import ftb.lib.api.gui.IGuiLM;
import latmod.lib.LMUtils;

@SideOnly(Side.CLIENT)
public abstract class ButtonLM extends WidgetLM implements IClickable
{
	private long lastClickMillis = LMUtils.millis();
	public boolean doubleClickRequired = false;
	
	public ButtonLM(IGuiLM g, int x, int y, int w, int h)
	{ super(g, x, y, w, h); }
	
	@Override
	public void mousePressed(int b)
	{
		if(mouseOver())
		{
			if(doubleClickRequired)
			{
				long l = LMUtils.millis();
				if(l - lastClickMillis < 300) onClicked(MouseButton.get(b));
				lastClickMillis = l;
			}
			
			else onClicked(MouseButton.get(b));
		}
	}
}