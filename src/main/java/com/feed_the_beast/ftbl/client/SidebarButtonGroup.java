package com.feed_the_beast.ftbl.client;

import com.feed_the_beast.ftbl.api.ISidebarButton;
import com.feed_the_beast.ftbl.api.ISidebarButtonGroup;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class SidebarButtonGroup implements ISidebarButtonGroup
{
	private final ResourceLocation id;
	private final int y;
	private final List<ISidebarButton> buttons;

	public SidebarButtonGroup(ResourceLocation _id, int _y)
	{
		id = _id;
		y = _y;
		buttons = new ArrayList<>();
	}

	@Override
	public ResourceLocation getId()
	{
		return id;
	}

	@Override
	public int getY()
	{
		return y;
	}

	@Override
	public List<ISidebarButton> getButtons()
	{
		return buttons;
	}
}
