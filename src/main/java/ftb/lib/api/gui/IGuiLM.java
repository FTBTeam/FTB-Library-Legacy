package ftb.lib.api.gui;

import ftb.lib.api.gui.widgets.PanelLM;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by LatvianModder on 18.01.2016.
 */
@SideOnly(Side.CLIENT)
public interface IGuiLM
{
	GuiScreen getGui();
	PanelLM getMainPanel();
	MouseLM mouse();
	void initLMGui();
	void refreshWidgets();
	float getZLevel();
	void setZLevel(float z);
	FontRenderer getFontRenderer();
	void close(GuiScreen g);
}
