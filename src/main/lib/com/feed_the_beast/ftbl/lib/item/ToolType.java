package com.feed_the_beast.ftbl.lib.item;

import net.minecraft.util.IStringSerializable;

public enum ToolType implements IStringSerializable
{
    PICK("pick"),
    SHOVEL("shovel"),
    AXE("axe"),
    WRENCH("wrench");

    /**
     * @author LatvianModder
     */
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