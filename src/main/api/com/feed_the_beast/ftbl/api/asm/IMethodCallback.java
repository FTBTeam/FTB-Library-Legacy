package com.feed_the_beast.ftbl.api.asm;

import java.lang.reflect.Method;

/**
 * Created by LatvianModder on 14.10.2016.
 */
public interface IMethodCallback
{
    void onCallback(Method method, Class<?>[] params, IAnnotationInfo data) throws Exception;
}