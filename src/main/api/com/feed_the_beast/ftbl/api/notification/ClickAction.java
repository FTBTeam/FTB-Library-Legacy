package com.feed_the_beast.ftbl.api.notification;

import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 23.01.2016.
 */
public final class ClickAction implements IJsonSerializable
{
    private String typeID;
    private JsonElement data;

    public ClickAction()
    {
    }

    public ClickAction(@Nullable String t, @Nullable JsonElement d)
    {
        typeID = t;
        data = (d != null && d.isJsonNull()) ? null : d;
    }

    public static ClickAction from(@Nonnull ClickEvent e)
    {
        JsonPrimitive p = new JsonPrimitive(e.getValue());

        switch(e.getAction())
        {
            case RUN_COMMAND:
                return new ClickAction(ClickActionTypeRegistry.CMD, p);
            case OPEN_FILE:
                return new ClickAction(ClickActionTypeRegistry.FILE, p);
            case SUGGEST_COMMAND:
                return new ClickAction(ClickActionTypeRegistry.SHOW_CMD, p);
            case OPEN_URL:
                return new ClickAction(ClickActionTypeRegistry.URL, p);
            case CHANGE_PAGE:
                return new ClickAction(ClickActionTypeRegistry.CHANGE_PAGE, p);
            default:
                return null;
        }
    }

    @Nonnull
    public JsonElement getData()
    {
        return data == null ? JsonNull.INSTANCE : data;
    }

    @Override
    @Nonnull
    public JsonElement getSerializableElement()
    {
        if(typeID == null)
        {
            return JsonNull.INSTANCE;
        }

        if(data == null)
        {
            return new JsonPrimitive(typeID);
        }

        JsonObject o = new JsonObject();
        o.add("type", new JsonPrimitive(typeID));
        o.add("data", data);
        return o;
    }

    @Override
    public void fromJson(@Nonnull JsonElement e)
    {
        if(e.isJsonNull())
        {
            typeID = null;
            data = null;
        }
        else if(e.isJsonPrimitive())
        {
            typeID = e.getAsString();
            data = null;
        }
        else
        {
            JsonObject o = e.getAsJsonObject();
            typeID = o.get("type").getAsString();
            data = o.get("data");

            if(data != null && data.isJsonNull())
            {
                data = null;
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void onClicked(@Nonnull IMouseButton button)
    {
        if(typeID != null)
        {
            IClickActionType type = ClickActionTypeRegistry.INSTANCE.get(typeID);

            if(type != null)
            {
                type.onClicked(data == null ? JsonNull.INSTANCE : data, button);
            }
        }
    }

    @Override
    public String toString()
    {
        return (data == null) ? typeID : data.toString();
    }
}