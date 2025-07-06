package mcp.mobius.waila.api.event;

public interface IWailaEventListener {

    void onWailaPluginRegister(WailaRegisterEvent.Plugin event);

    void onWailaConfigRegister(WailaRegisterEvent.Config event);

}
