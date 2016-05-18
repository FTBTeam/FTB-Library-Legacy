package com.feed_the_beast.ftbl.api.info;

import latmod.lib.net.LMConnection;
import latmod.lib.net.RequestMethod;

/**
 * Created by LatvianModder on 09.05.2016.
 */
public class URLResourceProvider implements IResourceProvider
{
    public static final URLResourceProvider INSTANCE = new URLResourceProvider();
    
    @Override
    public LMConnection getConnection(String s)
    { return new LMConnection(RequestMethod.SIMPLE_GET, s); }
}