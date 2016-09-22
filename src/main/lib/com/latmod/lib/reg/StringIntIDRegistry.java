package com.latmod.lib.reg;

/**
 * Created by LatvianModder on 17.09.2016.
 */
public class StringIntIDRegistry extends IntIDRegistry<String>
{
    @Override
    public String createStringFromKey(String s)
    {
        return s;
    }

    @Override
    public String createKeyFromString(String s)
    {
        return s;
    }
}
