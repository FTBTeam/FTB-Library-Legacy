package com.feed_the_beast.ftbl.lib.guide;

import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.api.guide.IGuideTextLine;
import com.feed_the_beast.ftbl.api.guide.IGuideTextLineProvider;
import com.feed_the_beast.ftbl.api.guide.SpecialGuideButton;
import com.feed_the_beast.ftbl.lib.FinalIDObject;
import com.feed_the_beast.ftbl.lib.RemoveFilter;
import com.feed_the_beast.ftbl.lib.client.ImageProvider;
import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import com.feed_the_beast.ftbl.lib.gui.Widget;
import com.feed_the_beast.ftbl.lib.gui.misc.GuiGuide;
import com.feed_the_beast.ftbl.lib.util.JsonUtils;
import com.feed_the_beast.ftbl.lib.util.MapUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GuidePage extends FinalIDObject
{
    private static final Comparator<Map.Entry<String, GuidePage>> COMPARATOR = (o1, o2) -> o1.getValue().getDisplayName().getUnformattedText().compareToIgnoreCase(o2.getValue().getDisplayName().getUnformattedText());
    private static final RemoveFilter<Map.Entry<String, GuidePage>> CLEANUP_FILTER = entry -> entry.getValue().isEmpty();
    public static final Map<String, IGuideTextLineProvider> LINE_PROVIDERS = new HashMap<>();

    public final List<IGuideTextLine> text;
    public final LinkedHashMap<String, GuidePage> childPages;
    public GuidePage parent;
    private ITextComponent title;
    private IDrawableObject pageIcon;
    public final List<SpecialGuideButton> specialButtons;

    public GuidePage(String id)
    {
        super(id);
        text = new ArrayList<>();
        childPages = new LinkedHashMap<>(0);
        pageIcon = ImageProvider.NULL;
        specialButtons = new ArrayList<>();
    }

    public GuidePage(String id, @Nullable GuidePage p, JsonElement json)
    {
        this(id);
        parent = p;

        if(!json.isJsonObject())
        {
            return;
        }

        JsonObject o = json.getAsJsonObject();

        if(o.has("title"))
        {
            setTitle(JsonUtils.deserializeTextComponent(o.get("title")));
        }
        if(o.has("text"))
        {
            JsonArray a = o.get("text").getAsJsonArray();
            for(int i = 0; i < a.size(); i++)
            {
                text.add(createLine(a.get(i)));
            }
        }
        if(o.has("pages"))
        {
            JsonObject o1 = o.get("pages").getAsJsonObject();

            for(Map.Entry<String, JsonElement> entry : o1.entrySet())
            {
                childPages.put(entry.getKey(), new GuidePage(entry.getKey(), this, entry.getValue()));
            }
        }
        if(o.has("icon"))
        {
            pageIcon = ImageProvider.get(o.get("icon"));
        }
        if(o.has("buttons"))
        {
            for(JsonElement e : o.get("buttons").getAsJsonArray())
            {
                specialButtons.add(new SpecialGuideButton(e.getAsJsonObject()));
            }
        }
    }

    public String getFullID()
    {
        return (parent == null) ? getName() : (parent.getFullID() + '.' + getName());
    }

    public ITextComponent getDisplayName()
    {
        return (title == null) ? new TextComponentString(getName()) : title;
    }

    public void println(@Nullable Object o)
    {
        if(o == null)
        {
            text.add(null);
        }
        else if(o instanceof IGuideTextLine)
        {
            if(o instanceof GuideTextLineString)
            {
                String text = ((GuideTextLineString) o).getUnformattedText();
                if(text.isEmpty())
                {
                    println(null);
                    return;
                }
                else if(text.startsWith("# "))
                {
                    ITextComponent component = new TextComponentString(text.substring(2));
                    component.getStyle().setBold(true);
                    component.getStyle().setUnderlined(true);
                    println(component);
                    return;
                }
                else if(text.startsWith("## "))
                {
                    ITextComponent component = new TextComponentString(text.substring(3));
                    component.getStyle().setBold(true);
                    println(component);
                    return;
                }
            }

            text.add((IGuideTextLine) o);
        }
        else if(o instanceof ITextComponent)
        {
            ITextComponent c = (ITextComponent) o;

            if(c instanceof TextComponentString && c.getStyle().isEmpty() && c.getSiblings().isEmpty())
            {
                text.add(new GuideTextLineString(((TextComponentString) c).getText()));
            }
            else
            {
                text.add(new GuideExtendedTextLine(c));
            }
        }
        else if(o instanceof GuidePage)
        {
            copyFrom((GuidePage) o);
        }
        else
        {
            text.add(new GuideTextLineString(String.valueOf(o)));
        }
    }

    public GuidePage addSub(GuidePage c)
    {
        c.parent = this;
        childPages.put(c.getName(), c);
        return c;
    }

    public GuidePage getSub(String id)
    {
        GuidePage p = childPages.get(id);

        if(p == null)
        {
            p = addSub(new GuidePage(id));
        }

        return p;
    }

    @Nullable
    public GuidePage getSubRaw(String id)
    {
        int i = id.indexOf('.');

        if(i >= 0)
        {
            GuidePage page = childPages.get(id.substring(0, i));
            return page == null ? null : page.getSubRaw(id.substring(i + 1));
        }
        else
        {
            return childPages.get(id);
        }
    }

    public void clear()
    {
        text.clear();
        childPages.clear();
    }

    public void cleanup()
    {
        childPages.values().forEach(GuidePage::cleanup);
        MapUtils.removeAll(childPages, CLEANUP_FILTER);
    }

    public boolean isEmpty()
    {
        if(!childPages.isEmpty())
        {
            return false;
        }

        for(IGuideTextLine line : text)
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
        MapUtils.sortMap(childPages, COMPARATOR);

        if(tree)
        {
            childPages.values().forEach(page -> page.sort(true));
        }
    }

    public void copyFrom(GuidePage c)
    {
        for(IGuideTextLine l : c.text)
        {
            text.add(l == null ? null : l.copy(this));
        }

        c.childPages.forEach((key, value) -> getSub(key).copyFrom(value));
    }

    public JsonElement toJson()
    {
        JsonObject o = new JsonObject();

        if(title != null)
        {
            o.add("title", JsonUtils.serializeTextComponent(title));
        }
        if(!text.isEmpty())
        {
            JsonArray a = new JsonArray();
            for(IGuideTextLine c : text)
            {
                a.add(c == null ? JsonNull.INSTANCE : c.getJson());
            }
            o.add("text", a);
        }
        if(!childPages.isEmpty())
        {
            JsonObject o1 = new JsonObject();
            childPages.forEach((key, value) -> o1.add(key, value.toJson()));
            o.add("pages", o1);
        }
        if(pageIcon != ImageProvider.NULL)
        {
            o.add("icon", pageIcon.getJson());
        }
        if(!specialButtons.isEmpty())
        {
            JsonArray a = new JsonArray();

            for(SpecialGuideButton button : specialButtons)
            {
                a.add(button.serialize());
            }

            o.add("buttons", a);
        }

        return o;
    }

    @Nullable
    public final ITextComponent getTitle()
    {
        return title;
    }

    public GuidePage setTitle(@Nullable ITextComponent t)
    {
        title = t;
        return this;
    }

    public void refreshGui(GuiBase gui)
    {
    }

    public final GuidePage addSpecialButton(SpecialGuideButton button)
    {
        specialButtons.add(button);
        return this;
    }

    public GuidePage setIcon(IDrawableObject icon)
    {
        pageIcon = icon;
        return this;
    }

    public IDrawableObject getIcon()
    {
        return pageIcon;
    }

    public Widget createWidget(GuiBase gui)
    {
        return new ButtonGuidePage((GuiGuide) gui, this, false);
    }

    public Side getSide()
    {
        return parent == null ? Side.CLIENT : parent.getSide();
    }

    @Nullable
    public IGuideTextLine createLine(@Nullable JsonElement json)
    {
        if(json == null || json.isJsonNull())
        {
            return null;
        }
        else if(json.isJsonPrimitive())
        {
            String s = json.getAsString();
            return s.trim().isEmpty() ? null : new GuideTextLineString(s);
        }
        else if(json.isJsonArray())
        {
            return new GuideExtendedTextLine(json);
        }
        else
        {
            JsonObject o = json.getAsJsonObject();
            IGuideTextLineProvider provider = null;

            if(o.has("id"))
            {
                String id = o.get("id").getAsString();
                provider = LINE_PROVIDERS.get(id);

                if(provider == null)
                {
                    ITextComponent component = new TextComponentString("Unknown ID: " + id);
                    component.getStyle().setColor(TextFormatting.DARK_RED);
                    component.getStyle().setBold(true);
                    return new GuideExtendedTextLine(component);
                }
            }
            /*
            else
            {
                provider = null;

                for(Map.Entry<String, JsonElement> entry : o.entrySet())
                {
                    provider = INFO_TEXT_LINE_PROVIDERS.get(entry.getKey());

                    if(provider != null)
                    {
                        break;
                    }
                }
            }*/

            IGuideTextLine line;

            if(provider != null)
            {
                line = provider.create(this, json);
            }
            else
            {
                line = new GuideExtendedTextLine(json);
            }

            return line;
        }
    }
}