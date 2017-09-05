package com.feed_the_beast.ftbl.api.config;

import javax.annotation.Nullable;
import java.io.File;
import java.util.function.Supplier;

/**
 * @author LatvianModder
 */
public interface IConfigFileProvider extends Supplier<File>
{
	@Override
	@Nullable
	File get();
}