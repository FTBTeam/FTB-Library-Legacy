package ftb.lib.api.permissions;

import java.lang.annotation.*;

/**
 * Created by LatvianModder on 24.02.2016.<br>
 * Default value = default player value, its always true for OPs
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ForgePermission
{
	boolean value();
}
