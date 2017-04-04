package com.feed_the_beast.ftbl.lib.info;

import com.feed_the_beast.ftbl.api.info.IInfoTextLine;
import com.feed_the_beast.ftbl.lib.client.DrawableItemList;
import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import com.feed_the_beast.ftbl.lib.gui.ItemListButton;
import com.feed_the_beast.ftbl.lib.gui.Panel;
import com.feed_the_beast.ftbl.lib.gui.Widget;
import com.feed_the_beast.ftbl.lib.item.ItemStackSerializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by LatvianModder on 25.01.2017.
 */
public class ItemListLine extends EmptyInfoPageLine
{
    private final List<ItemStack> list;
    private final int cols;

    public ItemListLine(Collection<ItemStack> l, int columns)
    {
        list = new ArrayList<>(l);
        cols = MathHelper.clamp(columns, 0, 16);
    }

    public ItemListLine(JsonElement json)
    {
        list = new ArrayList<>();

        if(json.isJsonObject())
        {
            JsonObject o = json.getAsJsonObject();
            cols = MathHelper.clamp(o.has("columns") ? o.get("columns").getAsInt() : 8, 0, 16);

            if(o.has("items"))
            {
                for(JsonElement e : o.get("items").getAsJsonArray())
                {
                    list.add(ItemStackSerializer.deserialize(e));
                }
            }
        }
        else
        {
            cols = 8;

            for(JsonElement e : json.getAsJsonArray())
            {
                list.add(ItemStackSerializer.deserialize(e));
            }
        }
    }

    @Override
    public JsonElement getJson()
    {
        JsonObject o = new JsonObject();
        o.add("id", new JsonPrimitive("item_list"));
        return o;
    }

    @Override
    public Widget createWidget(GuiBase gui, Panel parent)
    {
        return new ItemListButton(0, 0, new DrawableItemList(list), cols);
    }

    @Override
    public IInfoTextLine copy(InfoPage page)
    {
        return new ItemListLine(list, cols);
    }

    @Override
    public boolean isEmpty()
    {
        return list.isEmpty();
    }
}