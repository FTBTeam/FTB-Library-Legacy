package com.feed_the_beast.ftblib.lib.block;

/**
 * @author LatvianModder
 */
public interface BlockFlags
{
	int UPDATE = 1;
	int SEND_TO_CLIENTS = 2;
	int NO_RERENDER = 4;
	int RERENDER_MAIN_THREAD = 8;
	int NO_OBSERVERS = 16;

	int DEFAULT = UPDATE | SEND_TO_CLIENTS;
}