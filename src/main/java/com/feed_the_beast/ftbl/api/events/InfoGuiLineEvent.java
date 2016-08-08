package com.feed_the_beast.ftbl.api.events;

import com.feed_the_beast.ftbl.api.info.IGuiInfoPage;
import com.feed_the_beast.ftbl.api.info.IInfoTextLine;
import com.google.gson.JsonObject;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 09.05.2016.
 */
@Cancelable
public class InfoGuiLineEvent extends Event
{
    private final IGuiInfoPage page;
    private final JsonObject json;
    private IInfoTextLine line;

    public InfoGuiLineEvent(@Nonnull IGuiInfoPage p, @Nonnull JsonObject o)
    {
        page = p;
        json = o;
    }

    @Nonnull
    public IGuiInfoPage getPage()
    {
        return page;
    }

    @Nonnull
    public JsonObject getJson()
    {
        return json;
    }

    @Nullable
    public IInfoTextLine getLine()
    {
        return line;
    }

    public void setLine(@Nonnull IInfoTextLine l)
    {
        line = l;
    }
}