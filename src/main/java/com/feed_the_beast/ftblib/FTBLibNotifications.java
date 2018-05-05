package com.feed_the_beast.ftblib;

import com.feed_the_beast.ftblib.lib.util.text_components.Notification;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * @author LatvianModder
 */
public class FTBLibNotifications
{
	public static final ResourceLocation RELOAD_SERVER = new ResourceLocation(FTBLib.MOD_ID, "reload_server");
	public static final Notification NO_TEAM = Notification.of(new ResourceLocation(FTBLib.MOD_ID, "no_team"), new TextComponentTranslation("ftblib.lang.team.error.no_team")).setError();
}