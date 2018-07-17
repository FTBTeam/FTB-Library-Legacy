package com.feed_the_beast.ftblib.events;

import com.feed_the_beast.ftblib.events.universe.UniverseEvent;
import com.feed_the_beast.ftblib.lib.EnumReloadType;
import com.feed_the_beast.ftblib.lib.data.Universe;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

import java.util.Collection;
import java.util.Collections;

/**
 * @author LatvianModder
 */
public class ServerReloadEvent extends UniverseEvent
{
	public static final ResourceLocation ALL = new ResourceLocation("*:*");

	private final ICommandSender sender;
	private final EnumReloadType type;
	private final ResourceLocation reloadId;
	private final Collection<ResourceLocation> failed;
	private boolean clientReloadRequired;
	private final Collection<EntityPlayerMP> onlinePlayers;

	public ServerReloadEvent(Universe u, ICommandSender c, EnumReloadType t, ResourceLocation id, Collection<ResourceLocation> f)
	{
		super(u);
		sender = c;
		type = t;
		reloadId = id;
		failed = f;
		clientReloadRequired = false;
		onlinePlayers = u.server.getPlayerList() != null ? u.server.getPlayerList().getPlayers() : Collections.emptyList();
	}

	public ICommandSender getSender()
	{
		return sender;
	}

	public EnumReloadType getType()
	{
		return type;
	}

	public void setClientReloadRequired()
	{
		clientReloadRequired = true;
	}

	public boolean isClientReloadRequired()
	{
		return clientReloadRequired;
	}

	public Collection<EntityPlayerMP> getOnlinePlayers()
	{
		return onlinePlayers;
	}

	public boolean reload(ResourceLocation id)
	{
		String ridd = reloadId.getNamespace();
		String ridp = reloadId.getPath();
		return ridd.equals("*") || ridd.equals(reloadId.getNamespace()) && (ridp.equals("*") || ridp.equals(id.getPath()));
	}

	public void failedToReload(ResourceLocation id)
	{
		failed.add(id);
	}
}