package com.feed_the_beast.ftblib.events;

import com.feed_the_beast.ftblib.lib.config.ConfigValue;
import com.feed_the_beast.ftblib.lib.config.RankConfigValueInfo;
import com.feed_the_beast.ftblib.lib.util.misc.Node;

import java.util.function.Consumer;

/**
 * @author LatvianModder
 */
public class RegisterRankConfigEvent extends FTBLibEvent
{
	private final Consumer<RankConfigValueInfo> callback;

	public RegisterRankConfigEvent(Consumer<RankConfigValueInfo> c)
	{
		callback = c;
	}

	public void register(RankConfigValueInfo info)
	{
		callback.accept(info);
	}

	public RankConfigValueInfo register(Node id, ConfigValue defaultPlayerValue, ConfigValue defaultOPValue)
	{
		RankConfigValueInfo info = new RankConfigValueInfo(id, defaultPlayerValue, defaultOPValue);
		register(info);
		return info;
	}

	public RankConfigValueInfo register(Node id, ConfigValue defaultValue)
	{
		RankConfigValueInfo info = new RankConfigValueInfo(id, defaultValue, null);
		register(info);
		return info;
	}
}