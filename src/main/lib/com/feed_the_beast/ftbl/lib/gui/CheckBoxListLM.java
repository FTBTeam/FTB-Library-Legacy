package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.lib.client.ImageProvider;
import com.feed_the_beast.ftbl.lib.client.TexturelessRectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LatvianModder on 24.02.2017.
 */
public class CheckBoxListLM extends ButtonLM
{
    public static final IDrawableObject DEFAULT_SELECTED_ICON = new TexturelessRectangle(0xFFE0E0E0);
    public static final IDrawableObject DEFAULT_BACKGROUND = new TexturelessRectangle(0xFF919191);

    public final boolean radioButtons;
    private final List<CheckBoxEntry> entries;
    public IDrawableObject iconSelected = DEFAULT_SELECTED_ICON, iconDeselected = ImageProvider.NULL, background = DEFAULT_BACKGROUND;

    public static class CheckBoxEntry
    {
        public String name;
        public boolean isSelected = false;
        private CheckBoxListLM checkBoxList;

        public CheckBoxEntry(String n)
        {
            name = n;
        }

        public void onClicked(IGui gui, IMouseButton button, int index)
        {
            select(!isSelected);
            GuiHelper.playClickSound();
        }

        public void addMouseOverText(IGui gui, List<String> list)
        {
        }

        public CheckBoxEntry select(boolean value)
        {
            boolean old = isSelected;
            if(checkBoxList.radioButtons)
            {
                if(value)
                {
                    for(CheckBoxEntry entry : checkBoxList.entries)
                    {
                        entry.isSelected = false;
                    }
                }
                else
                {
                    return this;
                }
            }

            isSelected = value;

            if(old != isSelected)
            {
                onValueChanged();
            }

            return this;
        }

        public void onValueChanged()
        {
        }
    }

    public CheckBoxListLM(int x, int y, boolean radiobutton)
    {
        super(x, y, 10, 2);
        radioButtons = radiobutton;
        entries = new ArrayList<>();
    }

    public void addBox(IGui gui, CheckBoxEntry checkBox)
    {
        checkBox.checkBoxList = this;
        entries.add(checkBox);
        setWidth(Math.max(getWidth(), gui.getFont().getStringWidth(checkBox.name)));
        setHeight(getHeight() + 11);
    }

    public CheckBoxEntry addBox(IGui gui, String name)
    {
        CheckBoxEntry entry = new CheckBoxEntry(name);
        addBox(gui, entry);
        return entry;
    }

    @Override
    public void onClicked(IGui gui, IMouseButton button)
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
    public void addMouseOverText(IGui gui, List<String> list)
    {
    }

    @Override
    public void renderWidget(IGui gui)
    {
        int ax = getAX();
        int ay = getAY();

        for(int i = 0; i < entries.size(); i++)
        {
            CheckBoxEntry entry = entries.get(i);
            int y = ay + i * 11 + 1;
            background.draw(ax, y, 10, 10);
            (entry.isSelected ? iconSelected : iconDeselected).draw(ax + 1, y + 1, 8, 8);
            gui.drawString(entry.name, ax + 12, y + 1);
        }
    }
}