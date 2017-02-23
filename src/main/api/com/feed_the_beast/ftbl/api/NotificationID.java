package com.feed_the_beast.ftbl.api;

import net.minecraft.util.ResourceLocation;

/**
 * Created by LatvianModder on 19.11.2016.
 */
public final class NotificationID
{
    private final ResourceLocation ID;
    private final byte var;

    public NotificationID(ResourceLocation id, int variant)
    {
        ID = id;
        var = (byte) variant;
    }

    public NotificationID(String mod, String id, int variant)
    {
        this(new ResourceLocation(mod, id), variant);
    }

    public ResourceLocation getID()
    {
        return ID;
    }

    public byte getVariant()
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

    public boolean equalsID(NotificationID id)
    {
        return ID.equals(id.ID);
    }

    public boolean equals(Object o)
    {
        return o == this || (o instanceof NotificationID && ((NotificationID) o).equalsID(this));
    }

    public NotificationID variant(int v)
    {
        return new NotificationID(ID, v);
    }

    public int getChatMessageID()
    {
        return 42059283 + (ID.hashCode() & 0xFFF);
    }
}