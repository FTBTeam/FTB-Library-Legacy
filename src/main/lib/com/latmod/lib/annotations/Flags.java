package com.latmod.lib.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by LatvianModder on 26.03.2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface Flags
{
    /**
     * Will be synced with client
     */
    int SYNC = 1;
    /**
     * Will be hidden from config gui
     */
    int HIDDEN = 2;
    /**
     * Will be visible in config gui, but uneditable
     */
    int CANT_EDIT = 4;
    /**
     * Can add new config entries
     */
    int CAN_ADD = 8;
    /**
     * Will be excluded from writing / reading from files
     */
    int EXCLUDED = 16;
    /**
     * Use slider whenever that is available
     */
    int USE_SLIDER = 32;

    int value();
}