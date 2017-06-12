package com.feed_the_beast.ftbl.api.config;

import javax.annotation.Nullable;
import java.io.File;

/**
 * @author LatvianModder
 */
public interface IConfigFileProvider
{
	@Nullable
	File getFile();
}