package com.feed_the_beast.ftbl.api.info;

import com.feed_the_beast.ftbl.api.client.gui.widgets.ButtonLM;
import com.feed_the_beast.ftbl.api.events.InfoGuiLineEvent;
import com.feed_the_beast.ftbl.gui.info.ButtonInfoPage;
import com.feed_the_beast.ftbl.gui.info.GuiInfo;
import com.feed_the_beast.ftbl.net.MessageDisplayInfo;
import com.feed_the_beast.ftbl.util.JsonHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.latmod.lib.RemoveFilter;
import com.latmod.lib.json.LMJsonUtils;
import com.latmod.lib.util.LMMapUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InfoPage implements IJsonSerializable // GuideFile
{
    public static final Comparator<Map.Entry<String, InfoPage>> COMPARATOR = (o1, o2) -> o1.getValue().getTitleComponent(o1.getKey()).getFormattedText().compareToIgnoreCase(o2.getValue().getTitleComponent(o2.getKey()).getFormattedText());
    private static final RemoveFilter<Map.Entry<String, InfoPage>> CLEANUP_FILTER = entry -> entry.getValue().childPages.isEmpty() && entry.getValue().getUnformattedText().trim().isEmpty();
    public final List<InfoTextLine> text;
    public final LinkedHashMap<String, InfoPage> childPages;
    public InfoPage parent = null;
    public InfoPageTheme theme;
    public IResourceProvider resourceProvider;
    private ITextComponent title;

    public InfoPage()
    {
        text = new ArrayList<>();
        childPages = new LinkedHashMap<>();
    }

    public InfoPage setTitle(ITextComponent c)
    {
        title = c;
        return this;
    }

    public InfoPage setParent(InfoPage c)
    {
        parent = c;
        return this;
    }

    public InfoPage getOwner()
    {
        if(parent == null)
        {
            return this;
        }
        return parent.getOwner();
    }

    public InfoTextLine createLine(JsonElement e)
    {
        if(e == null || e.isJsonNull())
        {
            return null;
        }
        else if(e.isJsonPrimitive())
        {
            String s = e.getAsString();
            return s.trim().isEmpty() ? null : new InfoTextLine(this, s);
        }
        else
        {
            JsonObject o = e.getAsJsonObject();

            InfoExtendedTextLine l;

            if(o.has("image"))
            {
                l = new InfoImageLine(this);
            }
            else
            {
                InfoGuiLineEvent event = new InfoGuiLineEvent(this, o);
                MinecraftForge.EVENT_BUS.post(event);
                l = (event.line == null) ? new InfoExtendedTextLine(this, null) : event.line;
            }

            l.fromJson(o);
            return l;
        }
    }

    public void println(ITextComponent c)
    {
        if(c == null)
        {
            text.add(null);
        }
        else if(c instanceof TextComponentString && c.getStyle().isEmpty() && c.getSiblings().isEmpty())
        {
            printlnText(((TextComponentString) c).getText());
        }
        else
        {
            text.add(new InfoExtendedTextLine(this, c));
        }
    }

    public void printlnText(String s)
    {
        text.add((s == null || s.isEmpty()) ? null : new InfoTextLine(this, s));
    }

    public String getUnformattedText()
    {
        if(text.isEmpty())
        {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int s = text.size();
        for(int i = 0; i < s; i++)
        {
            InfoTextLine c = text.get(i);

            if(c == null || c.getText() == null)
            {
                sb.append('\n');
            }
            else
            {
                try
                {
                    sb.append(c.getText().getUnformattedText());
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
            }

            if(i != s - 1)
            {
                sb.append('\n');
            }
        }
        return sb.toString();
    }

    public void addSub(String id, InfoPage c)
    {
        childPages.put(id, c);
        c.setParent(this);
    }

    public ITextComponent getTitleComponent(String id)
    {
        return title == null ? new TextComponentString(id) : title;
    }

    public InfoPage getSub(String id)
    {
        InfoPage c = childPages.get(id);
        if(c == null)
        {
            c = new InfoPage();
            c.setParent(this);
            childPages.put(id, c);
        }

        return c;
    }

    public void clear()
    {
        text.clear();
        childPages.clear();
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
        for(InfoTextLine l : c.text)
        {
            text.add(l == null ? null : l.copy(this));
        }

        for(Map.Entry<String, InfoPage> entry : c.childPages.entrySet())
        {
            InfoPage p1 = new InfoPage();
            p1.copyFrom(entry.getValue());
            addSub(entry.getKey(), p1);
        }
    }

    public InfoPage copy()
    {
        InfoPage page = new InfoPage();
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

    @Nonnull
    @Override
    public JsonElement getSerializableElement()
    {
        JsonObject o = new JsonObject();

        if(title != null)
        {
            o.add("N", JsonHelper.serializeICC(title));
        }

        if(!text.isEmpty())
        {
            JsonArray a = new JsonArray();
            for(InfoTextLine c : text)
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
    public void fromJson(@Nonnull JsonElement e)
    {
        clear();

        if(e == null || !e.isJsonObject())
        {
            return;
        }
        JsonObject o = e.getAsJsonObject();

        title = o.has("N") ? JsonHelper.deserializeICC(o.get("N")) : null;

        if(o.has("T"))
        {
            JsonArray a = o.get("T").getAsJsonArray();
            for(int i = 0; i < a.size(); i++)
            {
                text.add(createLine(a.get(i)));
            }
        }

        if(o.has("S"))
        {
            JsonObject o1 = o.get("S").getAsJsonObject();

            for(Map.Entry<String, JsonElement> entry : o1.entrySet())
            {
                InfoPage c = new InfoPage();
                c.setParent(this);
                c.fromJson(entry.getValue());
                childPages.put(entry.getKey(), c);
            }
        }

        if(o.has("C"))
        {
            theme = new InfoPageTheme();
            theme.fromJson(o.get("C"));
        }
        else
        {
            theme = null;
        }
    }

    public MessageDisplayInfo displayGuide(EntityPlayerMP ep, String id)
    {
        MessageDisplayInfo m = new MessageDisplayInfo(id, this);
        if(ep != null && !(ep instanceof FakePlayer))
        {
            m.sendTo(ep);
        }
        return m;
    }

    public final InfoPageTheme getTheme()
    {
        return (theme == null) ? ((parent == null) ? InfoPageTheme.DEFAULT : parent.getTheme()) : theme;
    }

    public final IResourceProvider getResourceProvider()
    {
        return (resourceProvider == null) ? ((parent == null) ? URLResourceProvider.INSTANCE : parent.getResourceProvider()) : resourceProvider;
    }

    @SideOnly(Side.CLIENT)
    public final void refreshGuiTree(GuiInfo gui)
    {
        refreshGui(gui);
        for(InfoPage p : childPages.values())
        {
            p.refreshGuiTree(gui);
        }
    }

    @SideOnly(Side.CLIENT)
    public void refreshGui(GuiInfo gui)
    {
    }

    @SideOnly(Side.CLIENT)
    public ButtonLM createSpecialButton(GuiInfo gui)
    {
        return null;
    }

    @SideOnly(Side.CLIENT)
    public ButtonInfoPage createButton(GuiInfo gui, String id)
    {
        return new ButtonInfoPage(gui, id, this, null);
    }

    public void loadText(List<String> list) throws Exception
    {
        for(JsonElement e : LMJsonUtils.deserializeText(list))
        {
            text.add(createLine(e));
        }
    }
}