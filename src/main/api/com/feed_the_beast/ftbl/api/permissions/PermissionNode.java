package com.feed_the_beast.ftbl.api.permissions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by LatvianModder on 15.09.2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PermissionNode
{
    DefaultPermissionLevel level();

    String[] desc() default { };
}
