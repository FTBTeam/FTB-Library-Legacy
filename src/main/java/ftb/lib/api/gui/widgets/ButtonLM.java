package ftb.lib.api.gui.widgets;

import ftb.lib.api.gui.IGuiLM;
import latmod.lib.LMUtils;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public abstract class ButtonLM extends WidgetLM
{
	public int customID = 0;
	private long lastClickMillis = LMUtils.millis();
	public boolean doubleClickRequired = false;
	
	public ButtonLM(IGuiLM g, int x, int y, int w, int h)
	{ super(g, x, y, w, h); }
	
	public void mousePressed(int b)
	{
		if(mouseOver())
		{
			if(doubleClickRequired)
			{
				long l = LMUtils.millis();
				if(l - lastClickMillis < 300) onButtonPressed(b);
				lastClickMillis = l;
			}
			
			else onButtonPressed(b);
		}
	}
	
	public abstract void onButtonPressed(int b);
}