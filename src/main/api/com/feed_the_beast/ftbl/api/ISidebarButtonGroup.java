package com.feed_the_beast.ftbl.api;

import net.minecraft.util.ResourceLocation;

import java.util.List;

/**
 * @author LatvianModder
 */
public interface ISidebarButtonGroup extends Comparable<ISidebarButtonGroup>
{
	ResourceLocation getId();

	int getY();

	List<ISidebarButton> getButtons();

	@Override
	default int compareTo(ISidebarButtonGroup group)
	{
		return Integer.compare(getY(), group.getY());
	}
}