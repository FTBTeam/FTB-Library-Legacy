package com.feed_the_beast.ftbl.lib;

import com.feed_the_beast.ftbl.api.IRegistryObject;
import com.feed_the_beast.ftbl.api.RegistryObject;
import com.feed_the_beast.ftbl.api.asm.IAnnotationInfo;
import com.feed_the_beast.ftbl.api.asm.IClassCallback;
import com.feed_the_beast.ftbl.api.asm.IMethodCallback;
import com.feed_the_beast.ftbl.api.asm.IObjectCallback;
import com.feed_the_beast.ftbl.api.asm.IRegistryObjectCallback;
import com.feed_the_beast.ftbl.lib.util.LMUtils;
import net.minecraftforge.fml.common.discovery.ASMDataTable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

/**
 * Created by LatvianModder on 14.10.2016.
 */
public class AsmData
{
    private static final boolean DUMP_INFO = System.getProperty("ftbl.logAsm", "0").equals("1");
    private static final Comparator<ASMDataTable.ASMData> ASM_DATA_COMPARATOR = (o1, o2) ->
    {
        int i = o1.getClassName().compareToIgnoreCase(o2.getClassName());

        if(i == 0)
        {
            i = o1.getObjectName().compareToIgnoreCase(o2.getObjectName());
        }

        return i;
    };

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

    private Collection<ASMDataTable.ASMData> getASMData(Class<? extends Annotation> annotationClass)
    {
        if(DUMP_INFO)
        {
            ArrayList<ASMDataTable.ASMData> list = new ArrayList<>(table.getAll(annotationClass.getName()));
            Collections.sort(list, ASM_DATA_COMPARATOR);
            return list;
        }

        return table.getAll(annotationClass.getName());
    }

    private static Class<?> getClass(ASMDataTable.ASMData data) throws Exception
    {
        return Class.forName(data.getClassName());
    }

    public <T> void findAnnotatedObjects(Class<T> objClass, Class<? extends Annotation> annotationClass, IObjectCallback<T> callback)
    {
        if(DUMP_INFO)
        {
            LMUtils.DEV_LOGGER.info("Scanning ASM Objects: Annotation: @" + annotationClass.getName() + ", Class: " + objClass.getName());
        }

        for(ASMDataTable.ASMData data : getASMData(annotationClass))
        {
            try
            {
                if(data.getObjectName().indexOf('(') != -1)
                {
                    continue;
                }

                if(data.getObjectName().contains("ISound") || data.getClassName().contains("ISound"))
                {
                    LMUtils.DEV_LOGGER.info("ERROR! invalid ASM entry found! :: " + data.getClassName() + "#" + data.getObjectName());
                    continue;
                }

                if(DUMP_INFO)
                {
                    LMUtils.DEV_LOGGER.info("-  " + data.getClassName() + "#" + data.getObjectName() + " with info " + data.getAnnotationInfo());
                }

                Field field = getClass(data).getDeclaredField(data.getObjectName());

                if(field == null || !objClass.isAssignableFrom(field.getType()))
                {
                    continue;
                }

                if(DUMP_INFO)
                {
                    LMUtils.DEV_LOGGER.info("-  Match found!");
                }

                field.setAccessible(true);
                callback.onCallback((T) field.get(null), field, new AnnotationInfo(data.getAnnotationInfo()));
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }

        if(DUMP_INFO)
        {
            LMUtils.DEV_LOGGER.info("End of ASM Scan");
        }
    }

    public <T> void findAnnotatedClasses(Class<T> interfaceClass, Class<? extends Annotation> annotationClass, IClassCallback<T> callback)
    {
        if(DUMP_INFO)
        {
            LMUtils.DEV_LOGGER.info("Scanning ASM Classes: Annotation: @" + annotationClass.getName() + ", Interface: " + interfaceClass.getName());
        }

        for(ASMDataTable.ASMData data : getASMData(annotationClass))
        {
            try
            {
                if(DUMP_INFO)
                {
                    LMUtils.DEV_LOGGER.info("-  " + data.getClassName() + "#" + data.getObjectName() + " with info " + data.getAnnotationInfo());
                }

                Class<?> clazz = getClass(data);

                if(interfaceClass.isAssignableFrom(clazz))
                {
                    if(DUMP_INFO)
                    {
                        LMUtils.DEV_LOGGER.info("-  Match found!");
                    }

                    callback.onCallback((Class<T>) clazz, new AnnotationInfo(data.getAnnotationInfo()));
                }
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }

        if(DUMP_INFO)
        {
            LMUtils.DEV_LOGGER.info("End of ASM Scan");
        }
    }

    public void findAnnotatedMethods(Class<? extends Annotation> annotationClass, IMethodCallback callback)
    {
        if(DUMP_INFO)
        {
            LMUtils.DEV_LOGGER.info("Scanning ASM Methods: Annotation: @" + annotationClass.getName());
        }

        for(ASMDataTable.ASMData data : getASMData(annotationClass))
        {
            try
            {
                int index = data.getObjectName().indexOf('(');

                if(index != -1 && data.getObjectName().indexOf(')') == index + 1)
                {
                    if(DUMP_INFO)
                    {
                        LMUtils.DEV_LOGGER.info("-  " + data.getClassName() + "#" + data.getObjectName() + " with info " + data.getAnnotationInfo());
                    }

                    Method method = getClass(data).getDeclaredMethod(data.getObjectName().substring(0, index));

                    if(method != null)
                    {
                        if(DUMP_INFO)
                        {
                            LMUtils.DEV_LOGGER.info("-  Match found!");
                        }

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

        if(DUMP_INFO)
        {
            LMUtils.DEV_LOGGER.info("End of ASM Scan");
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
