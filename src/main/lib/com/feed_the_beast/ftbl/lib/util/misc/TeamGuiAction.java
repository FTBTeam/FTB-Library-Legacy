package com.feed_the_beast.ftbl.lib.util.misc;

import com.feed_the_beast.ftbl.api.team.ITeamGuiAction;
import com.feed_the_beast.ftbl.lib.icon.Icon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

/**
 * @author LatvianModder
 */
public abstract class TeamGuiAction implements ITeamGuiAction
{
	private final ResourceLocation id;
	private ITextComponent title;
	private boolean requiresConfirm;
	private Icon icon;
	private int order;

	public TeamGuiAction(ResourceLocation _id, ITextComponent t, Icon i, int o)
	{
		id = _id;
		title = t;
		requiresConfirm = false;
		icon = i;
		order = o;
	}

	@Override
	public final ResourceLocation getId()
	{
		return id;
	}

	public TeamGuiAction setTitle(ITextComponent t)
	{
		title = t;
		return this;
	}

	@Override
	public ITextComponent getTitle()
	{
		return title;
	}

	public TeamGuiAction setRequiresConfirm()
	{
		requiresConfirm = true;
		return this;
	}

	@Override
	public boolean getRequireConfirm()
	{
		return requiresConfirm;
	}

	public TeamGuiAction setIcon(Icon i)
	{
		icon = i;
		return this;
	}

	@Override
	public Icon getIcon()
	{
		return icon;
	}

	public TeamGuiAction setOrder(int o)
	{
		order = o;
		return this;
	}

	@Override
	public int getOrder()
	{
		return order;
	}

	public final int hashCode()
	{
		return id.hashCode();
	}

	public final boolean equals(Object o)
	{
		return o == this;
	}

	public final String toString()
	{
		return id.toString();
	}
}