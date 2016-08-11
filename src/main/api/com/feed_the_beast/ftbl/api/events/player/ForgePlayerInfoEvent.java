package com.feed_the_beast.ftbl.api.events.player;

import com.feed_the_beast.ftbl.api.IForgePlayer;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public class ForgePlayerInfoEvent extends ForgePlayerEvent
{
    private final List<ITextComponent> list;
    private final long currentTime;

    public ForgePlayerInfoEvent(IForgePlayer p, List<ITextComponent> l, long t)
    {
        super(p);
        list = l;
        currentTime = t;
    }

    public List<ITextComponent> getInfo()
    {
        return list;
    }

    public long getCurrentTime()
    {
        return currentTime;
    }
}