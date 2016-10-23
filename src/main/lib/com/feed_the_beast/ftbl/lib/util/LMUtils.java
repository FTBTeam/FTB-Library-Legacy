package com.feed_the_beast.ftbl.lib.util;

import com.feed_the_beast.ftbl.FTBLibFinals;
import com.feed_the_beast.ftbl.api.block.IBlockWithItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Made by LatvianModder
 */
public class LMUtils
{
    public static final boolean DEV_ENV = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
    public static final Logger DEV_LOGGER = LogManager.getLogger("FTBLibDev");
    public static final String FORMATTING = "\u00a7";
    public static final Pattern TEXT_FORMATTING_PATTERN = Pattern.compile("(?i)" + FORMATTING + "[0-9A-FK-OR]");

    public static boolean userIsLatvianModder = false;
    public static File folderConfig, folderMinecraft, folderModpack, folderLocal, folderWorld;

    public static final Comparator<Package> PACKAGE_COMPARATOR = (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName());

    public static void init(File configFolder)
    {
        folderConfig = configFolder;
        folderMinecraft = folderConfig.getParentFile();
        folderModpack = new File(folderMinecraft, "modpack/");
        folderLocal = new File(folderMinecraft, "local/");

        if(!folderModpack.exists())
        {
            folderModpack.mkdirs();
        }
        if(!folderLocal.exists())
        {
            folderLocal.mkdirs();
        }

        if(DEV_LOGGER instanceof org.apache.logging.log4j.core.Logger)
        {
            if(DEV_ENV)
            {
                ((org.apache.logging.log4j.core.Logger) DEV_LOGGER).setLevel(org.apache.logging.log4j.Level.ALL);
            }
            else
            {
                ((org.apache.logging.log4j.core.Logger) DEV_LOGGER).setLevel(org.apache.logging.log4j.Level.OFF);
            }
        }
        else
        {
            FTBLibFinals.LOGGER.info("DevLogger isn't org.apache.logging.log4j.core.Logger! It's " + DEV_LOGGER.getClass().getName());
        }
    }

    public static <K extends IForgeRegistryEntry<?>> void register(ResourceLocation id, K object)
    {
        object.setRegistryName(id);
        GameRegistry.register(object);

        if(object instanceof IBlockWithItem)
        {
            ItemBlock ib = ((IBlockWithItem) object).createItemBlock();
            ib.setRegistryName(id);
            GameRegistry.register(ib);
        }
    }

    public static <E> List<E> getObjects(@Nullable Class<E> type, Class<?> fields, @Nullable Object obj) throws IllegalAccessException
    {
        List<E> l = new ArrayList<>();

        for(Field f : fields.getDeclaredFields())
        {
            f.setAccessible(true);
            Object o = f.get(obj);

            if(type == null || type.isAssignableFrom(o.getClass()))
            {
                l.add((E) o);
            }
        }

        return l;
    }

    public static String removeFormatting(String s)
    {
        return s.isEmpty() ? s : TEXT_FORMATTING_PATTERN.matcher(s).replaceAll("");
    }
}