package com.feed_the_beast.ftbl.api.info;

import com.feed_the_beast.ftbl.api.MouseButton;
import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.notification.ClickAction;
import com.feed_the_beast.ftbl.gui.info.ButtonInfoExtendedTextLine;
import com.feed_the_beast.ftbl.gui.info.ButtonInfoTextLine;
import com.feed_the_beast.ftbl.gui.info.GuiInfo;
import com.feed_the_beast.ftbl.util.JsonHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by LatvianModder on 20.03.2016.
 */
public class InfoExtendedTextLine extends InfoTextLine
{
    protected ITextComponent text;
    private ClickAction clickAction;
    private List<ITextComponent> hover;
    
    public InfoExtendedTextLine(InfoPage c, ITextComponent cc)
    {
        super(c, null);
        text = cc;
        
        if(text != null)
        {
            ClickEvent clickEvent = text.getStyle().getClickEvent();
            if(clickEvent != null)
            {
                clickAction = ClickAction.from(clickEvent);
            }
            
            HoverEvent hoverEvent = text.getStyle().getHoverEvent();
            if(hoverEvent != null && hoverEvent.getAction() == HoverEvent.Action.SHOW_TEXT)
            {
                hover = Collections.singletonList(hoverEvent.getValue());
            }
        }
    }
    
    @Override
    public ITextComponent getText()
    { return text; }
    
    @Override
    @SideOnly(Side.CLIENT)
    public ButtonInfoTextLine createWidget(GuiInfo gui)
    { return new ButtonInfoExtendedTextLine(gui, this); }
    
    public List<ITextComponent> getHover()
    { return hover; }
    
    @SideOnly(Side.CLIENT)
    public boolean hasClickAction()
    { return clickAction != null; }
    
    @SideOnly(Side.CLIENT)
    public void onClicked(MouseButton button)
    {
        if(clickAction != null)
        {
            FTBLibClient.playClickSound();
            clickAction.onClicked(button);
        }
    }
    
    @Override
    public void fromJson(JsonElement e)
    {
        JsonObject o = e.getAsJsonObject();
        
        text = o.has("text") ? JsonHelper.deserializeICC(o.get("text")) : null;
        
        if(o.has("click"))
        {
            clickAction = new ClickAction();
            clickAction.fromJson(o.get("click"));
        }
        else { clickAction = null; }
        
        if(o.has("hover"))
        {
            hover = new ArrayList<>();
            
            JsonElement e1 = o.get("hover");
            
            if(e1.isJsonPrimitive()) { hover.add(JsonHelper.deserializeICC(e1)); }
            else
            {
                for(JsonElement e2 : o.get("hover").getAsJsonArray())
                {
                    hover.add(JsonHelper.deserializeICC(e2));
                }
            }
            
            if(hover.isEmpty()) { hover = null; }
        }
        else { hover = null; }
    }
    
    @Override
    public JsonElement getSerializableElement()
    {
        JsonObject o = new JsonObject();
        if(text != null) { o.add("text", JsonHelper.serializeICC(text)); }
        
        if(clickAction != null)
        {
            o.add("click", clickAction.getSerializableElement());
        }
        
        if(hover != null && !hover.isEmpty())
        {
            if(hover.size() == 1)
            {
                o.add("hover", JsonHelper.serializeICC(hover.get(0)));
            }
            else
            {
                JsonArray a = new JsonArray();
                for(ITextComponent c : hover)
                {
                    a.add(JsonHelper.serializeICC(c));
                }
                
                o.add("hover", a);
            }
        }
        
        return o;
    }
    
    public void setClickAction(ClickAction a)
    { clickAction = a; }
    
    public void setHover(List<ITextComponent> h)
    {
        if(h == null || h.isEmpty()) { hover = null; }
        else
        {
            hover = new ArrayList<>(h.size());
            hover.addAll(h);
        }
    }
}