package com.feed_the_beast.ftbl.client.teamsgui;

import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.lib.gui.GuiLM;
import com.feed_the_beast.ftbl.lib.gui.PanelLM;
import com.feed_the_beast.ftbl.lib.gui.PanelScrollBar;
import com.feed_the_beast.ftbl.lib.gui.SliderLM;

import java.util.List;

/**
 * Created by LatvianModder on 24.02.2017.
 */
public class GuiSelectTeam extends GuiLM
{
    private List<TeamInst> teams;
    private final PanelLM panelTeams;
    private final SliderLM scrollTeams;

    public GuiSelectTeam(List<TeamInst> t)
    {
        super(200, 200);
        teams = t;

        panelTeams = new PanelLM(0, 20, 180, 180)
        {
            @Override
            public void addWidgets()
            {
            }
        };

        scrollTeams = new PanelScrollBar(180, 0, 20, 180, 10, panelTeams);
    }

    @Override
    public void addWidgets()
    {
        add(panelTeams);
        add(scrollTeams);
    }

    @Override
    public IDrawableObject getIcon(IGui gui)
    {
        return GuiMyTeam.BACKGROUND;
    }
}