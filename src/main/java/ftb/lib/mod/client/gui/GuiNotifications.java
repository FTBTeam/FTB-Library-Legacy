package ftb.lib.mod.client.gui;

import ftb.lib.gui.GuiLM;
import ftb.lib.mod.client.FTBLibModClient;
import ftb.lib.notification.ClientNotifications;
import latmod.lib.MathHelperLM;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.*;

import java.util.*;

@SideOnly(Side.CLIENT)
public class GuiNotifications extends GuiLM
{
	public final ArrayList<ButtonNotification> buttonList;
	
	public GuiNotifications(GuiScreen parent)
	{
		super(parent, null, null);
		hideNEI = true;
		ySize = 25 * 7;
		
		buttonList = new ArrayList<>();
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
		
		GlStateManager.color(0F, 0F, 0F, 0.4F);
		drawBlankRect(guiLeft, guiTop, zLevel, xSize, ySize);
		
		for(int i = 1; i < 7; i++)
			drawBlankRect(guiLeft, guiTop + i * 25 - 1, zLevel, xSize, 1);
		
		GlStateManager.color(1F, 1F, 1F, 1F);
		
		for(ButtonNotification b : buttonList)
			b.renderWidget();
	}
}