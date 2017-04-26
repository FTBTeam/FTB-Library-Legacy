package com.feed_the_beast.ftbl.api.guide;

import com.feed_the_beast.ftbl.lib.guide.GuidePage;
import com.google.gson.JsonElement;

/**
 * Created by LatvianModder on 02.10.2016.
 */
public interface IGuideTextLineProvider
{
    IGuideTextLine create(GuidePage page, JsonElement json);
}