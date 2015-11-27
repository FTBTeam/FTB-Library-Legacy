package ftb.lib.mod.client.gui;

import org.lwjgl.input.Mouse;

import cpw.mods.fml.relauncher.*;
import ftb.lib.api.config.*;
import ftb.lib.api.gui.GuiIcons;
import ftb.lib.client.GlStateManager;
import ftb.lib.gui.GuiLM;
import ftb.lib.gui.widgets.*;
import latmod.lib.*;
import latmod.lib.config.*;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;

@SideOnly(Side.CLIENT)
public class GuiEditConfig extends GuiLM implements IClientActionGui
{
	public final GuiScreen parent;
	public final IConfigProvider provider;
	public final String title;
	public final PanelLM configPanel;
	public final ButtonLM buttonClose;
	public final SliderLM scroll;
	
	public GuiEditConfig(GuiScreen g, IConfigProvider p)
	{
		super(null, null);
		hideNEI = true;
		parent = g;
		provider = p;
		title = p.getTitle().getFormattedText();
		
		configPanel = new PanelLM(this, 0, 0, 0, 20)
		{
			public void addWidgets()
			{
				FastList<ButtonConfigEntry> temp = new FastList<ButtonConfigEntry>();
				
				provider.getList().sort();
				
				for(ConfigGroup c : provider.getList().groups)
				{
					ButtonCategory cat = new ButtonCategory(GuiEditConfig.this, c);
					
					temp.clear();
					
					for(ConfigEntry e : c.entries)
						if(!e.hideEntry) temp.add(new ButtonConfigEntry(cat, e));
					
					if(!temp.isEmpty())
					{
						add(cat);
						for(int i = 0; i < temp.size(); i++)
						{
							ButtonConfigEntry e = temp.get(i);
							add(e);
							e.handler.initGui();
						}
					}
				}
			}
			
			public void renderWidget()
			{
				for(int i = 0; i < widgets.size(); i++)
					widgets.get(i).renderWidget();
			}
		};
		
		buttonClose = new ButtonLM(this, 0, 2, 16, 16)
		{
			public void onButtonPressed(int b)
			{
				gui.playClickSound();
				if(parent == null) container.player.closeScreen();
				else mc.displayGuiScreen(parent);
			}
		};
		
		scroll = new SliderLM(this, -16, 20, 16, 0, 10)
		{
			public boolean canMouseScroll()
			{ return true; }
		};
		scroll.isVertical = true;
		scroll.displayMin = scroll.displayMax = 0;
	}
	
	public void initLMGui()
	{
		xSize = width;
		ySize = height;
		guiLeft = guiTop = 0;
		buttonClose.posX = width - 18;
		scroll.posX = width - 16;
		scroll.height = height - 20;
		configPanel.posY = 20;
		scroll.value = 0F;
		refreshWidgets();
	}
	
	public void addWidgets()
	{
		mainPanel.add(buttonClose);
		configPanel.height = 20;
		configPanel.width = width;
		configPanel.posX = 0;
		configPanel.posY = 0;
		mainPanel.add(configPanel);
		if(configPanel.height > height) mainPanel.add(scroll);
	}
	
	public void onLMGuiClosed()
	{
		provider.save();
	}
	
	public void drawBackground()
	{
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		GlStateManager.color(1F, 1F, 1F, 1F);
		
		boolean drawScroll = configPanel.height > height;
		
		if(drawScroll)
		{
			float pvalue = scroll.value;
			scroll.update();
			
			int dw = Mouse.getDWheel();
			if(dw != 0)
			{
				float s = (20F / (float)(height - configPanel.height)) * 3F;
				if(dw < 0) scroll.value -= s;
				else scroll.value += s;
				scroll.value = MathHelperLM.clampFloat(scroll.value, 0F, 1F);
			}
			
			if(scroll.value != pvalue)
				configPanel.posY = (int)(scroll.value * (height - configPanel.height));
		}
		
		configPanel.renderWidget();
		
		drawRect(0, 0, width, 20, 0x99333333);
		drawCenteredString(fontRendererObj, title, width / 2, 6, 0xFFFFFFFF);
		
		if(drawScroll)
		{
			drawRect(scroll.posX, scroll.posY, scroll.posX + scroll.width, scroll.posY + scroll.height, 0x99333333);
			int sy = (int)(scroll.posY + scroll.value * (scroll.height - scroll.sliderSize));
			drawRect(scroll.posX, sy, scroll.posX + scroll.width, sy + scroll.sliderSize, 0x99666666);
		}
		
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		GlStateManager.color(1F, 1F, 1F, 1F);
		
		buttonClose.render(GuiIcons.accept);
	}
	
	public static abstract class ConfigLine extends ButtonLM
	{
		public final GuiEditConfig gui;
		public final ConfigGroup group;
		
		public ConfigLine(GuiEditConfig g, ConfigGroup gr)
		{
			super(g, 0, g.configPanel.height, g.width - 16, 16);
			gui = g;
			group = gr;
			g.configPanel.height += height;
			title = gr.ID;
		}
		
		public boolean isVisible()
		{ int ay = getAY(); return ay > -height && ay < gui.height; }
		
		public abstract void onButtonPressed(int b);
		
		public void addMouseOverText(FastList<String> l) { }
	}
	
	public static class ButtonCategory extends ConfigLine
	{
		public ButtonCategory(GuiEditConfig g, ConfigGroup gr)
		{
			super(g, gr);
			title = EnumChatFormatting.BOLD + "- " + I18n.format(gr.parentList.ID + '.' + gr.ID) + " -";
		}
		
		public void renderWidget()
		{
			if(!isVisible()) return;
			int y = getAY();
			gui.drawString(gui.fontRendererObj, title, 4, y + 4, 0xFFFFFFFF);
		}
		
		public void onButtonPressed(int b)
		{
		}
	}
	
	public static class ButtonConfigEntry extends ConfigLine
	{
		public final ButtonCategory category;
		public final ConfigEntry entry;
		public final ClientConfigHandler handler;
		
		public ButtonConfigEntry(ButtonCategory c, ConfigEntry e)
		{
			super(c.gui, e.parentGroup);
			category = c;
			entry = e;
			handler = ClientConfigHandler.create(entry);
			title = handler.getTitle();
		}
		
		public void renderWidget()
		{
			if(!isVisible()) return;
			boolean mouseOver = mouseOver();
			int textCol = handler.getColor(mouseOver);
			int y = getAY();
			
			if(mouseOver) drawBlankRect(0, y, gui.zLevel, width, height, 0x22FFFFFF);
			gui.drawString(gui.fontRendererObj, title, 4, y + 4, mouseOver ? 0xFFFFFFFF : 0xFF999999);
			String s = handler.getText();
			gui.drawString(gui.fontRendererObj, s, gui.width - (gui.fontRendererObj.getStringWidth(s) + 20), y + 4, textCol);
		}
		
		public void onButtonPressed(int b)
		{ gui.playClickSound(); handler.onClicked(); }
		
		public void addMouseOverText(FastList<String> l)
		{ handler.mouseText(l); }
	}
	
	public void onClientDataChanged()
	{ refreshWidgets(); }
}