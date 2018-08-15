package com.feed_the_beast.ftblib.lib.util;

import net.minecraftforge.fml.common.Loader;

import java.io.File;

/**
 * @author LatvianModder
 */
public class Folders
{
	public static File getConfig()
	{
		return Loader.instance().getConfigDir();
	}

	public static File getMinecraft()
	{
		return getConfig().getParentFile();
	}

	public static File getLocal()
	{
		File file = new File(getMinecraft(), "local");

		if (!file.exists())
		{
			file.mkdir();
		}

		return file;
	}
}