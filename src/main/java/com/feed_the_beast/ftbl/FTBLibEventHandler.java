package com.feed_the_beast.ftbl;

import com.feed_the_beast.ftbl.api.ForgePlayerMP;
import com.feed_the_beast.ftbl.api.ForgeWorld;
import com.feed_the_beast.ftbl.api.ForgeWorldMP;
import com.feed_the_beast.ftbl.api.IWorldTick;
import com.feed_the_beast.ftbl.api.ServerTickCallback;
import com.feed_the_beast.ftbl.api.item.ICreativeSafeItem;
import com.feed_the_beast.ftbl.api.tile.IInfoTile;
import com.feed_the_beast.ftbl.api.tile.ISecureTile;
import com.feed_the_beast.ftbl.api.tile.TileInfoDataAccessor;
import com.feed_the_beast.ftbl.util.FTBLib;
import com.feed_the_beast.ftbl.util.MathHelperMC;
import com.tamashenning.forgeanalytics.client.ForgeAnalyticsConstants;
import com.tamashenning.forgeanalytics.events.AnalyticsEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.silentchaos512.wit.api.WitBlockInfoEvent;

import java.util.ArrayList;
import java.util.List;

public class FTBLibEventHandler
{
    public static final FTBLibEventHandler instance = new FTBLibEventHandler();
    public static final List<ServerTickCallback> callbacks = new ArrayList<>();
    public static final List<ServerTickCallback> pendingCallbacks = new ArrayList<>();
    public static final List<IWorldTick> ticking = new ArrayList<>();

    @SubscribeEvent
    public void onWorldSaved(WorldEvent.Save event)
    {
        if(event.getWorld().provider.getDimensionType() == DimensionType.OVERWORLD && event.getWorld() instanceof WorldServer)
        {
            try
            {
                ForgeWorldMP.inst.save();
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    @Optional.Method(modid = "WIT")
    @SubscribeEvent
    public void onWIT(WitBlockInfoEvent event)
    {
        if(event.blockState.getBlock().hasTileEntity(event.blockState))
        {
            TileEntity te = event.getWorld().getTileEntity(event.pos);

            if(te instanceof IInfoTile)
            {
                TileInfoDataAccessor.inst.player = event.player;
                TileInfoDataAccessor.inst.world = event.getWorld();
                TileInfoDataAccessor.inst.hit = MathHelperMC.rayTrace(event.player);
                TileInfoDataAccessor.inst.state = event.blockState;
                ((IInfoTile) te).getInfo(TileInfoDataAccessor.inst, event.lines, event.advanced);
            }
        }
    }

    @Optional.Method(modid = "forgeanalytics")
    @SubscribeEvent
    public void onAnalytics(AnalyticsEvent event)
    {
        ForgeWorld w = ForgeWorld.getFrom(event.side);

        if(w != null)
        {
            ForgeAnalyticsConstants.CustomProperties.put("FTB_PackMode", w.getMode().getID());
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent e)
    {
        if(e.player instanceof EntityPlayerMP)
        {
            EntityPlayerMP ep = (EntityPlayerMP) e.player;

            ForgePlayerMP p = ForgeWorldMP.inst.getPlayer(ep);

            boolean firstLogin = p == null;

            if(firstLogin)
            {
                p = new ForgePlayerMP(ep.getGameProfile());
                ForgeWorldMP.inst.playerMap.put(p.getProfile().getId(), p);
                p.init();
            }
            else if(!p.getProfile().getName().equals(ep.getName()))
            {
                p.setProfile(ep.getGameProfile());
            }

            p.setPlayer(ep);
            p.onLoggedIn(firstLogin);
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent e)
    {
        if(e.player instanceof EntityPlayerMP)
        {
            ForgeWorldMP.inst.getPlayer(e.player).onLoggedOut();
        }
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent e)
    {
        if(e.getEntity() instanceof EntityPlayerMP)
        {
            ForgeWorldMP.inst.getPlayer(e.getEntity()).onDeath();
        }
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent e)
    {
        if(!e.world.isRemote && e.side == Side.SERVER && e.phase == TickEvent.Phase.END && e.type == TickEvent.Type.WORLD)
        {
            if(e.world.provider.getDimensionType() == DimensionType.OVERWORLD)
            {
                if(!pendingCallbacks.isEmpty())
                {
                    callbacks.addAll(pendingCallbacks);
                    pendingCallbacks.clear();
                }

                if(!callbacks.isEmpty())
                {
                    for(int i = callbacks.size() - 1; i >= 0; i--)
                    {
                        if(callbacks.get(i).incAndCheck())
                        {
                            callbacks.remove(i);
                        }
                    }
                }
            }

            if(e.world instanceof WorldServer && !ticking.isEmpty())
            {
                WorldServer ws = (WorldServer) e.world;
                long now = System.currentTimeMillis();

                for(IWorldTick t : ticking)
                {
                    t.onTick(ws, now);
                }
            }
        }
    }

    //FIXME: Right click / left click needs a rewrite
    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent e)
    {
        if(e.getEntityPlayer() instanceof FakePlayer || e instanceof PlayerInteractEvent.RightClickEmpty)
        {
            return;
        }
        else if(!canInteract(e.getEntityPlayer(), e.getHand(), e.getPos(), e instanceof PlayerInteractEvent.LeftClickBlock))
        {
            e.setCanceled(true);
        }
        else if(FTBLib.ftbu != null && e.getEntityPlayer() instanceof EntityPlayerMP)
        {
            if(!FTBLib.ftbu.canPlayerInteract(ForgeWorldMP.inst.getPlayer(e.getEntityPlayer()), e.getPos(), e instanceof PlayerInteractEvent.LeftClickBlock))
            {
                e.setCanceled(true);
            }
        }
    }

    private boolean canInteract(EntityPlayer ep, EnumHand hand, BlockPos pos, boolean leftClick)
    {
        ItemStack heldItem = ep.getHeldItem(hand);

        if(ep.capabilities.isCreativeMode && leftClick && heldItem != null && heldItem.getItem() instanceof ICreativeSafeItem)
        {
            if(!ep.worldObj.isRemote)
            {
                ep.worldObj.markBlockRangeForRenderUpdate(pos, pos);
            }
            //FIXME: else ep.worldObj.markChunkDirty(pos, null);
            return false;
        }

        if(!ep.worldObj.isRemote)
        {
            IBlockState state = ep.worldObj.getBlockState(pos);

            if(state.getBlock().hasTileEntity(state))
            {
                TileEntity te = ep.worldObj.getTileEntity(pos);
                ForgePlayerMP player = ForgeWorldMP.inst.getPlayer(ep);
                if(te instanceof ISecureTile && !((ISecureTile) te).canPlayerInteract(player, leftClick))
                {
                    ((ISecureTile) te).onPlayerNotOwner(player, leftClick);
                    return false;
                }
            }
        }

        return true;
    }
}