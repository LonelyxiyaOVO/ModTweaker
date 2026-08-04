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

## Complete method index

The following index lists the remaining public CRT methods so that every
registered compatibility class has a discoverable entry point.

### Better With Mods

- `mods.betterwithmods.FilteredHopper`: `addFilter(name, filter)`, `addFilteredItem(name, item)`, `addFilterRecipe(name, input, outputs, secondary)`, `addSoulUrnRecipe(input, outputs, secondary)`, `clearFilter(name)`, `removeFilter(name)`, `removeByFilter(filter)`, `removeByFiltered(filtered)`, `removeRecipe(outputs, secondary)`, `removeRecipeByInput(input)`, `removeAll()`
- `mods.betterwithmods.Mill`: `builder()`, `addRecipe(inputs, outputs)`, `remove(output)`, `removeByInput(input)`, `removeAll()`
- `mods.betterwithmods.HeatRegistry`: `addHeatSource(state, heat)`, `addHeatSource(states, displayStack, heat)`, `addHeatSource(stack, heat)`, `addHeatSource(input, heat)`
- `mods.betterwithmods.Anvil`: `addShaped(output, inputs)`, `addShapedFixed(output, inputs)`, `addShapeless(output, inputs)`, `removeShaped(output, inputs)`, `removeShapedFixed(output, inputs)`, `removeShapeless(output, inputs)`, `removeAll()`
- `mods.betterwithmods.Kiln`: `builder()`, `add(input, outputs)`, `remove(input)`, `remove(outputs)`, `removeAll()`, `registerBlock(block)`
- `mods.betterwithmods.Saw`: `builder()`, `add(input, outputs)`, `remove(input)`, `remove(outputs)`, `removeAll()`
- `mods.betterwithmods.Turntable`: `builder()`, `add(inputBlock, additionalOutput)`, `add(inputBlock, productState, additionalOutput)`, `remove(input)`, `removeByProductState(output)`, `removeAll()`
- `mods.betterwithmods.Cauldron`, `mods.betterwithmods.Crucible`: `builder()`, `addStoked(inputs, outputs)`, `addUnstoked(inputs, outputs)`, `add(output, secondaryOutput, inputs)`, `add(output, inputs)`, `remove(outputs)`, `removeAll()`
- `mods.betterwithmods.Bellows`, `mods.betterwithmods.Buoyancy`, `mods.betterwithmods.Movement`: `set(input, value)`
- `mods.betterwithmods.Misc`: `setFurnaceSmeltingTime(input, time)`
- `mods.betterwithmods.PulleyManager`: `addPulleyBlock(state)`

### Extra Utilities 2

- `mods.extrautils2.Crusher`: `add(output, input, secondaryOutput, secondaryChance)`, `remove(input)`, `removeAll()`
- `mods.extrautils2.Enchanter`: `add(output, input, lapis, energy, time, enchantName)`, `remove(input)`, `removeAll()`
- `mods.extrautils2.Furnace`: `add(output, input)`, `remove(input)`, `removeAll()`
- `mods.extrautils2.Resonator`: `add(output, input, energy, addOwnerTag)`, `remove(output)`, `removeByInput(input)`
- `mods.extrautils2.Generator`: `remove(generator, itemInput)`, `remove(generator, fluidInput)`, `removeByGenerator(generator)`, `removeAll()`

### Thermal Expansion

- Devices: `Brewer`, `Charger`, `Fisher`, `FisherBait`, `Tapper`, `TapperFertilizer`, `TapperTree`, `XpCollector`
- Machines: `Centrifuge`, `Compactor`, `Crucible`, `Diffuser`, `Enchanter`, `Extruder`, `Furnace`, `FurnacePyrolysis`, `InductionSmelter`, `Insolator`, `Precipitator`, `Pulverizer`, `RedstoneFurnace`, `Refinery`, `RefineryPotion`, `SawMill`, `Smelter`, `Transposer`
- Dynamos: `CompressionDynamo`, `EnervationDynamo`, `MagmaticDynamo`, `NumisticDynamo`, `ReactantDynamo`, `SteamDynamo`
- Other: `Coolant`, `Factorizer`, `Lapidary`

