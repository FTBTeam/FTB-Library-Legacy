package com.feed_the_beast.ftbl.lib;

import com.feed_the_beast.ftbl.lib.util.LMUtils;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.ModCandidate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class AsmHelper
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
        private final ModCandidate mod;
        private final Map<String, Object> map;

        private AnnotationInfo(ModCandidate mc, Map<String, Object> m)
        {
            mod = mc;
            map = m;
        }

        @Override
        public ModCandidate getModCandidate()
        {
            return mod;
        }

        @Override
        public Object getObject(String id, Object def)
        {
            Object val = map.get(id);
            return val == null ? def : val;
        }
    }

    private static Collection<ASMDataTable.ASMData> getASMData(ASMDataTable table, Class<? extends Annotation> annotationClass)
    {
        if(DUMP_INFO)
        {
            ArrayList<ASMDataTable.ASMData> list = new ArrayList<>(table.getAll(annotationClass.getName()));
            list.sort(ASM_DATA_COMPARATOR);
            return list;
        }

        return table.getAll(annotationClass.getName());
    }

    private static Class<?> getClass(ASMDataTable.ASMData data) throws Exception
    {
        return Class.forName(data.getClassName());
    }

    public static <T> void findAnnotatedObjects(ASMDataTable table, Class<T> objClass, Class<? extends Annotation> annotationClass, IObjectCallback<T> callback)
    {
        if(DUMP_INFO)
        {
            LMUtils.DEV_LOGGER.info("Scanning ASM Objects: Annotation: @" + annotationClass.getName() + ", Class: " + objClass.getName());
        }

        for(ASMDataTable.ASMData data : getASMData(table, annotationClass))
        {
            try
            {
                if(data.getObjectName().indexOf('(') != -1)
                {
                    continue;
                }

                if(data.getClassName().startsWith("net.minecraft.client") || data.getObjectName().startsWith("net.minecraft.client"))
                {
                    LMUtils.DEV_LOGGER.error("ERROR! invalid ASM entry found! :: " + data.getClassName() + "#" + data.getObjectName());
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
                callback.onCallback((T) field.get(null), field, new AnnotationInfo(data.getCandidate(), data.getAnnotationInfo()));
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

    public static <T> Collection<T> findPlugins(ASMDataTable table, Class<T> pluginClass, Class<? extends Annotation> annotationClass)
    {
        List<T> list = new ArrayList<>();

        if(DUMP_INFO)
        {
            LMUtils.DEV_LOGGER.info("Scanning ASM Plugins: Annotation: @" + annotationClass.getName() + ", Interface: " + pluginClass.getName());
        }

        for(ASMDataTable.ASMData data : getASMData(table, annotationClass))
        {
            try
            {
                if(data.getObjectName().indexOf('(') != -1)
                {
                    continue;
                }

                if(DUMP_INFO)
                {
                    LMUtils.DEV_LOGGER.info("-  " + data.getClassName() + "#" + data.getObjectName() + " with info " + data.getAnnotationInfo());
                }

                Field field = getClass(data).getDeclaredField(data.getObjectName());

                if(field == null || !pluginClass.isAssignableFrom(field.getType()))
                {
                    continue;
                }

                if(DUMP_INFO)
                {
                    LMUtils.DEV_LOGGER.info("-  Match found!");
                }

                field.setAccessible(true);
                list.add((T) field.get(null));
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

        return Collections.unmodifiableList(list);
    }
}