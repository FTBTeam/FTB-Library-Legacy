package com.feed_the_beast.ftblib.lib.util.text_components;

import com.feed_the_beast.ftblib.lib.math.Ticks;
import com.feed_the_beast.ftblib.lib.util.ServerUtils;
import com.feed_the_beast.ftblib.lib.util.StringJoiner;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class Notification extends TextComponentString
{
	public static final ResourceLocation VANILLA_STATUS = new ResourceLocation("minecraft", "status");

	public static Notification of(ResourceLocation id, String text, ITextComponent... lines)
	{
		Notification n = new Notification(id, text);

		for (ITextComponent line : lines)
		{
			n.addLine(line);
		}

		return n;
	}

	public static Notification of(ResourceLocation id, ITextComponent... lines)
	{
		return of(id, "", lines);
	}

	private final ResourceLocation id;
	private Ticks timer;
	private boolean important;

	private Notification(ResourceLocation i, String text)
	{
		super(text);
		id = i;
		timer = Ticks.SECOND.x(3);
		important = false;
	}

	public Notification(Notification n)
	{
		this(n.getId(), n.getUnformattedComponentText());

		setStyle(n.getStyle().createShallowCopy());

		for (ITextComponent line : n.getSiblings())
		{
			getSiblings().add(line.createCopy());
		}

		setTimer(n.getTimer());
		setImportant(n.isImportant());
	}

	public Notification addLine(ITextComponent line)
	{
		if (!getSiblings().isEmpty())
		{
			appendText("\n");
		}

		appendSibling(line);
		return this;
	}

	public Notification setError()
	{
		getStyle().setColor(TextFormatting.DARK_RED);
		important = true;
		return this;
	}

	@Override
	public Notification appendText(String text)
	{
		return appendSibling(new TextComponentString(text));
	}

	@Override
	public Notification appendSibling(ITextComponent component)
	{
		super.appendSibling(component);
		return this;
	}

	public int hashCode()
	{
		return id.hashCode();
	}

	public boolean equals(Object o)
	{
		return o == this || (o instanceof Notification && ((Notification) o).getId().equals(getId()));
	}

	public String toString()
	{
		return "Notification{" + StringJoiner.with(", ").joinObjects("id=" + id, "siblings=" + siblings, "style=" + getStyle(), "timer=" + timer, "important=" + important) + '}';
	}

	public ResourceLocation getId()
	{
		return id;
	}

	public Ticks getTimer()
	{
		return timer;
	}

	public Notification setTimer(Ticks t)
	{
		timer = t;
		return this;
	}

	public boolean isImportant()
	{
		return important;
	}

	public Notification setImportant(boolean v)
	{
		important = v;
		return this;
	}

	@Override
	public Notification createCopy()
	{
		return new Notification(this);
	}

	public void send(MinecraftServer server, @Nullable EntityPlayer player)
	{
		ServerUtils.notify(server, player, this);
	}

	public void send(MinecraftServer server, @Nullable ICommandSender sender)
	{
		if (sender == null)
		{
			ServerUtils.notify(server, null, this);
		}
		else if (sender instanceof EntityPlayer)
		{
			ServerUtils.notify(server, (EntityPlayer) sender, this);
		}
		else
		{
			sender.sendMessage(this);
		}
	}
}