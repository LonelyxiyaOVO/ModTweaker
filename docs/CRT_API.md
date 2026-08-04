# ModTweaker CRT API

This is the CRT-facing API reference for the ports in ModTweaker. Each entry
uses the same structure as the CraftTweaker 1.12 documentation: package name,
addition methods, removal methods, and a runnable script example.

`IIngredient` parameters accept item stacks and, where supported by the
handler, ore-dictionary entries such as `<ore:ingotIron>`.

## Actually Additions

### Ball of Fur

#### Package

`mods.actuallyadditions.BallOfFur`

#### Addition

```zenscript
mods.actuallyadditions.BallOfFur.addReturn(<minecraft:diamond>, 5);
```

#### Removal

```zenscript
mods.actuallyadditions.BallOfFur.removeReturn(<minecraft:diamond>);
mods.actuallyadditions.BallOfFur.removeAll();
```

### Atomic Reconstructor

#### Package

`mods.actuallyadditions.AtomicReconstructor`

#### Addition

```zenscript
mods.actuallyadditions.AtomicReconstructor.addRecipe(<minecraft:diamond>, <minecraft:coal>, 5000);
```

#### Removal

```zenscript
mods.actuallyadditions.AtomicReconstructor.removeRecipe(<minecraft:diamond>);
mods.actuallyadditions.AtomicReconstructor.removeAll();
```

### Crusher

#### Package

`mods.actuallyadditions.Crusher`

#### Addition

```zenscript
mods.actuallyadditions.Crusher.addRecipe(<minecraft:iron_ingot>, <ore:oreIron>);
```

#### Removal

```zenscript
mods.actuallyadditions.Crusher.removeRecipe(<minecraft:iron_ingot>);
mods.actuallyadditions.Crusher.removeAll();
```

### Empowerer

#### Package

`mods.actuallyadditions.Empowerer`

#### Addition

```zenscript
mods.actuallyadditions.Empowerer.addRecipe(
    <minecraft:diamond>, <minecraft:coal>, <minecraft:redstone>,
    <minecraft:lapis_block>, <minecraft:gold_ingot>, <minecraft:iron_ingot>,
    1000, 100
);
```

#### Removal

```zenscript
mods.actuallyadditions.Empowerer.removeRecipe(<minecraft:diamond>);
mods.actuallyadditions.Empowerer.removeAll();
```

### Oil Generator

#### Package

`mods.actuallyadditions.OilGen`

#### Addition and Removal

```zenscript
mods.actuallyadditions.OilGen.addRecipe(<liquid:oil>, 20, 100);
mods.actuallyadditions.OilGen.removeRecipe(<liquid:oil>);
mods.actuallyadditions.OilGen.removeAll();
```

## Better With Mods

### Filtered Hopper

#### Package

`mods.betterwithmods.FilteredHopper`

#### Addition

```zenscript
mods.betterwithmods.FilteredHopper.addFilter("my_filter", <ore:ingotIron>);
mods.betterwithmods.FilteredHopper.addFilteredItem("my_filter", <minecraft:gold_ingot>);
mods.betterwithmods.FilteredHopper.addFilterRecipe(
    "my_filter", <minecraft:wheat>, [<minecraft:flour>], []
);
```

#### Removal

```zenscript
mods.betterwithmods.FilteredHopper.clearFilter("my_filter");
mods.betterwithmods.FilteredHopper.removeFilter("my_filter");
mods.betterwithmods.FilteredHopper.removeByFilter(<ore:ingotIron>);
mods.betterwithmods.FilteredHopper.removeByFiltered(<minecraft:gold_ingot>);
mods.betterwithmods.FilteredHopper.removeAll();
```

### Mill

#### Package

`mods.betterwithmods.Mill`

#### Addition

```zenscript
mods.betterwithmods.Mill.addRecipe(
    [<minecraft:wheat>], [<minecraft:flour>]
);
```

#### Removal

```zenscript
mods.betterwithmods.Mill.removeByInput(<minecraft:wheat>);
mods.betterwithmods.Mill.remove([<minecraft:flour>]);
mods.betterwithmods.Mill.removeAll();
```

## Blood Magic

### Blood Altar

#### Package

`mods.bloodmagic.BloodAltar`

#### Addition and Removal

```zenscript
mods.bloodmagic.BloodAltar.addRecipe(
    <minecraft:gold_ingot>, <ore:ingotIron>, 2, 5000, 20, 20
);
mods.bloodmagic.BloodAltar.removeRecipe(<ore:ingotGold>);
```

