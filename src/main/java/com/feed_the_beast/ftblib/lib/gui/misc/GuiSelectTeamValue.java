package com.feed_the_beast.ftblib.lib.gui.misc;

import com.feed_the_beast.ftblib.lib.config.ConfigTeamClient;
import com.feed_the_beast.ftblib.lib.gui.GuiHelper;
import com.feed_the_beast.ftblib.lib.gui.IOpenableGui;
import com.feed_the_beast.ftblib.lib.gui.Panel;
import com.feed_the_beast.ftblib.lib.gui.SimpleTextButton;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import net.minecraft.client.resources.I18n;

/**
 * @author LatvianModder
 */
public class GuiSelectTeamValue extends GuiButtonListBase
{
	private final ConfigTeamClient value;
	private final IOpenableGui callbackGui;
	private final Runnable callback;

	public GuiSelectTeamValue(ConfigTeamClient v, IOpenableGui c, Runnable cb)
	{
		setTitle(I18n.format("ftblib.select_team.gui"));
		setHasSearchBox(true);
		value = v;
		callbackGui = c;
		callback = cb;
	}

	@Override
	public void addButtons(Panel panel)
	{
		for (ConfigTeamClient.TeamInst inst : value.map.values())
		{
			panel.add(new SimpleTextButton(panel, inst.title.getFormattedText(), inst.icon)
			{
				@Override
				public void onClicked(MouseButton button)
				{
					GuiHelper.playClickSound();
					callbackGui.openGui();
					value.setString(inst.getId());
					callback.run();
				}
			});
		}
	}
}