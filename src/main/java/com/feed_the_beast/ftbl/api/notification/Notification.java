package com.feed_the_beast.ftbl.api.notification;

import com.feed_the_beast.ftbl.api.ForgePlayerMP;
import com.feed_the_beast.ftbl.api.ForgeWorldMP;
import com.feed_the_beast.ftbl.net.MessageNotifyPlayer;
import com.feed_the_beast.ftbl.util.EnumNotificationDisplay;
import com.feed_the_beast.ftbl.util.FTBLib;
import com.feed_the_beast.ftbl.util.JsonHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import latmod.lib.FinalIDObject;
import latmod.lib.util.LMColorUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public final class Notification extends FinalIDObject implements IJsonSerializable
{
    public final List<ITextComponent> text;
    private ItemStack item;
    private ClickAction clickAction;
    private int timer, color;

    public Notification(@Nonnull String s)
    {
        super(s);
        text = new ArrayList<>();
    }

    public static Notification error(@Nonnull String id, @Nonnull ITextComponent title)
    {
        title.getStyle().setColor(TextFormatting.WHITE);
        return new Notification(id).addText(title).setTimer(3000).setColor(0xFF5959);
    }

    public static Notification deserialize(JsonElement e)
    {
        if(e != null && e.isJsonObject())
        {
            JsonObject o = e.getAsJsonObject();

            if(o.has("id"))
            {
                Notification n = new Notification(o.get("id").getAsString());
                n.fromJson(o);
                return n;
            }
        }

        return null;
    }

    private void setDefaults()
    {
        text.clear();
        timer = 3000;
        color = 0xA0A0A0;
        item = null;
        clickAction = null;
    }

    public Notification addText(ITextComponent t)
    {
        text.add(t);
        return this;
    }

    public ItemStack getItem()
    {
        return item;
    }

    public Notification setItem(ItemStack is)
    {
        item = is;
        return this;
    }

    public boolean hasItem()
    {
        return item != null;
    }

    public ClickAction getClickAction()
    {
        return clickAction;
    }

    public Notification setClickAction(ClickAction e)
    {
        clickAction = e;
        return this;
    }

    public boolean isTemp()
    {
        return clickAction == null;
    }

    public int getTimer()
    {
        return timer;
    }

    public Notification setTimer(int t)
    {
        timer = t;
        return this;
    }

    public int getColor()
    {
        return color;
    }

    public Notification setColor(int c)
    {
        color = 0x00FFFFFF & c;
        return this;
    }

    @Nonnull
    @Override
    public JsonElement getSerializableElement()
    {
        JsonObject o = new JsonObject();
        o.add("id", new JsonPrimitive(getID()));

        JsonArray a = new JsonArray();

        for(ITextComponent t : text)
        {
            a.add(JsonHelper.serializeICC(t));
        }

        o.add("text", a);

        if(timer != 3000)
        {
            o.add("timer", new JsonPrimitive(timer));
        }

        if(item != null)
        {
            o.add("item", new JsonPrimitive(item.getItem().getRegistryName().toString() + ' ' + item.stackSize + ' ' + item.getMetadata()));
        }

        if(color != 0xA0A0A0)
        {
            o.add("color", LMColorUtils.serialize(color));
        }

        if(clickAction != null)
        {
            o.add("click", clickAction.getSerializableElement());
        }

        return o;
    }

    @Override
    public void fromJson(@Nonnull JsonElement e)
    {
        if(e.isJsonObject())
        {
            setDefaults();
            JsonObject o = e.getAsJsonObject();

            if(o.has("text"))
            {
                for(JsonElement e1 : o.get("text").getAsJsonArray())
                {
                    text.add(JsonHelper.deserializeICC(e1));
                }
            }

            timer = o.has("timer") ? o.get("timer").getAsInt() : 3000;

            if(o.has("color"))
            {
                setColor(LMColorUtils.deserialize(o.get("color")));
            }

            if(o.has("item"))
            {
                if(o.get("item").isJsonPrimitive())
                {
                    String[] s = o.get("item").getAsString().split(" ");
                    if(s.length > 0)
                    {
                        Item item = Item.REGISTRY.getObject(new ResourceLocation(s[0]));

                        if(item != null)
                        {
                            int size = 1;
                            int dmg = 0;

                            if(s.length > 1)
                            {
                                size = Integer.parseInt(s[1]);
                            }

                            if(s.length > 2)
                            {
                                dmg = Integer.parseInt(s[2]);
                            }

                            setItem(new ItemStack(item, size, dmg));
                        }
                    }
                }
            }

            if(o.has("click"))
            {
                clickAction = new ClickAction();
                clickAction.fromJson(o.get("click"));
            }
        }
    }

    @Nonnull
    @Override
    public String toString()
    {
        return getSerializableElement().toString();
    }

    public void sendTo(@Nullable EntityPlayerMP ep)
    {
        if(ep == null)
        {
            if(FTBLib.hasOnlinePlayers())
            {
                for(EntityPlayerMP ep1 : FTBLib.getServer().getPlayerList().getPlayerList())
                {
                    sendTo(ep1);
                }
            }
        }
        else
        {
            ForgePlayerMP p = ForgeWorldMP.inst.getPlayer(ep);

            if(p != null)
            {
                EnumNotificationDisplay e = p.notifications.get();

                if(e != EnumNotificationDisplay.OFF)
                {
                    new MessageNotifyPlayer(this, e).sendTo(ep);
                }
            }
        }
    }
}