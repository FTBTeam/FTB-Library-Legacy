package com.feed_the_beast.ftbl.lib;

import java.lang.reflect.Field;

/**
 * @author LatvianModder
 */
public interface IObjectCallback<T>
{
	void onCallback(T obj, Field field, IAnnotationInfo info) throws Exception;
}