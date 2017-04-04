package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.lib.client.ImageProvider;
import com.feed_the_beast.ftbl.lib.client.TexturelessRectangle;
import net.minecraft.client.renderer.GlStateManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LatvianModder on 24.02.2017.
 */
public class CheckBoxList extends Button
{
    public static final IDrawableObject DEFAULT_SELECTED_ICON = new TexturelessRectangle(0xFFE0E0E0);
    public static final IDrawableObject DEFAULT_BACKGROUND = new TexturelessRectangle(0xFF919191);

    public final boolean radioButtons;
    private final List<CheckBoxEntry> entries;
    public IDrawableObject background = DEFAULT_BACKGROUND;
    public IDrawableObject[] icons = {ImageProvider.NULL, DEFAULT_SELECTED_ICON};

    public static class CheckBoxEntry
    {
        public String name;
        public int value = 0;
        private CheckBoxList checkBoxList;

        public CheckBoxEntry(String n)
        {
            name = n;
        }

        public void onClicked(GuiBase gui, IMouseButton button, int index)
        {
            select((value + 1) % checkBoxList.icons.length);
            GuiHelper.playClickSound();
        }

        public void addMouseOverText(GuiBase gui, List<String> list)
        {
        }

        public CheckBoxEntry select(int v)
        {
            if(checkBoxList.radioButtons)
            {
                if(v > 0)
                {
                    for(CheckBoxEntry entry : checkBoxList.entries)
                    {
                        boolean old1 = entry.value > 0;
                        entry.value = 0;

                        if(old1)
                        {
                            entry.onValueChanged();
                        }
                    }
                }
                else
                {
                    return this;
                }
            }

            int old = value;
            value = v;

            if(old != value)
            {
                onValueChanged();
            }

            return this;
        }

        public void onValueChanged()
        {
        }
    }

    public CheckBoxList(int x, int y, boolean radiobutton)
    {
        super(x, y, 10, 2);
        radioButtons = radiobutton;
        entries = new ArrayList<>();
    }

    public void addBox(GuiBase gui, CheckBoxEntry checkBox)
    {
        checkBox.checkBoxList = this;
        entries.add(checkBox);
        setWidth(Math.max(getWidth(), gui.getFont().getStringWidth(checkBox.name)));
        setHeight(getHeight() + 11);
    }

    public CheckBoxEntry addBox(GuiBase gui, String name)
    {
        CheckBoxEntry entry = new CheckBoxEntry(name);
        addBox(gui, entry);
        return entry;
    }

    @Override
    public void onClicked(GuiBase gui, IMouseButton button)
    {
        int y = gui.getMouseY() - getAY();

        if(y % 11 == 10)
        {
            return;
        }

        int i = y / 11;

        if(i >= 0 && i < entries.size())
        {
            entries.get(i).onClicked(gui, button, i);
        }
    }

    @Override
    public void addMouseOverText(GuiBase gui, List<String> list)
    {
    }

    @Override
    public void renderWidget(GuiBase gui)
    {
        int ax = getAX();
        int ay = getAY();

        for(int i = 0; i < entries.size(); i++)
        {
            CheckBoxEntry entry = entries.get(i);
            int y = ay + i * 11 + 1;
            background.draw(ax, y, 10, 10);
            icons[entry.value].draw(ax + 1, y + 1, 8, 8);
            gui.drawString(entry.name, ax + 12, y + 1);
        }

        GlStateManager.color(1F, 1F, 1F, 1F);
    }
}