Every Thermal Expansion class listed above exposes its documented `add` or
`addRecipe` methods, matching `remove` methods, and/or `removeAll()` methods as
listed in the source-level API names below:

- `Brewer`: `addRecipe`, `removeRecipeByInput`, `removeRecipeByOutput`, `removeAll`
- `Charger`: `addRecipe`, `removeRecipeByInput`, `removeRecipeByOutput`, `removeAll`
- `Fisher`, `FisherBait`, `TapperFertilizer`: `addRecipe`, `removeRecipe`, `removeAll`
- `TapperTree`: `addRecipe`, `removeRecipeByLog`, `removeRecipeByLeaf`, `removeAll`
- `Tapper`: `addItem`, `addBlock`, `removeItemByInput`, `removeBlockByInput`, `removeAllItems`, `removeAllBlocks`, `removeAll`
- `Furnace`: `addRecipe`, `addFood`, `removeFood`, `removeRecipeByInput`, `removeRecipeByOutput`, `removeAllFood`, `removeAll`
- `FurnacePyrolysis`: `addRecipe`, `removeRecipeByInput`, `removeRecipeByOutput`, `removeAll`
- `Smelter`: `addRecipe`, `addFlux`, `removeFlux`, `removeRecipeByInput`, `removeRecipeByOutput`, `removeAll`
- `Diffuser`, `Coolant`, `Lapidary`: add/remove methods and `removeAll`
- `Centrifuge`: `addRecipe`, `addRecipeMob`, `removeRecipe`, `removeRecipeMob`, `removeRecipeByOutput`, `removeRecipeMobByOutput`, `removeAll`
- `Compactor`, `Factorizer`: mode-specific add/remove methods and `removeAll`
- `Crucible`, `Enchanter`, `Insolator`, `Pulverizer`, `SawMill`: add/remove methods and `removeAll`
- `Extruder`: `addRecipe`, `removeRecipeByInput`, `removeRecipeByOutput`, `removeByType`, `removeAll`
- `Refinery`: recipe, potion, fossil-fuel and bio-fuel add/remove methods plus clear methods
- `RefineryPotion`: `addRecipe`, `removeRecipeByInput`, `removeRecipeByOutput`, `removeAll`
- `Transposer`: extract/fill add/remove methods, `removeAllExtractRecipes`, `removeAllFillRecipes`, `removeAll`
- Dynamos: reaction/fuel methods and `removeAll()`; `ReactantDynamo` also exposes elemental reactant methods

### Botania

- `Brew`: `addRecipe`, `removeRecipe`, `removeRecipeByInput`, `removeAll`
- `ManaInfusion`: `addInfusion`, `addAlchemy`, `addConjuration`, `removeRecipe`, `removeRecipeByInput`, `removeRecipeByCatalyst`, `removeAll`
- `RuneAltar`: `addRecipe`, `removeRecipe`, `removeRecipeByInput`, `removeRecipeByInputs`, `removeAll`
- `ElvenTrade`: `addRecipe`, `removeRecipe`, `removeRecipeByInput`, `removeRecipeByInputs`, `removeAll`
- `Magnet`: `addToBlacklist`, `removeFromBlacklist`, `isInBlacklist`, `removeAll`
- `Apothecary`: `addRecipe`, `removeRecipe`, `removeAll`
- `Orechid`, `OrechidIgnem`: `addOre`, `removeOre`, `removeAll`
- `PureDaisy`: `addRecipe`, `removeRecipe`, `removeRecipeByInput`, `removeAll`
- `Flowers`: `registerFlower`, `registerFlowerWithMini`
- `Knowledge`, `Lexicon`: knowledge and lexicon entry/page registration methods

### Forestry

- `BeeMutations`: `add`, `remove`, `removeByOutput`, `removeAll`
- `BeeProduce`: `add`, `removeProduct`, `removeSpecialty`, `removeAll(species)`, `removeAll()`
- `Carpenter`: `addRecipe`, `removeRecipe`, `removeByFluidInput`, `removeByBox`, `removeByInput`, `removeAll`
- `Centrifuge`: `addRecipe`, `removeRecipe`, `removeByOutput`, `removeByOutputs`, `removeAll`
- `Fermenter`: `addRecipe`, `removeRecipe`, `removeByInput`, `removeByCatalyst`, `removeByOutput`, `addFuel`, `removeFuel`, `removeAll`
- `Squeezer`: `addRecipe`, `removeRecipe`, `removeByOutput`, `removeByInput`, `removeByInputs`, `removeAll`
- `Still`: `addRecipe`, `removeRecipe`, `removeAll`
- `ThermionicFabricator`: `addSmelting`, `addCast`, `removeSmelting`, `removeCast`, `removeByFluid`, `removeByCatalyst`, `removeByInput`, `removeSmeltingByOutput`, `removeAll`
- `Moistener`: recipe and fuel add/remove methods plus `removeAllFuel()`
- `CharcoalPile`: wall add/remove methods

