package com.feed_the_beast.ftbl.api.asm;

import com.feed_the_beast.ftbl.api.IRegistryObject;

import java.lang.reflect.Field;

/**
 * Created by LatvianModder on 14.10.2016.
 */
public interface IRegistryObjectCallback<T extends IRegistryObject>
{
    void onCallback(T obj, Field field, String id) throws Exception;
}