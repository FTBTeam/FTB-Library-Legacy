package com.feed_the_beast.ftblib.client;

import com.feed_the_beast.ftblib.lib.gui.GuiBase;
import com.feed_the_beast.ftblib.lib.gui.GuiHelper;
import com.feed_the_beast.ftblib.lib.gui.GuiLang;
import com.feed_the_beast.ftblib.lib.gui.Panel;
import com.feed_the_beast.ftblib.lib.gui.SimpleTextButton;
import com.feed_the_beast.ftblib.lib.gui.misc.GuiButtonListBase;
import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.util.StringUtils;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;

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
		private final SidebarButton sidebarButton;
		private String tooltip = "";

		public ButtonConfigSidebarButton(GuiBase gui, SidebarButton s)
		{
			super(gui, 0, 0, StringUtils.translate(s.getLangKey()), s.getIcon());
			sidebarButton = s;

			if (StringUtils.canTranslate(s.getTooltipLangKey()))
			{
				tooltip = StringUtils.translate(s.getTooltipLangKey());
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
		for (SidebarButtonGroup group : FTBLibModClient.SIDEBAR_BUTTON_GROUPS)
		{
			for (SidebarButton button : group.getButtons())
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