package com.feed_the_beast.ftbl.api.guide;

import com.feed_the_beast.ftbl.lib.guide.GuidePage;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public interface IGuideGui
{
	GuidePage getSelectedPage();

	void setSelectedPage(@Nullable GuidePage page);
}