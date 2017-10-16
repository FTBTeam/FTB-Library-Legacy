package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.lib.icon.Icon;
import net.minecraft.util.IStringSerializable;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public interface ISidebarButton extends IStringSerializable, Comparable<ISidebarButton>
{
	ISidebarButtonGroup getGroup();

	Icon getIcon();

	int getX();

	@Nullable
	default Boolean getDefaultConfig()
	{
		return null;
	}

	void onClicked(boolean shift);

	boolean isVisible();

	boolean isAvailable();

	boolean hasCustomText();

	boolean getConfig();

	void setConfig(boolean value);

	@Override
	default int compareTo(ISidebarButton button)
	{
		return getX() - button.getX();
	}
}