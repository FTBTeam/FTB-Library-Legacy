package ftb.lib.mod.client.gui.info;

import ftb.lib.api.client.FTBLibClient;
import ftb.lib.api.gui.widgets.ButtonLM;
import ftb.lib.api.info.InfoPage;
import net.minecraft.util.*;

import java.util.List;

/**
 * Created by LatvianModder on 04.03.2016.
 */
public class ButtonInfoPage extends ButtonLM
{
	public final GuiInfo guiInfo;
	public final InfoPage page;
	public String hover;
	
	public ButtonInfoPage(GuiInfo g, InfoPage p)
	{
		super(g, 0, g.panelPages.height, g.panelWidth - 36, 13);
		guiInfo = g;
		page = p;
		updateTitle();
	}
	
	public void onButtonPressed(int b)
	{
		FTBLibClient.playClickSound();
		
		if(page.childPages.isEmpty())
		{
			guiInfo.selectedPage = page;
			guiInfo.sliderText.value = 0F;
			guiInfo.panelText.posY = 10;
			guiInfo.panelText.refreshWidgets();
		}
		else FTBLibClient.openGui(new GuiInfo(guiInfo, page));
	}
	
	public void updateTitle()
	{
		IChatComponent titleC = page.getTitleComponent().createCopy();
		if(guiInfo.selectedPage == page) titleC.getChatStyle().setBold(true);
		title = titleC.getFormattedText();
		hover = null;
		
		if(gui.getFontRenderer().getStringWidth(title) > width)
		{
			hover = title + "";
			title = gui.getFontRenderer().trimStringToWidth(title, width - 3) + "...";
		}
	}
	
	public void addMouseOverText(List<String> l)
	{
		if(hover != null) l.add(hover);
	}
	
	public void renderWidget()
	{
		int ay = getAY();
		if(ay < -height || ay > guiInfo.mainPanel.height) return;
		int ax = getAX();
		guiInfo.getFontRenderer().drawString(mouseOver(ax, ay) ? (EnumChatFormatting.UNDERLINE + title) : title, ax + 1, ay + 1, guiInfo.colorText);
	}
}