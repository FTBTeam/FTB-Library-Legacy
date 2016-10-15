package com.feed_the_beast.ftbl.api.asm;

import java.util.Collections;
import java.util.List;

/**
 * Created by LatvianModder on 14.10.2016.
 */
public interface IAnnotationInfo
{
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