package com.feed_the_beast.ftbl.api.gui;

import com.feed_the_beast.ftbl.api.EnumSelf;
import com.feed_the_beast.ftbl.api.ForgePlayer;
import com.feed_the_beast.ftbl.api.ResourceLocationObject;
import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.util.FTBLib;
import com.feed_the_beast.ftbl.util.TextureCoords;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public abstract class PlayerAction extends ResourceLocationObject implements Comparable<PlayerAction>
{
    public final EnumSelf type;
    public final int priority;
    public final TextureCoords icon;

    public PlayerAction(EnumSelf t, ResourceLocation id, int p, TextureCoords c)
    {
        super(id);
        type = (t == null) ? EnumSelf.SELF : t;
        priority = p;
        icon = c;
    }

    public abstract void onClicked(ForgePlayer self, ForgePlayer other);

    public String getLangKey()
    {
        return "player_action." + getID();
    }

    public String getDisplayName()
    {
        return I18n.format(getLangKey());
    }

    public void render(int ax, int ay, double z)
    {
        FTBLibClient.setTexture(icon.texture);
        GuiLM.drawTexturedRectD(ax, ay, z, 16, 16, 0D, 0D, 1D, 1D);
    }

    public void postRender(int ax, int ay, double z)
    {
    }

    @Override
    public int compareTo(PlayerAction a)
    {
        int i = Integer.compare(a.priority, priority);
        return (i == 0) ? FTBLib.RESOURCE_LOCATION_COMPARATOR.compare(getResourceLocation(), a.getResourceLocation()) : i;
    }

    public boolean isVisibleFor(ForgePlayer self, ForgePlayer other)
    {
        return true;
    }

    public Boolean configDefault()
    {
        return null;
    }
}