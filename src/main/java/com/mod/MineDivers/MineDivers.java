package com.mod.minedivers;

import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ThrowableItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.item.ThrowableItem;;

@Mod(minedivers.MODID)
public class minedivers {
    public static final String MODID = "minedivers";
    
    public static final DeferredRegister<Item> ITEMS = 
        DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = 
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    
    // Register your custom ball item that acts like a snowball with reduced distance
    public static final RegistryObject<Item> BALL = ITEMS.register("ball",
        () -> new ShortRangeSnowballItem(new Item.Properties()
            .setId(ITEMS.key("ball"))
            .stacksTo(16)
            .rarity(Rarity.COMMON)));
    
    // Register a creative tab with the name "minedivers"
    public static final RegistryObject<CreativeModeTab> minedivers_TAB = CREATIVE_MODE_TABS.register("minedivers_tab", 
        () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> new ItemStack(BALL.get()))
            .title(net.minecraft.network.chat.Component.translatable("itemGroup.minedivers"))
            .displayItems((parameters, output) -> {
                output.accept(BALL.get());
            }).build());
    
    public minedivers(FMLJavaModLoadingContext context) {
        var modBusGroup = context.getModBusGroup();
        
        // Register the DeferredRegister to the mod event bus
        ITEMS.register(modBusGroup);
        CREATIVE_MODE_TABS.register(modBusGroup);
        
        // Register creative tab contents
        BuildCreativeModeTabContentsEvent.getBus(modBusGroup).addListener(this::addCreative);
    }
    
    // Add items to creative tabs
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(BALL);
        }
    }
    
    // Custom throwable item with reduced distance
    public static class ShortRangeSnowballItem extends ThrowableItem {
        public ShortRangeSnowballItem(Item.Properties properties) {
            super(properties);
        }
        
        @Override
        protected void createProjectile(Level level, net.minecraft.world.entity.player.Player player, 
                                      net.minecraft.world.InteractionHand hand) {
            Snowball snowball = new Snowball(EntityType.SNOWBALL, level);
            snowball.setOwner(player);
            snowball.setItem(new ItemStack(this));
            snowball.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
            
            // Reduce the velocity for shorter throw distance (0.75 instead of the default 1.5)
            snowball.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 0.75F, 1.0F);
            
            level.addFreshEntity(snowball);
        }
        
        @Override
        public net.minecraft.sounds.SoundEvent getThrowSound() {
            return SoundEvents.SNOWBALL_THROW;
        }
    }
}