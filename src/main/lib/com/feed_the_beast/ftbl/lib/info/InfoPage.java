package com.feed_the_beast.ftbl.lib.info;

import com.feed_the_beast.ftbl.api.gui.IWidget;
import com.feed_the_beast.ftbl.api.info.IGuiInfoPage;
import com.feed_the_beast.ftbl.api.info.IInfoPage;
import com.feed_the_beast.ftbl.api.info.IInfoPageTheme;
import com.feed_the_beast.ftbl.api.info.IInfoTextLine;
import com.feed_the_beast.ftbl.api.info.ISpecialInfoButton;
import com.feed_the_beast.ftbl.lib.RemoveFilter;
import com.feed_the_beast.ftbl.lib.gui.misc.GuiInfo;
import com.feed_the_beast.ftbl.lib.util.LMJsonUtils;
import com.feed_the_beast.ftbl.lib.util.LMMapUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InfoPage implements IGuiInfoPage // GuideFile
{
    private static final Comparator<Map.Entry<String, IGuiInfoPage>> COMPARATOR = (o1, o2) -> o1.getValue().getDisplayName().getUnformattedText().compareToIgnoreCase(o2.getValue().getDisplayName().getUnformattedText());
    private static final RemoveFilter<Map.Entry<String, IGuiInfoPage>> CLEANUP_FILTER = entry -> entry.getValue().getPages().isEmpty() && InfoPageHelper.getUnformattedText(entry.getValue()).trim().isEmpty();

    private final String ID;
    private final List<IInfoTextLine> text;
    private final LinkedHashMap<String, IGuiInfoPage> childPages;
    public IInfoPage parent = null;
    public IInfoPageTheme theme;
    private ITextComponent title;

    public InfoPage(String id)
    {
        ID = id;
        text = new ArrayList<>();
        childPages = new LinkedHashMap<>();
    }

    public int hashCode()
    {
        return ID.hashCode();
    }

    public boolean equals(Object o)
    {
        return o == this || (o instanceof IStringSerializable && ((IStringSerializable) o).getName().equals(getName()));
    }

    @Override
    public String getName()
    {
        return ID;
    }

    @Nullable
    @Override
    public IInfoPage getParent()
    {
        return parent;
    }

    @Override
    public void setParent(@Nullable IInfoPage c)
    {
        parent = c;
    }

    @Override
    public void println(@Nullable Object o)
    {
        if(o == null)
        {
            text.add(null);
        }
        else if(o instanceof IInfoTextLine)
        {
            text.add((IInfoTextLine) o);
        }
        else if(o instanceof ITextComponent)
        {
            ITextComponent c = (ITextComponent) o;

            if(c instanceof TextComponentString && c.getStyle().isEmpty() && c.getSiblings().isEmpty())
            {
                text.add(new InfoTextLineString(((TextComponentString) c).getText()));
            }
            else
            {
                text.add(new InfoExtendedTextLine(c));
            }
        }
        else
        {
            text.add(new InfoTextLineString(String.valueOf(o)));
        }
    }

    @Override
    public void addSub(IGuiInfoPage c)
    {
        getPages().put(c.getName(), c);
        c.setParent(this);
    }

    @Override
    public IGuiInfoPage getSub(String id)
    {
        IGuiInfoPage c = getPages().get(id);
        if(c == null)
        {
            c = new InfoPage(id);
            c.setParent(this);
            getPages().put(c.getName(), c);
        }

        return c;
    }

    @Override
    public void clear()
    {
        setTitle(null);
        getText().clear();
        getPages().clear();
        theme = null;
    }

    @Override
    public void cleanup()
    {
        childPages.values().forEach(IGuiInfoPage::cleanup);
        LMMapUtils.removeAll(getPages(), CLEANUP_FILTER);
    }

    @Override
    public void sortAll()
    {
        LMMapUtils.sortMap(getPages(), COMPARATOR);

        for(IGuiInfoPage c : getPages().values())
        {
            c.sortAll();
        }
    }

    public void copyFrom(IGuiInfoPage c)
    {
        for(IInfoTextLine l : c.getText())
        {
            getText().add(l == null ? null : InfoPageHelper.createLine(this, l.getSerializableElement()));
        }

        getPages().forEach((key, value) ->
        {
            InfoPage p1 = new InfoPage(key);
            p1.copyFrom(value);
            addSub(p1);
        });
    }

    @Override
    public IGuiInfoPage copy()
    {
        InfoPage page = new InfoPage(getName());
        page.fromJson(getSerializableElement());
        return page;
    }

    @Override
    public JsonElement getSerializableElement()
    {
        JsonObject o = new JsonObject();

        if(getTitle() != null)
        {
            o.add("N", LMJsonUtils.serializeTextComponent(getTitle()));
        }

        if(!text.isEmpty())
        {
            JsonArray a = new JsonArray();
            for(IInfoTextLine c : text)
            {
                a.add(c == null ? JsonNull.INSTANCE : c.getSerializableElement());
            }
            o.add("T", a);
        }

        if(!getPages().isEmpty())
        {
            JsonObject o1 = new JsonObject();
            getPages().forEach((key, value) -> o1.add(key, value.getSerializableElement()));
            o.add("S", o1);
        }

        if(theme != null)
        {
            o.add("C", theme.getSerializableElement());
        }

        return o;
    }

    @Override
    public void fromJson(JsonElement e)
    {
        clear();

        if(!e.isJsonObject())
        {
            return;
        }

        JsonObject o = e.getAsJsonObject();

        if(o.has("N"))
        {
            setTitle(LMJsonUtils.deserializeTextComponent(o.get("N")));
        }

        if(o.has("T"))
        {
            JsonArray a = o.get("T").getAsJsonArray();
            for(int i = 0; i < a.size(); i++)
            {
                getText().add(InfoPageHelper.createLine(this, a.get(i)));
            }
        }

        if(o.has("S"))
        {
            JsonObject o1 = o.get("S").getAsJsonObject();

            for(Map.Entry<String, JsonElement> entry : o1.entrySet())
            {
                InfoPage c = new InfoPage(entry.getKey());
                c.setParent(this);
                c.fromJson(entry.getValue());
                getPages().put(c.getName(), c);
            }
        }

        if(o.has("C"))
        {
            theme = new InfoPageTheme();
            theme.fromJson(o.get("C"));
        }
    }

    @Nullable
    @Override
    public ITextComponent getTitle()
    {
        return title;
    }

    @Override
    public IGuiInfoPage setTitle(@Nullable ITextComponent t)
    {
        title = t;
        return this;
    }

    @Override
    public List<IInfoTextLine> getText()
    {
        return text;
    }

    @Override
    public LinkedHashMap<String, IGuiInfoPage> getPages()
    {
        return childPages;
    }

    @Override
    public IInfoPageTheme getTheme()
    {
        return (theme == null) ? ((parent == null) ? InfoPageTheme.DEFAULT : (parent instanceof IGuiInfoPage ? ((IGuiInfoPage) parent).getTheme() : InfoPageTheme.DEFAULT)) : theme;
    }

    @Override
    public void setTheme(@Nullable IInfoPageTheme t)
    {
        theme = t;
    }

    @Override
    public void refreshGui(GuiInfo gui)
    {
    }

    @Override
    public ISpecialInfoButton createSpecialButton(GuiInfo gui)
    {
        return null;
    }

    @Override
    public IWidget createButton(GuiInfo gui)
    {
        return new ButtonInfoPage(gui, this, null);
    }
}