package com.feed_the_beast.ftbl.api.info;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by LatvianModder on 09.08.2016.
 */
@SideOnly(Side.CLIENT)
public interface IGuiInfoPageTree
{
    String getID();

    IGuiInfoPage getPage();

    @Nullable
    IGuiInfoPageTree getParent();

    String getFormattedTitle();

    default String getFullID()
    {
        return getParent() == null ? getID() : (getParent().getFullID() + '.' + getID());
    }

    List<? extends IGuiInfoPageTree> getPages();
}