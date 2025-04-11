package mcp.mobius.waila.commands;

import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IEntityProvider;
import mcp.mobius.waila.api.impl.WailaRegistrar;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class CommandDumpHandlers extends CommandBase {

    @Override
    public String getCommandName() {
        return "dumphandlers";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/dumphandlers";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {

        System.out.print("\n\n== HEAD BLOCK PROVIDERS ==\n");
        for (Class<?> clazz : WailaRegistrar.instance().headBlockProviders.keySet()) {
            System.out.printf("+ %s +\n", clazz.getName());
            for (IDataProvider provider : WailaRegistrar.instance().headBlockProviders.get(clazz)) {
                System.out.printf("  - %s\n", provider.getClass().getName());
            }
            System.out.print("\n");
        }

        System.out.print("\n\n== BODY BLOCK PROVIDERS ==\n");
        for (Class<?> clazz : WailaRegistrar.instance().bodyBlockProviders.keySet()) {
            System.out.printf("+ %s +\n", clazz.getName());
            for (IDataProvider provider : WailaRegistrar.instance().bodyBlockProviders.get(clazz)) {
                System.out.printf("  - %s\n", provider.getClass().getName());
            }
            System.out.print("\n");
        }

        System.out.print("\n\n== TAIL BLOCK PROVIDERS ==\n");
        for (Class<?> clazz : WailaRegistrar.instance().tailBlockProviders.keySet()) {
            System.out.printf("+ %s +\n", clazz.getName());
            for (IDataProvider provider : WailaRegistrar.instance().tailBlockProviders.get(clazz)) {
                System.out.printf("  - %s\n", provider.getClass().getName());
            }
            System.out.print("\n");
        }

        System.out.print("\n\n== STACK BLOCK PROVIDERS ==\n");
        for (Class<?> clazz : WailaRegistrar.instance().stackBlockProviders.keySet()) {
            System.out.printf("+ %s +\n", clazz.getName());
            for (IDataProvider provider : WailaRegistrar.instance().stackBlockProviders.get(clazz)) {
                System.out.printf("  - %s\n", provider.getClass().getName());
            }
            System.out.print("\n");
        }

        System.out.print("\n\n== HEAD ENTITY PROVIDERS ==\n");
        for (Class<?> clazz : WailaRegistrar.instance().headEntityProviders.keySet()) {
            System.out.printf("+ %s +\n", clazz.getName());
            for (IEntityProvider provider : WailaRegistrar.instance().headEntityProviders.get(clazz)) {
                System.out.printf("  - %s\n", provider.getClass().getName());
            }
            System.out.print("\n");
        }

        System.out.print("\n\n== BODY ENTITY PROVIDERS ==\n");
        for (Class<?> clazz : WailaRegistrar.instance().bodyEntityProviders.keySet()) {
            System.out.printf("+ %s +\n", clazz.getName());
            for (IEntityProvider provider : WailaRegistrar.instance().bodyEntityProviders.get(clazz)) {
                System.out.printf("  - %s\n", provider.getClass().getName());
            }
            System.out.print("\n");
        }

        System.out.print("\n\n== TAIL ENTITY PROVIDERS ==\n");
        for (Class<?> clazz : WailaRegistrar.instance().tailEntityProviders.keySet()) {
            System.out.printf("+ %s +\n", clazz.getName());
            for (IEntityProvider provider : WailaRegistrar.instance().tailEntityProviders.get(clazz)) {
                System.out.printf("  - %s\n", provider.getClass().getName());
            }
            System.out.print("\n");
        }

        System.out.print("\n\n== STACK ENTITY PROVIDERS ==\n");
        for (Class<?> clazz : WailaRegistrar.instance().overrideEntityProviders.keySet()) {
            System.out.printf("+ %s +\n", clazz.getName());
            for (IEntityProvider provider : WailaRegistrar.instance().overrideEntityProviders.get(clazz)) {
                System.out.printf("  - %s\n", provider.getClass().getName());
            }
            System.out.print("\n");
        }

    }

    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        //if ((sender instanceof EntityPlayerMP) && ((EntityPlayerMP)sender).playerNetServerHandler.netManager
        // instanceof MemoryConnection) return true;
        return super.canCommandSenderUseCommand(sender);
    }

}
