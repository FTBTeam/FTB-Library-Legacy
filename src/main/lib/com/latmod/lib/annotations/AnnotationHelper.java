package com.latmod.lib.annotations;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LatvianModder on 26.03.2016.
 */
public class AnnotationHelper
{
    private static final Map<Class<? extends Annotation>, Handler> map = new HashMap<>();

    public interface Handler
    {
        boolean onAnnotationDeclared(@Nonnull Annotation a, @Nonnull IAnnotationContainer container);
    }

    static
    {
        register(Info.class, (a, container) ->
        {
            if(container instanceof IInfoContainer)
            {
                String[] info = ((Info) a).value();
                if(info.length == 0)
                {
                    info = null;
                }
                ((IInfoContainer) container).setInfo(info);
                return true;
            }

            return false;
        });

        register(NumberBounds.class, (a, container) ->
        {
            if(container instanceof INumberBoundsContainer)
            {
                NumberBounds b = (NumberBounds) a;
                ((INumberBoundsContainer) container).setBounds(b.min(), b.max());
                return true;
            }

            return false;
        });

        register(Flags.class, (a, container) ->
        {
            if(container instanceof IFlagContainer)
            {
                IFlagContainer fc = (IFlagContainer) container;
                fc.setFlags(((Flags) a).value());
                return true;
            }

            return false;
        });
    }

    public static void register(@Nonnull Class<? extends Annotation> c, @Nonnull Handler h)
    {
        map.put(c, h);
    }

    public static void inject(@Nonnull Field field, Object obj) throws Exception
    {
        if(obj instanceof IAnnotationContainer)
        {
            IAnnotationContainer container = (IAnnotationContainer) obj;

            for(Annotation a : field.getDeclaredAnnotations())
            {
                Handler h = map.get(a.annotationType());
                if(h != null)
                {
                    h.onAnnotationDeclared(a, container);
                }
            }
        }
    }
}