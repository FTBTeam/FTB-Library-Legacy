package com.feed_the_beast.ftbl.api.events.player;

import com.feed_the_beast.ftbl.api.IForgePlayer;
import net.minecraft.util.DamageSource;

/**
 * @author LatvianModder
 */
public class ForgePlayerDeathEvent extends ForgePlayerEvent
{
	private final DamageSource damageSource;

	public ForgePlayerDeathEvent(IForgePlayer player, DamageSource ds)
	{
		super(player);
		damageSource = ds;
	}

	public DamageSource getDamageSource()
	{
		return damageSource;
	}
}