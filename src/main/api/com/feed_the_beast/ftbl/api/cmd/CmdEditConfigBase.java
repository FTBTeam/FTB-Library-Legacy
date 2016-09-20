package com.feed_the_beast.ftbl.api.cmd;

import com.feed_the_beast.ftbl.FTBLibLang;
import com.feed_the_beast.ftbl.FTBLibMod;
import com.feed_the_beast.ftbl.api.config.IConfigContainer;
import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigTree;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api_impl.config.ConfigManager;
import com.feed_the_beast.ftbl.api_impl.config.SimpleConfigKey;
import com.feed_the_beast.ftbl.net.MessageEditConfig;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.latmod.lib.util.LMJsonUtils;
import com.latmod.lib.util.LMStringUtils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by LatvianModder on 07.06.2016.
 */
public abstract class CmdEditConfigBase extends CommandLM
{
    public CmdEditConfigBase(String s)
    {
        super(s);
    }

    @Override
    public String getCommandUsage(ICommandSender ics)
    {
        return "/" + commandName + " [ID] [value]";
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
    {
        try
        {
            Map<IConfigKey, IConfigValue> map = getConfigContainer(sender).createGroup().getTree();

            if(args.length == 1)
            {
                List<IConfigKey> keys = new ArrayList<>();
                keys.addAll(map.keySet());
                Collections.sort(keys, null);
                return getListOfStringsMatchingLastWord(args, keys);
            }
            else if(args.length == 2)
            {
                IConfigValue entry = map.get(new SimpleConfigKey(args[0]));

                if(entry != null)
                {
                    List<String> variants = entry.getVariants();

                    if(variants != null && !variants.isEmpty())
                    {
                        return getListOfStringsMatchingLastWord(args, variants);
                    }
                }
            }
        }
        catch(CommandException ex)
        {
            //ITextComponent c = new TextComponentTranslation(ex.getMessage(), ex.getErrorObjects());
            //c.getStyle().setColor(TextFormatting.DARK_RED);
            //sender.addChatMessage(c);
        }

        return super.getTabCompletionOptions(server, sender, args, pos);
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if(args.length == 0 && sender instanceof EntityPlayerMP)
        {
            EntityPlayerMP ep = getCommandSenderAsPlayer(sender);
            IConfigContainer cc = getConfigContainer(sender);
            ConfigManager.INSTANCE.getTempConfig().put(ep.getGameProfile().getId(), cc);
            new MessageEditConfig(null, cc).sendTo(ep);
            return;
        }

        checkArgs(args, 1, "[ID] [value]");

        IConfigContainer cc = getConfigContainer(sender);
        IConfigKey key = new SimpleConfigKey(args[0]);
        IConfigTree tree = cc.createGroup();

        if(!tree.has(key))
        {
            throw FTBLibLang.RAW.commandError("Can't find config entry '" + args[0] + "'!"); //TODO: Lang
        }

        IConfigValue entry = tree.get(key);

        if(args.length >= 2)
        {
            String json = LMStringUtils.unsplitSpaceUntilEnd(1, args);

            FTBLibMod.logger.info("Setting " + args[0] + " to " + json); //TODO: Lang

            try
            {
                JsonElement value = LMJsonUtils.fromJson(json);
                sender.addChatMessage(new TextComponentString(args[0] + " set to " + value)); //TODO: Lang
                JsonObject json1 = new JsonObject();
                json1.add(args[0], value);
                cc.saveConfig(sender, null, json1);
                return;
            }
            catch(Exception ex)
            {
                throw FTBLibLang.RAW.commandError(ex.toString());
            }
        }

        sender.addChatMessage(new TextComponentString(String.valueOf(entry.getSerializableElement())));
    }

    public abstract IConfigContainer getConfigContainer(ICommandSender sender) throws CommandException;
}