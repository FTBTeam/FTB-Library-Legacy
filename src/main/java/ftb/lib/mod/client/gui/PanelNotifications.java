package ftb.lib.mod.client.gui;

import ftb.lib.gui.widgets.*;
import ftb.lib.notification.ClientNotifications;

public class PanelNotifications extends PanelLM
{
	public final SliderLM scrollBar;
	
	public PanelNotifications(GuiNotifications g)
	{
		super(g, 0, 0, 0, 0);
		width = 120;
		
		scrollBar = new SliderLM(g, 0, 0, 16, 0, 8)
		{
			public boolean isEnabled()
			{ return parentPanel.mouseOver() || mouseOver(); }
		};
		
		scrollBar.displayMax = 0;
		scrollBar.isVertical = true;
	}
	
	public void addWidgets()
	{
		//add(scrollBar);
		
		width = 0;
		
		ClientNotifications.Perm.list.sort(null);
		for(ClientNotifications.Perm p : ClientNotifications.Perm.list)
		{
			ButtonNotification b = new ButtonNotification(this, p);
			add(b);
			width = Math.max(width, b.width);
		}
	}
	
	public void renderWidget()
	{
		for(WidgetLM w : widgets)
			w.renderWidget();
	}
}