package com.feed_the_beast.ftbl.api.client.gui.guibuttons;

import com.feed_the_beast.ftbl.api.ForgePlayerSP;
import com.feed_the_beast.ftbl.api.ResourceLocationObject;
import com.feed_the_beast.ftbl.api.client.gui.GuiLM;
import com.feed_the_beast.ftbl.util.FTBLib;
import com.feed_the_beast.ftbl.util.TextureCoords;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public abstract class ActionButton extends ResourceLocationObject implements Comparable<ActionButton>
{
    public final int priority;
    public final TextureCoords icon;
    public final Boolean configDefault;

    public ActionButton(ResourceLocation id, int p, TextureCoords c, Boolean b)
    {
        super(id);
        priority = p;
        icon = c;
        configDefault = b;
    }

    public abstract void onClicked(ForgePlayerSP player);

    public abstract String getLangKey();

    public String getDisplayName()
    {
        return I18n.format(getLangKey());
    }

    @Override
    public int compareTo(ActionButton a)
    {
        int i = Integer.compare(a.priority, priority);
        return (i == 0) ? FTBLib.RESOURCE_LOCATION_COMPARATOR.compare(getResourceLocation(), a.getResourceLocation()) : i;
    }

    public boolean isVisibleFor(ForgePlayerSP player)
    {
        return true;
    }

    public void render(int ax, int ay, float z)
    {
        GuiLM.render(icon, ax, ay, z, 16D, 16D);
    }

    public void postRender(int ax, int ay, float z)
    {
    }
}