package com.feed_the_beast.ftbl.util;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public enum EnumNotificationDisplay
{
    OFF,
    SCREEN,
    CHAT,
    CHAT_ALL
}