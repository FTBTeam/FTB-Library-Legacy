package com.feed_the_beast.ftbl.util;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;
import java.util.EnumMap;

/**
 * Created by LatvianModder on 05.06.2016.
 */
public class FTBLibReflection
{
    private static EnumMap<EnumDyeColor, TextFormatting> dyeToTextFormattingMap;
    private static EnumMap<TextFormatting, Character> textFormattingToCharMap;
    private static EnumMap<EnumDyeColor, Integer> dyeTextFormattingColorsLight, dyeTextFormattingColorsDark;

    public static TextFormatting getFromDyeColor(EnumDyeColor color)
    {
        if(dyeToTextFormattingMap == null)
        {
            dyeToTextFormattingMap = new EnumMap<>(EnumDyeColor.class);

            for(EnumDyeColor col : EnumDyeColor.values())
            {
                try
                {
                    Field field = ReflectionHelper.findField(EnumDyeColor.class, "chatColor", "field_176793_x");
                    field.setAccessible(true);
                    dyeToTextFormattingMap.put(col, (TextFormatting) field.get(col));
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }

        return dyeToTextFormattingMap.get(color);
    }

    public static char getTextFormattingChar(TextFormatting formatting)
    {
        if(textFormattingToCharMap == null)
        {
            textFormattingToCharMap = new EnumMap<>(TextFormatting.class);

            for(TextFormatting f : TextFormatting.values())
            {
                try
                {
                    Field field = ReflectionHelper.findField(TextFormatting.class, "formattingCode", "field_96329_z");
                    field.setAccessible(true);
                    textFormattingToCharMap.put(f, (Character) field.get(f));
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }

        return textFormattingToCharMap.get(formatting);
    }

    public static int getDyeColor(EnumDyeColor color, boolean isLighter)
    {
        if(isLighter)
        {
            if(dyeTextFormattingColorsLight == null)
            {
                dyeTextFormattingColorsLight = new EnumMap<>(EnumDyeColor.class);

                for(EnumDyeColor col : EnumDyeColor.values())
                {
                    dyeTextFormattingColorsLight.put(col, GuiUtils.getColorCode(getTextFormattingChar(getFromDyeColor(color)), true));
                }
            }

            return dyeTextFormattingColorsLight.get(color);
        }
        else
        {
            if(dyeTextFormattingColorsDark == null)
            {
                dyeTextFormattingColorsDark = new EnumMap<>(EnumDyeColor.class);

                for(EnumDyeColor col : EnumDyeColor.values())
                {
                    dyeTextFormattingColorsDark.put(col, GuiUtils.getColorCode(getTextFormattingChar(getFromDyeColor(color)), false));
                }
            }

            return dyeTextFormattingColorsDark.get(color);
        }
    }
}
