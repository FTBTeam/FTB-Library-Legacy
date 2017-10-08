package com.feed_the_beast.ftbl.client;

import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.ISidebarButton;
import com.feed_the_beast.ftbl.api.ISidebarButtonGroup;
import com.feed_the_beast.ftbl.lib.gui.Button;
import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.GuiLang;
import com.feed_the_beast.ftbl.lib.gui.Panel;
import com.feed_the_beast.ftbl.lib.gui.PanelScrollBar;
import com.feed_the_beast.ftbl.lib.gui.Widget;
import com.feed_the_beast.ftbl.lib.gui.WidgetLayout;
import com.feed_the_beast.ftbl.lib.icon.Color4I;
import com.feed_the_beast.ftbl.lib.icon.CombinedIcon;
import com.feed_the_beast.ftbl.lib.icon.Icon;
import com.feed_the_beast.ftbl.lib.util.StringUtils;
import com.feed_the_beast.ftbl.lib.util.misc.MouseButton;

import java.util.List;

/**
 * @author LatvianModder
 */
public class GuiSidebarButtonConfig extends GuiBase
{
	private static final Color4I COLOR_ENABLED = Color4I.rgba(0x5547BF41);
	private static final Color4I COLOR_UNAVAILABLE = Color4I.rgba(0x550094FF);
	private static final Color4I COLOR_DISABLED = Color4I.rgba(0x55BC4242);

	private class ButtonConfigSidebarButton extends Button
	{
		private final ISidebarButton sidebarButton;
		private String tooltip = "";

		public ButtonConfigSidebarButton(GuiBase gui, ISidebarButton s)
		{
			super(gui, 0, 0, 0, 20);
			sidebarButton = s;
			String title = StringUtils.translate("sidebar_button." + s.getName());
			setWidth(gui.getStringWidth(title) + 28);
			setTitle(title);

			if (StringUtils.canTranslate("sidebar_button." + s.getName() + ".tooltip"))
			{
				tooltip = StringUtils.translate("sidebar_button." + s.getName() + ".tooltip");
			}
		}

		@Override
		public void addMouseOverText(List<String> list)
		{
			list.add((sidebarButton.getConfig() ? GuiLang.ENABLED : GuiLang.DISABLED).translate());

			if (!tooltip.isEmpty())
			{
				list.add(tooltip);
			}
		}

		@Override
		public void renderWidget()
		{
			int ax = getAX();
			int ay = getAY();

			getIcon().draw(ax, ay, width, height);
			sidebarButton.getIcon().draw(ax + 2, ay + 2, 16, 16);
			gui.drawString(getTitle(), ax + 21, ay + 6, SHADOW);
		}

		@Override
		public Icon getIcon()
		{
			return gui.isMouseOver(this) ? gui.getTheme().getButton(true) : new CombinedIcon(gui.getTheme().getButton(false), sidebarButton.getConfig() ? (sidebarButton.isAvailable() ? COLOR_ENABLED : COLOR_UNAVAILABLE) : COLOR_DISABLED);
		}

		@Override
		public void onClicked(MouseButton button)
		{
			GuiHelper.playClickSound();
			sidebarButton.setConfig(!sidebarButton.getConfig());
		}
	}

	private final Panel panelButtons;
	private final PanelScrollBar scrollBar;

	public GuiSidebarButtonConfig()
	{
		super(0, 0);

		panelButtons = new Panel(this, 3, 3, 0, 158)
		{
			@Override
			public void addWidgets()
			{
				width = 0;

				for (ISidebarButtonGroup group : FTBLibAPI.API.getSidebarButtonGroups())
				{
					for (ISidebarButton button : group.getButtons())
					{
						if (button.getDefaultConfig() != null)
						{
							Button b = new ButtonConfigSidebarButton(gui, button);
							add(b);
							setWidth(Math.max(width, b.width));
						}
					}
				}

				for (Widget w : widgets)
				{
					w.setWidth(width - 4);
				}

				updateWidgetPositions();
			}

			@Override
			public void updateWidgetPositions()
			{
				scrollBar.setElementSize(align(new WidgetLayout.Vertical(1, 2, 1)));
				scrollBar.setSrollStepFromOneElementSize(20);
			}
		};

		panelButtons.addFlags(Panel.DEFAULTS);

		scrollBar = new PanelScrollBar(this, 0, 3, 16, 158, 0, panelButtons)
		{
			@Override
			public boolean shouldRender()
			{
				return true;
			}
		};

		setHeight(164);
	}

	@Override
	public void addWidgets()
	{
		addAll(panelButtons, scrollBar);
		scrollBar.setX(panelButtons.width + 1);
		setWidth(panelButtons.width + 20);
		posX = (getScreen().getScaledWidth() - width) / 2;
	}

	@Override
	public void onClosed()
	{
		FTBLibModClient.saveSidebarButtonConfig();
	}
}