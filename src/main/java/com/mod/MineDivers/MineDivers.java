package com.mod.minedivers;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(minedivers.MODID)
public final class minedivers {
    public static final String MODID = "minedivers";
    
    // Create DeferredRegister for items
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    
    public minedivers() {
        // Register the DeferredRegister
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}