package com.feed_the_beast.ftblib.lib.data;

import com.feed_the_beast.ftblib.lib.icon.Icon;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

/**
 * @author LatvianModder
 */
public abstract class TeamGuiAction
{
	public enum Type
	{
		ENABLED,
		DISABLED,
		INVISIBLE
	}

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

	public final ResourceLocation getId()
	{
		return id;
	}

	public abstract Type getType(ForgePlayer player, NBTTagCompound data);

	public abstract void onAction(ForgePlayer player, NBTTagCompound data);

	public TeamGuiAction setTitle(ITextComponent t)
	{
		title = t;
		return this;
	}

	public ITextComponent getTitle()
	{
		return title;
	}

	public TeamGuiAction setRequiresConfirm()
	{
		requiresConfirm = true;
		return this;
	}

	public boolean getRequireConfirm()
	{
		return requiresConfirm;
	}

	public TeamGuiAction setIcon(Icon i)
	{
		icon = i;
		return this;
	}

	public Icon getIcon()
	{
		return icon;
	}

	public TeamGuiAction setOrder(int o)
	{
		order = o;
		return this;
	}

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