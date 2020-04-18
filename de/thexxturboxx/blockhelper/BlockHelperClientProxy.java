package de.thexxturboxx.blockhelper;

import java.io.File;

import cpw.mods.fml.relauncher.FMLInjectionData;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;

public class BlockHelperClientProxy extends BlockHelperCommonProxy {

	static int mode;

	@Override
	public void registerRenderers() {
		super.registerRenderers();
	}

	@Override
	public EntityPlayer getPlayer() {
		return Minecraft.getMinecraft().thePlayer;
	}

	@Override
	public World getWorld() {
		return Minecraft.getMinecraft().theWorld;
	}

	@Override
	public void load(mod_BlockHelper instance) {
		super.load(instance);
		mod_BlockHelper.isClient = true;
		Configuration cfg = new Configuration(new File((File) FMLInjectionData.data()[6], "config/BlockHelper.cfg"));
		mode = cfg.get("General", "Mode", 0, "0 = DEFAULT; 1 = LIGHT").getInt();
	}

}