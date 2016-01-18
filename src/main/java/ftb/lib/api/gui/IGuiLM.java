package ftb.lib.api.gui;

import ftb.lib.api.gui.widgets.PanelLM;
import net.minecraft.client.gui.*;
import net.minecraftforge.fml.relauncher.*;

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
