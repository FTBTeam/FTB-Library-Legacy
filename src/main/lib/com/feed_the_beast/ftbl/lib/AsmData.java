package com.feed_the_beast.ftbl.lib;

import com.feed_the_beast.ftbl.api.IRegistryObject;
import com.feed_the_beast.ftbl.api.RegistryObject;
import com.feed_the_beast.ftbl.api.asm.IAnnotationInfo;
import com.feed_the_beast.ftbl.api.asm.IClassCallback;
import com.feed_the_beast.ftbl.api.asm.IMethodCallback;
import com.feed_the_beast.ftbl.api.asm.IObjectCallback;
import com.feed_the_beast.ftbl.api.asm.IRegistryObjectCallback;
import net.minecraftforge.fml.common.discovery.ASMDataTable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by LatvianModder on 14.10.2016.
 */
public class AsmData
{
    private static class AnnotationInfo implements IAnnotationInfo
    {
        private final Map<String, Object> map;

        private AnnotationInfo(Map<String, Object> m)
        {
            map = m;
        }

        @Override
        public Object getObject(String id, Object def)
        {
            Object val = map.get(id);
            return val == null ? def : val;
        }
    }

    private final ASMDataTable table;

    public AsmData(ASMDataTable t)
    {
        table = t;
    }

    public <T> void findAnnotatedObjects(Class<T> objClass, Class<? extends Annotation> annotationClass, IObjectCallback<T> callback)
    {
        for(ASMDataTable.ASMData data : table.getAll(annotationClass.getName()))
        {
            try
            {
                int index = data.getObjectName().indexOf('(');

                if(index != -1)
                {
                    continue;
                }

                Field field = Class.forName(data.getClassName()).getDeclaredField(data.getObjectName());

                if(field == null || !objClass.isAssignableFrom(field.getType()))
                {
                    continue;
                }

                field.setAccessible(true);
                callback.onCallback((T) field.get(null), field, new AnnotationInfo(data.getAnnotationInfo()));
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    public <T> void findAnnotatedClasses(Class<T> interfaceClass, Class<? extends Annotation> annotationClass, IClassCallback<T> callback)
    {
        for(ASMDataTable.ASMData data : table.getAll(annotationClass.getName()))
        {
            try
            {
                Class<?> clazz = Class.forName(data.getClassName());

                if(interfaceClass.isAssignableFrom(clazz))
                {
                    callback.onCallback((Class<T>) clazz, new AnnotationInfo(data.getAnnotationInfo()));
                }
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    public void findAnnotatedMethods(Class<? extends Annotation> annotationClass, IMethodCallback callback)
    {
        for(ASMDataTable.ASMData data : table.getAll(annotationClass.getName()))
        {
            try
            {
                int index = data.getObjectName().indexOf('(');

                if(index != -1 && data.getObjectName().indexOf(')') == index + 1)
                {
                    Method method = Class.forName(data.getClassName()).getDeclaredMethod(data.getObjectName().substring(0, index));

                    if(method != null)
                    {
                        method.setAccessible(true);
                        callback.onCallback(method, method.getParameterTypes(), new AnnotationInfo(data.getAnnotationInfo()));
                    }
                }
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    //TODO: Improve? Nah
    public <T extends IRegistryObject> void findRegistryObjects(Class<T> objClass, final boolean needNonBlankID, final IRegistryObjectCallback<T> callback)
    {
        findAnnotatedObjects(objClass, RegistryObject.class, (obj, field, info) ->
        {
            String id = info.getString("value", "");

            if(!needNonBlankID || !id.isEmpty())
            {
                callback.onCallback(obj, field, id);
            }
        });
    }
}
