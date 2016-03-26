package ftb.lib.api.config.old;

import java.lang.annotation.*;

/**
 * Created by LatvianModder on 14.02.2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MaxValue
{
	double value();
}