package com.feed_the_beast.ftbl.lib.info;

import com.feed_the_beast.ftbl.api.gui.IWidget;
import com.feed_the_beast.ftbl.api.info.IInfoTextLine;
import com.feed_the_beast.ftbl.api.info.IPageIconRenderer;
import com.feed_the_beast.ftbl.api.info.ISpecialInfoButton;
import com.feed_the_beast.ftbl.lib.RemoveFilter;
import com.feed_the_beast.ftbl.lib.gui.misc.GuiInfo;
import com.feed_the_beast.ftbl.lib.util.LMJsonUtils;
import com.feed_the_beast.ftbl.lib.util.LMMapUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InfoPage implements IJsonSerializable, IStringSerializable // GuideFile
{
    private static final Comparator<Map.Entry<String, InfoPage>> COMPARATOR = (o1, o2) -> o1.getValue().getDisplayName().getUnformattedText().compareToIgnoreCase(o2.getValue().getDisplayName().getUnformattedText());
    private static final RemoveFilter<Map.Entry<String, InfoPage>> CLEANUP_FILTER = entry -> entry.getValue().getPages().isEmpty() && InfoPageHelper.getUnformattedText(entry.getValue()).trim().isEmpty();

    private final String ID;
    private final List<IInfoTextLine> text;
    private final LinkedHashMap<String, InfoPage> childPages;
    public InfoPage parent = null;
    public InfoPageTheme theme;
    private ITextComponent title;
    private IPageIconRenderer pageIcon;

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
        ITextComponent t = getTitle();
        return (t == null) ? new TextComponentString(getName()) : t;
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
        getPages().put(c.getName(), c);
        c.setParent(this);
    }

    public InfoPage getSub(String id)
    {
        InfoPage c = getPages().get(id);
        if(c == null)
        {
            c = new InfoPage(id);
            c.setParent(this);
            getPages().put(c.getName(), c);
        }

        return c;
    }

    public void clear()
    {
        setTitle(null);
        getText().clear();
        getPages().clear();
        theme = null;
    }

    public void cleanup()
    {
        childPages.values().forEach(InfoPage::cleanup);
        LMMapUtils.removeAll(getPages(), CLEANUP_FILTER);
    }

    public void sortAll()
    {
        LMMapUtils.sortMap(getPages(), COMPARATOR);
        getPages().values().forEach(InfoPage::sortAll);
    }

    public void copyFrom(InfoPage c)
    {
        for(IInfoTextLine l : c.getText())
        {
            getText().add(l == null ? null : l.copy(this));
        }

        getPages().forEach((key, value) ->
        {
            InfoPage p1 = new InfoPage(key);
            p1.copyFrom(value);
            addSub(p1);
        });
    }

    public InfoPage copy()
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
                a.add(c == null ? JsonNull.INSTANCE : c.getJson());
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
                IInfoTextLine line = InfoPageHelper.createLine(this, a.get(i));

                if(line != null)
                {
                    getText().add(line);
                }
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
    public ITextComponent getTitle()
    {
        return title;
    }

    public InfoPage setTitle(@Nullable ITextComponent t)
    {
        title = t;
        return this;
    }

    public List<IInfoTextLine> getText()
    {
        return text;
    }

    public LinkedHashMap<String, InfoPage> getPages()
    {
        return childPages;
    }

    public InfoPageTheme getTheme()
    {
        return (theme == null) ? ((parent == null) ? InfoPageTheme.DEFAULT : parent.getTheme()) : theme;
    }

    public void setTheme(@Nullable InfoPageTheme t)
    {
        theme = t;
    }

    public void refreshGui(GuiInfo gui)
    {
    }

    @Nullable
    public ISpecialInfoButton createSpecialButton(GuiInfo gui)
    {
        return null;
    }

    public InfoPage setIcon(IPageIconRenderer icon)
    {
        pageIcon = icon;
        return this;
    }

    public IWidget createWidget(GuiInfo gui)
    {
        return new ButtonInfoPage(gui, this, pageIcon);
    }
}