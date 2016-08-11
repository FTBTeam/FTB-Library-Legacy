package com.feed_the_beast.ftbl.api.info.impl;

import com.feed_the_beast.ftbl.api.info.IGuiInfoPage;
import com.feed_the_beast.ftbl.api.info.IGuiInfoPageTree;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by LatvianModder on 09.08.2016.
 */
@SideOnly(Side.CLIENT)
public final class GuiInfoPageTree implements IGuiInfoPageTree
{
    private final String ID, formattedTitle;
    private final IGuiInfoPage page;
    private final IGuiInfoPageTree parent;
    private List<GuiInfoPageTree> pages;

    public GuiInfoPageTree(@Nonnull String id, @Nullable IGuiInfoPageTree pr, @Nonnull IGuiInfoPage p)
    {
        ID = id;
        parent = pr;
        page = p;
        formattedTitle = page.getName() == null ? ID : page.getName().getFormattedText();
    }

    @Override
    public String getID()
    {
        return ID;
    }

    @Override
    public IGuiInfoPage getPage()
    {
        return page;
    }

    @Override
    @Nullable
    public IGuiInfoPageTree getParent()
    {
        return parent;
    }

    @Override
    public String getFormattedTitle()
    {
        return formattedTitle;
    }

    @Override
    public List<? extends IGuiInfoPageTree> getPages()
    {
        if(pages == null)
        {
            pages = new ArrayList<>();

            for(Map.Entry<String, ? extends IGuiInfoPage> entry : page.getPages().entrySet())
            {
                pages.add(new GuiInfoPageTree(entry.getKey(), this, entry.getValue()));
            }
        }

        return pages;
    }

    public int hashCode()
    {
        return ID.hashCode();
    }

    public boolean equals(Object o)
    {
        return o == this || (o != null && o.toString().equals(ID));
    }

    public String toString()
    {
        return ID;
    }

    public JsonObject toJson()
    {
        JsonObject o = new JsonObject();
        o.add("id", new JsonPrimitive(ID));
        o.add("title", new JsonPrimitive(formattedTitle));
        o.add("page", page.getSerializableElement());

        JsonArray o1 = new JsonArray();

        for(GuiInfoPageTree p : pages)
        {
            o1.add(p.toJson());
        }

        o.add("pages", o1);
        return o;
    }
}