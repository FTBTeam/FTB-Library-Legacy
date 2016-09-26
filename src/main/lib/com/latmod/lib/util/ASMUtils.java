package com.latmod.lib.util;

import net.minecraftforge.fml.common.discovery.ASMDataTable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by LatvianModder on 26.09.2016.
 */
public class ASMUtils
{
    public static class AnnotationInfo
    {
        private Map<String, Object> map;

        private AnnotationInfo(Map<String, Object> m)
        {
            map = m;
        }

        public String getString(String id, String def)
        {
            Object val = map.get(id);
            return val == null ? def : val.toString();
        }

        public int getInt(String id, int def)
        {
            Object val = map.get(id);
            return val == null ? def : val.hashCode();
        }

        public boolean getBoolean(String id, boolean def)
        {
            Object val = map.get(id);
            return val == null ? def : (Boolean) val;
        }

        public List<String> getStringList(String id)
        {
            Object val = map.get(id);

            if(val instanceof String)
            {
                return Collections.singletonList(val.toString());
            }
            else if(val instanceof List<?>)
            {
                return ((List<String>) val);
            }

            return Collections.emptyList();
        }
    }

    public interface ObjectCallback<T>
    {
        void onCallback(T obj, Field field, AnnotationInfo info) throws Exception;
    }

    public static <T> void findAnnotatedObjects(ASMDataTable table, Class<T> objClass, Class<?> annotationClass, ObjectCallback<T> callback)
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

                callback.onCallback((T) field.get(null), field, new AnnotationInfo(data.getAnnotationInfo()));
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    public interface ClassCallback<T>
    {
        void onCallback(Class<T> clazz, AnnotationInfo data) throws Exception;
    }

    public static <T> void findAnnotatedClasses(ASMDataTable table, Class<T> interfaceClass, Class<?> annotationClass, ClassCallback<T> callback)
    {
        for(ASMDataTable.ASMData data : table.getAll(annotationClass.getName()))
        {
            try
            {
                callback.onCallback((Class<T>) Class.forName(data.getClassName()).asSubclass(interfaceClass), new AnnotationInfo(data.getAnnotationInfo()));
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    public interface MethodCallback
    {
        void onCallback(Method method, Class<?>[] params, AnnotationInfo data) throws Exception;
    }

    //TODO: Figure out how to find methods with more parameters
    public static void findAnnotatedMethods(ASMDataTable table, Class<?> annotationClass, MethodCallback callback)
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
}
