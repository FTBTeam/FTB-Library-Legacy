package com.feed_the_beast.ftbl.api.info;

import com.feed_the_beast.ftbl.api.info.impl.ButtonInfoPage;
import com.feed_the_beast.ftbl.gui.GuiInfo;

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

    void refreshGui(GuiInfo gui);

    @Nullable
    ISpecialInfoButton createSpecialButton(GuiInfo gui);

    ButtonInfoPage createButton(GuiInfo gui);
}
