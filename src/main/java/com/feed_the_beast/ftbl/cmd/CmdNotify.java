package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.api.INotification;
import com.feed_the_beast.ftbl.api.cmd.CommandLM;
import com.feed_the_beast.ftbl.api_impl.FTBLibAPI_Impl;
import com.feed_the_beast.ftbl.api_impl.Notification;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CmdNotify extends CommandLM
{
    public static class CustomNotifications
    {
        private static final Map<ResourceLocation, INotification> MAP = new HashMap<>();

        public void loadFromJson(JsonObject o)
        {
            MAP.clear();

            for(Map.Entry<String, JsonElement> entry : o.entrySet())
            {
                ResourceLocation key = new ResourceLocation(entry.getKey());
                int id = FTBLibAPI_Impl.INSTANCE.getRegistries().notifications().getOrCreateIDFromKey(key);
                Notification n = new Notification(id);

                if(entry.getValue().isJsonObject())
                {
                    //FIXME: JsonObject custom notification loading
                }
                else
                {
                    n.addText(new TextComponentString(entry.getValue().getAsString()));
                }

                MAP.put(key, n);
            }
        }
    }

    public CmdNotify()
    {
        super("notify");
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender ics)
    {
        return "/" + commandName + " <player> <id>";
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
        if(args.length == 2)
        {
            return getListOfStringsMatchingLastWord(args, CustomNotifications.MAP.keySet());
        }

        return super.getTabCompletionOptions(server, sender, args, pos);
    }

    @Override
    public boolean isUsernameIndex(String[] args, int i)
    {
        return i == 0;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender ics, String[] args) throws CommandException
    {
        checkArgs(args, 2, "<player> <id>");
        EntityPlayerMP player = getPlayer(server, ics, args[0]);
        INotification n = CustomNotifications.MAP.get(new ResourceLocation(args[1]));

        if(n == null)
        {
            throw new IllegalArgumentException("Notification '" + args[1] + "' not found!");
        }

        FTBLibAPI_Impl.INSTANCE.sendNotification(player, n);
    }
}