package com.feed_the_beast.ftbl.lib.item;

import com.feed_the_beast.ftbl.api.item.IMaterial;
import net.minecraft.item.Item;

public class MaterialItem implements IMaterial
{
    private final int metadata;
    private final String name;
    private Item item;

    public MaterialItem(int d, String s)
    {
        metadata = d;
        name = s;
    }

    @Override
    public Item getItem()
    {
        return item;
    }

    @Override
    public void setItem(Item i)
    {
        item = i;
    }

    @Override
    public int getMetadata()
    {
        return metadata;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public boolean isAdded()
    {
        return true;
    }

    public final boolean equals(Object o)
    {
        return o != null && (o == this || o.hashCode() == metadata);
    }

    public final int hashCode()
    {
        return metadata;
    }

    public final String toString()
    {
        return name;
    }
}