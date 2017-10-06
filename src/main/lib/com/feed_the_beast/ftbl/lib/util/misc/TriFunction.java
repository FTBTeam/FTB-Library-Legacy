package com.feed_the_beast.ftbl.lib.util.misc;

/**
 * @author LatvianModder
 */
public interface TriFunction<R, A, B, C>
{
	R apply(A a, B b, C c);
}