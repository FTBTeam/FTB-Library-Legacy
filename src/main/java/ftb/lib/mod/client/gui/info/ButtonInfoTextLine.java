package ftb.lib.mod.client.gui.info;

import cpw.mods.fml.relauncher.*;
import ftb.lib.api.client.GlStateManager;
import ftb.lib.api.gui.GuiLM;
import ftb.lib.api.gui.widgets.ButtonLM;
import ftb.lib.api.info.lines.InfoTextLine;
import net.minecraft.util.IChatComponent;

import java.util.List;

/**
 * Created by LatvianModder on 04.03.2016.
 */
@SideOnly(Side.CLIENT)
public class ButtonInfoTextLine extends ButtonLM
{
	public final GuiInfo guiInfo;
	public List<String> text;
	
	public ButtonInfoTextLine(GuiInfo g, InfoTextLine l)
	{
		super(g, 0, g.panelText.height, 0, 0);
		guiInfo = g;
		
		if(l != null)
		{
			IChatComponent c = l.getText();
			
			if(c != null)
			{
				text = guiInfo.getFontRenderer().listFormattedStringToWidth(c.getFormattedText(), g.panelText.width);
				if(text.isEmpty()) text = null;
			}
		}
		
		if(text != null)
		{
			if(text.size() > 1) width = g.panelText.width;
			else width = g.getFontRenderer().getStringWidth(text.get(0));
			height = 10 * text.size();
		}
		else
		{
			width = 0;
			height = 11;
		}
	}
	
	public void addMouseOverText(List<String> l)
	{
	}
	
	public void onButtonPressed(int b)
	{
	}
	
	public void renderWidget()
	{
		int ay = getAY();
		if(ay < -height || ay > guiInfo.mainPanel.height) return;
		int ax = getAX();
		
		boolean mouseOver = mouseOver();
		
		if(text != null)
		{
			for(int i = 0; i < text.size(); i++)
			{
				guiInfo.getFontRenderer().drawString(text.get(i), ax, ay + i * 10, guiInfo.colorText);
			}
		}
		
		if(mouseOver)
		{
			GlStateManager.color(0F, 0F, 0F, 0.101F);
			GuiLM.drawBlankRect(ax, ay, guiInfo.getZLevel(), width, height);
		}
	}
}
