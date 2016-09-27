package com.feed_the_beast.ftbl.api.info;

import com.feed_the_beast.ftbl.lib.io.LMConnection;

/**
 * Created by LatvianModder on 09.05.2016.
 */
public interface IResourceProvider
{
    LMConnection getConnection(String s);
}