package com.feed_the_beast.ftblib.lib.gui.misc;

import com.feed_the_beast.ftblib.lib.config.ConfigGroup;
import com.feed_the_beast.ftblib.lib.config.ConfigList;
import com.feed_the_beast.ftblib.lib.config.ConfigValue;
import com.feed_the_beast.ftblib.lib.config.ConfigValueInstance;
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
import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.icon.MutableColor4I;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

/**
 * @author LatvianModder
 */
public class GuiEditConfigList extends GuiBase
{
	public class ButtonConfigValue extends Button
	{
		public final ConfigValueInstance inst;
		private String valueString = null;

		public ButtonConfigValue(Panel panel, ConfigValueInstance v)
		{
			super(panel);
			setHeight(16);
			inst = v;
		}

		public String getValueString()
		{
			if (valueString == null)
			{
				valueString = trimStringToWidth(inst.getValue().getStringForGUI().getFormattedText(), width);
			}

			return valueString;
		}

		@Override
		public void draw()
		{
			boolean mouseOver = getMouseY() >= 20 && isMouseOver();

			int ax = getAX();
			int ay = getAY();

			MutableColor4I textCol = inst.getValue().getColor().mutable();
			textCol.setAlpha(255);

			if (mouseOver)
			{
				textCol.addBrightness(60);

				Color4I.WHITE.withAlpha(33).draw(ax, ay, width, height);

				if (getMouseX() >= ax + width - 19)
				{
					Color4I.WHITE.withAlpha(33).draw(ax + width - 19, ay, 19, height);
				}
			}

			drawString(getValueString(), ax + 4, ay + 4, textCol, 0);

			if (mouseOver)
			{
				drawString("[-]", ax + width - 16, ay + 4, Color4I.WHITE, 0);
			}

			GlStateManager.color(1F, 1F, 1F, 1F);
		}

		@Override
		public void onClicked(MouseButton button)
		{
			GuiHelper.playClickSound();

			if (getMouseX() >= getAX() + width - 19)
			{
				if (originalConfigList.getCanEdit())
				{
					configList.getList().remove(inst.getValue());
					parent.refreshWidgets();
				}
			}
			else
			{
				inst.getValue().onClicked(getGui(), inst, button);
			}
		}

		@Override
		public void addMouseOverText(List<String> list)
		{
			if (getMouseX() >= getAX() + width - 19)
			{
				list.add("Delete");
			}
			else
			{
				inst.getValue().addInfo(inst, list);
			}
		}
	}

	public class ButtonAddValue extends Button
	{
		public ButtonAddValue(Panel panel)
		{
			super(panel);
			setHeight(16);
			setTitle("+ " + I18n.format("gui.add"));
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

			drawString(getTitle(), ax + 4, ay + 4, getTheme().getContentColor(getWidgetType()), SHADOW);
			GlStateManager.color(1F, 1F, 1F, 1F);
		}

		@Override
		public void onClicked(MouseButton button)
		{
			GuiHelper.playClickSound();
			configList.add(configList.getType().copy());
			parent.refreshWidgets();
		}

		@Override
		public void addMouseOverText(List<String> list)
		{
		}
	}

	private final ConfigValueInstance originalConfigList;
	private final ConfigList<ConfigValue> configList;

	private final String title;
	private final Panel configPanel;
	private final Button buttonAccept, buttonCancel;
	private final PanelScrollBar scroll;

	public GuiEditConfigList(ConfigValueInstance c)
	{
		originalConfigList = c;
		configList = (ConfigList<ConfigValue>) originalConfigList.getValue().copy();

		ITextComponent title0 = originalConfigList.getDisplayName().createCopy();
		title0.getStyle().setBold(true);
		title = title0.getFormattedText();

		configPanel = new Panel(this)
		{
			@Override
			public void addWidgets()
			{
				for (int i = 0; i < configList.getList().size(); i++)
				{
					add(new ButtonConfigValue(this, new ConfigValueInstance(Integer.toString(i), ConfigGroup.DEFAULT, configList.getList().get(i))));
				}

				add(new ButtonAddValue(this));
			}

			@Override
			public void alignWidgets()
			{
				for (Widget w : widgets)
				{
					w.setWidth(width - 16);
				}

				scroll.setMaxValue(align(WidgetLayout.VERTICAL));
			}
		};

		scroll = new PanelScrollBar(this, configPanel);

		buttonAccept = new SimpleButton(this, I18n.format("gui.accept"), GuiIcons.ACCEPT, (widget, button) ->
		{
			originalConfigList.getValue().setValueFromOtherValue(configList);
			widget.getGui().closeGui();
		});

		buttonCancel = new SimpleButton(this, I18n.format("gui.cancel"), GuiIcons.CANCEL, (widget, button) -> widget.getGui().closeGui());
	}

	@Override
	public boolean onInit()
	{
		for (Widget widget : configPanel.widgets)
		{
			if (widget instanceof ButtonConfigValue)
			{
				((ButtonConfigValue) widget).valueString = null;
			}
		}

		return setFullscreen();
	}

	@Override
	public void addWidgets()
	{
		add(buttonAccept);
		add(buttonCancel);
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
		GuiEditConfig.COLOR_BACKGROUND.draw(0, 0, width, 20);
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
		return GuiEditConfig.THEME;
	}
}