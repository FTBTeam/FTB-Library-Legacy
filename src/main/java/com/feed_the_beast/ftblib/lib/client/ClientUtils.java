package com.feed_the_beast.ftblib.lib.client;

import com.feed_the_beast.ftblib.lib.gui.GuiBase;
import com.feed_the_beast.ftblib.lib.gui.IGuiWrapper;
import com.feed_the_beast.ftblib.lib.icon.PlayerHeadIcon;
import com.feed_the_beast.ftblib.lib.util.misc.NameMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.client.ClientCommandHandler;

import javax.annotation.Nullable;
import java.util.function.BooleanSupplier;

public class ClientUtils
{
	public static final NameMap<BlockRenderLayer> BLOCK_RENDER_LAYER_NAME_MAP = NameMap.create(BlockRenderLayer.SOLID, BlockRenderLayer.values());
	public static final BooleanSupplier IS_CLIENT_OP = () -> Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().player.getPermissionLevel() > 0;

	private static float lastBrightnessX, lastBrightnessY;

	public static PlayerHeadIcon localPlayerHead;

	public static int getDim()
	{
		return Minecraft.getMinecraft().world != null ? Minecraft.getMinecraft().world.provider.getDimension() : 0;
	}

	public static void spawnParticle(Particle particle)
	{
		Minecraft.getMinecraft().effectRenderer.addEffect(particle);
	}

	public static void pushBrightness(int u, int t)
	{
		lastBrightnessX = OpenGlHelper.lastBrightnessX;
		lastBrightnessY = OpenGlHelper.lastBrightnessY;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, u, t);
	}

	public static void pushMaxBrightness()
	{
		pushBrightness(240, 240);
	}

	public static void popBrightness()
	{
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastBrightnessX, lastBrightnessY);
	}

	public static void execClientCommand(String command, boolean printChat)
	{
		if (printChat)
		{
			Minecraft.getMinecraft().ingameGUI.getChatGUI().addToSentMessages(command);
		}

		if (ClientCommandHandler.instance.executeCommand(Minecraft.getMinecraft().player, command) == 0)
		{
			Minecraft.getMinecraft().player.sendChatMessage(command);
		}
	}

	public static void execClientCommand(String command)
	{
		execClientCommand(command, false);
	}

	public static void runLater(final Runnable runnable)
	{
		new Thread(() -> Minecraft.getMinecraft().addScheduledTask(runnable)).start();
	}

	@Nullable
	@SuppressWarnings("unchecked")
	public static <T> T getGuiAs(GuiScreen gui, Class<T> clazz)
	{
		if (gui instanceof IGuiWrapper)
		{
			GuiBase guiBase = ((IGuiWrapper) gui).getGui();

			if (clazz.isAssignableFrom(guiBase.getClass()))
			{
				return (T) guiBase;
			}
		}

		return clazz.isAssignableFrom(gui.getClass()) ? (T) Minecraft.getMinecraft().currentScreen : null;
	}

	@Nullable
	public static <T> T getCurrentGuiAs(Class<T> clazz)
	{
		return Minecraft.getMinecraft().currentScreen == null ? null : getGuiAs(Minecraft.getMinecraft().currentScreen, clazz);
	}
}