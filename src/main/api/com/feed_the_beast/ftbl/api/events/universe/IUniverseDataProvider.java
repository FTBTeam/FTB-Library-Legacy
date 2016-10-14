package com.feed_the_beast.ftbl.api.events.universe;

import com.feed_the_beast.ftbl.api.IRegistryObject;
import com.feed_the_beast.ftbl.api.IUniverse;
import com.feed_the_beast.ftbl.lib.INBTData;

/**
 * Created by LatvianModder on 10.10.2016.
 */
public interface IUniverseDataProvider extends IRegistryObject
{
    INBTData getUniverseData(IUniverse universe);
}
