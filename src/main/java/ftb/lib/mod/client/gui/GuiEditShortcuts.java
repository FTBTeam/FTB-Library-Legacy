package ftb.lib.mod.client.gui;

import ftb.lib.api.client.FTBLibClient;
import ftb.lib.api.gui.*;
import ftb.lib.api.gui.widgets.*;
import ftb.lib.mod.FTBLibMod;
import ftb.lib.mod.client.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.*;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiEditShortcuts extends GuiLM
{
	public final String title;
	public final PanelLM configPanel;
	public final ButtonLM buttonClose, buttonAddKeyBound, buttonAddButtonBound;
	public final SliderLM scroll;
	private boolean shouldClose = false;
	private static Shortcuts.Shortcut currentlyEditing = null;
	
	public GuiEditShortcuts()
	{
		super(null, null);
		
		title = FTBLibMod.proxy.translate(FTBLibModClient.edit_shortcuts.getFullID()) + " [WIP! Edit client/local/shortcuts.json instead!]";
		
		configPanel = new PanelLM(this, 0, 0, 0, 20)
		{
			public void addWidgets()
			{
				height = 0;
				
				for(Shortcuts.Shortcut s : Shortcuts.shortcuts)
				{
					ButtonShortcut b = new ButtonShortcut(GuiEditShortcuts.this, s);
					height += b.height;
					add(b);
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
				FTBLibClient.playClickSound();
				shouldClose = true;
				close(null);
			}
		};
		
		buttonClose.title = FTBLibLang.button_close();
		
		buttonAddKeyBound = new ButtonLM(this, 2, 2, 16, 16)
		{
			public void onButtonPressed(int b)
			{
				FTBLibClient.playClickSound();
			}
		};
		
		buttonAddKeyBound.title = FTBLibMod.mod.translate("button.shortcut.add_key");
		
		buttonAddButtonBound = new ButtonLM(this, 20, 2, 16, 16)
		{
			public void onButtonPressed(int b)
			{
				FTBLibClient.playClickSound();
			}
		};
		
		buttonAddButtonBound.title = FTBLibMod.mod.translate("button.shortcut.add_button");
		
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
		mainPanel.width = width;
		mainPanel.height = height;
		mainPanel.posX = mainPanel.posY = 0;
		buttonClose.posX = width - 18 * 1;
		scroll.posX = width - 16;
		scroll.height = height - 20;
		configPanel.posY = 20;
		scroll.value = 0F;
	}
	
	public void addWidgets()
	{
		configPanel.height = 20;
		configPanel.width = width;
		configPanel.posX = 0;
		configPanel.posY = 20;
		mainPanel.add(buttonClose);
		mainPanel.add(buttonAddKeyBound);
		mainPanel.add(buttonAddButtonBound);
		mainPanel.add(configPanel);
		mainPanel.add(scroll);
	}
	
	public void onLMGuiClosed()
	{
		if(shouldClose) Shortcuts.save();
	}
	
	public void onClosedByKey()
	{
		shouldClose = true;
		super.onClosedByKey();
	}
	
	public void drawBackground()
	{
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		GlStateManager.color(1F, 1F, 1F, 1F);
		
		if(configPanel.height + 20 > height)
		{
			scroll.scrollStep = 40F / (configPanel.height + 20F);
			scroll.update();
			configPanel.posY = (int) (scroll.value * (height - configPanel.height - 20)) + 20;
		}
		else
		{
			scroll.value = 0F;
			configPanel.posY = 20;
		}
		
		configPanel.renderWidget();
		
		drawRect(0, 0, width, 20, 0x99333333);
		drawCenteredString(fontRendererObj, title, width / 2, 6, 0xFFFFFFFF);
		
		drawRect(scroll.posX, scroll.posY, scroll.posX + scroll.width, scroll.posY + scroll.height, 0x99333333);
		int sy = (int) (scroll.posY + scroll.value * (scroll.height - scroll.sliderSize));
		drawRect(scroll.posX, sy, scroll.posX + scroll.width, sy + scroll.sliderSize, 0x99666666);
		
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		GlStateManager.color(1F, 1F, 1F, 1F);
		
		buttonClose.render(GuiIcons.accept);
		buttonAddKeyBound.render(GuiIcons.add);
		buttonAddButtonBound.render(GuiIcons.add);
	}
	
	public static class ButtonShortcut extends ButtonLM
	{
		public final GuiEditShortcuts gui;
		public final Shortcuts.Shortcut shortcut;
		
		public ButtonShortcut(GuiEditShortcuts g, Shortcuts.Shortcut s)
		{
			super(g, 0, g.configPanel.height, g.width - 16, 16);
			gui = g;
			shortcut = s;
			title = shortcut.getTitle();
		}
		
		public boolean isVisible()
		{
			int ay = getAY();
			return ay > -height && ay < gui.height;
		}
		
		public void renderWidget()
		{
			if(!isVisible()) return;
			boolean mouseOver = gui.mouse().y >= 20 && mouseOver();
			
			int ax = getAX();
			int ay = getAY();
			
			if(mouseOver)
			{
				GlStateManager.color(1F, 1F, 1F, 0.13F);
				drawBlankRect(ax, ay, gui.zLevel, width, height);
				GlStateManager.color(1F, 1F, 1F, 1F);
			}
			
			gui.drawString(gui.fontRendererObj, title, ax + 4, ay + 4, mouseOver ? 0xFFFFFFFF : 0xFF999999);
		}
		
		public void onButtonPressed(int b)
		{
			if(gui.mouse().y < 20) return;
			
			FTBLibClient.playClickSound();
		}
		
		public void addMouseOverText(List<String> l)
		{
			if(gui.mouse().x < gui.fontRendererObj.getStringWidth(title) + 10)
			{
			}
		}
	}
}