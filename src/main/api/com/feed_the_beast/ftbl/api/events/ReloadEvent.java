package com.feed_the_beast.ftbl.api.events;

import com.feed_the_beast.ftbl.api.EnumReloadType;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * @author LatvianModder
 */
public class ReloadEvent extends FTBLibEvent
{
	public static final ResourceLocation ALL = new ResourceLocation("*:*");

	private final Side side;
	private final ICommandSender sender;
	private final EnumReloadType type;
	private final ResourceLocation reloadId;
	private final Collection<ResourceLocation> failed;

	public ReloadEvent(Side s, ICommandSender c, EnumReloadType t, ResourceLocation id, Collection<ResourceLocation> f)
	{
		side = s;
		sender = c;
		type = t;
		reloadId = id;
		failed = f;
	}

	public Side getSide()
	{
		return side;
	}

	public ICommandSender getSender()
	{
		return sender;
	}

	public EnumReloadType getType()
	{
		return type;
	}

	public boolean reload(ResourceLocation id)
	{
		String ridd = reloadId.getResourceDomain();
		String ridp = reloadId.getResourcePath();
		return ridd.equals("*") || ridd.equals(reloadId.getResourceDomain()) && (ridp.equals("*") || ridp.equals(id.getResourcePath()));
	}

	public void failedToReload(ResourceLocation id)
	{
		failed.add(id);
	}

	public static class RegisterIds extends FTBLibEvent
	{
		private final Consumer<ResourceLocation> callback;

		public RegisterIds(Consumer<ResourceLocation> c)
		{
			callback = c;
		}

		public void register(ResourceLocation id)
		{
			callback.accept(id);
		}
	}
}