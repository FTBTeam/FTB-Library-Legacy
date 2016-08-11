package com.latmod.lib.annotations;

/**
 * Created by LatvianModder on 26.03.2016.
 */
public interface IInfoContainer extends IAnnotationContainer
{
    String[] getInfo();

    void setInfo(String[] s);
}