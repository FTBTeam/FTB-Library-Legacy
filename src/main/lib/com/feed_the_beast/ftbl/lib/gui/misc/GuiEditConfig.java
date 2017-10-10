package com.feed_the_beast.ftbl.lib.gui.misc;

import com.feed_the_beast.ftbl.lib.client.ClientUtils;
import com.feed_the_beast.ftbl.lib.config.ConfigGroup;
import com.feed_the_beast.ftbl.lib.config.ConfigValue;
import com.feed_the_beast.ftbl.lib.config.ConfigValueInfo;
import com.feed_the_beast.ftbl.lib.config.ConfigValueInstance;
import com.feed_the_beast.ftbl.lib.config.IConfigCallback;
import com.feed_the_beast.ftbl.lib.config.IGuiEditConfig;
import com.feed_the_beast.ftbl.lib.gui.Button;
import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.GuiIcons;
import com.feed_the_beast.ftbl.lib.gui.GuiLang;
import com.feed_the_beast.ftbl.lib.gui.Panel;
import com.feed_the_beast.ftbl.lib.gui.PanelScrollBar;
import com.feed_the_beast.ftbl.lib.gui.SimpleButton;
import com.feed_the_beast.ftbl.lib.gui.Widget;
import com.feed_the_beast.ftbl.lib.gui.WidgetLayout;
import com.feed_the_beast.ftbl.lib.icon.Color4I;
import com.feed_the_beast.ftbl.lib.icon.MutableColor4I;
import com.feed_the_beast.ftbl.lib.io.Bits;
import com.feed_the_beast.ftbl.lib.util.StringUtils;
import com.feed_the_beast.ftbl.lib.util.misc.MouseButton;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GuiEditConfig extends GuiBase implements IGuiEditConfig
{
	public static final Color4I COLOR_BACKGROUND = Color4I.rgba(0x99333333);

	public class ButtonConfigGroup extends Button
	{
		public final String groupId;
		public String title = "-", info = "";
		public boolean collapsed = false;

		public ButtonConfigGroup(GuiBase gui, String id)
		{
			super(gui, 0, 0, 0, 16);
			groupId = id;

			if (!groupId.isEmpty())
			{
				StringBuilder current = new StringBuilder();
				StringBuilder text = new StringBuilder();

				String[] sa = groupId.split("\\.");

				for (int i = 0; i < sa.length; i++)
				{
					if (i != 0)
					{
						current.append(".");
					}

					current.append(sa[i]);
					String key = current.toString();

					text.append(group.getGroupName(key).getFormattedText());

					if (i != sa.length - 1)
					{
						text.append(" > ");
					}
				}

				title = text.toString();
				text.setLength(0);
				text.append(groupId);
				text.append(".info");
				String infoKey = text.toString();

				if (StringUtils.canTranslate(infoKey))
				{
					info = StringUtils.translate(infoKey);
				}
			}

			setCollapsed(collapsed);
		}

		public void setCollapsed(boolean v)
		{
			collapsed = v;
			setTitle((collapsed ? (TextFormatting.RED + "[-] ") : (TextFormatting.GREEN + "[v] ")) + TextFormatting.RESET + title);
		}

		@Override
		public void renderWidget()
		{
			int ax = getAX();
			int ay = getAY();

			COLOR_BACKGROUND.draw(ax, ay, width, height);
			gui.drawString(getTitle(), ax + 3, ay + 4);
			GlStateManager.color(1F, 1F, 1F, 1F);

			if (gui.isMouseOver(this))
			{
				Color4I.WHITE_A[33].draw(ax, ay, width, height);
			}
		}

		@Override
		public void addMouseOverText(List<String> list)
		{
			if (!info.isEmpty())
			{
				list.add(info);
			}
		}

		@Override
		public void onClicked(MouseButton button)
		{
			setCollapsed(!collapsed);
			gui.refreshWidgets();
		}
	}

	public class ButtonConfigEntry extends Button
	{
		public final ButtonConfigGroup group;
		public final ConfigValueInfo info;
		public final ConfigValue value;
		public String keyText;
		public List<String> infoText;

		public ButtonConfigEntry(GuiEditConfig gui, ButtonConfigGroup g, ConfigValueInfo i, ConfigValue e)
		{
			super(gui, 0, 0, 0, 16);
			group = g;
			info = i;
			value = e;
			String keyLang = gui.group.getNameKey(info);
			keyText = StringUtils.translate(keyLang);
			String infoText = StringUtils.canTranslate(keyLang + ".tooltip") ? StringUtils.translate(keyLang + ".tooltip") : "";

			if (!infoText.isEmpty())
			{
				this.infoText = new ArrayList<>();

				for (String s : infoText.split("\\\\n"))
				{
					this.infoText.addAll(gui.listFormattedStringToWidth(s, 230));
				}
			}

			if (this.infoText == null || this.infoText.isEmpty())
			{
				this.infoText = Collections.emptyList();
			}
		}

		@Override
		public void renderWidget()
		{
			boolean mouseOver = gui.getMouseY() >= 20 && gui.isMouseOver(this);

			int ax = getAX();
			int ay = getAY();

			if (mouseOver)
			{
				Color4I.WHITE_A[33].draw(ax, ay, width, height);
			}

			gui.drawString(keyText, ax + 4, ay + 4, Bits.setFlag(0, DARK, !mouseOver));
			GlStateManager.color(1F, 1F, 1F, 1F);

			String s = value.hasCustomName() ? value.getCustomDisplayName().getFormattedText() : value.getString();

			int slen = gui.getStringWidth(s);

			if (slen > 150)
			{
				s = gui.trimStringToWidth(s, 150, false) + "...";
				slen = 152;
			}

			Color4I col = value.getCustomColor();

			if (col.isEmpty())
			{
				col = value.getColor();
			}

			MutableColor4I textCol = col.mutable();
			textCol.setAlpha(255);

			if (mouseOver)
			{
				textCol.addBrightness(60);

				if (gui.getMouseX() > ax + width - slen - 9)
				{
					Color4I.WHITE_A[33].draw(ax + width - slen - 8, ay, slen + 8, height);
				}
			}

			gui.drawString(s, gui.width - (slen + 20), ay + 4, textCol, 0);
			GlStateManager.color(1F, 1F, 1F, 1F);
		}

		@Override
		public void onClicked(MouseButton button)
		{
			if (gui.getMouseY() >= 20 && !info.cantEdit)
			{
				GuiHelper.playClickSound();
				value.onClicked(GuiEditConfig.this, info, button);
			}
		}

		@Override
		public void addMouseOverText(List<String> list)
		{
			if (gui.getMouseY() > 18)
			{
				if (!infoText.isEmpty() && gui.getMouseX() < getAX() + gui.getStringWidth(keyText) + 10)
				{
					list.addAll(infoText);
				}

				if (gui.getMouseX() > gui.width - (Math.min(150, gui.getStringWidth(value.getString())) + 25))
				{
					value.addInfo(info, list);
				}
			}
		}
	}

	private final ConfigGroup group;
	private final IConfigCallback callback;
	private final JsonObject modifiedConfig;

	private final String title;
	private final List<Widget> configEntryButtons;
	private final Panel configPanel;
	private final Button buttonAccept, buttonCancel, buttonCollapseAll, buttonExpandAll;
	private final PanelScrollBar scroll;
	private int shouldClose = 0;

	public GuiEditConfig(ConfigGroup g, IConfigCallback c)
	{
		super(0, 0);
		group = g;
		callback = c;

		ITextComponent title0 = g.getTitle().createCopy();
		title0.getStyle().setBold(true);
		title = title0.getFormattedText();
		modifiedConfig = new JsonObject();

		configEntryButtons = new ArrayList<>();

		List<ConfigValueInstance> list = new ArrayList<>();

		for (ConfigValueInstance instance : group.getMap().values())
		{
			if (!instance.info.hidden)
			{
				list.add(instance);
			}
		}

		if (!list.isEmpty())
		{
			list.sort((o1, o2) ->
			{
				int i = o1.info.group.compareToIgnoreCase(o2.info.group);
				return i == 0 ? StringUtils.translate(group.getNameKey(o1.info)).compareToIgnoreCase(StringUtils.translate(group.getNameKey(o2.info))) : i;
			});

			ButtonConfigGroup group = null;

			for (ConfigValueInstance instance : list)
			{
				if (group == null || !group.groupId.equalsIgnoreCase(instance.info.group))
				{
					group = new ButtonConfigGroup(this, instance.info.group);
					configEntryButtons.add(group);
				}

				configEntryButtons.add(new ButtonConfigEntry(this, group, instance.info, instance.value.copy()));
			}
		}

		configPanel = new Panel(this, 0, 20, 0, 20)
		{
			@Override
			public void addWidgets()
			{
				for (Widget w : configEntryButtons)
				{
					if (!(w instanceof ButtonConfigEntry) || !((ButtonConfigEntry) w).group.collapsed)
					{
						add(w);
					}
				}
			}

			@Override
			public void updateWidgetPositions()
			{
				scroll.setElementSize(align(WidgetLayout.VERTICAL));
			}
		};

		configPanel.addFlags(Panel.DEFAULTS);

		scroll = new PanelScrollBar(this, -16, 20, 16, 0, 0, configPanel)
		{
			@Override
			public boolean shouldRender()
			{
				return true;
			}
		};

		buttonAccept = new SimpleButton(this, 0, 2, GuiLang.ACCEPT, GuiIcons.ACCEPT, (gui, button) ->
		{
			shouldClose = 1;
			gui.closeGui();
		});

		buttonCancel = new SimpleButton(this, 0, 2, GuiLang.CANCEL, GuiIcons.CANCEL, (gui, button) ->
		{
			shouldClose = 2;
			gui.closeGui();
		});

		buttonCollapseAll = new SimpleButton(this, 0, 2, GuiLang.COLLAPSE_ALL, GuiIcons.REMOVE, (gui, button) ->
		{
			for (Widget w : configEntryButtons)
			{
				if (w instanceof ButtonConfigGroup)
				{
					((ButtonConfigGroup) w).setCollapsed(true);
				}
			}

			scroll.setValue(0D);
			gui.refreshWidgets();
		});

		buttonExpandAll = new SimpleButton(this, 0, 2, GuiLang.EXPAND_ALL, GuiIcons.ADD, (gui, button) ->
		{
			for (Widget w : configEntryButtons)
			{
				if (w instanceof ButtonConfigGroup)
				{
					((ButtonConfigGroup) w).setCollapsed(false);
				}
			}

			scroll.setValue(0D);
			gui.refreshWidgets();
		});
	}

	@Override
	public void onInit()
	{
		setWidth(getScreen().getScaledWidth());
		setHeight(getScreen().getScaledHeight());
	}

	@Override
	public void updateWidgetPositions()
	{
		buttonAccept.posX = width - 18;
		buttonCancel.posX = width - 38;
		buttonExpandAll.posX = width - 58;
		buttonCollapseAll.posX = width - 78;

		configPanel.setHeight(height - 20);
		configPanel.setWidth(width);

		scroll.posX = width - 16;
		scroll.setHeight(configPanel.height);

		for (Widget b : configEntryButtons)
		{
			b.setWidth(scroll.posX);
		}
	}

	@Override
	public void addWidgets()
	{
		addAll(buttonAccept, buttonCancel, buttonCollapseAll, buttonExpandAll);
		addAll(configPanel, scroll);
	}

	@Override
	public void onClosed()
	{
		if (shouldClose == 1 && !modifiedConfig.entrySet().isEmpty())
		{
			callback.saveConfig(group, ClientUtils.MC.player, modifiedConfig);
		}
	}

	@Override
	public boolean onClosedByKey()
	{
		buttonCancel.onClicked(MouseButton.LEFT);
		return false;
	}

	@Override
	public void onChanged(String key, JsonElement val)
	{
		modifiedConfig.add(key, val);
	}

	@Override
	public void drawBackground()
	{
		COLOR_BACKGROUND.draw(0, 0, width, 20);
		drawString(getTitle(), 6, 6, DARK);
	}

	@Override
	public String getTitle()
	{
		return title;
	}
}