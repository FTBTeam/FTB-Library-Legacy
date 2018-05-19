package com.feed_the_beast.ftblib.lib.data;

import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

/**
 * @author LatvianModder
 */
public abstract class Action
{
	public enum Type
	{
		ENABLED,
		DISABLED,
		INVISIBLE;

		public static Type fromBoolean(boolean value)
		{
			return value ? ENABLED : DISABLED;
		}

		public boolean isEnabled()
		{
			return this == ENABLED;
		}

		public boolean isVisible()
		{
			return this != INVISIBLE;
		}
	}

	public static class Inst implements Comparable<Inst>
	{
		public static final DataOut.Serializer<Inst> SERIALIZER = (data, object) -> object.writeData(data);
		public static final DataIn.Deserializer<Inst> DESERIALIZER = Inst::new;

		public final ResourceLocation id;
		public final ITextComponent title;
		public final boolean requiresConfirm;
		public final Icon icon;
		public boolean enabled;
		public short order;

		private Inst(DataIn data)
		{
			id = data.readResourceLocation();
			title = data.readTextComponent();
			requiresConfirm = data.readBoolean();
			icon = data.readIcon();
			enabled = data.readBoolean();
			order = data.readShort();
		}

		public Inst(Action action, Action.Type t)
		{
			id = action.getId();
			title = action.getTitle();
			requiresConfirm = action.getRequireConfirm();
			icon = action.getIcon();
			enabled = t.isEnabled();
			order = (short) action.getOrder();
		}

		private void writeData(DataOut data)
		{
			data.writeResourceLocation(id);
			data.writeTextComponent(title);
			data.writeBoolean(requiresConfirm);
			data.writeIcon(icon);
			data.writeBoolean(enabled);
			data.writeShort(order);
		}

		@Override
		public int compareTo(Inst o)
		{
			int i = Short.compare(order, o.order);
			return i == 0 ? title.getUnformattedText().compareToIgnoreCase(o.title.getUnformattedText()) : i;
		}
	}

	private final ResourceLocation id;
	private ITextComponent title;
	private boolean requiresConfirm;
	private Icon icon;
	private int order;

	public Action(ResourceLocation _id, ITextComponent t, Icon i, int o)
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

	public Action setTitle(ITextComponent t)
	{
		title = t;
		return this;
	}

	public ITextComponent getTitle()
	{
		return title;
	}

	public Action setRequiresConfirm()
	{
		requiresConfirm = true;
		return this;
	}

	public boolean getRequireConfirm()
	{
		return requiresConfirm;
	}

	public Action setIcon(Icon i)
	{
		icon = i;
		return this;
	}

	public Icon getIcon()
	{
		return icon;
	}

	public Action setOrder(int o)
	{
		order = MathHelper.clamp(o, Short.MIN_VALUE, Short.MAX_VALUE);
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