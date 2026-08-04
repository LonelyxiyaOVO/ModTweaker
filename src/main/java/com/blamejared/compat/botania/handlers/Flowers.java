package com.blamejared.compat.botania.handlers;

import com.blamejared.ModTweaker;
import com.blamejared.mtlib.utils.BaseAction;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.api.subtile.signature.BasicSignature;

@ZenClass("mods.botania.Flowers")
@ModOnly("botania")
@ZenRegister
public class Flowers {

    @ZenMethod
    public static void registerFlower(String name, String className) {
        ModTweaker.LATE_ADDITIONS.add(new BaseAction("Botania Flowers") {
            @Override
            public void apply() {
                try {
                    Class<?> type = Class.forName(className);
                    if (!SubTileEntity.class.isAssignableFrom(type)) {
                        throw new IllegalArgumentException(className + " is not a Botania SubTileEntity");
                    }
                    Class<? extends SubTileEntity> flower = type.asSubclass(SubTileEntity.class);
                    BotaniaAPI.registerSubTile(name, flower);
                    BotaniaAPI.registerSubTileSignature(flower, new BasicSignature(name));
                    BotaniaAPI.addSubTileToCreativeMenu(name);
                } catch (ReflectiveOperationException | RuntimeException e) {
                    throw new IllegalStateException("Unable to register Botania flower " + name, e);
                }
            }

            @Override
            protected String getRecipeInfo() {
                return name + " -> " + className;
            }
        });
    }

    @ZenMethod
    public static void registerFlowerWithMini(String name, String flowerClass, String miniClass) {
        ModTweaker.LATE_ADDITIONS.add(new BaseAction("Botania Flowers") {
            @Override
            public void apply() {
                try {
                    Class<? extends SubTileEntity> flower = Class.forName(flowerClass).asSubclass(SubTileEntity.class);
                    Class<? extends SubTileEntity> mini = Class.forName(miniClass).asSubclass(SubTileEntity.class);
                    BotaniaAPI.registerSubTile(name, flower);
                    BotaniaAPI.registerSubTileSignature(flower, new BasicSignature(name));
                    BotaniaAPI.registerMiniSubTile(name + "Chibi", mini, name);
                    BotaniaAPI.registerSubTileSignature(mini, new BasicSignature(name + "Chibi"));
                    BotaniaAPI.addSubTileToCreativeMenu(name);
                } catch (ReflectiveOperationException e) {
                    throw new IllegalStateException("Unable to register Botania flower " + name, e);
                }
            }

            @Override
            protected String getRecipeInfo() {
                return name;
            }
        });
    }
}
