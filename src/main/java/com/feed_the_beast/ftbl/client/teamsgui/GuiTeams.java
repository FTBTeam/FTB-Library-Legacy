package com.feed_the_beast.ftbl.client.teamsgui;

import com.feed_the_beast.ftbl.lib.gui.GuiLM;

import java.util.List;

/**
 * Created by LatvianModder on 05.02.2017.
 */
public class GuiTeams extends GuiLM
{
    private final List<TeamInst> teams;
    private final List<PlayerInst> players;

    public GuiTeams(List<TeamInst> t, List<PlayerInst> p)
    {
        super(200, 140);
        teams = t;
        players = p;
    }

    @Override
    public void addWidgets()
    {
    }
}