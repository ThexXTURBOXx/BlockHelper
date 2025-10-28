package mcp.mobius.waila.addons.nei;

import codechicken.nei.NEIClientConfig;
import codechicken.nei.forge.GuiContainerManager;
import codechicken.nei.forge.IContainerInputHandler;
import java.util.Map;
import mcp.mobius.waila.gui.screens.info.ScreenEnchants;
import mcp.mobius.waila.overlay.DisplayUtil;
import mcp.mobius.waila.utils.Constants;
import mcp.mobius.waila.utils.ModIdentification;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import static mcp.mobius.waila.api.SpecialChars.BLUE;
import static mcp.mobius.waila.api.SpecialChars.ITALIC;
import static mcp.mobius.waila.api.SpecialChars.RED;
import static mcp.mobius.waila.api.SpecialChars.WHITE;
import static mcp.mobius.waila.api.SpecialChars.YELLOW;

public final class HandlerEnchants implements IContainerInputHandler {

    public static final IContainerInputHandler INSTANCE = new HandlerEnchants();

    private HandlerEnchants() {
    }

    @Override
    public boolean keyTyped(GuiContainer gui, char keyChar, int keyCode) {
        return false;
    }

    @Override
    public void onKeyTyped(GuiContainer gui, char keyChar, int keyID) {
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean lastKeyTyped(GuiContainer gui, char keyChar, int keyID) {
        GuiContainerManager manager = gui.manager;
        ItemStack stackover = manager.getStackMouseOver();
        if (stackover == null)
            return false;

        if (keyID == NEIClientConfig.getKeyBinding(Constants.BIND_SCREEN_ENCH)) {
            //try{
            int itemEnchantability = stackover.getItem().getItemEnchantability();
            if (itemEnchantability == 0) {
                return false;
            }

            Minecraft mc = Minecraft.getMinecraft();
            ScreenEnchants screen = new ScreenEnchants(mc.currentScreen);
            screen.setStack(stackover);
            screen.setName(DisplayUtil.itemDisplayNameShort(stackover));
            screen.setEnchantability(String.valueOf(itemEnchantability));

            Enchantment[] enchants;
            if (stackover.getItem() == Item.book)
                enchants = Enchantment.field_92090_c;
            else
                enchants = Enchantment.enchantmentsList;

            for (Enchantment enchant : enchants) {
                boolean isCompatible = true;
                int level = 0;
                boolean isApplied = false;

                if (enchant == null) {
                    continue;
                }
                if (enchant.canApplyAtEnchantingTable(stackover) || stackover.getItem() == Item.book) {

                    if (stackover.isItemEnchanted()) {
                        Map<Integer, Integer> stackenchants =
                                (Map<Integer, Integer>) EnchantmentHelper.getEnchantments(stackover);
                        // Use entrySet() instead of keySet() to avoid double lookup
                        for (Map.Entry<Integer, Integer> entry : stackenchants.entrySet()) {
                            Integer id = entry.getKey();
                            if (!enchant.canApplyTogether(Enchantment.enchantmentsList[id]))
                                isCompatible = false;
                            if (id == enchant.effectId) {
                                isApplied = true;
                                level = entry.getValue();
                            }
                        }
                    }

                    for (int lvl = enchant.getMinLevel(); lvl <= enchant.getMaxLevel(); lvl++) {
                        int minEnchantEnchantability = enchant.getMinEnchantability(lvl);
                        int maxEnchantEnchantability = enchant.getMaxEnchantability(lvl);

                        int minItemEnchantability = 1;
                        int maxItemEnchantability = 1 + itemEnchantability / 2;

                        int minModifiedEnchantability = (int) (0.85 * minItemEnchantability + 0.5);
                        int maxModifiedEnchantability = (int) (1.15 * maxItemEnchantability + 0.5);

                        int minLevel = (int) ((minEnchantEnchantability - minModifiedEnchantability) / 1.15);
                        int maxLevel = (int) ((maxEnchantEnchantability - maxModifiedEnchantability) / 0.85);

                        String colorcode = isCompatible ? WHITE : RED;

                        if (isApplied && lvl == level)
                            colorcode = YELLOW;

                        screen.addRow(colorcode + enchant.getTranslatedName(lvl),
                                colorcode + minLevel,
                                colorcode + maxLevel,
                                colorcode + enchant.getWeight(),
                                BLUE + ITALIC + ModIdentification.nameFromObject(enchant));
                    }
                }
            }
            mc.displayGuiScreen(screen);
        }

        return false;
    }

    @Override
    public boolean mouseClicked(GuiContainer gui, int mousex, int mousey, int button) {
        return false;
    }

    @Override
    public void onMouseClicked(GuiContainer gui, int mousex, int mousey, int button) {
    }

    @Override
    public void onMouseUp(GuiContainer gui, int mousex, int mousey, int button) {
    }

    @Override
    public boolean mouseScrolled(GuiContainer gui, int mousex, int mousey, int scrolled) {
        return false;
    }

    @Override
    public void onMouseScrolled(GuiContainer gui, int mousex, int mousey, int scrolled) {
    }

    @Override
    public void onMouseDragged(GuiContainer gui, int mousex, int mousey, int button, long heldTime) {
    }

}
