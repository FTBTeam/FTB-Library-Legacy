package com.feed_the_beast.ftbl.lib.info;

import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IWidget;
import com.feed_the_beast.ftbl.api.info.IInfoTextLine;
import com.feed_the_beast.ftbl.api.info.IPageIconRenderer;
import com.feed_the_beast.ftbl.api.info.ISpecialInfoButton;
import com.feed_the_beast.ftbl.lib.FinalIDObject;
import com.feed_the_beast.ftbl.lib.RemoveFilter;
import com.feed_the_beast.ftbl.lib.gui.misc.GuiInfo;
import com.feed_the_beast.ftbl.lib.util.LMJsonUtils;
import com.feed_the_beast.ftbl.lib.util.LMMapUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InfoPage extends FinalIDObject
{
    private static final Comparator<Map.Entry<String, InfoPage>> COMPARATOR = (o1, o2) -> o1.getValue().getDisplayName().getUnformattedText().compareToIgnoreCase(o2.getValue().getDisplayName().getUnformattedText());
    private static final RemoveFilter<Map.Entry<String, InfoPage>> CLEANUP_FILTER = entry -> entry.getValue().isEmpty();

    private final List<IInfoTextLine> text;
    private final LinkedHashMap<String, InfoPage> childPages;
    public InfoPage parent = null;
    private ITextComponent title;
    private IPageIconRenderer pageIcon;

    public InfoPage(String id)
    {
        super(id);
        text = new ArrayList<>();
        childPages = new LinkedHashMap<>(0);
    }

    public InfoPage(String id, @Nullable InfoPage p, JsonElement json)
    {
        this(id);
        setParent(p);

        if(!json.isJsonObject())
        {
            return;
        }

        JsonObject o = json.getAsJsonObject();

        if(o.has("N"))
        {
            setTitle(LMJsonUtils.deserializeTextComponent(o.get("N")));
        }

        if(o.has("T"))
        {
            JsonArray a = o.get("T").getAsJsonArray();
            for(int i = 0; i < a.size(); i++)
            {
                text.add(InfoPageHelper.createLine(this, a.get(i)));
            }
        }

        if(o.has("S"))
        {
            JsonObject o1 = o.get("S").getAsJsonObject();

            for(Map.Entry<String, JsonElement> entry : o1.entrySet())
            {
                childPages.put(entry.getKey(), new InfoPage(entry.getKey(), this, entry.getValue()));
            }
        }
    }

    @Nullable
    public InfoPage getParent()
    {
        return parent;
    }

    public String getFullID()
    {
        InfoPage parent = getParent();
        return (parent == null) ? getName() : (parent.getFullID() + '.' + getName());
    }

    public ITextComponent getDisplayName()
    {
        return (title == null) ? new TextComponentString(getName()) : title;
    }

    public void setParent(@Nullable InfoPage c)
    {
        parent = c;
    }

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

    public void addSub(InfoPage c)
    {
        childPages.put(c.getName(), c);
        c.setParent(this);
    }

    public InfoPage getSub(String id)
    {
        InfoPage c = childPages.get(id);
        if(c == null)
        {
            c = new InfoPage(id);
            c.setParent(this);
            childPages.put(c.getName(), c);
        }

        return c;
    }

    public void clear()
    {
        setTitle(null);
        text.clear();
        childPages.clear();
    }

    public void cleanup()
    {
        childPages.values().forEach(InfoPage::cleanup);
        LMMapUtils.removeAll(childPages, CLEANUP_FILTER);
    }

    public boolean isEmpty()
    {
        if(!childPages.isEmpty())
        {
            return false;
        }

        for(IInfoTextLine line : text)
        {
            if(line != null && !line.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    public void sort(boolean tree)
    {
        LMMapUtils.sortMap(childPages, COMPARATOR);

        if(tree)
        {
            childPages.values().forEach(page -> page.sort(true));
        }
    }

    public void copyFrom(InfoPage c)
    {
        for(IInfoTextLine l : c.text)
        {
            text.add(l == null ? null : l.copy(this));
        }

        childPages.forEach((key, value) ->
        {
            InfoPage p1 = new InfoPage(key);
            p1.copyFrom(value);
            addSub(p1);
        });
    }

    public JsonElement toJson()
    {
        JsonObject o = new JsonObject();

        if(title != null)
        {
            o.add("N", LMJsonUtils.serializeTextComponent(title));
        }

        if(!text.isEmpty())
        {
            JsonArray a = new JsonArray();
            for(IInfoTextLine c : text)
            {
                a.add(c == null ? JsonNull.INSTANCE : c.getJson());
            }
            o.add("T", a);
        }

        if(!childPages.isEmpty())
        {
            JsonObject o1 = new JsonObject();
            childPages.forEach((key, value) -> o1.add(key, value.toJson()));
            o.add("S", o1);
        }

        return o;
    }

    @Nullable
    public final ITextComponent getTitle()
    {
        return title;
    }

    public InfoPage setTitle(@Nullable ITextComponent t)
    {
        title = t;
        return this;
    }

    public final List<IInfoTextLine> getText()
    {
        return text;
    }

    public final LinkedHashMap<String, InfoPage> getPages()
    {
        return childPages;
    }

    public void refreshGui(IGui gui)
    {
    }

    @Nullable
    public ISpecialInfoButton createSpecialButton(IGui gui)
    {
        return null;
    }

    public InfoPage setIcon(IPageIconRenderer icon)
    {
        pageIcon = icon;
        return this;
    }

    public IWidget createWidget(IGui gui)
    {
        return new ButtonInfoPage((GuiInfo) gui, this, pageIcon);
    }
}