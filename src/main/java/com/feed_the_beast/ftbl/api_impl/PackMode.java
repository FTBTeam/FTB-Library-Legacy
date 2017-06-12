package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.api.IPackMode;
import com.feed_the_beast.ftbl.lib.FinalIDObject;
import com.feed_the_beast.ftbl.lib.util.LMUtils;

import java.io.File;

/**
 * @author LatvianModder
 */
public class PackMode extends FinalIDObject implements IPackMode
{
	public PackMode(String id)
	{
		super(id);
	}

	@Override
	public File getFolder()
	{
		File f = new File(LMUtils.folderModpack, getName());
		if (!f.exists())
		{
			f.mkdirs();
		}
		return f;
	}
}