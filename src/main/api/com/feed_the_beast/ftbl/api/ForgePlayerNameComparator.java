package com.feed_the_beast.ftbl.api;

import java.util.Comparator;

public enum ForgePlayerNameComparator implements Comparator<ForgePlayer>
{
    INSTANCE;

    @Override
    public int compare(ForgePlayer o1, ForgePlayer o2)
    {
        return o1.getProfile().getName().compareToIgnoreCase(o2.getProfile().getName());
    }
}