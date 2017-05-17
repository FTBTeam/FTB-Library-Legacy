package com.feed_the_beast.ftbl.lib.gui.misc;

import com.feed_the_beast.ftbl.lib.gui.GuiBase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author LatvianModder
 */
public class GuiRadial extends GuiBase
{
    public static class Entry
    {
        private final int lineIndex;
        private String displayName = "";
        private Collection<String> centerText = Collections.emptyList();
        private Collection<String> mouseOverText = Collections.emptyList();
        private Runnable clickAction;

        public Entry(int i)
        {
            lineIndex = i;
        }

        public Entry setName(String s)
        {
            displayName = s;
            return this;
        }

        public Entry setCenterText(Collection<String> list)
        {
            centerText = list;
            return this;
        }

        public Entry setMouseOverText(Collection<String> list)
        {
            mouseOverText = list;
            return this;
        }

        public Entry onClick(Runnable action)
        {
            clickAction = action;
            return this;
        }
    }

    private List<List<Entry>> entries;

    public GuiRadial(int lines)
    {
        super(200, 200);

        entries = new ArrayList<>();

        for(int i = 0; i < lines; i++)
        {
            entries.add(new ArrayList<>());
        }
    }

    public void addButton(Entry entry)
    {
        entries.get(entry.lineIndex).add(entry);
    }

    public Entry addButton(int lineIndex)
    {
        Entry entry = new Entry(lineIndex);
        entries.get(lineIndex).add(entry);
        return entry;
    }

    @Override
    public void addWidgets()
    {
    }
}