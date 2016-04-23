package ftb.lib.mod.client.gui;

import cpw.mods.fml.relauncher.*;
import ftb.lib.TextureCoords;
import ftb.lib.api.client.FTBLibClient;
import ftb.lib.api.gui.GuiLM;
import ftb.lib.api.gui.widgets.ButtonLM;
import net.minecraft.client.gui.GuiScreen;

@SideOnly(Side.CLIENT)
public class GuiViewImage extends GuiLM
{
	public final GuiScreen parent;
	public final TextureCoords texCoords;
	public final ButtonLM buttonClose;
	
	public GuiViewImage(GuiScreen p, TextureCoords t)
	{
		super(null, null);
		parent = p;
		texCoords = t;
		
		buttonClose = new ButtonLM(this, 0, 0, 0, 0)
		{
			@Override
			public void onButtonPressed(int b)
			{ close(parent); }
		};
	}
	
	@Override
	public void initLMGui()
	{
		mainPanel.width = buttonClose.width = width;
		mainPanel.height = buttonClose.height = height;
	}
	
	@Override
	public void addWidgets()
	{
		mainPanel.add(buttonClose);
	}
	
	@Override
	public void drawBackground()
	{
		super.drawBackground();
		
		if(texCoords != null && texCoords.isValid())
		{
			FTBLibClient.setTexture(texCoords.texture);
			
			double w = texCoords.width;
			double h = texCoords.height;
			
			if(w > width)
			{
				w = width;
				h = texCoords.getHeight(w);
			}
			
			if(h > height)
			{
				h = height;
				w = texCoords.getWidth(h);
			}
			
			GuiLM.render(texCoords, (width - w) / 2, (height - h) / 2, zLevel, w, h);
		}
	}
}