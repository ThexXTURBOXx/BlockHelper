package mcp.mobius.waila.proxy;

import cpw.mods.fml.common.FMLCommonHandler;

public class ProxyServer extends ProxyCommon {

    public ProxyServer() {
        // This function *may* yield Side.BUKKIT
        super(FMLCommonHandler.instance().getSide());
    }

}
