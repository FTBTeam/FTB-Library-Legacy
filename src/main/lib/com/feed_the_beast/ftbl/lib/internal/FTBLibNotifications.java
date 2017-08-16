package com.feed_the_beast.ftbl.lib.internal;

import com.feed_the_beast.ftbl.lib.Notification;
import com.feed_the_beast.ftbl.lib.client.DrawableItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/**
 * @author LatvianModder
 */
public class FTBLibNotifications
{
	public static final Notification NEW_TEAM_MESSAGE = Notification.of(FTBLibFinals.get("team_msg")).setIcon(new DrawableItem(new ItemStack(Items.WRITABLE_BOOK)));
	public static final Notification NO_TEAM = Notification.of(FTBLibFinals.get("no_team"), FTBLibLang.TEAM_NO_TEAM.textComponent()).setError();
}