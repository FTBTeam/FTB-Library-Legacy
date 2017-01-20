package com.feed_the_beast.ftbl.lib.item;

import net.minecraft.util.IStringSerializable;

/**
 * Created by LatvianModder on 28.08.2016.
 */
public enum ToolType implements IStringSerializable
{
    PICK("pick"),
    SHOVEL("shovel"),
    AXE("axe"),
    WRENCH("wrench");

    private String name;

    ToolType(String n)
    {
        name = n;
    }

    @Override
    public String getName()
    {
        return name;
    }
}
