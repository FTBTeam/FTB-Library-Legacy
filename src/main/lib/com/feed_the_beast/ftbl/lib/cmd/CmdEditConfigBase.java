package com.feed_the_beast.ftbl.lib.cmd;

import com.feed_the_beast.ftbl.api.config.IConfigContainer;
import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigTree;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.lib.config.SimpleConfigKey;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import com.feed_the_beast.ftbl.lib.internal.FTBLibIntegrationInternal;
import com.feed_the_beast.ftbl.lib.internal.FTBLibLang;
import com.feed_the_beast.ftbl.lib.util.JsonUtils;
import com.feed_the_beast.ftbl.lib.util.StringUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
public abstract class CmdEditConfigBase extends CmdBase
{
    @Override
    public String getUsage(ICommandSender ics)
    {
        return "/" + getName() + " [ID] [value]";
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
    {
        try
        {
            Map<IConfigKey, IConfigValue> map = getConfigContainer(sender).getConfigTree().getTree();

            if(args.length == 1)
            {
                List<IConfigKey> keys = new ArrayList<>();
                keys.addAll(map.keySet());
                Collections.sort(keys, (o1, o2) -> o1.getName().compareTo(o2.getName()));
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

        return super.getTabCompletions(server, sender, args, pos);
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if(args.length == 0 && sender instanceof EntityPlayerMP)
        {
            FTBLibIntegrationInternal.API.editServerConfig(getCommandSenderAsPlayer(sender), null, getConfigContainer(sender));
            return;
        }

        checkArgs(args, 1, "[ID] [value]");

        IConfigContainer cc = getConfigContainer(sender);
        IConfigKey key = new SimpleConfigKey(args[0]);
        IConfigTree tree = cc.getConfigTree();

        if(!tree.has(key))
        {
            throw FTBLibLang.RAW.commandError("Can't find config entry '" + args[0] + "'!"); //TODO: Lang
        }

        IConfigValue entry = tree.get(key);

        if(args.length >= 2)
        {
            String json = String.valueOf(StringUtils.joinSpaceUntilEnd(1, args));
            FTBLibFinals.LOGGER.info("Setting " + args[0] + " to " + json); //TODO: Lang

            try
            {
                JsonElement value = JsonUtils.fromJson(JsonUtils.fixJsonString(json));
                sender.sendMessage(new TextComponentString(args[0] + " set to " + value)); //TODO: Lang
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

        sender.sendMessage(new TextComponentString(String.valueOf(entry.getSerializableElement())));
    }

    public abstract IConfigContainer getConfigContainer(ICommandSender sender) throws CommandException;
}