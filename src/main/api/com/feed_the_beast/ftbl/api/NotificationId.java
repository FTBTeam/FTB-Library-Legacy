package com.feed_the_beast.ftbl.api;

import net.minecraft.util.ResourceLocation;

/**
 * @author LatvianModder
 */
public final class NotificationId
{
    private final ResourceLocation ID;
    private final int var;

    public NotificationId(ResourceLocation id, int variant)
    {
        ID = id;
        var = variant;
    }

    public NotificationId(String mod, String id, int variant)
    {
        this(new ResourceLocation(mod, id), variant);
    }

    public ResourceLocation getID()
    {
        return ID;
    }

    public int getVariant()
    {
        return var;
    }

    public int hashCode()
    {
        return ID.hashCode() * 31 + var;
    }

    public String toString()
    {
        return ID + "@" + var;
    }

    public boolean equalsID(NotificationId id)
    {
        return ID.equals(id.ID);
    }

    public boolean equals(Object o)
    {
        return o == this || (o instanceof NotificationId && ((NotificationId) o).equalsID(this));
    }

    public NotificationId variant(int v)
    {
        return new NotificationId(ID, v);
    }

    public int getChatMessageID()
    {
        return 42059283 + (ID.hashCode() & 0xFFF);
    }
}