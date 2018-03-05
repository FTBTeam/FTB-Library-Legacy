package com.feed_the_beast.ftblib.events.player;

import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import net.minecraft.util.ResourceLocation;

/**
 * @author LatvianModder
 */
public class ForgePlayerLoggedInEvent extends ForgePlayerEvent
{
	public ForgePlayerLoggedInEvent(ForgePlayer player)
	{
		super(player);
	}

	public boolean isFirstLogin(ResourceLocation id)
	{
		return getPlayer().isFirstLogin(id);
	}
}