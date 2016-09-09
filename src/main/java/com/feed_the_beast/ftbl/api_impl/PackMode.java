package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.api.IPackMode;
import com.latmod.lib.util.LMUtils;

import java.io.File;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public class PackMode implements IPackMode
{
    private String ID;

    public PackMode(String id)
    {
        ID = id;
    }

    @Override
    public String getID()
    {
        return ID;
    }

    public String toString()
    {
        return ID;
    }

    public int hashCode()
    {
        return ID.hashCode();
    }

    public boolean equals(Object o)
    {
        return o == this || (o != null && o.toString().equals(ID));
    }

    @Override
    public File getFolder()
    {
        File f = new File(LMUtils.folderModpack, getID());
        if(!f.exists())
        {
            f.mkdirs();
        }
        return f;
    }
}