package com.feed_the_beast.ftbl.api_impl;

/**
 * Created by LatvianModder on 17.09.2016.
 */
public class StringIntIDRegistry extends AbstractIntIDRegistry<String>
{
    @Override
    public String createFromKey(String s)
    {
        return s;
    }

    @Override
    public String createFromString(String s)
    {
        return s;
    }
}
