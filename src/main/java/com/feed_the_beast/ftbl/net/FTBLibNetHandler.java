package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api.net.LMNetworkWrapper;

public class FTBLibNetHandler
{
    public static final LMNetworkWrapper NET = LMNetworkWrapper.newWrapper("FTBL");

    public static void init()
    {
        NET.register(1, new MessageReload());
        NET.register(2, new MessageEditConfig());
        NET.register(3, new MessageEditConfigResponse());
        //4
        //5
        //6
        //7
        //8
        //9
        //10
        NET.register(11, new MessageOpenGui());
        //12
        //13
        //14
        NET.register(15, new MessageNotifyPlayer());
        NET.register(16, new MessageLMPlayerInfo());
        //17
        NET.register(18, new MessageDisplayInfo());
    }
}