package ftb.lib.api.gui;

import cpw.mods.fml.relauncher.*;
import ftb.lib.api.gui.widgets.PanelLM;
import net.minecraft.client.gui.*;

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
