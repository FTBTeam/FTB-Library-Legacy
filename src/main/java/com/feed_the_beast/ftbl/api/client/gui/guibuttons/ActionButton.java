package com.feed_the_beast.ftbl.api.client.gui.guibuttons;

import com.feed_the_beast.ftbl.api.ForgePlayerSP;
import com.feed_the_beast.ftbl.api.ResourceLocationObject;
import com.feed_the_beast.ftbl.api.client.gui.GuiLM;
import com.feed_the_beast.ftbl.util.FTBLib;
import com.feed_the_beast.ftbl.util.TextureCoords;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public abstract class ActionButton extends ResourceLocationObject implements Comparable<ActionButton>
{
    public final int priority;
    public final TextureCoords icon;
    public final Boolean configDefault;
    public final ITextComponent displayName;

    public ActionButton(ResourceLocation id, int p, TextureCoords c, Boolean b)
    {
        super(id);
        priority = p;
        icon = c;
        configDefault = b;
        displayName = getDisplayName();
    }

    protected abstract ITextComponent getDisplayName();

    public abstract void onClicked(ForgePlayerSP player);

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

    public void render(double ax, double ay)
    {
        GuiLM.render(icon, ax, ay, 16D, 16D);
    }

    public void postRender(double ax, double ay)
    {
    }
}