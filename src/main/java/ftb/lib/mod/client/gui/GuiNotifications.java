package ftb.lib.mod.client.gui;

import cpw.mods.fml.relauncher.*;
import ftb.lib.client.GlStateManager;
import ftb.lib.gui.GuiLM;
import ftb.lib.gui.widgets.WidgetLM;
import latmod.lib.MathHelperLM;
import net.minecraft.client.gui.GuiScreen;

@SideOnly(Side.CLIENT)
public class GuiNotifications extends GuiLM
{
	public final PanelNotifications panel;
	
	public GuiNotifications(GuiScreen parent)
	{
		super(parent, null, null);
		hideNEI = true;
		ySize = 200;
		
		panel = new PanelNotifications(this);
	}
	
	public void addWidgets()
	{
		mainPanel.add(panel);
		
		xSize = panel.width = MathHelperLM.clampInt(panel.width, 200, 300);
		
		for(WidgetLM w : panel.widgets)
			w.width = xSize;
	}
	
	public void drawForeground()
	{
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		GlStateManager.color(1F, 1F, 1F, 1F);
		
		drawBlankRect(guiLeft, guiTop, zLevel, xSize, ySize, 0x33000000);
		panel.renderWidget();
	}
}