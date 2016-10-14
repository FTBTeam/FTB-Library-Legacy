package com.feed_the_beast.ftbl.api.asm;

/**
 * Created by LatvianModder on 14.10.2016.
 */
public interface IClassCallback<T>
{
    void onCallback(Class<T> clazz, IAnnotationInfo data) throws Exception;
}