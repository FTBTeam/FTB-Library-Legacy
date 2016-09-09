package com.feed_the_beast.ftbl.api.info.impl;

import com.feed_the_beast.ftbl.api.info.IGuiInfoPage;
import com.feed_the_beast.ftbl.api.info.IInfoPageTheme;
import com.feed_the_beast.ftbl.api.info.IInfoTextLine;
import com.feed_the_beast.ftbl.api.info.ISpecialInfoButton;
import com.feed_the_beast.ftbl.gui.GuiInfo;
import com.feed_the_beast.ftbl.net.MessageDisplayInfo;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.latmod.lib.RemoveFilter;
import com.latmod.lib.util.LMJsonUtils;
import com.latmod.lib.util.LMMapUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.util.FakePlayer;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InfoPage implements IGuiInfoPage // GuideFile
{
    private static final Comparator<Map.Entry<String, InfoPage>> COMPARATOR = (o1, o2) -> o1.getValue().getDisplayName().getUnformattedText().compareToIgnoreCase(o2.getValue().getDisplayName().getUnformattedText());
    private static final RemoveFilter<Map.Entry<String, InfoPage>> CLEANUP_FILTER = entry -> entry.getValue().childPages.isEmpty() && InfoPageHelper.getUnformattedText(entry.getValue()).trim().isEmpty();

    private final String ID;
    private final List<IInfoTextLine> text;
    private final LinkedHashMap<String, InfoPage> childPages;
    public InfoPage parent = null;
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

    public InfoPage setTitle(@Nullable ITextComponent c)
    {
        title = c;
        return this;
    }

    @Override
    public String getName()
    {
        return ID;
    }

    @Nullable
    @Override
    public IGuiInfoPage getParent()
    {
        return parent;
    }

    public InfoPage setParent(InfoPage c)
    {
        parent = c;
        return this;
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
        theme = null;
    }

    public void cleanup()
    {
        childPages.values().forEach(InfoPage::cleanup);
        LMMapUtils.removeAll(childPages, CLEANUP_FILTER);
    }

    public void sortAll()
    {
        LMMapUtils.sortMap(childPages, COMPARATOR);

        for(InfoPage c : childPages.values())
        {
            c.sortAll();
        }
    }

    public void copyFrom(InfoPage c)
    {
        for(IInfoTextLine l : c.text)
        {
            text.add(l == null ? null : InfoPageHelper.createLine(this, l.getSerializableElement()));
        }

        for(Map.Entry<String, InfoPage> entry : c.childPages.entrySet())
        {
            InfoPage p1 = new InfoPage(entry.getKey());
            p1.copyFrom(entry.getValue());
            addSub(p1);
        }
    }

    public InfoPage copy()
    {
        InfoPage page = new InfoPage(getName());
        page.fromJson(getSerializableElement());
        return page;
    }

    public InfoPage getParentTop()
    {
        if(parent == null)
        {
            return this;
        }

        return parent.getParentTop();
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

        if(!childPages.isEmpty())
        {
            JsonObject o1 = new JsonObject();
            for(Map.Entry<String, InfoPage> entry : childPages.entrySet())
            {
                o1.add(entry.getKey(), entry.getValue().getSerializableElement());
            }
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
                text.add(InfoPageHelper.createLine(this, a.get(i)));
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
                childPages.put(c.getName(), c);
            }
        }

        if(o.has("C"))
        {
            theme = new InfoPageTheme();
            theme.fromJson(o.get("C"));
        }
    }

    public MessageDisplayInfo displayGuide(EntityPlayerMP ep)
    {
        MessageDisplayInfo m = new MessageDisplayInfo(this);
        if(ep != null && !(ep instanceof FakePlayer))
        {
            m.sendTo(ep);
        }
        return m;
    }

    @Nullable
    @Override
    public ITextComponent getTitle()
    {
        return title;
    }

    @Override
    public final List<IInfoTextLine> getText()
    {
        return text;
    }

    @Override
    public final List<IGuiInfoPage> getPages()
    {
        List<IGuiInfoPage> list = new ArrayList<>(childPages.size());
        list.addAll(childPages.values());
        return list;
    }

    @Override
    public final IInfoPageTheme getTheme()
    {
        return (theme == null) ? ((parent == null) ? InfoPageTheme.DEFAULT : parent.getTheme()) : theme;
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
    public ButtonInfoPage createButton(GuiInfo gui)
    {
        return new ButtonInfoPage(gui, this, null);
    }
}