package ftb.lib.mod.client.gui;

import java.util.*;

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
	public final IConfigProvider provider;
	public final String title;
	public final FastList<ButtonConfigEntry> configEntryButtons;
	public final PanelLM configPanel;
	public final ButtonLM buttonClose, buttonExpandAll, buttonCollapseAll;
	public final SliderLM scroll;
	public boolean changed = false;
	
	public GuiEditConfig(GuiScreen g, IConfigProvider p)
	{
		super(g, null, null);
		hideNEI = true;
		provider = p;
		
		
		title = p.getGroupTitle(p.getGroup());
		
		configEntryButtons = new FastList<ButtonConfigEntry>();
		
		configPanel = new PanelLM(this, 0, 0, 0, 20)
		{
			public void addWidgets()
			{
				height = 0;
				for(ButtonConfigEntry b : configEntryButtons)
					addCE(b);
			}
			
			public void renderWidget()
			{
				for(int i = 0; i < widgets.size(); i++)
					widgets.get(i).renderWidget();
			}
			
			private void addCE(ButtonConfigEntry e)
			{
				add(e);
				e.posY = height;
				height += e.height;
				if(e.expanded)
				{
					for(ButtonConfigEntry b : e.subButtons)
						addCE(b);
				}
			}
		};
		
		buttonClose = new ButtonLM(this, 0, 2, 16, 16)
		{
			public void onButtonPressed(int b)
			{
				gui.playClickSound();
				close(parentScreen);
			}
		};
		
		buttonExpandAll = new ButtonLM(this, 2, 2, 16, 16)
		{
			public void onButtonPressed(int b)
			{
				gui.playClickSound();
				for(ButtonConfigEntry e : configEntryButtons)
					expandAll(e);
				gui.refreshWidgets();
			}
			
			private void expandAll(ButtonConfigEntry e)
			{
				if(e.entry.getAsGroup() != null)
				{
					e.expanded = true;
					for(ButtonConfigEntry e1 : e.subButtons)
						expandAll(e1);
				}
			}
		};
		
		buttonCollapseAll = new ButtonLM(this, 20, 2, 16, 16)
		{
			public void onButtonPressed(int b)
			{
				gui.playClickSound();
				for(ButtonConfigEntry e : configEntryButtons)
					collapseAll(e);
				gui.refreshWidgets();
			}
			
			private void collapseAll(ButtonConfigEntry e)
			{
				if(e.entry.getAsGroup() != null)
				{
					e.expanded = false;
					for(ButtonConfigEntry e1 : e.subButtons)
						collapseAll(e1);
				}
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
		buttonClose.posX = width - 18 * 1;
		scroll.posX = width - 16;
		scroll.height = height - 20;
		configPanel.posY = 20;
		scroll.value = 0F;
		
		if(configEntryButtons.isEmpty())
		{
			configEntryButtons.clear();
			ConfigGroup group = provider.getGroup();
			group.sort(new Comparator<ConfigEntry>()
			{
				public int compare(ConfigEntry o1, ConfigEntry o2)
				{
					int i = Boolean.compare(o2.getAsGroup() != null, o1.getAsGroup() != null);
					return (i == 0) ? o1.compareTo(o2) : i;
				}
			});
			
			for(ConfigEntry e : group.entries)
				addCE(null, e, 0);
		}
	}
	
	private void addCE(ButtonConfigEntry parent, ConfigEntry e, int level)
	{
		if(!e.isHidden())
		{
			ButtonConfigEntry b = new ButtonConfigEntry(this, e);
			b.posX += level * 12;
			b.width -= level * 12;
			if(parent == null)
			{
				b.expanded = true;
				configEntryButtons.add(b);
			}
			else parent.subButtons.add(b);
			
			ConfigGroup g = e.getAsGroup();
			if(g != null)
			{
				for(ConfigEntry e1 : g.entries)
					addCE(b, e1, level + 1);
			}
		}
	}
	
	public void addWidgets()
	{
		configPanel.height = 20;
		configPanel.width = width;
		configPanel.posX = 0;
		configPanel.posY = 20;
		mainPanel.add(buttonClose);
		mainPanel.add(buttonExpandAll);
		mainPanel.add(buttonCollapseAll);
		mainPanel.add(configPanel);
		mainPanel.add(scroll);
	}
	
	public void onLMGuiClosed()
	{ if(changed) provider.save(); }
	
	public void drawBackground()
	{
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		GlStateManager.color(1F, 1F, 1F, 1F);
		
		scroll.scrollStep = 0F;
		scroll.update();
		
		if(configPanel.height + 20 < height) scroll.value = 0F;
		else if(mouseDWheel != 0)
		{
			float s = (20F / (float)(height - configPanel.height + 20)) * 1F;
			if(mouseDWheel < 0) scroll.value -= s;
			else scroll.value += s;
			scroll.value = MathHelperLM.clampFloat(scroll.value, 0F, 1F);
		}
		
		configPanel.posY = (int)(scroll.value * (height - configPanel.height - 20)) + 20;
		
		configPanel.renderWidget();
		
		drawRect(0, 0, width, 20, 0x99333333);
		drawCenteredString(fontRendererObj, title, width / 2, 6, 0xFFFFFFFF);
		
		drawRect(scroll.posX, scroll.posY, scroll.posX + scroll.width, scroll.posY + scroll.height, 0x99333333);
		int sy = (int)(scroll.posY + scroll.value * (scroll.height - scroll.sliderSize));
		drawRect(scroll.posX, sy, scroll.posX + scroll.width, sy + scroll.sliderSize, 0x99666666);
		
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		GlStateManager.color(1F, 1F, 1F, 1F);
		
		buttonClose.render(GuiIcons.accept);
		buttonExpandAll.render(GuiIcons.add);
		buttonCollapseAll.render(GuiIcons.remove);
	}
	
	public static class ButtonConfigEntry extends ButtonLM
	{
		public final GuiEditConfig gui;
		public final ConfigEntry entry;
		public boolean expanded = false;
		public final FastList<ButtonConfigEntry> subButtons;
		
		public ButtonConfigEntry(GuiEditConfig g, ConfigEntry e)
		{
			super(g, 0, g.configPanel.height, g.width - 16, 16);
			gui = g;
			entry = e;
			title = g.provider.getEntryTitle(entry);
			subButtons = new FastList<ButtonConfigEntry>();
		}
		
		public boolean isVisible()
		{ int ay = getAY(); return ay > -height && ay < gui.height; }
		
		public void renderWidget()
		{
			if(!isVisible()) return;
			boolean mouseOver = gui.mouseY >= 20 && mouseOver();
			
			int ax = getAX();
			int ay = getAY();
			boolean isGroup = entry.getAsGroup() != null;
			
			if(mouseOver) drawBlankRect(ax, ay, gui.zLevel, width, height, 0x22FFFFFF);
			gui.drawString(gui.fontRendererObj, isGroup ? (((expanded ? "[-] " : "[+] ") + title)) : title, ax + 4, ay + 4, mouseOver ? 0xFFFFFFFF : (isGroup ? 0xFFCCCCCC : 0xFF999999));
			
			if(!isGroup)
			{
				String s = entry.getAsString();
				
				int slen = gui.fontRendererObj.getStringWidth(s);
				
				if(slen > 150)
				{
					s = gui.fontRendererObj.trimStringToWidth(s, 150);
					s += "...";
					slen = 152;
				}
				
				int textCol = 0xFF000000 | getColor();
				if(mouseOver) textCol = LMColorUtils.addBrightness(textCol, 60);
				
				if(mouseOver && gui.mouseX > ax + width - slen - 9)
					drawBlankRect(ax + width - slen - 8, ay, gui.zLevel, slen + 8, height, 0x22FFFFFF);
				
				gui.drawString(gui.fontRendererObj, s, gui.width - (slen + 20), ay + 4, textCol);
			}
		}
		
		public void onButtonPressed(int b)
		{
			if(gui.mouseY < 20) return;
			
			gui.playClickSound();
			
			if(entry instanceof IClickableConfigEntry)
			{
				((IClickableConfigEntry)entry).onClicked();
				((GuiEditConfig)gui).changed = true;
			}
			else if(entry.getAsGroup() != null)
			{
				expanded = !expanded;
				gui.refreshWidgets();
			}
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
			else if(entry.type == PrimitiveType.INT || entry.type == PrimitiveType.DOUBLE || entry.type == PrimitiveType.STRING)
			{
				if(entry.type == PrimitiveType.INT)
				{
					LMGuis.displayFieldSelector(entry.getFullID(), entry.type, ((ConfigEntryInt)entry).get(), new IFieldCallback()
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
				else if(entry.type == PrimitiveType.DOUBLE)
				{
					LMGuis.displayFieldSelector(entry.getFullID(), entry.type, ((ConfigEntryDouble)entry).get(), new IFieldCallback()
					{
						public void onFieldSelected(FieldSelected c)
						{
							if(c.set)
							{
								((ConfigEntryDouble)entry).set(c.getF());
								((GuiEditConfig)gui).changed = true;
							}
							
							if(c.closeGui) FTBLibClient.mc.displayGuiScreen(gui);
						}
					});
				}
				else if(entry.type == PrimitiveType.STRING)
				{
					LMGuis.displayFieldSelector(entry.getFullID(), entry.type, ((ConfigEntryString)entry).get(), new IFieldCallback()
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
				return ((ConfigEntryBool)entry).get() ? 0x33AA33 : 0xD52834;
			else if(entry.type.isArray)
				return 0xFF4F34;
			else if(entry.type.isNumber)
				return 0xAA5AE8;
			else if(entry.type.isString)
				return 0xFFAA49;
			return 0x999999;
		}
		
		@SuppressWarnings("unchecked")
		public void addMouseOverText(List<String> l)
		{
			if(gui.mouseX < gui.fontRendererObj.getStringWidth(title) + 10)
			{
				if(entry.info != null && !entry.info.isEmpty())
				{
					String[] sl = entry.info.split("\n");
					for(String s : sl) l.addAll(FTBLibClient.mc.fontRenderer.listFormattedStringToWidth(s, 230));
				}
			}
			
			if(entry.getAsGroup() == null && gui.mouseX > gui.width - (Math.min(150, gui.fontRendererObj.getStringWidth(entry.getAsString())) + 25))
			{
				if(entry.defaultValue != null) l.add(EnumChatFormatting.AQUA + "Def: " + entry.defaultValue);
				if(entry.getMinValue() != null) l.add(EnumChatFormatting.AQUA + "Min: " + entry.getMinValue());
				if(entry.getMaxValue() != null) l.add(EnumChatFormatting.AQUA + "Max: " + entry.getMaxValue());
			}
		}
	}
	
	public void onClientDataChanged()
	{ refreshWidgets(); }
}