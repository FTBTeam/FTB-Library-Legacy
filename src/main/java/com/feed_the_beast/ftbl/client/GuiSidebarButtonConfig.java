package com.feed_the_beast.ftbl.client;

import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.client.TexturelessRectangle;
import com.feed_the_beast.ftbl.lib.gui.Button;
import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.Panel;
import com.feed_the_beast.ftbl.lib.gui.PanelScrollBar;
import com.feed_the_beast.ftbl.lib.gui.Widget;
import com.feed_the_beast.ftbl.lib.gui.WidgetLayout;
import com.feed_the_beast.ftbl.lib.util.StringUtils;

import java.util.List;

/**
 * @author LatvianModder
 */
public class GuiSidebarButtonConfig extends GuiBase
{
	private class ButtonConfigSidebarButton extends Button
	{
		private final SidebarButton sidebarButton;

		private final TexturelessRectangle BACKGROUND_ENABLED = new TexturelessRectangle(Color4I.NONE).setLineColor(0xFF47BF41);
		private final TexturelessRectangle BACKGROUND_DISABLED = new TexturelessRectangle(Color4I.NONE).setLineColor(0xFFBC4242);

		public ButtonConfigSidebarButton(SidebarButton s)
		{
			super(0, 0, 0, 20);
			sidebarButton = s;
			String title = StringUtils.translate("sidebar_button." + s.getName());
			setWidth(getFont().getStringWidth(title) + 28);
			setTitle(title);
		}

		@Override
		public void addMouseOverText(GuiBase gui, List<String> list)
		{
		}

		@Override
		public void renderWidget(GuiBase gui)
		{
			int ax = getAX();
			int ay = getAY();

			(sidebarButton.configValue ? BACKGROUND_ENABLED : BACKGROUND_DISABLED).draw(ax, ay, width, height, Color4I.NONE);

			sidebarButton.icon.draw(ax + 2, ay + 2, 16, 16, Color4I.NONE);
			gui.drawString(getTitle(gui), ax + 21, ay + 6);

			if (gui.isMouseOver(ax, ay, width, height))
			{
				DEFAULT_MOUSE_OVER.draw(ax, ay, width, height, Color4I.NONE);
			}
		}

		@Override
		public void onClicked(GuiBase gui, IMouseButton button)
		{
			GuiHelper.playClickSound();
			sidebarButton.configValue = !sidebarButton.configValue;
		}
	}

	private final Panel panelButtons;
	private final PanelScrollBar scrollBar;

	public GuiSidebarButtonConfig()
	{
		super(0, 0);

		panelButtons = new Panel(2, 1, 0, 162)
		{
			@Override
			public void addWidgets()
			{
				width = 0;

				for (SidebarButton button : FTBLibModClient.getSidebarButtons(true))
				{
					if (button.defaultConfig != null)
					{
						Button b = new ButtonConfigSidebarButton(button);
						add(b);
						setWidth(Math.max(width, b.width));
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
				scrollBar.setElementSize(align(new WidgetLayout.Vertical(1, 1, 1)));
				scrollBar.setSrollStepFromOneElementSize(19);
			}
		};

		panelButtons.addFlags(Panel.FLAG_DEFAULTS);

		scrollBar = new PanelScrollBar(202, 2, 16, 160, 0, panelButtons)
		{
			@Override
			public boolean shouldRender(GuiBase gui)
			{
				return true;
			}
		};

		scrollBar.background = Button.DEFAULT_BACKGROUND;
		scrollBar.slider = new TexturelessRectangle(Color4I.WHITE_A[33]).setLineColor(Button.DEFAULT_BACKGROUND.getLineColor());

		setWidth(220);
		setHeight(164);
	}

	@Override
	public void addWidgets()
	{
		addAll(panelButtons, scrollBar);
		scrollBar.setX(panelButtons.width - 1);
		setWidth(panelButtons.width + 17);
		posX = (getScreen().getScaledWidth() - width) / 2;
	}

	@Override
	public void onClosed()
	{
		FTBLibModClient.saveSidebarButtonConfig();
	}

	@Override
	public IDrawableObject getIcon(GuiBase gui)
	{
		return Button.DEFAULT_BACKGROUND;
	}
}