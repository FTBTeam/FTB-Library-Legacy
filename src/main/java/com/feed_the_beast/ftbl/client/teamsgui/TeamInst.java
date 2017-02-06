package com.feed_the_beast.ftbl.client.teamsgui;

import com.feed_the_beast.ftbl.lib.FinalIDObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LatvianModder on 05.02.2017.
 */
public class TeamInst extends FinalIDObject
{
    public final String displayName, description;
    public PlayerInst owner;
    public final List<PlayerInst> members;

    public TeamInst(String n, String dn, String d)
    {
        super(n);
        displayName = dn;
        description = d;
        members = new ArrayList<>();
    }
}