package com.feed_the_beast.ftblib.client;

import com.feed_the_beast.ftblib.lib.gui.GuiHelper;
import com.feed_the_beast.ftblib.lib.gui.Panel;
import com.feed_the_beast.ftblib.lib.gui.SimpleTextButton;
import com.feed_the_beast.ftblib.lib.gui.misc.GuiButtonListBase;
import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import net.minecraft.client.resources.I18n;

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

		public ButtonConfigSidebarButton(Panel panel, SidebarButton s)
		{
			super(panel, I18n.format(s.getLangKey()), s.getIcon());
			sidebarButton = s;

			if (I18n.hasKey(s.getTooltipLangKey()))
			{
				tooltip = I18n.format(s.getTooltipLangKey());
			}
		}

		@Override
		public void addMouseOverText(List<String> list)
		{
			list.add(sidebarButton.getConfig() ? I18n.format("addServer.resourcePack.enabled") : I18n.format("addServer.resourcePack.disabled"));

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
		setTitle(I18n.format("sidebar_button"));
	}

	@Override
	public void addButtons(Panel panel)
	{
		for (SidebarButtonGroup group : FTBLibClient.SIDEBAR_BUTTON_GROUPS)
		{
			for (SidebarButton button : group.getButtons())
			{
				panel.add(new ButtonConfigSidebarButton(panel, button));
			}
		}
	}

	@Override
	public void onClosed()
	{
		FTBLibClient.saveSidebarButtonConfig();
	}
}