### Alchemy Array

#### Package

`mods.bloodmagic.AlchemyArray`

```zenscript
mods.bloodmagic.AlchemyArray.addRecipe(
    <minecraft:diamond>, <minecraft:coal>, <minecraft:redstone>
);
mods.bloodmagic.AlchemyArray.removeRecipe(<minecraft:coal>, <minecraft:redstone>);
```

### Meteor, Sacrificial and Tranquility

#### Packages

`mods.bloodmagic.Meteor`, `mods.bloodmagic.Sacrificial`,
`mods.bloodmagic.Tranquility`

```zenscript
mods.bloodmagic.Meteor.removeAll();
mods.bloodmagic.Sacrificial.add("minecraft:zombie", 100);
mods.bloodmagic.Sacrificial.remove("minecraft:zombie");
mods.bloodmagic.Tranquility.removeAll();
```

## Botania

### Mana Infusion

#### Package

`mods.botania.ManaInfusion`

```zenscript
mods.botania.ManaInfusion.addInfusion(<minecraft:diamond>, <minecraft:coal>, 500);
mods.botania.ManaInfusion.removeRecipe(<minecraft:diamond>);
mods.botania.ManaInfusion.removeRecipeByInput(<minecraft:coal>);
mods.botania.ManaInfusion.removeAll();
```

### Rune Altar and Elven Trade

#### Packages

`mods.botania.RuneAltar`, `mods.botania.ElvenTrade`

```zenscript
mods.botania.RuneAltar.addRecipe(
    <minecraft:diamond>, [<minecraft:coal>, <minecraft:redstone>], 500
);
mods.botania.RuneAltar.removeRecipeByInputs([<minecraft:coal>, <minecraft:redstone>]);
mods.botania.ElvenTrade.removeAll();
```

### Orechid and Magnet

#### Packages

`mods.botania.Orechid`, `mods.botania.OrechidIgnem`, `mods.botania.Magnet`

```zenscript
mods.botania.Orechid.addOre("oreCopper", 20);
mods.botania.Orechid.removeAll();
mods.botania.Magnet.addToBlacklist(<minecraft:stone>);
mods.botania.Magnet.removeFromBlacklist(<minecraft:stone>);
mods.botania.Magnet.removeAll();
```

## Chisel

### Carving

#### Package

`mods.chisel.Carving`

```zenscript
mods.chisel.Carving.addGroup("my_group");
mods.chisel.Carving.addVariation("my_group", <minecraft:stonebrick>);
mods.chisel.Carving.removeVariation("my_group", <minecraft:stonebrick>);
mods.chisel.Carving.removeAll();
```

## Extra Utilities 2

### Furnace, Enchanter and Resonator

#### Packages

`mods.extrautils2.Furnace`, `mods.extrautils2.Enchanter`,
`mods.extrautils2.Resonator`

```zenscript
mods.extrautils2.Furnace.add(<minecraft:iron_ingot>, <ore:oreIron>);
mods.extrautils2.Furnace.remove(<ore:oreIron>);
mods.extrautils2.Furnace.removeAll();

mods.extrautils2.Enchanter.remove(<minecraft:book>);
mods.extrautils2.Enchanter.removeAll();
mods.extrautils2.Resonator.removeByInput(<minecraft:iron_ingot>);
```

### Generator

#### Package

`mods.extrautils2.Generator`

```zenscript
mods.extrautils2.Generator.remove("extrautils2:generator", <minecraft:coal>);
mods.extrautils2.Generator.remove("extrautils2:generator", <liquid:lava>);
mods.extrautils2.Generator.removeByGenerator("extrautils2:generator");
mods.extrautils2.Generator.removeAll();
```

## Forestry

### Bee Mutations and Produce

#### Packages

`mods.forestry.BeeMutations`, `mods.forestry.BeeProduce`

```zenscript
mods.forestry.BeeMutations.add(
    "forestry.speciesForest", "forestry.speciesMeadows", "forestry.speciesCommon", 10
);
mods.forestry.BeeMutations.removeByOutput("forestry.speciesForest");
mods.forestry.BeeProduce.add("forestry.speciesForest", <minecraft:apple>, 0.25);
mods.forestry.BeeProduce.removeProduct("forestry.speciesForest", <minecraft:apple>);
mods.forestry.BeeProduce.removeAll();
```

### Machine Removal

#### Packages

