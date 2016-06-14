package com.feed_the_beast.ftbl.api.notification;

import com.feed_the_beast.ftbl.api.MouseButton;
import com.feed_the_beast.ftbl.api.client.gui.IClickable;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.text.event.ClickEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 23.01.2016.
 */
public class ClickAction implements IJsonSerializable, IClickable
{
    public ClickActionType type;
    public JsonElement data;

    public ClickAction()
    {
    }

    public ClickAction(@Nonnull ClickActionType t, @Nullable JsonElement d)
    {
        type = t;
        data = d == null ? JsonNull.INSTANCE : d;
    }

    public static ClickAction from(@Nonnull ClickEvent e)
    {
        JsonPrimitive p = new JsonPrimitive(e.getValue());

        switch(e.getAction())
        {
            case RUN_COMMAND:
                return new ClickAction(ClickActionType.CMD, p);
            case OPEN_FILE:
                return new ClickAction(ClickActionType.FILE, p);
            case SUGGEST_COMMAND:
                return new ClickAction(ClickActionType.SHOW_CMD, p);
            case OPEN_URL:
                return new ClickAction(ClickActionType.URL, p);
            case CHANGE_PAGE:
                return new ClickAction(ClickActionType.CHANGE_INFO_PAGE, p);
            default:
                return null;
        }
    }

    @Override
    @Nonnull
    public JsonElement getSerializableElement()
    {
        if(type == null)
        {
            return JsonNull.INSTANCE;
        }

        if(data == null || data.isJsonNull())
        {
            return new JsonPrimitive(type.getID());
        }

        JsonObject o = new JsonObject();
        o.add("type", new JsonPrimitive(type.getID()));
        o.add("data", data);
        return o;
    }

    @Override
    public void fromJson(@Nonnull JsonElement e)
    {
        if(e.isJsonNull())
        {
            type = null;
            data = null;
        }
        else if(e.isJsonPrimitive())
        {
            type = ClickActionRegistry.get(e.getAsString());
            data = null;
        }
        else
        {
            JsonObject o = e.getAsJsonObject();
            type = ClickActionRegistry.get(o.get("type").getAsString());
            data = o.get("data");

            if(data == JsonNull.INSTANCE)
            {
                data = null;
            }
        }
    }

    @Override
    public void onClicked(@Nonnull MouseButton button)
    {
        if(type != null)
        {
            type.onClicked(data == null ? JsonNull.INSTANCE : data, button);
        }
    }

    @Override
    public String toString()
    {
        return (data == null) ? type.getID() : data.toString();
    }
}