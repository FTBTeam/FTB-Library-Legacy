package com.feed_the_beast.ftbl.client;

import com.feed_the_beast.ftbl.FTBLibModCommon;
import com.feed_the_beast.ftbl.api.ForgeWorld;
import com.feed_the_beast.ftbl.api.ForgeWorldSP;
import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.config.ClientConfigRegistry;
import com.feed_the_beast.ftbl.api.config.ConfigEntryBool;
import com.feed_the_beast.ftbl.api.config.ConfigEntryEnum;
import com.feed_the_beast.ftbl.api.config.ConfigEntryString;
import com.feed_the_beast.ftbl.api.gui.LMGuiHandler;
import com.feed_the_beast.ftbl.api.gui.LMGuiHandlerRegistry;
import com.feed_the_beast.ftbl.api.gui.PlayerActionRegistry;
import com.feed_the_beast.ftbl.api.item.IItemLM;
import com.feed_the_beast.ftbl.api.tile.IGuiTile;
import com.feed_the_beast.ftbl.cmd.CmdReloadClient;
import com.feed_the_beast.ftbl.gui.info.InfoClientSettings;
import com.feed_the_beast.ftbl.util.EnumScreen;
import com.feed_the_beast.ftbl.util.EventBusHelper;
import com.feed_the_beast.ftbl.util.FTBLib;
import latmod.lib.LMColorUtils;
import latmod.lib.LMUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.particle.ParticleRedstone;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.UUID;

@SideOnly(Side.CLIENT)
public class FTBLibModClient extends FTBLibModCommon
{
    public static final ConfigEntryBool item_ore_names = new ConfigEntryBool("item_ore_names", false);

    public static final ConfigEntryEnum<EnumScreen> notifications = new ConfigEntryEnum<>("notifications", EnumScreen.values(), EnumScreen.SCREEN, false);
    public static final ConfigEntryString reload_client_cmd = new ConfigEntryString("reload_client_cmd", "reload_client");
    public static final ConfigEntryBool action_buttons_on_top = new ConfigEntryBool("action_buttons_on_top", true);
    public static final ConfigEntryEnum<FTBLibRenderHandler.LightValueTexture> light_value_texture = new ConfigEntryEnum<>("light_value_texture", FTBLibRenderHandler.LightValueTexture.values(), FTBLibRenderHandler.LightValueTexture.O, false);
    public static final ConfigEntryBool sort_friends_az = new ConfigEntryBool("sort_friends_az", false);
    
	/*
    public static final ConfigEntryBlank edit_shortcuts = new ConfigEntryBlank("edit_shortcuts")
	{
		public void onClicked()
		{ FTBLibClient.openGui(new GuiEditShortcuts()); }
	};
	*/

    @Override
    public void preInit()
    {
        //JsonHelper.initClient();
        EventBusHelper.register(FTBLibClientEventHandler.instance);
        EventBusHelper.register(FTBLibRenderHandler.instance);
        LMGuiHandlerRegistry.add(FTBLibGuiHandler.instance);

        //For Dev reasons, see DevConsole
        FTBLib.userIsLatvianModder = FTBLibClient.mc.getSession().getProfile().getId().equals(LMUtils.fromString("5afb9a5b207d480e887967bc848f9a8f"));

        ClientConfigRegistry.addGroup("ftbl", FTBLibModClient.class);
        ClientConfigRegistry.addGroup("ftbl_info", InfoClientSettings.class);

        ClientConfigRegistry.add(PlayerActionRegistry.configGroup);

        ClientCommandHandler.instance.registerCommand(new CmdReloadClient());

        FTBLibActions.init();
    }

    @Override
    public void postInit()
    {
        ClientConfigRegistry.provider().save();
    }

    @Override
    public boolean isShiftDown()
    {
        return GuiScreen.isShiftKeyDown();
    }

    @Override
    public boolean isCtrlDown()
    {
        return GuiScreen.isCtrlKeyDown();
    }

    @Override
    public boolean isTabDown()
    {
        return Keyboard.isKeyDown(Keyboard.KEY_TAB);
    }

    @Override
    public boolean inGameHasFocus()
    {
        return FTBLibClient.mc.inGameHasFocus;
    }

    @Override
    public EntityPlayer getClientPlayer()
    {
        return FMLClientHandler.instance().getClientPlayerEntity();
    }

    @Override
    public EntityPlayer getClientPlayer(UUID id)
    {
        return FTBLibClient.getPlayerSP(id);
    }

    @Override
    public World getClientWorld()
    {
        return FMLClientHandler.instance().getWorldClient();
    }

    @Override
    public double getReachDist(EntityPlayer ep)
    {
        if(ep == null)
        {
            return 0D;
        }
        else if(ep instanceof EntityPlayerMP)
        {
            return super.getReachDist(ep);
        }
        PlayerControllerMP c = FTBLibClient.mc.playerController;
        return (c == null) ? 0D : c.getBlockReachDistance();
    }

    @Override
    public void spawnDust(World w, double x, double y, double z, int col)
    {
        ParticleRedstone fx = new ParticleRedstone(w, x, y, z, 0F, 0F, 0F)
        { };

        float alpha = LMColorUtils.getAlpha(col) / 255F;
        float red = LMColorUtils.getRed(col) / 255F;
        float green = LMColorUtils.getGreen(col) / 255F;
        float blue = LMColorUtils.getBlue(col) / 255F;
        if(alpha == 0F)
        {
            alpha = 1F;
        }

        fx.setRBGColorF(red, green, blue);
        fx.setAlphaF(alpha);
        FTBLibClient.mc.effectRenderer.addEffect(fx);
    }

    @Override
    public boolean openClientGui(EntityPlayer ep, String mod, int id, NBTTagCompound data)
    {
        LMGuiHandler h = LMGuiHandlerRegistry.get(mod);

        if(h != null)
        {
            GuiScreen g = h.getGui(ep, id, data);

            if(g != null)
            {
                FTBLibClient.openGui(g);
                return true;
            }
        }

        return false;
    }

    @Override
    public void openClientTileGui(EntityPlayer ep, IGuiTile t, NBTTagCompound data)
    {
        if(ep != null && t != null)
        {
            GuiScreen g = t.getGui(ep, data);
            if(g != null)
            {
                FTBLibClient.openGui(g);
            }
        }
    }

    @Override
    public ForgeWorld getClientLMWorld()
    {
        return ForgeWorldSP.inst;
    }

    @Override
    public void loadModels(IItemLM i)
    {
        i.loadModels();
    }
}