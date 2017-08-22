package com.feed_the_beast.ftbl.lib.internal;

import com.feed_the_beast.ftbl.lib.Notification;

/**
 * @author LatvianModder
 */
public class FTBLibNotifications
{
	public static final Notification NO_TEAM = Notification.of(FTBLibFinals.get("no_team"), FTBLibLang.TEAM_NO_TEAM.textComponent()).setError();
}