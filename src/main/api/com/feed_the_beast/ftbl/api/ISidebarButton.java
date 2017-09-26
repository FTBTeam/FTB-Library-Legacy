package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.lib.icon.Icon;
import net.minecraft.util.IStringSerializable;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Map;

/**
 * @author LatvianModder
 */
public interface ISidebarButton extends IStringSerializable
{
	Icon getIcon();

	@Nullable
	default Boolean getDefaultConfig()
	{
		return null;
	}

	void onClicked(boolean shift);

	default boolean isVisible()
	{
		return getConfig() && isAvailable();
	}

	boolean isAvailable();

	boolean hasCustomText();

	boolean getConfig();

	void setConfig(boolean value);

	default Map<String, Boolean> getDependencies()
	{
		return Collections.emptyMap();
	}
}