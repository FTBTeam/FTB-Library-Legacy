package com.feed_the_beast.ftbl.client;

import com.feed_the_beast.ftbl.FTBLibModCommon;
import com.feed_the_beast.ftbl.api_impl.ConfigManager;
import com.latmod.lib.util.LMColorUtils;
import com.latmod.lib.util.LMStringUtils;
import com.latmod.lib.util.LMUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.particle.ParticleRedstone;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;

import javax.annotation.Nullable;

public class FTBLibModClient extends FTBLibModCommon
{
    @Override
    public void preInit()
    {
        //JsonHelper.initClient();
        MinecraftForge.EVENT_BUS.register(FTBLibClientEventHandler.instance);

        //For Dev reasons, see DevConsole
        if(Minecraft.getMinecraft().getSession().getProfile().getId().equals(LMStringUtils.fromString("5afb9a5b207d480e887967bc848f9a8f")))
        {
            LMUtils.userIsLatvianModder = true;
        }
    }

    @Override
    public void postInit()
    {
        ConfigManager.INSTANCE.saveClientConfig();
    }

    @Override
    @Nullable
    public EntityPlayer getClientPlayer()
    {
        return FMLClientHandler.instance().getClientPlayerEntity();
    }

    @Override
    public double getReachDist(@Nullable EntityPlayer ep)
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
}