package com.feed_the_beast.ftbl.client;

import com.feed_the_beast.ftbl.FTBLibModCommon;
import com.feed_the_beast.ftbl.api.config.ClientConfigRegistry;
import com.feed_the_beast.ftbl.api.config.ConfigEntryBool;
import com.feed_the_beast.ftbl.api.gui.GuiHandler;
import com.feed_the_beast.ftbl.api.gui.guibuttons.SidebarButtonRegistry;
import com.feed_the_beast.ftbl.gui.InfoClientSettings;
import com.feed_the_beast.ftbl.util.FTBLib;
import com.latmod.lib.util.LMColorUtils;
import com.latmod.lib.util.LMUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.particle.ParticleRedstone;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FTBLibModClient extends FTBLibModCommon
{
    public static final ConfigEntryBool item_ore_names = new ConfigEntryBool(false);
    public static final ConfigEntryBool action_buttons_on_top = new ConfigEntryBool(true);

    @Override
    public void preInit()
    {
        //JsonHelper.initClient();
        MinecraftForge.EVENT_BUS.register(FTBLibClientEventHandler.instance);

        //For Dev reasons, see DevConsole
        FTBLib.userIsLatvianModder = Minecraft.getMinecraft().getSession().getProfile().getId().equals(LMUtils.fromString("5afb9a5b207d480e887967bc848f9a8f"));

        ClientConfigRegistry.addGroup("ftbl", FTBLibModClient.class);
        ClientConfigRegistry.addGroup("ftbl_info", InfoClientSettings.class);
        ClientConfigRegistry.addGroup("sidebar_buttons", SidebarButtonRegistry.configGroup);

        FTBLibActions.init();
    }

    @Override
    public void postInit()
    {
        ClientConfigRegistry.saveConfig();
    }

    @Override
    public EntityPlayer getClientPlayer()
    {
        return FMLClientHandler.instance().getClientPlayerEntity();
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
        PlayerControllerMP c = Minecraft.getMinecraft().playerController;
        return (c == null) ? 0D : c.getBlockReachDistance();
    }

    @Override
    public void spawnDust(World w, double x, double y, double z, int col)
    {
        ParticleRedstone fx = new ParticleRedstone(w, x, y, z, 0F, 0F, 0F) { };

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
        Minecraft.getMinecraft().effectRenderer.addEffect(fx);
    }

    @Override
    public void openClientGui(GuiHandler handler, EntityPlayer ep, int ID, NBTTagCompound data, int wid)
    {
        GuiScreen g = handler.getGui(ep, ID, data);

        if(g != null)
        {
            Minecraft mc = Minecraft.getMinecraft();
            mc.displayGuiScreen(g);
            mc.thePlayer.openContainer.windowId = wid;
        }
    }
}