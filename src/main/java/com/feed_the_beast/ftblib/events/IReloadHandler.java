package com.feed_the_beast.ftblib.events;

/**
 * @author LatvianModder
 */
@FunctionalInterface
public interface IReloadHandler
{
	boolean onReload(ServerReloadEvent reloadEvent) throws Exception;
}