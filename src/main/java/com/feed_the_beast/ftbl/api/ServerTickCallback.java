package com.feed_the_beast.ftbl.api;

public abstract class ServerTickCallback
{
    public final int maxTick;
    private int ticks = 0;

    public ServerTickCallback(int i)
    {
        maxTick = Math.max(0, i);
    }

    public ServerTickCallback()
    {
        this(1);
    }

    public boolean incAndCheck()
    {
        ticks++;
        if(ticks >= maxTick)
        {
            onCallback();
            return true;
        }

        return false;
    }

    public abstract void onCallback();
}