### Other compatibility classes

- `mods.actuallyadditions.BallOfFur`: `addReturn`, `removeReturn`, `removeAll`
- `mods.actuallyadditions.AtomicReconstructor`: `addRecipe`, `removeRecipe`, `removeAll`
- `mods.actuallyadditions.Compost`: `addRecipe`, `removeRecipe`, `removeAll`
- `mods.actuallyadditions.Crusher`: `addRecipe`, `removeRecipe`, `removeAll`
- `mods.actuallyadditions.Empowerer`: `addRecipe`, `removeRecipe`, `removeAll`
- `mods.actuallyadditions.OilGen`: `addRecipe`, `removeRecipe`, `removeAll`
- `mods.actuallyadditions.TreasureChest`: `addLoot`, `removeLoot`, `removeAll`
- `mods.actuallyadditions.MiningLens`: `addStoneOre`, `addNetherOre`, `removeStoneOre`, `removeNetherOre`
- `mods.chisel.Carving`: `addGroup`, `addVariation`, `removeGroup`, `removeVariation`, `setSound`, `removeAll`
- `mods.bloodmagic.AlchemyArray`: `addRecipe`, `removeRecipe`
- `mods.bloodmagic.AlchemyTable`: `addRecipe`, `addPotionRecipe`, `removeRecipe`
- `mods.bloodmagic.BloodAltar`: `addRecipe`, `removeRecipe`
- `mods.bloodmagic.Meteor`: `addRecipe`, `removeRecipe`, `removeRecipeByInput`, `removeRecipeByCatalyst`, `removeAll`
- `mods.bloodmagic.Sacrificial`: `add`, `remove`, `removeAll`
- `mods.bloodmagic.TartaricForge`: `addRecipe`, `removeRecipe`
- `mods.bloodmagic.Tranquility`: block/blockstate add/remove methods and `removeAll`
- `mods.inspirations.AnvilSmashing`: `add`, `addBreaking`, `removeByInput`, `removeByOutput`, `removeAll`
- `mods.tcomplement.Melter`: `removeByInput`, `removeByOutput`, `removeByInputAndOutput`
- `mods.tcomplement.Blacklist`: `addRecipe`, `removeRecipe`, `removeAll`
- `mods.tconstruct.Melting`: `addRecipe`, `addEntityMelting`, `removeRecipe`, `removeEntityMelting`
- `mods.tconstruct.Alloy`: `addRecipe`, `removeRecipe`
- `mods.tconstruct.Casting`: `addTableRecipe`, `addBasinRecipe`, `removeTableRecipe`, `removeBasinRecipe`
- `mods.tconstruct.Drying`: `addRecipe`, `removeRecipe`
- `mods.thaumcraft.Crucible`: `registerRecipe`, `removeRecipe`
- `mods.thaumcraft.ArcaneWorkbench`: shaped/shapeless registration and removal methods
- `mods.thaumcraft.Infusion`: `registerRecipe`, `removeRecipe`
- `mods.thaumcraft.DustTrigger`: `addSingleConversion`, `removeSingleConversion`
- `mods.thaumcraft.LootBag`: `addLoot`, `removeLoot`, `removeAll(rarity)`
- `mods.thaumcraft.SmeltingBonus`: `addSmeltingBonus`, `removeSmeltingBonus`
- `mods.thaumcraft.Warp`: `addWarp`, `setWarp`, `clearWarp`
- `mods.thaumcraft.Research`: category, node, connection, research-location and item/block scannable methods

The method index is intentionally limited to CRT-callable methods. Groovy
closures, internal GS recipe objects, virtualized registries and ASM accessors
are not included.
