package com.latmod.lib;

import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 * Created by LatvianModder on 17.04.2016.
 */
public final class LangKey
{
    private static final Object[] EMPTY_FORMATTING = new Object[0];

    private final String key;

    public LangKey(@Nonnull String s)
    {
        key = s;
    }

    @Nonnull
    @SideOnly(Side.CLIENT)
    public String translate()
    {
        return I18n.format(key, EMPTY_FORMATTING);
    }

    @Nonnull
    @SideOnly(Side.CLIENT)
    public String translateFormatted(Object... o)
    {
        return I18n.format(key, o);
    }

    @Nonnull
    public ITextComponent textComponent(Object... o)
    {
        return new TextComponentTranslation(key, o);
    }

    public void printChat(@Nonnull ICommandSender ics, Object... o)
    {
        ics.addChatMessage(textComponent(o));
    }

    @Nonnull
    public CommandException commandError(Object... o)
    {
        return new CommandException(key, o);
    }
}
