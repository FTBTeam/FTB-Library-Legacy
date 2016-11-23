package com.feed_the_beast.ftbl.lib;

import java.lang.reflect.Field;

/**
 * Created by LatvianModder on 14.10.2016.
 */
public interface IObjectCallback<T>
{
    void onCallback(T obj, Field field, IAnnotationInfo info) throws Exception;
}