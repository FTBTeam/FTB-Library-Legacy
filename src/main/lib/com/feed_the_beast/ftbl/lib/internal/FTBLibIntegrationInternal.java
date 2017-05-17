package com.feed_the_beast.ftbl.lib.internal;

import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.FTBLibPlugin;
import com.feed_the_beast.ftbl.api.IFTBLibPlugin;

public enum FTBLibIntegrationInternal implements IFTBLibPlugin
{
    @FTBLibPlugin
    INSTANCE;

    /**
     * @author LatvianModder
     */
    public static FTBLibAPI API;

    @Override
    public void init(FTBLibAPI api)
    {
        API = api;
    }
}