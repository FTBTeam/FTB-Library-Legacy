package com.feed_the_beast.ftbl.lib.info;

import com.feed_the_beast.ftbl.api.gui.IClickable;
import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.api.info.ISpecialInfoButton;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import net.minecraft.util.text.ITextComponent;

/**
 * Created by LatvianModder on 02.09.2016.
 */
public class SpecialInfoButton implements ISpecialInfoButton
{
    public final ITextComponent title;
    public final IDrawableObject icon;
    public final IClickable clickAction;

    public SpecialInfoButton(ITextComponent s, IDrawableObject icn, IClickable c)
    {
        title = s;
        icon = icn;
        clickAction = c;
    }

    @Override
    public String getTitle(GuiBase gui)
    {
        return title.getFormattedText();
    }

    @Override
    public void draw(GuiBase gui, int x, int y, int w, int h)
    {
        icon.draw(x, y, w, h, Color4I.NONE);
    }

    @Override
    public void onClicked(GuiBase gui, IMouseButton button)
    {
        clickAction.onClicked(button);
    }
}