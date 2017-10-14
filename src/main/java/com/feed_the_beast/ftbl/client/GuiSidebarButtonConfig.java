package com.feed_the_beast.ftbl.client;

import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.ISidebarButton;
import com.feed_the_beast.ftbl.api.ISidebarButtonGroup;
import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.GuiLang;
import com.feed_the_beast.ftbl.lib.gui.Panel;
import com.feed_the_beast.ftbl.lib.gui.SimpleTextButton;
import com.feed_the_beast.ftbl.lib.gui.misc.GuiButtonListBase;
import com.feed_the_beast.ftbl.lib.icon.Color4I;
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

	private class ButtonConfigSidebarButton extends SimpleTextButton
	{
		private final ISidebarButton sidebarButton;
		private String tooltip = "";

		public ButtonConfigSidebarButton(GuiBase gui, ISidebarButton s)
		{
			super(gui, 0, 0, StringUtils.translate("sidebar_button." + s.getName()), s.getIcon());
			sidebarButton = s;

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
		public Icon getButtonBackground()
		{
			return super.getButtonBackground().combineWith(sidebarButton.getConfig() ? (sidebarButton.isAvailable() ? COLOR_ENABLED : COLOR_UNAVAILABLE) : COLOR_DISABLED);
		}

		@Override
		public void onClicked(MouseButton button)
		{
			GuiHelper.playClickSound();
			sidebarButton.setConfig(!sidebarButton.getConfig());
		}
	}

	public GuiSidebarButtonConfig()
	{
		setTitle(StringUtils.translate("sidebar_button"));
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