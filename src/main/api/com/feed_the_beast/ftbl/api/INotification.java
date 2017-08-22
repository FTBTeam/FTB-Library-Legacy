package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.lib.util.ServerUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public interface INotification extends ITextComponent
{
	ResourceLocation VANILLA_STATUS = new ResourceLocation("minecraft", "status");

	ResourceLocation getId();

	int getTimer();

	default void send(@Nullable EntityPlayer player)
	{
		ServerUtils.notify(player, this);
	}
}