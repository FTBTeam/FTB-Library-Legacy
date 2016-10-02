package com.feed_the_beast.ftbl.api.info;

import com.feed_the_beast.ftbl.api.gui.IWidget;
import com.feed_the_beast.ftbl.gui.GuiInfo;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Created by LatvianModder on 08.08.2016.
 */
public interface IGuiInfoPage extends IInfoPage
{
    @Override
    IGuiInfoPage setTitle(@Nullable ITextComponent title);

    @Override
    Map<String, IGuiInfoPage> getPages();

    void addSub(IGuiInfoPage c);

    @Override
    IGuiInfoPage getSub(String id);

    IInfoPageTheme getTheme();

    void refreshGui(GuiInfo gui);

    @Nullable
    ISpecialInfoButton createSpecialButton(GuiInfo gui);

    IWidget createButton(GuiInfo gui);

    void setTheme(@Nullable IInfoPageTheme theme);
}
