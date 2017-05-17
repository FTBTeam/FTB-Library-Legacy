package com.feed_the_beast.ftbl.lib;

import net.minecraftforge.fml.common.discovery.ModCandidate;

import java.util.Collections;
import java.util.List;

/**
 * @author LatvianModder
 */
public interface IAnnotationInfo
{
    ModCandidate getModCandidate();

    Object getObject(String id, Object def);

    default String getString(String id, String def)
    {
        return getObject(id, def).toString();
    }

    default int getInt(String id, int def)
    {
        return getObject(id, def).hashCode();
    }

    default boolean getBoolean(String id, boolean def)
    {
        return (boolean) getObject(id, def);
    }

    default List<String> getStringList(String id)
    {
        Object val = getObject(id, Collections.emptyList());

        if(val instanceof String)
        {
            return Collections.singletonList(val.toString());
        }

        return (List<String>) val;
    }
}