package com.feed_the_beast.ftbl.api_impl;

import net.minecraft.util.ITickable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by LatvianModder on 10.10.2016.
 */
public class TickHandler implements ITickable
{
    public static TickHandler INSTANCE;
    private final List<ServerTickCallback> CALLBACKS = new ArrayList<>();
    private final List<ServerTickCallback> PENDING_CALLBACKS = new ArrayList<>();
    final Collection<ITickable> TICKABLES = new ArrayList<>();

    private static class ServerTickCallback
    {
        private final int maxTick;
        private Runnable runnable;
        private int ticks = 0;

        private ServerTickCallback(int i, Runnable r)
        {
            maxTick = i;
            runnable = r;
        }

        private boolean incAndCheck()
        {
            ticks++;
            if(ticks >= maxTick)
            {
                runnable.run();
                return true;
            }

            return false;
        }
    }

    public void addServerCallback(int timer, Runnable runnable)
    {
        if(timer <= 0)
        {
            runnable.run();
        }
        else
        {
            PENDING_CALLBACKS.add(new ServerTickCallback(timer, runnable));
        }
    }

    @Override
    public void update()
    {
        if(!TICKABLES.isEmpty())
        {
            TICKABLES.forEach(ITickable::update);
        }

        if(!PENDING_CALLBACKS.isEmpty())
        {
            CALLBACKS.addAll(PENDING_CALLBACKS);
            PENDING_CALLBACKS.clear();
        }

        if(!CALLBACKS.isEmpty())
        {
            for(int i = CALLBACKS.size() - 1; i >= 0; i--)
            {
                if(CALLBACKS.get(i).incAndCheck())
                {
                    CALLBACKS.remove(i);
                }
            }
        }
    }
}
