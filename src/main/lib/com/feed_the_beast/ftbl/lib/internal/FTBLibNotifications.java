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
	public static final Notification NEW_TEAM_MESSAGE = new Notification(FTBLibFinals.get("team_msg")).setIcon(new DrawableItem(new ItemStack(Items.WRITABLE_BOOK)));
	public static final Notification NO_TEAM = new Notification(FTBLibFinals.get("no_team")).setError(FTBLibLang.TEAM_NO_TEAM.textComponent());
}