`mods.forestry.Carpenter`, `mods.forestry.Centrifuge`,
`mods.forestry.Fermenter`, `mods.forestry.Squeezer`,
`mods.forestry.Still`, `mods.forestry.ThermionicFabricator`

```zenscript
mods.forestry.Carpenter.removeByBox(<minecraft:chest>);
mods.forestry.Centrifuge.removeByOutput(<minecraft:honeycomb>);
mods.forestry.Fermenter.removeByOutput(<liquid:ethanol>);
mods.forestry.Squeezer.removeByInput([<minecraft:apple>]);
mods.forestry.ThermionicFabricator.removeByFluid(<liquid:glass>);
mods.forestry.ThermionicFabricator.removeSmeltingByOutput(<liquid:glass>);
mods.forestry.Still.removeAll();
```

## Inspirations

### Anvil Smashing

#### Package

`mods.inspirations.AnvilSmashing`

```zenscript
mods.inspirations.AnvilSmashing.add(<minecraft:iron_block>, <minecraft:iron_ingot>);
mods.inspirations.AnvilSmashing.removeByInput(<minecraft:iron_block>);
mods.inspirations.AnvilSmashing.removeByOutput(<minecraft:iron_ingot>);
mods.inspirations.AnvilSmashing.removeAll();
```

## Tinkers' Construct and Tinkers' Complement

### Packages

`mods.tconstruct.Melting`, `mods.tconstruct.Alloy`,
`mods.tconstruct.Casting`, `mods.tcomplement.Melter`,
`mods.tcomplement.Blacklist`

```zenscript
mods.tconstruct.Melting.addRecipe(<liquid:molten_iron>, <minecraft:iron_ingot>, 800);
mods.tconstruct.Melting.removeRecipe(<liquid:molten_iron>, <minecraft:iron_ingot>);
mods.tconstruct.Alloy.addRecipe(
    <liquid:molten_bronze>, [<liquid:molten_copper>, <liquid:molten_tin>]
);
mods.tcomplement.Melter.removeByInput(<minecraft:iron_ingot>);
mods.tcomplement.Melter.removeByOutput(<liquid:molten_iron>);
mods.tcomplement.Blacklist.addRecipe(<liquid:molten_gold>, <ore:ingotGold>);
mods.tcomplement.Blacklist.removeAll();
```

## Thaumcraft

### Crucible and Research

#### Packages

`mods.thaumcraft.Crucible`, `mods.thaumcraft.Research`

```zenscript
mods.thaumcraft.Crucible.registerRecipe(
    "example_crucible", "UNLOCKALCHEMY@3", <minecraft:diamond>,
    <ore:ingotGold>, [<aspect:metallum> * 5]
);
mods.thaumcraft.Crucible.removeRecipe(<minecraft:diamond>);
mods.thaumcraft.Research.addNode("EXAMPLE", "NODE", "example.node", 1, 1);
mods.thaumcraft.Research.connectNodes("EXAMPLE", "BASICS", "NODE");
mods.thaumcraft.Research.removeNode("EXAMPLE", "NODE");
```

## Thermal Expansion

### Furnace, Smelter and Transposer

#### Packages

`mods.thermalexpansion.Furnace`, `mods.thermalexpansion.Smelter`,
`mods.thermalexpansion.Transposer`

```zenscript
mods.thermalexpansion.Furnace.addRecipe(<minecraft:iron_ingot>, <ore:oreIron>, 2000);
mods.thermalexpansion.Furnace.removeRecipeByInput(<ore:oreIron>);
mods.thermalexpansion.Furnace.removeAll();
mods.thermalexpansion.Smelter.removeRecipeByOutput(<minecraft:iron_ingot>);
mods.thermalexpansion.Transposer.removeAll();
```

### Dynamos and Devices

#### Packages

`mods.thermalexpansion.CompressionDynamo`,
`mods.thermalexpansion.ReactantDynamo`,
`mods.thermalexpansion.Centrifuge`, `mods.thermalexpansion.Refinery`

```zenscript
mods.thermalexpansion.CompressionDynamo.removeAll();
mods.thermalexpansion.ReactantDynamo.removeAll();
mods.thermalexpansion.Centrifuge.removeRecipeByOutput(<minecraft:iron_ingot>);
mods.thermalexpansion.Refinery.removeAll();
```

## Intentionally unavailable GS-only features

Groovy `Closure` callbacks, GroovyScript RecipeBuilder objects, internal
recipe-object registration, virtualized registries, reload rollback and ASM
accessors are not CRT APIs and are intentionally not documented as available
methods here.
