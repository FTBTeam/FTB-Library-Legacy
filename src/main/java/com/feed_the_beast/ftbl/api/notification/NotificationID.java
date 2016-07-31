package com.feed_the_beast.ftbl.api.notification;

import gnu.trove.map.hash.TIntObjectHashMap;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LatvianModder on 31.07.2016.
 */
public class NotificationID
{
    private static final Map<ResourceLocation, Integer> MAP = new HashMap<>();
    private static final TIntObjectHashMap<ResourceLocation> INV_MAP = new TIntObjectHashMap<>();
    private static int nextID = 234927908; //Magic number

    public static int get(@Nonnull ResourceLocation id)
    {
        return get(id, 0);
    }

    public static int get(@Nonnull ResourceLocation id, int intid)
    {
        Integer nid = MAP.get(id);

        if(nid == null)
        {
            nid = (intid <= 0) ? (++nextID) : intid;
            MAP.put(id, nid);
            INV_MAP.put(nid, id);
        }

        return nid;
    }

    public static ResourceLocation getResourceLocation(int id)
    {
        return INV_MAP.get(id);
    }
}