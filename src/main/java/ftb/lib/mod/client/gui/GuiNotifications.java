package ftb.lib.mod.client.gui;

import cpw.mods.fml.relauncher.*;
import ftb.lib.gui.GuiLM;
import ftb.lib.mod.client.FTBLibModClient;
import ftb.lib.notification.ClientNotifications;
import latmod.lib.*;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

import java.util.Collections;

@SideOnly(Side.CLIENT)
public class GuiNotifications extends GuiLM
{
	public final FastList<ButtonNotification> buttonList;
	
	public GuiNotifications(GuiScreen parent)
	{
		super(parent, null, null);
		hideNEI = true;
		ySize = 25 * 7;
		
		buttonList = new FastList<ButtonNotification>();
	}
	
	public void initLMGui()
	{
		xSize = 0;
		
		buttonList.clear();
		
		Collections.sort(ClientNotifications.Perm.list, null);
		
		int s = Math.min(ClientNotifications.Perm.list.size(), 7);
		
		for(int i = 0; i < s; i++)
		{
			ClientNotifications.Perm p = ClientNotifications.Perm.list.get(i);
			ButtonNotification b = new ButtonNotification(this, p);
			buttonList.add(b);
			xSize = Math.max(xSize, b.width);
		}
		
		xSize = MathHelperLM.clampInt(xSize, 200, 300);
		
		for(ButtonNotification b : buttonList)
			b.width = xSize;
	}
	
	public void addWidgets()
	{
		mainPanel.addAll(buttonList);
	}
	
	public void drawBackground()
	{
		super.drawBackground();
		
		fontRendererObj.drawString(I18n.format(FTBLibModClient.notifications.getFullID()), guiLeft + 4, guiTop - 11, 0xFFFFFFFF);
		
		drawBlankRect(guiLeft, guiTop, zLevel, xSize, ySize, 0x66000000);
		
		for(int i = 1; i < 7; i++)
			drawBlankRect(guiLeft, guiTop + i * 25 - 1, zLevel, xSize, 1, 0x66000000);
		
		for(ButtonNotification b : buttonList)
			b.renderWidget();
	}
}