package com.feed_the_beast.ftbl.api;

import net.minecraftforge.fml.relauncher.Side;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Registers an event handler class to {@code MinecraftForge.EVENT_BUS} at PREINIT state
 * <p>
 * You have to use this for FTB Lib and FTB Utilities plugins
 *
 * @author LatvianModder
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EventHandler
{
	Side[] value() default {Side.CLIENT, Side.SERVER};

	/**
	 * Array of required mods.
	 * <p>
	 * If any of the listed mods is missing, handler won't be registered
	 */
	String[] requiredMods() default { };
}