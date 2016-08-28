package com.feed_the_beast.ftbl.api.info;

import com.feed_the_beast.ftbl.api.gui.widgets.ButtonLM;
import com.feed_the_beast.ftbl.api.info.impl.ButtonInfoPage;
import com.feed_the_beast.ftbl.gui.GuiInfo;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by LatvianModder on 08.08.2016.
 */
public interface IGuiInfoPage extends IInfoPage
{
    @Override
    @Nullable
    IGuiInfoPage getParent();

    @Override
    List<? extends IGuiInfoPage> getPages();

    IInfoPageTheme getTheme();

    IResourceProvider getResourceProvider();

    @SideOnly(Side.CLIENT)
    void refreshGui(GuiInfo gui);

    @SideOnly(Side.CLIENT)
    ButtonLM createSpecialButton(GuiInfo gui);

    @SideOnly(Side.CLIENT)
    ButtonInfoPage createButton(GuiInfo gui);
}
