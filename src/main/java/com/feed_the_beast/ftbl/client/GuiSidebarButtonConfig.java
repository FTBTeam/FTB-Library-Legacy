package com.feed_the_beast.ftbl.client;

import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.ISidebarButton;
import com.feed_the_beast.ftbl.api.ISidebarButtonGroup;
import com.feed_the_beast.ftbl.lib.gui.Button;
import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.GuiLang;
import com.feed_the_beast.ftbl.lib.gui.Panel;
import com.feed_the_beast.ftbl.lib.gui.misc.GuiButtonListBase;
import com.feed_the_beast.ftbl.lib.icon.Color4I;
import com.feed_the_beast.ftbl.lib.icon.CombinedIcon;
import com.feed_the_beast.ftbl.lib.icon.Icon;
import com.feed_the_beast.ftbl.lib.util.StringUtils;
import com.feed_the_beast.ftbl.lib.util.misc.MouseButton;

import java.util.List;

/**
 * @author LatvianModder
 */
public class GuiSidebarButtonConfig extends GuiButtonListBase
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

	@Override
	public void addButtons(Panel panel)
	{
		for (ISidebarButtonGroup group : FTBLibAPI.API.getSidebarButtonGroups())
		{
			for (ISidebarButton button : group.getButtons())
			{
				if (button.getDefaultConfig() != null)
				{
					panel.add(new ButtonConfigSidebarButton(gui, button));
				}
			}
		}
	}

	@Override
	public void onClosed()
	{
		FTBLibModClient.saveSidebarButtonConfig();
	}
}