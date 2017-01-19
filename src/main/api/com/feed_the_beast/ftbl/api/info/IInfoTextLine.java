package com.feed_the_beast.ftbl.api.info;

import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IPanel;
import com.feed_the_beast.ftbl.api.gui.IWidget;
import com.feed_the_beast.ftbl.lib.info.InfoPage;
import com.google.gson.JsonElement;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 08.08.2016.
 */
public interface IInfoTextLine
{
    @Nullable
    String getUnformattedText();

    IWidget createWidget(IGui gui, IPanel parent);

    IInfoTextLine copy(InfoPage page);

    JsonElement getJson();
}