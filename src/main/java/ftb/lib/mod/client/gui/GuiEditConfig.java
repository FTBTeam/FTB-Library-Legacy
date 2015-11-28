package ftb.lib.mod.client.gui;

import org.lwjgl.input.Mouse;

import cpw.mods.fml.relauncher.*;
import ftb.lib.api.config.IConfigProvider;
import ftb.lib.api.gui.*;
import ftb.lib.api.gui.callback.*;
import ftb.lib.client.*;
import ftb.lib.gui.GuiLM;
import ftb.lib.gui.widgets.*;
import latmod.lib.*;
import latmod.lib.config.*;
import net.minecraft.client.gui.GuiScreen;
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
	public boolean changed = false;
	
	public GuiEditConfig(GuiScreen g, IConfigProvider p)
	{
		super(null, null);
		hideNEI = true;
		parent = g;
		provider = p;
		title = p.getTitle();
		
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
						if(!e.isHidden()) temp.add(new ButtonConfigEntry(GuiEditConfig.this, cat, e));
					
					if(!temp.isEmpty())
					{
						add(cat);
						for(int i = 0; i < temp.size(); i++)
						{
							ButtonConfigEntry e = temp.get(i);
							add(e);
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
	{ if(changed) provider.save(); }
	
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
			title = EnumChatFormatting.BOLD + "- " + g.provider.getGroupTitle(gr) + " -";
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
		
		public ButtonConfigEntry(GuiEditConfig g, ButtonCategory c, ConfigEntry e)
		{
			super(c.gui, e.parentGroup);
			category = c;
			entry = e;
			title = g.provider.getEntryTitle(entry);
		}
		
		public void renderWidget()
		{
			if(!isVisible()) return;
			boolean mouseOver = mouseOver();
			int textCol = 0xFF000000 | getColor();
			if(mouseOver) textCol = LMColorUtils.addBrightness(textCol, 60);
			int y = getAY();
			
			if(mouseOver) drawBlankRect(0, y, gui.zLevel, width, height, 0x22FFFFFF);
			gui.drawString(gui.fontRendererObj, title, 4, y + 4, mouseOver ? 0xFFFFFFFF : 0xFF999999);
			String s = entry.getValue();
			
			int slen = gui.fontRendererObj.getStringWidth(s);
			
			if(slen > 150)
			{
				s = gui.fontRendererObj.trimStringToWidth(s, 150);
				s += "...";
				slen = 152;
			}
			
			gui.drawString(gui.fontRendererObj, s, gui.width - (slen + 20), y + 4, textCol);
		}
		
		public void onButtonPressed(int b)
		{
			gui.playClickSound();
			
			if(entry instanceof IClickableConfigEntry)
				((IClickableConfigEntry)entry).onClicked();
			else if(entry.type == PrimitiveType.COLOR)
			{
				LMGuis.displayColorSelector(new IColorCallback()
				{
					public void onColorSelected(ColorSelected c)
					{
						if(c.set)
						{
							((ConfigEntryColor)entry).set(c.color);
							((GuiEditConfig)gui).changed = true;
						}
						
						if(c.closeGui) FTBLibClient.mc.displayGuiScreen(gui);
					}
				},
				((ConfigEntryColor)entry).get(), 0, false);
			}
			else if(entry.type == PrimitiveType.INT || entry.type == PrimitiveType.FLOAT || entry.type == PrimitiveType.STRING)
			{
				if(entry.type == PrimitiveType.INT)
				{
					LMGuis.displayFieldSelector(entry.getFullID(), FieldType.INT, ((ConfigEntryInt)entry).get(), new IFieldCallback()
					{
						public void onFieldSelected(FieldSelected c)
						{
							if(c.set)
							{
								((ConfigEntryInt)entry).set(c.getI());
								((GuiEditConfig)gui).changed = true;
							}
							
							if(c.closeGui) FTBLibClient.mc.displayGuiScreen(gui);
						}
					});
				}
				else if(entry.type == PrimitiveType.FLOAT)
				{
					LMGuis.displayFieldSelector(entry.getFullID(), FieldType.FLOAT, ((ConfigEntryFloat)entry).get(), new IFieldCallback()
					{
						public void onFieldSelected(FieldSelected c)
						{
							if(c.set)
							{
								((ConfigEntryFloat)entry).set(c.getF());
								((GuiEditConfig)gui).changed = true;
							}
							
							if(c.closeGui) FTBLibClient.mc.displayGuiScreen(gui);
						}
					});
				}
				else if(entry.type == PrimitiveType.STRING)
				{
					LMGuis.displayFieldSelector(entry.getFullID(), FieldType.TEXT, ((ConfigEntryString)entry).get(), new IFieldCallback()
					{
						public void onFieldSelected(FieldSelected c)
						{
							if(c.set)
							{
								((ConfigEntryString)entry).set(c.getS());
								((GuiEditConfig)gui).changed = true;
							}
							
							if(c.closeGui) FTBLibClient.mc.displayGuiScreen(gui);
						}
					});
				}
			}
			
			((GuiEditConfig)gui).changed = true;
		}
		
		public int getColor()
		{
			if(entry instanceof ConfigEntryBlank)
				return 0xFFAA00;
			else if(entry.type == PrimitiveType.COLOR)
				return ((ConfigEntryColor)entry).get();
			else if(entry.type.isEnum)
				return 0x0094FF;
			else if(entry.type.isBoolean)
				return ((ConfigEntryBool)entry).get() ? 0x339933 : 0x993333;
			else if(entry.type.isArray)
				return 0xFF4F34;
			else if(entry.type.isNumber)
				return 0x933ABC;
			return 0x999999;
		}
		
		@SuppressWarnings("unchecked")
		public void addMouseOverText(FastList<String> l)
		{
			if(gui.mouseX < gui.fontRendererObj.getStringWidth(title) + 10)
			{
				if(entry.info != null && entry.info.info != null && !entry.info.info.isEmpty())
				{
					String[] sl = entry.info.info.split("\n");
					for(String s : sl) l.addAll(FTBLibClient.mc.fontRenderer.listFormattedStringToWidth(s, 230));
				}
			}
		}
	}
	
	public void onClientDataChanged()
	{ refreshWidgets(); }
}