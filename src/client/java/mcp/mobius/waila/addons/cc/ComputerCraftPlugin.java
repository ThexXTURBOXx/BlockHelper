package mcp.mobius.waila.addons.cc;

import java.util.logging.Level;
import mcp.mobius.waila.addons.vanilla.StackDropFixer;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.utils.AccessHelper;
import net.minecraft.src.mod_BlockHelper;

public final class ComputerCraftPlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new ComputerCraftPlugin();

    public static Class<?> BlockPeripheral;

    private ComputerCraftPlugin() {
    }

    @Override
    public boolean shouldRegister() {
        try {
            AccessHelper.getClass("mod_ComputerCraft");
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[ComputerCraft] Mod not found.");
            return false;
        }
        try {
            BlockPeripheral = AccessHelper.getClass("dan200.computer.shared.BlockPeripheral");
            mod_BlockHelper.LOG.log(Level.INFO, "[ComputerCraft] Mod found.");
            return true;
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[ComputerCraft] No need to fix versions < 1.3.");
            return false;
        }
    }

    @Override
    public void register(IRegistrar registrar) {
        try {
            registrar.registerStackProvider(StackDropFixer.DEFAULT, BlockPeripheral);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[ComputerCraft] Error while loading peripheral hooks.", t);
        }
    }

}
