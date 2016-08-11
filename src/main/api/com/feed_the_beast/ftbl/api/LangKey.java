package com.feed_the_beast.ftbl.api;

import com.latmod.lib.FinalIDObject;
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
public final class LangKey extends FinalIDObject
{
    private static final Object[] EMPTY_FORMATTING = new Object[0];

    public LangKey(@Nonnull String s)
    {
        super(s);
    }

    @Nonnull
    @SideOnly(Side.CLIENT)
    public String translate()
    {
        return I18n.format(getID(), EMPTY_FORMATTING);
    }

    @Nonnull
    @SideOnly(Side.CLIENT)
    public String translateFormatted(Object... o)
    {
        return I18n.format(getID(), o);
    }

    @Nonnull
    public ITextComponent textComponent(Object... o)
    {
        return new TextComponentTranslation(getID(), o);
    }

    public void printChat(@Nonnull ICommandSender ics, Object... o)
    {
        ics.addChatMessage(textComponent(o));
    }

    @Nonnull
    public CommandException commandError(Object... o)
    {
        return new CommandException(getID(), o);
    }
}
