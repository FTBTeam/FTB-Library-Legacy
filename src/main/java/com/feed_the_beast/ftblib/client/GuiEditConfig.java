package com.feed_the_beast.ftblib.client;

import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import com.feed_the_beast.ftblib.lib.config.ConfigGroup;
import com.feed_the_beast.ftblib.lib.config.ConfigValueInstance;
import com.feed_the_beast.ftblib.lib.config.IConfigCallback;
import com.feed_the_beast.ftblib.lib.gui.Button;
import com.feed_the_beast.ftblib.lib.gui.GuiBase;
import com.feed_the_beast.ftblib.lib.gui.GuiHelper;
import com.feed_the_beast.ftblib.lib.gui.GuiIcons;
import com.feed_the_beast.ftblib.lib.gui.Panel;
import com.feed_the_beast.ftblib.lib.gui.PanelScrollBar;
import com.feed_the_beast.ftblib.lib.gui.SimpleButton;
import com.feed_the_beast.ftblib.lib.gui.Theme;
import com.feed_the_beast.ftblib.lib.gui.Widget;
import com.feed_the_beast.ftblib.lib.gui.WidgetLayout;
import com.feed_the_beast.ftblib.lib.gui.WidgetType;
import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.icon.MutableColor4I;
import com.feed_the_beast.ftblib.lib.io.Bits;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GuiEditConfig extends GuiBase
{
	public static final Color4I COLOR_BACKGROUND = Color4I.rgba(0x99333333);
	public static final Comparator<ConfigValueInstance> COMPARATOR = (o1, o2) -> {
		int i = o1.getGroup().getPath().compareToIgnoreCase(o2.getGroup().getPath());

		if (i == 0)
		{
			i = Byte.compare(o1.getOrder(), o2.getOrder());
		}

		if (i == 0)
		{
			i = o1.getDisplayName().getUnformattedText().compareToIgnoreCase(o2.getDisplayName().getUnformattedText());
		}

		return i;
	};

	public static Theme THEME = new Theme()
	{
		@Override
		public Icon getGui(WidgetType type)
		{
			return Icon.EMPTY;
		}

		@Override
		public Icon getScrollBarBackground(WidgetType type)
		{
			return Color4I.BLACK.withAlpha(70);
		}

		@Override
		public Icon getScrollBar(WidgetType type, boolean vertical)
		{
			return getContentColor(WidgetType.NORMAL).withAlpha(100).withOutline(Color4I.GRAY.withAlpha(100), false);
		}
	};

	public class ButtonConfigGroup extends Button
	{
		public final ConfigGroup group;
		public String title, info;
		public boolean collapsed = false;

		public ButtonConfigGroup(Panel panel, ConfigGroup g)
		{
			super(panel);
			group = g;
			title = g.getDisplayName().getFormattedText();
			String infoKey = g.getPath() + ".info";
			info = I18n.hasKey(infoKey) ? I18n.format(infoKey) : "";
			setCollapsed(collapsed);
		}

		public void setCollapsed(boolean v)
		{
			collapsed = v;
			setTitle((collapsed ? (TextFormatting.RED + "[-] ") : (TextFormatting.GREEN + "[v] ")) + TextFormatting.RESET + title);
		}

		@Override
		public void draw()
		{
			int ax = getAX();
			int ay = getAY();

			COLOR_BACKGROUND.draw(ax, ay, width, height);
			drawString(getTitle(), ax + 3, ay + 4);
			GlStateManager.color(1F, 1F, 1F, 1F);

			if (isMouseOver())
			{
				Color4I.WHITE.withAlpha(33).draw(ax, ay, width, height);
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
			getGui().refreshWidgets();
		}
	}

	public class ButtonConfigEntry extends Button
	{
		public final ButtonConfigGroup group;
		public final ConfigValueInstance inst;
		public String keyText;
		private String valueString = null;

		public ButtonConfigEntry(Panel panel, ButtonConfigGroup g, ConfigValueInstance i)
		{
			super(panel);
			group = g;
			inst = i;
			keyText = inst.getDisplayName().getFormattedText();
		}

		public String getValueString()
		{
			if (valueString == null)
			{
				valueString = inst.getValue().getStringForGUI().getFormattedText();
			}

			return valueString;
		}

		@Override
		public void draw()
		{
			boolean mouseOver = getMouseY() >= 20 && isMouseOver();

			int ax = getAX();
			int ay = getAY();

			if (mouseOver)
			{
				Color4I.WHITE.withAlpha(33).draw(ax, ay, width, height);
			}

			drawString(keyText, ax + 4, ay + 4, Bits.setFlag(0, SHADOW, mouseOver));
			GlStateManager.color(1F, 1F, 1F, 1F);

			String s = getValueString();
			int slen = getStringWidth(s);

			if (slen > 150)
			{
				s = trimStringToWidth(s, 150, false) + "...";
				slen = 152;
			}

			MutableColor4I textCol = inst.getValue().getColor().mutable();
			textCol.setAlpha(255);

			if (mouseOver)
			{
				textCol.addBrightness(60);

				if (getMouseX() > ax + width - slen - 9)
				{
					Color4I.WHITE.withAlpha(33).draw(ax + width - slen - 8, ay, slen + 8, height);
				}
			}

			drawString(s, getGui().width - (slen + 20), ay + 4, textCol, 0);
			GlStateManager.color(1F, 1F, 1F, 1F);
		}

		@Override
		public void onClicked(MouseButton button)
		{
			if (getMouseY() >= 20 && inst.getCanEdit())
			{
				GuiHelper.playClickSound();
				inst.getValue().onClicked(GuiEditConfig.this, inst, button);
			}
		}

		@Override
		public void addMouseOverText(List<String> list)
		{
			if (getMouseY() > 18)
			{
				if (getMouseX() < getAX() + getStringWidth(keyText) + 10)
				{
					ITextComponent infoText = inst.getInfo();

					if (!(infoText instanceof TextComponentTranslation) || I18n.hasKey(((TextComponentTranslation) infoText).getKey()))
					{
						for (String s : infoText.getFormattedText().split("\\\n"))
						{
							list.add(s);
						}
					}
				}

				if (getMouseX() > getGui().width - (Math.min(150, getStringWidth(getValueString())) + 25))
				{
					inst.getValue().addInfo(inst, list);
				}
			}
		}
	}

	private final ConfigGroup group;
	private final IConfigCallback callback;

	private final String title;
	private final List<Widget> configEntryButtons;
	private final Panel configPanel;
	private final Button buttonAccept, buttonCancel, buttonCollapseAll, buttonExpandAll;
	private final PanelScrollBar scroll;
	private int shouldClose = 0;

	public GuiEditConfig(ConfigGroup g, IConfigCallback c)
	{
		group = g;
		callback = c;

		ITextComponent title0 = g.getDisplayName().createCopy();
		title0.getStyle().setBold(true);
		title = title0.getFormattedText();

		configEntryButtons = new ArrayList<>();

		configPanel = new Panel(this)
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
			public void alignWidgets()
			{
				for (Widget w : widgets)
				{
					w.setPosAndSize(0, 0, width - 16, 16);
				}

				scroll.setMaxValue(align(WidgetLayout.VERTICAL));
			}
		};

		List<ConfigValueInstance> list = new ArrayList<>();
		collectAllConfigValues(group, list);

		if (!list.isEmpty())
		{
			list.sort(COMPARATOR);

			ButtonConfigGroup group = null;

			for (ConfigValueInstance instance : list)
			{
				if (group == null || !group.group.equals(instance.getGroup()))
				{
					group = new ButtonConfigGroup(configPanel, instance.getGroup());
					configEntryButtons.add(group);
				}

				configEntryButtons.add(new ButtonConfigEntry(configPanel, group, instance));
			}
		}

		scroll = new PanelScrollBar(this, configPanel);

		buttonAccept = new SimpleButton(this, I18n.format("gui.accept"), GuiIcons.ACCEPT, (widget, button) ->
		{
			shouldClose = 1;
			widget.getGui().closeGui();
		});

		buttonCancel = new SimpleButton(this, I18n.format("gui.cancel"), GuiIcons.CANCEL, (widget, button) ->
		{
			shouldClose = 2;
			widget.getGui().closeGui();
		});

		buttonExpandAll = new SimpleButton(this, I18n.format("gui.expand_all"), GuiIcons.ADD, (widget, button) ->
		{
			for (Widget w : configEntryButtons)
			{
				if (w instanceof ButtonConfigGroup)
				{
					((ButtonConfigGroup) w).setCollapsed(false);
				}
			}

			scroll.setValue(0);
			widget.getGui().refreshWidgets();
		});

		buttonCollapseAll = new SimpleButton(this, I18n.format("gui.collapse_all"), GuiIcons.REMOVE, (widget, button) ->
		{
			for (Widget w : configEntryButtons)
			{
				if (w instanceof ButtonConfigGroup)
				{
					((ButtonConfigGroup) w).setCollapsed(true);
				}
			}

			scroll.setValue(0);
			widget.getGui().refreshWidgets();
		});
	}

	private void collectAllConfigValues(ConfigGroup group, List<ConfigValueInstance> list)
	{
		for (ConfigValueInstance instance : group.getValues())
		{
			if (!instance.getHidden())
			{
				list.add(instance);
			}
		}

		for (ConfigGroup group1 : group.getGroups())
		{
			collectAllConfigValues(group1, list);
		}
	}

	@Override
	public boolean onInit()
	{
		for (Widget widget : configEntryButtons)
		{
			if (widget instanceof ButtonConfigEntry)
			{
				((ButtonConfigEntry) widget).valueString = null;
			}
		}

		return setFullscreen();
	}

	@Override
	public void addWidgets()
	{
		add(buttonAccept);
		add(buttonCancel);
		add(buttonExpandAll);
		add(buttonCollapseAll);
		add(configPanel);
		add(scroll);
	}

	@Override
	public void alignWidgets()
	{
		configPanel.setPosAndSize(0, 20, width, height - 20);
		configPanel.alignWidgets();
		scroll.setPosAndSize(width - 16, 20, 16, height - 20);

		buttonAccept.setPos(width - 18, 2);
		buttonCancel.setPos(width - 38, 2);
		buttonExpandAll.setPos(width - 58, 2);
		buttonCollapseAll.setPos(width - 78, 2);
	}

	@Override
	public void onClosed()
	{
		if (shouldClose == 1)
		{
			callback.onConfigSaved(group, ClientUtils.MC.player);
		}
	}

	@Override
	public boolean onClosedByKey(int key)
	{
		if (super.onClosedByKey(key))
		{
			buttonCancel.onClicked(MouseButton.LEFT);
		}

		return false;
	}

	@Override
	public void drawBackground()
	{
		COLOR_BACKGROUND.draw(0, 0, width, 20);
		drawString(getTitle(), 6, 6, SHADOW);
	}

	@Override
	public String getTitle()
	{
		return title;
	}

	@Override
	public Theme createTheme()
	{
		return THEME;
	}
}