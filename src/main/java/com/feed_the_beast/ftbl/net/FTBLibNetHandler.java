package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.lib.net.LMNetworkWrapper;

public class FTBLibNetHandler
{
    public static final LMNetworkWrapper NET = LMNetworkWrapper.newWrapper("FTBL");

    public static void init()
    {
        NET.register(1, new MessageReload());
        NET.register(2, new MessageEditConfig());
        NET.register(3, new MessageEditConfigResponse());
        NET.register(4, new MessageOpenGui());
        NET.register(5, new MessageNotifyPlayer());
        NET.register(6, new MessageDisplayInfo());
        NET.register(7, new MessageLogin());
        NET.register(8, new MessageNotifyPlayerCustom());
        //9
        NET.register(10, new MessageMyTeamGuiResponse());
    }
}