package mcp.mobius.waila.api.event;

public interface IWailaEventListener {

    void onWailaRenderPre(WailaRenderEvent.Pre event);

    void onWailaRenderPost(WailaRenderEvent.Post event);

    void onWailaTooltip(WailaTooltipEvent event);

    void onWailaPluginRegister(WailaRegisterEvent.Plugin event);

    void onWailaConfigRegister(WailaRegisterEvent.Config event);

    void onClientFirstTickInWorld(ClientFirstTickInWorldEvent event);

}
