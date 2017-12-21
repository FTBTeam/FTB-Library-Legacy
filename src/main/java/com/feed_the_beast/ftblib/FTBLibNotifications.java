package com.feed_the_beast.ftblib;

import com.feed_the_beast.ftblib.lib.util.text_components.Notification;

/**
 * @author LatvianModder
 */
public class FTBLibNotifications
{
	public static final Notification NO_TEAM = Notification.of(FTBLibFinals.get("no_team"), FTBLibLang.TEAM_NO_TEAM.textComponent(null)).setError();
}