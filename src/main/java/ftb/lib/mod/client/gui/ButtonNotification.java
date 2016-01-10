package ftb.lib.mod.client.gui;

import ftb.lib.api.gui.*;
import ftb.lib.client.GlStateManager;
import ftb.lib.gui.GuiLM;
import ftb.lib.gui.widgets.ButtonLM;
import ftb.lib.notification.ClientNotifications;
import latmod.lib.LMColorUtils;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ButtonNotification extends ButtonLM
{
	public final ClientNotifications.Perm notification;
	
	public ButtonNotification(GuiNotifications g, ClientNotifications.Perm n)
	{
		super(g, 0, 0, 0, 24);
		notification = n;
		posY += g.buttonList.size() * 25;
		title = n.notification.title.getFormattedText();
		width = gui.getFontRenderer().getStringWidth(n.notification.title.getFormattedText());
		if(n.notification.desc != null)
			width = Math.max(width, gui.getFontRenderer().getStringWidth(n.notification.desc.getFormattedText()));
		if(n.notification.item != null) width += 20;
		width += 8;
	}
	
	public void renderWidget()
	{
		int ax = getAX();
		int ay = getAY();
		
		int tx = 4;
		ItemStack is = notification.notification.item;
		if(is != null)
		{
			tx += 20;
			gui.drawItem(is, ax + 4, ay + 4);
		}
		
		GlStateManager.color(1F, 1F, 1F, 1F);
		
		int color = LMColorUtils.getRGBA(notification.notification.color, mouseOver(ax, ay) ? 255 : 185);
		
		GuiLM.drawBlankRect(ax, ay, gui.getZLevel(), parentPanel.width, height, color);
		gui.getFontRenderer().drawString(title, ax + tx, ay + 4, 0xFFFFFFFF);
		if(notification.notification.desc != null)
			gui.getFontRenderer().drawString(notification.notification.desc.getFormattedText(), ax + tx, ay + 14, 0xFFFFFFFF);
		
		if(mouseOver(ax, ay))
		{
			float alpha = 0.4F;
			if(gui.mouseX >= ax + width - 16) alpha = 1F;
			
			GlStateManager.color(1F, 1F, 1F, alpha);
			gui.render(GuiIcons.close, ax + width - 18, ay + 4);
			GlStateManager.color(1F, 1F, 1F, 1F);
		}
	}
	
	public void onButtonPressed(int b)
	{
		gui.playClickSound();
		
		if(gui.mouseX < getAX() + width - 16) notification.onClicked();
		ClientNotifications.Perm.list.remove(notification);
		
		gui.initLMGui();
		gui.refreshWidgets();
	}
	
	public void addMouseOverText(List<String> l)
	{
		int ax = getAX();
		if(mouseOver(ax, getAY()) && gui.mouseX >= ax + width - 16)
		{
			l.add(FTBLibLang.button_close());
			return;
		}
		
		if(notification.notification.mouse != null && notification.notification.mouse.hover != null)
			for(int i = 0; i < notification.notification.mouse.hover.length; i++)
				if(notification.notification.mouse.hover[i] != null)
					l.add(notification.notification.mouse.hover[i].getFormattedText());
	}
}