package com.feed_the_beast.ftbl.api.info;

import com.feed_the_beast.ftbl.api.client.gui.widgets.ButtonLM;
import com.feed_the_beast.ftbl.gui.info.ButtonInfoPage;
import com.feed_the_beast.ftbl.gui.info.GuiInfo;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * Created by LatvianModder on 08.08.2016.
 */
public interface IGuiInfoPage extends IInfoPage
{
    @Nonnull
    @Override
    Map<String, ? extends IGuiInfoPage> getPages();

    @Nonnull
    IInfoPageTheme getTheme();

    @Nonnull
    IResourceProvider getResourceProvider();

    @SideOnly(Side.CLIENT)
    void refreshGui(@Nonnull GuiInfo gui);

    @SideOnly(Side.CLIENT)
    ButtonLM createSpecialButton(@Nonnull GuiInfo gui);

    @SideOnly(Side.CLIENT)
    ButtonInfoPage createButton(@Nonnull GuiInfo guiInfo, @Nonnull String key);
}
