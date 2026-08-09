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
// Normal recipe: all matched inputs are consumed.
mods.botania.RuneAltar.addRecipe(
    <minecraft:diamond>, [<minecraft:coal>, <minecraft:redstone>], 500
);
mods.botania.RuneAltar.removeRecipeByInputs([<minecraft:coal>, <minecraft:redstone>]);
mods.botania.ElvenTrade.removeAll();
```

With a reusable input, add the `.reuse()` transformer explicitly:

```zenscript
// Reusable recipe: coal is returned after a successful craft.
mods.botania.RuneAltar.addRecipe(
    <minecraft:emerald>, [<minecraft:coal>.reuse(), <minecraft:redstone>], 500
);
```

`IIngredient.reuse()` is supported for CRT Rune Altar recipes. The matching
ingredient is returned after a successful craft; normal ingredients continue to
be consumed. This behavior applies to recipes added through
`mods.botania.RuneAltar.addRecipe` and does not change native Botania recipes.

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

## PizzaCraft

### Bakeware

#### Package

`mods.pizzacraft.Bakeware`

#### Addition

```zenscript
// Shaped: use rows followed by character/ingredient pairs.
mods.pizzacraft.Bakeware.addShaped(
    <minecraft:bread>,
    "WWW", " F ",
    'W', <ore:wool>,
    'F', <minecraft:flint>
);

// A matrix is useful when a row contains empty slots or only one cell.
mods.pizzacraft.Bakeware.addShaped(
    <minecraft:bread>,
    [[<minecraft:wheat>, null, <minecraft:wheat>],
     [null, <minecraft:flint>, null]] as IIngredient[][]
);

mods.pizzacraft.Bakeware.addShapeless(
    <minecraft:cookie> * 2,
    [<minecraft:wheat>, <minecraft:wheat>, <minecraft:dye:3>]
);
```

#### Removal

```zenscript
mods.pizzacraft.Bakeware.remove(<pizzacraft:raw_pizza_0>);
mods.pizzacraft.Bakeware.removeAll();
```

`addRecipe(output, inputs)` remains as a deprecated alias for
`addShapeless(output, inputs)`.

### Mortar

#### Package

`mods.pizzacraft.Mortar`

#### Addition

```zenscript
// Shaped inputs are matched in the order of the mortar slots.
mods.pizzacraft.Mortar.addShaped(
    <minecraft:bread>, 6,
    [<ore:wheat>, <pizzacraft:onion_slice>]
);

mods.pizzacraft.Mortar.addShapeless(
    <minecraft:string> * 3, 4,
    [<minecraft:wool>, <ore:nuggetGold>]
);
```

#### Removal

```zenscript
mods.pizzacraft.Mortar.remove(<pizzacraft:flour_corn>);
mods.pizzacraft.Mortar.removeAll();
```

`addRecipe(output, duration, inputs)` remains as a deprecated alias for
`addShapeless(output, duration, inputs)`.

### Chopping Board

#### Package

`mods.pizzacraft.ChoppingBoard`

#### Addition

```zenscript
mods.pizzacraft.ChoppingBoard.addRecipe(
    <minecraft:iron_ingot>, <ore:plankWood>
);
mods.pizzacraft.ChoppingBoard.addRecipe(
    <minecraft:wool>, <minecraft:pumpkin> | <minecraft:brewing_stand>
);
```

#### Removal

```zenscript
mods.pizzacraft.ChoppingBoard.removeByOutput(<pizzacraft:onion_slice>);
mods.pizzacraft.ChoppingBoard.removeByInput(<ore:cropTomato> | <pizzacraft:cucumber>);
mods.pizzacraft.ChoppingBoard.removeAll();
```

## Railcraft

### Blast Furnace

#### Package

`mods.railcraft.BlastFurnace`

#### Addition and Removal

```zenscript
mods.railcraft.BlastFurnace.addRecipe(
    "example_blast", <minecraft:iron_ingot>, <ore:oreIron>, 200, 1
);
mods.railcraft.BlastFurnace.removeRecipe("railcraft:smelt_horse_armor");
mods.railcraft.BlastFurnace.removeRecipe(<minecraft:iron_ingot>, <ore:oreIron>);
mods.railcraft.BlastFurnace.removeAll();
```

### Coke Oven

#### Package

`mods.railcraft.CokeOven`

#### Addition and Removal

```zenscript
mods.railcraft.CokeOven.addRecipe(
    "example_coke", <minecraft:coal>, <ore:logWood>, 600,
    <liquid:creosote> * 1000
);
mods.railcraft.CokeOven.removeRecipe("railcraft:coke_block");
mods.railcraft.CokeOven.removeRecipe(<minecraft:coal>, <ore:logWood>);
mods.railcraft.CokeOven.removeAll();
```

### Rock Crusher

#### Package

`mods.railcraft.RockCrusher`

#### Addition and Removal

```zenscript
mods.railcraft.RockCrusher.addRecipe(
    "example_rock", [
        <minecraft:gravel>,
        <minecraft:sand> % 50,
        <minecraft:flint> % 25
    ], <ore:oreIron>
);
mods.railcraft.RockCrusher.removeRecipe("railcraft:crushed_obsidian");
mods.railcraft.RockCrusher.removeRecipeByInput(<ore:oreIron>);
mods.railcraft.RockCrusher.removeAll();
```

### Rolling Machine

#### Package

`mods.railcraft.RollingMachine`

#### Addition and Removal

```zenscript
mods.railcraft.RollingMachine.addShaped(
    "example_rolling", <minecraft:diamond>, [
        [<minecraft:iron_ingot>, null, <minecraft:iron_ingot>],
        [null, <minecraft:iron_ingot>, null],
        [<minecraft:iron_ingot>, null, <minecraft:iron_ingot>]
    ], 100
);
mods.railcraft.RollingMachine.addShapeless(
    "example_rolling_flat", <minecraft:gold_ingot>,
    [<minecraft:iron_ingot>, <minecraft:redstone>], 100
);
mods.railcraft.RollingMachine.remove("modtweaker:example_rolling");
mods.railcraft.RollingMachine.removeByOutput(<railcraft:rail>);
mods.railcraft.RollingMachine.removeAll();
```

### Fluid Fuel

#### Package

`mods.railcraft.FluidFuel`

#### Addition and Removal

```zenscript
mods.railcraft.FluidFuel.addFuel(<liquid:creosote>, 4800);
mods.railcraft.FluidFuel.addFuel(<liquid:lava>.definition, 2400);
mods.railcraft.FluidFuel.removeFuel(<liquid:creosote>);
mods.railcraft.FluidFuel.removeAll();
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

### Railcraft

- `mods.railcraft.BlastFurnace`: `addRecipe(name, output, input, time, slag)`, `removeRecipe(name)`, `removeRecipe(output, input)`, `removeAll()`
- `mods.railcraft.CokeOven`: `addRecipe(name, output, input, time, outputFluid)`, `removeRecipe(name)`, `removeRecipe(output, input)`, `removeAll()`
- `mods.railcraft.RockCrusher`: `addRecipe(name, outputs, input)`, `removeRecipe(name)`, `removeRecipe(input)`, `removeRecipeByInput(input)`, `removeAll()`
- `mods.railcraft.RollingMachine`: `addShaped(name, output, inputs, time)`, `addShapeless(name, output, inputs, time)`, `remove(name)`, `remove(output)`, `removeByOutput(output)`, `removeAll()`
- `mods.railcraft.FluidFuel`: `addFuel(liquid, heatValuePerBucket)`, `removeFuel(liquid)`, `removeAll()`

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
- `mods.pizzacraft.Bakeware`: `addShaped(output, rows, key/ingredient pairs)`, `addShaped(output, matrix)`, `addShapeless(output, inputs)`, deprecated `addRecipe(output, inputs)`, `remove(output)`, `removeAll()`
- `mods.pizzacraft.Mortar`: `addShaped(output, duration, inputs)`, `addShapeless(output, duration, inputs)`, deprecated `addRecipe(output, duration, inputs)`, `remove(output)`, `removeAll()`
- `mods.pizzacraft.ChoppingBoard`: `addRecipe(output, input)`, `removeByOutput(output)`, `removeByInput(input)`, `removeAll()`

The method index is intentionally limited to CRT-callable methods. Groovy
closures, internal GS recipe objects, virtualized registries and ASM accessors
are not included.

## Parameter semantics by mod

The same parameter name does not always mean the same machine slot. Use the
following tables when adapting an example to a different mod.

### Better With Mods

| Parameter | Meaning |
| --- | --- |
| `name` | The native filtered-hopper registry name. It is not an item ID. |
| `filter` | The item ingredient tested by the hopper before a recipe is selected. |
| `filtered` | An item allowed by the named filter. |
| `input` | The item entering the filtered-hopper recipe. |
| `outputs` | Primary output stacks from the hopper recipe. |
| `secondary` | Secondary output stacks from the hopper recipe. |
| `heat` | Better With Mods heat value, not RF or furnace ticks. |

For `Mill`, `inputs` are the ingredients consumed by the Mill Stone and
`outputs` are the resulting item stacks. `removeByInput` matches the ingredient
list, while `remove` matches the output list.

### Extra Utilities 2

| Class | Parameter meaning |
| --- | --- |
| `Furnace` | `input` is the item placed in the machine; `output` is the smelted item. |
| `Crusher` | `input` is crushed; `output` is the primary result; `secondaryOutput` is the optional bonus result. |
| `Enchanter` | `input` is the item being enchanted; `lapis` is the lapis cost; `energy` is RF; `time` is processing time. |
| `Resonator` | `input` is resonated; `output` is the result; `energy` is the total GP/RF-style recipe cost used by the native recipe. |
| `Generator` | `generator` is the machine registry name; item/fluid input is the fuel consumed by that generator. |

`Generator.remove("extrautils2:generator", ...)` addresses a specific native
machine. `Generator.removeByGenerator(...)` removes every recipe from that
machine, while `Generator.removeAll()` affects all supported generator types.

### Railcraft

| Class | Parameter meaning |
| --- | --- |
| `BlastFurnace` | `name` is the recipe registry name; `output` and `input` are the item result and ingredient; `time` is processing ticks; `slag` is the slag byproduct count. |
| `CokeOven` | `name` is the recipe registry name; `output` and `input` are the item result and ingredient; `time` is cooking ticks; `outputFluid` is the optional fluid byproduct. |
| `RockCrusher` | `name` is the recipe registry name; `outputs` contains weighted item results; `input` is the item ingredient. Each output chance is supplied by `WeightedItemStack`. |
| `RollingMachine` | `name` is a local ID placed in the `modtweaker` namespace; `inputs` is a shaped matrix or shapeless list; `time` is processing ticks; `output` is the crafted item. |
| `FluidFuel` | `liquid` is the fuel fluid or fluid stack; `heatValuePerBucket` is Railcraft boiler heat for 1000 mB, not RF or ticks. |

For Blast Furnace, Coke Oven and Rock Crusher, `removeRecipe(name)` uses the
native registry name. Rolling Machine additions use names such as
`modtweaker:example_rolling`; its `remove(output)` overload matches the
complete output stack instead. `IIngredient` inputs accept ore-dictionary
entries such as `<ore:oreIron>`.

### Thermal Expansion

| Parameter | Meaning |
| --- | --- |
| `input` | The primary item or ingredient entering the machine. |
| `primaryInput` | The first input slot of a two-input machine. |
| `secondaryInput` | The second input slot of a two-input machine. |
| `output` / `primaryOutput` | The main item or fluid produced by the recipe. |
| `secondaryOutput` | Optional bonus output; `secondaryChance` is its chance value. |
| `fluidInput` | Fluid consumed by the machine. |
| `fluidOutput` | Fluid produced by the machine. |
| `energy` | RF required for one processing operation. |
| `water` | Water amount consumed by the Precipitator or Insolator, in mB. |
| `creosote` | Creosote amount produced by pyrolysis, in mB. |
| `xp` / `factor` | Xp Collector values: base XP and its multiplier/factor. |
| `rf` / `factor` | Coolant values: RF conversion value and efficiency factor. |
| `sedimentary` | Extruder recipe category selector, not a chance or amount. |

For `Refinery`, `input` and `output` are fluids. For `Transposer`, an Extract
recipe has a fluid `output` and item `input`; a Fill recipe has an item `output`,
item `input`, and a consumed `fluid`.

### Forestry

| Class | Parameter meaning |
| --- | --- |
| `Carpenter` | `output` is the crafted item; `ingredients` are the shaped grid; `fluidInput` is the Carpenter tank; `box` is the packaging box slot; `packagingTime` is ticks. |
| `Centrifuge` | `ingredients`/`input` is the item processed; `output`/`outputs` are weighted products; `packagingTime` is processing ticks. |
| `Fermenter` | `resource` is the organic item; `fluidInput` is the tank fluid; `fluidOutput` is the produced fluid; `fermentationValue` and `fluidOutputModifier` are native Fermenter values. |
| `Squeezer` | `ingredients` are item resources; `fluidOutput` is the tank result; `itemOutput` is the optional remnant; `timePerItem` is processing time. |
| `Still` | `fluidInput` is consumed and `fluidOutput` is produced; `timePerUnit` is processing time. |
| `ThermionicFabricator` | `itemInput`/`input` are smelting or casting ingredients; `liquidStack`/`fluid` is the glass-like tank fluid; `plan` is the plan slot; `meltingPoint` is the smelting threshold. |
| `BeeMutations` | `first` and `second` are parent bee species IDs; `output` is the child species ID; `chance` is the mutation chance. |
| `BeeProduce` | `species` is the Forestry bee species UID; `output` is a product stack; `chance` is product probability; `specialty` selects the specialty map instead of the normal product map. |

Bee species parameters are Forestry allele UIDs such as
`"forestry.speciesForest"`, not item IDs or ore-dictionary names.

### Botania

| Parameter | Meaning |
| --- | --- |
| `output` | Item stack produced by the Botania recipe. |
| `input` / `inputs` | Item ingredient(s) consumed by the recipe; Rune Altar inputs marked with `.reuse()` are returned after crafting. |
| `catalyst` | The block or catalyst used by Mana Infusion. |
| `mana` | Mana cost, not RF and not ticks. |
| `weight` | Orechid generation weight; larger values make an ore more likely. |
| `brewName` | Registered Botania brew name, not an item ID. |

`RuneAltar.removeRecipeByInput` matches the ordered recipe input list, while
`removeRecipeByInputs` matches the complete set of inputs. `ElvenTrade` uses
the same distinction for its input list.

### Blood Magic

| Parameter | Meaning |
| --- | --- |
| `syphon` | Blood Magic Life Essence/Syphon cost. |
| `ticks` | Processing duration in ticks. |
| `minimumTier` / `minTier` | Required Blood Altar or Alchemy Table tier. |
| `consumeRate` | Blood Altar consumption rate. |
| `drainRate` | Blood Altar drain rate. |
| `catalyst` | The Meteor or Alchemy Array catalyst item, depending on the class. |
| `components` | Meteor output component identifiers. |
| `weights` | Meteor component weights, paired by array index with `components`. |
| `minSouls` / `soulDrain` | Tartaric Forge minimum souls and per-operation drain. |
| `type` / `value` | Tranquility block type and its contribution value. |

### Tinkers' Construct and Tinkers' Complement

| Class | Parameter meaning |
| --- | --- |
| `Melting` | `input` is the item melted; `output` is the molten fluid; `temp` is the required temperature. |
| `Alloy` | `inputs` are molten fluids consumed together; `output` is the alloy fluid. |
| `Casting` | `cast` is the cast ingredient; `fluid` is molten input; `amount` is mB; `consumeCast` controls cast consumption; `time` is ticks. |
| `Drying` | `input` is dried; `output` is the resulting item; `time` is ticks. |
| `Melter` | `input` is the item melted by Tinkers' Complement; `output` is its fluid result. |
| `HighOven` | `fuel`, `oxidizer`, `reducer`, and `purifier` are High Oven additives; `burnTime`, `tempRate`, and `temp` are native High Oven values. |

### Inspirations

| Parameter | Meaning |
| --- | --- |
| `input` | The item or block placed into the Inspirations cauldron/anvil operation. |
| `output` | The resulting item or block state. |
| `reagent` | Extra ingredient required by a brewing recipe. |
| `potion` | Potion registry name used by a potion recipe. |
| `fluid` | Fluid consumed or transformed by a fluid recipe. |
| `levels` | Cauldron levels consumed or required. |
| `boiling` | Whether the recipe requires the boiling cauldron state. |

### Thaumcraft

| Parameter | Meaning |
| --- | --- |
| `researchKey` / `research` | Thaumcraft research lock required by the recipe or scan. |
| `aspects` / `formula` | Thaumcraft aspect amounts, not item ingredients. |
| `vis` | Vis cost for Arcane Workbench crafting. |
| `instability` | Infusion instability value. |
| `centralItem` | Infusion Altar central item. |
| `recipe` | Infusion pedestal inputs. |
| `category` | Research category key, not the displayed category name. |
| `parents` | Research node keys that must precede the child node. |
| `column` / `row` | Research node position in the Thaumonomicon category. |
| `icon`, `background`, `background2` | Resource locations for the research category graphics. |

For `Research.addResearchLocation`, `mod` is the namespace and `location` is
the resource path. For `Research.addNode`, `name` is the localization key; it
is not an item or research key.

### PizzaCraft

| Class | Parameter meaning |
| --- | --- |
| `Bakeware` | `output` is the item produced; shaped `rows` and `matrix` describe the 1x1 to 3x3 Bakeware grid; `inputs` are consumed CraftTweaker ingredients. |
| `Mortar` | `output` is the item produced; `duration` is processing time in ticks; shaped `inputs` are checked by slot order, while shapeless inputs are matched in any slot. |
| `ChoppingBoard` | `output` is the item produced; `input` is the item ingredient placed on the board. Ore-dictionary and OR ingredients expand to every matching item stack in the native registry. |

PizzaCraft's `remove`/`removeByOutput` methods compare the complete output stack,
while `ChoppingBoard.removeByInput` tests the registered input against the
CraftTweaker ingredient. `removeAll()` clears the native recipe registry for
that machine only.

## Parameter signatures

The grouped entries above are expanded here with the CRT parameter types used by
the implementation. Optional parameters are marked with `@Optional`.

### PizzaCraft signatures

```text
Bakeware.addShaped(IItemStack output, IIngredient[][] inputs)
Bakeware.addShaped(IItemStack output, String[] rows, Object... keyAndIngredientPairs)
Bakeware.addShapeless(IItemStack output, IIngredient[] inputs)
Bakeware.addRecipe(IItemStack output, IIngredient[] inputs)              // deprecated alias
Bakeware.remove(IItemStack output)
Bakeware.removeAll()

Mortar.addShaped(IItemStack output, int duration, IIngredient[] inputs)
Mortar.addShapeless(IItemStack output, int duration, IIngredient[] inputs)
Mortar.addRecipe(IItemStack output, int duration, IIngredient[] inputs)   // deprecated alias
Mortar.remove(IItemStack output)
Mortar.removeAll()

ChoppingBoard.addRecipe(IItemStack output, IIngredient input)
ChoppingBoard.removeByOutput(IItemStack output)
ChoppingBoard.removeByInput(IIngredient input)
ChoppingBoard.removeAll()
```

### Railcraft signatures

```text
BlastFurnace.addRecipe(String name, IItemStack output, IIngredient input, @Optional int time, @Optional int slag)
BlastFurnace.removeRecipe(String name)
BlastFurnace.removeRecipe(IItemStack output, @Optional IIngredient input)
BlastFurnace.removeAll()

CokeOven.addRecipe(String name, IItemStack output, IIngredient input, @Optional int time, @Optional ILiquidStack outputFluid)
CokeOven.removeRecipe(String name)
CokeOven.removeRecipe(IItemStack output, @Optional IIngredient input)
CokeOven.removeAll()

RockCrusher.addRecipe(String name, WeightedItemStack[] outputs, IIngredient input)
RockCrusher.removeRecipe(String name)
RockCrusher.removeRecipe(IItemStack input)
RockCrusher.removeRecipeByInput(IItemStack input)
RockCrusher.removeAll()

RollingMachine.addShaped(String name, IItemStack output, IIngredient[][] inputs, @Optional int time)
RollingMachine.addShapeless(String name, IItemStack output, IIngredient[] inputs, @Optional int time)
RollingMachine.remove(String name)
RollingMachine.remove(IItemStack output)
RollingMachine.removeByOutput(IItemStack output)
RollingMachine.removeAll()

FluidFuel.addFuel(ILiquidStack liquid, int heatValuePerBucket)
FluidFuel.addFuel(ILiquidDefinition liquidType, int heatValuePerBucket)
FluidFuel.removeFuel(ILiquidStack liquid)
FluidFuel.removeFuel(ILiquidDefinition liquidType)
FluidFuel.removeAll()
```

### Thermal Expansion signatures

```text
Brewer.addRecipe(IIngredient input, ILiquidStack fluidInput, ILiquidStack fluidOutput, int energy)
Charger.addRecipe(IIngredient input, IItemStack output, int energy)
Fisher.addRecipe(IItemStack fish, int weight)
FisherBait.addRecipe(IItemStack bait, int multiplier)
Tapper.addItem(IItemStack input, ILiquidStack output)
Tapper.addBlock(IItemStack input, ILiquidStack output)
TapperTree.addRecipe(IBlockState log, IBlockState leaf)
TapperFertilizer.addRecipe(IItemStack bait, int multiplier)
XpCollector.addRecipe(IIngredient catalyst, int xp, int factor)
Coolant.addCoolant(ILiquidStack fluid, int coolantRf, int coolantFactor)
Diffuser.addRecipe(IItemStack input, int amplifier, int duration)
Fisher.removeRecipe(IIngredient fish)
FisherBait.removeRecipe(IIngredient bait)
Tapper.removeItemByInput(IIngredient input)
Tapper.removeBlockByInput(IIngredient input)
TapperTree.removeRecipeByLog(IBlockState log)
TapperTree.removeRecipeByLeaf(IBlockState leaf)
XpCollector.remove(IIngredient catalyst)
Coolant.removeCoolant(ILiquidStack fluid)
Diffuser.removeRecipe(IIngredient input)

Furnace.addRecipe(IIngredient input, IItemStack output, int energy)
Furnace.addFood(IItemStack input)
Furnace.removeFood(IItemStack input)
Furnace.removeRecipeByInput(IIngredient input)
Furnace.removeRecipeByOutput(IIngredient output)
FurnacePyrolysis.addRecipe(IIngredient input, IItemStack output, int energy, int creosote)
FurnacePyrolysis.removeRecipeByInput(IIngredient input)
FurnacePyrolysis.removeRecipeByOutput(IIngredient output)
Smelter.addRecipe(IIngredient primaryInput, IIngredient secondaryInput, IItemStack primaryOutput, IItemStack secondaryOutput, int secondaryChance, int energy)
Smelter.addFlux(IItemStack input)
Smelter.removeFlux(IItemStack input)
Smelter.removeRecipeByInput(IIngredient input)
Smelter.removeRecipeByOutput(IIngredient output)
Pulverizer.addRecipe(IItemStack output, IIngredient input, int energy, @Optional IItemStack secondaryOutput, @Optional int secondaryChance)
Pulverizer.removeRecipeByInput(IIngredient input)
Pulverizer.removeRecipeByOutput(IIngredient output)
RedstoneFurnace.addRecipe(IItemStack output, IIngredient input, int energy)
RedstoneFurnace.addPyrolysisRecipe(IItemStack output, IIngredient input, int energy, int creosote)
RedstoneFurnace.removeRecipe(IItemStack input)
RedstoneFurnace.removePyrolysisRecipe(IItemStack input)
Precipitator.addRecipe(IItemStack output, int water, int energy)
Precipitator.removeRecipeByInput(IIngredient input)
Precipitator.removeRecipeByOutput(IIngredient output)

Centrifuge.addRecipe(WeightedItemStack[] outputs, IIngredient input, ILiquidStack fluid, int energy)
Centrifuge.addRecipeMob(String entityId, WeightedItemStack[] outputs, ILiquidStack fluid, int energy, int xp)
Centrifuge.removeRecipe(IIngredient input)
Centrifuge.removeRecipeMob(String entityId)
Centrifuge.removeRecipeByOutput(IIngredient output)
Centrifuge.removeRecipeMobByOutput(IIngredient output)
Compactor.addRecipe(CompactorManager.Mode mode, IItemStack output, IIngredient input, int energy)
Compactor.removeByInput(CompactorManager.Mode mode, IIngredient input)
Compactor.removeByOutput(CompactorManager.Mode mode, IIngredient output)
Extruder.addRecipe(IItemStack output, int fluidHot, int fluidCold, int energy, boolean sedimentary)
Extruder.removeRecipeByInput(IIngredient input)
Extruder.removeRecipeByOutput(IIngredient output)
Extruder.removeByType(boolean sedimentary)
InductionSmelter.addRecipe(IItemStack primaryOutput, IIngredient primaryInput, IIngredient secondaryInput, int energy, @Optional IItemStack secondaryOutput, @Optional int secondaryChance)
InductionSmelter.removeRecipe(IItemStack primaryInput, IItemStack secondaryInput)
Insolator.addRecipe(IItemStack primaryOutput, IIngredient primaryInput, IIngredient secondaryInput, int energy, @Optional IItemStack secondaryOutput, @Optional int secondaryChance, @Optional int water)
Insolator.removeRecipeByInput(IIngredient input)
Insolator.removeRecipeByOutput(IIngredient output)
Refinery.addRecipe(ILiquidStack output, WeightedItemStack outputItem, ILiquidStack input, int energy)
Refinery.addRecipePotion(ILiquidStack output, ILiquidStack input, int energy)
Refinery.removeRecipeByInput(IIngredient input)
Refinery.removeRecipeByOutput(IIngredient output)
Refinery.removeRecipePotion(ILiquidStack input)
RefineryPotion.addRecipe(ILiquidStack input, ILiquidStack output, @Optional WeightedItemStack outputItem, int chance, int energy)
RefineryPotion.removeRecipeByInput(IIngredient input)
RefineryPotion.removeRecipeByOutput(IIngredient output)
Transposer.addExtractRecipe(ILiquidStack output, IIngredient input, int energy, @Optional WeightedItemStack itemOut)
Transposer.removeExtractRecipe(IIngredient input)
Transposer.removeExtractRecipeByOutput(IIngredient output)
Transposer.addFillRecipe(IItemStack output, IIngredient input, ILiquidStack fluid, int energy)
Transposer.removeFillRecipe(IIngredient input, ILiquidStack fluid)
Transposer.removeFillRecipeByOutput(IIngredient output)
```

All Thermal Expansion machine, device and dynamo classes also expose the
corresponding `removeAll()` method where listed in the method index.

### Botania signatures

```text
Brew.addRecipe(IIngredient[] inputItems, String brewName)
Brew.removeRecipe(String brewName)
Brew.removeRecipeByInput(IIngredient[] inputs)
ManaInfusion.addInfusion(IItemStack output, IIngredient input, int mana)
ManaInfusion.addAlchemy(IItemStack output, IIngredient input, int mana)
ManaInfusion.addConjuration(IItemStack output, IIngredient input, int mana)
ManaInfusion.removeRecipe(IIngredient output)
ManaInfusion.removeRecipeByInput(IIngredient input)
ManaInfusion.removeRecipeByCatalyst(IBlockState catalyst)
RuneAltar.addRecipe(IItemStack output, IIngredient[] input, int mana)
RuneAltar.removeRecipe(IIngredient output)
RuneAltar.removeRecipeByInput(IIngredient[] inputs)
RuneAltar.removeRecipeByInputs(IIngredient[] inputs)
ElvenTrade.addRecipe(IItemStack[] outputs, IIngredient[] input)
ElvenTrade.removeRecipe(IIngredient output)
ElvenTrade.removeRecipeByInput(IIngredient[] inputs)
ElvenTrade.removeRecipeByInputs(IIngredient[] inputs)
Apothecary.addRecipe(IItemStack output, IIngredient[] input)
Apothecary.removeRecipe(IIngredient output)
Orechid.addOre(IOreDictEntry oreDict, int weight)
Orechid.addOre(String oreDict, int weight)
Orechid.removeOre(IOreDictEntry oreDict)
Orechid.removeOre(String oreDict)
PureDaisy.addRecipe(IIngredient blockInput, IItemStack blockOutput, @Optional int time)
PureDaisy.removeRecipe(IIngredient output)
PureDaisy.removeRecipeByInput(IIngredient input)
Magnet.addToBlacklist(IIngredient item)
Magnet.removeFromBlacklist(IIngredient item)
Magnet.isInBlacklist(IIngredient item)
Flowers.registerFlower(String name, String flowerClass)
Flowers.registerFlowerWithMini(String name, String flowerClass, String miniClass)
Knowledge.registerKnowledgeType(String unlocalized, String localized, String color, boolean autoUnlock)
```

### Forestry, Blood Magic and Tinkers signatures

```text
BeeMutations.add(String output, String first, String second, int chance)
BeeMutations.remove(String output, String first, String second)
BeeMutations.removeByOutput(String output)
BeeProduce.add(String species, IItemStack output, float chance, @Optional boolean specialty)
BeeProduce.removeProduct(String species, IIngredient output)
BeeProduce.removeSpecialty(String species, IIngredient output)
Carpenter.addRecipe(IItemStack output, IIngredient[][] ingredients, int packagingTime, @Optional ILiquidStack fluidInput, @Optional IItemStack box)
Carpenter.removeByFluidInput(ILiquidStack fluidInput)
Carpenter.removeByBox(IIngredient box)
Carpenter.removeByInput(IIngredient[] inputs)
Centrifuge.addRecipe(WeightedItemStack[] output, IItemStack ingredients, int packagingTime)
Centrifuge.removeByOutput(IIngredient output)
Centrifuge.removeByOutputs(IIngredient[] outputs)
Fermenter.addRecipe(ILiquidStack fluidOutput, IItemStack resource, ILiquidStack fluidInput, int fermentationValue, float fluidOutputModifier)
Fermenter.removeByInput(ILiquidStack input)
Fermenter.removeByCatalyst(IIngredient catalyst)
Fermenter.removeByOutput(ILiquidStack output)
Squeezer.addRecipe(ILiquidStack fluidOutput, IIngredient[] ingredients, int timePerItem, @Optional WeightedItemStack itemOutput)
Squeezer.removeByOutput(ILiquidStack output)
Squeezer.removeByInput(IIngredient[] inputs)
Squeezer.removeByInputs(IIngredient[] inputs)
ThermionicFabricator.addSmelting(ILiquidStack liquidStack, IItemStack itemInput, int meltingPoint)
ThermionicFabricator.addCast(IItemStack output, IIngredient[][] ingredients, ILiquidStack liquidStack, @Optional IItemStack plan)
ThermionicFabricator.removeByFluid(ILiquidStack fluid)
ThermionicFabricator.removeByCatalyst(IIngredient catalyst)
ThermionicFabricator.removeByInput(IIngredient input)
ThermionicFabricator.removeSmeltingByOutput(ILiquidStack output)

AlchemyArray.addRecipe(IItemStack output, IIngredient input, IIngredient catalyst, @Optional String textureLocation)
AlchemyArray.removeRecipe(IIngredient input, IIngredient catalyst)
AlchemyTable.addRecipe(IItemStack output, IIngredient[] inputs, int syphon, int ticks, int minTier)
AlchemyTable.addPotionRecipe(IItemStack[] inputs, IPotionEffect effects, int syphon, int ticks, int minTier)
AlchemyTable.removeRecipe(IItemStack[] inputs)
BloodAltar.addRecipe(IItemStack output, IIngredient input, int minimumTier, int syphon, int consumeRate, int drainRate)
BloodAltar.removeRecipe(IIngredient input)
Meteor.addRecipe(IItemStack catalyst, String[] components, int[] weights, float explosionStrength, float radius, int cost)
Sacrificial.add(String entity, int value)
Tranquility.add(IBlock block, String type, double value)
Tranquility.add(IBlockState state, String type, double value)
TartaricForge.addRecipe(IItemStack output, IIngredient[] inputs, double minSouls, double soulDrain)

Alloy.addRecipe(ILiquidStack output, ILiquidStack[] inputs)
Alloy.removeRecipe(ILiquidStack output, @Optional ILiquidStack[] input)
Casting.addTableRecipe(IItemStack output, IIngredient cast, ILiquidStack fluid, int amount, @Optional boolean consumeCast, @Optional int time)
Casting.addBasinRecipe(IItemStack output, IIngredient cast, ILiquidStack fluid, int amount, @Optional boolean consumeCast, @Optional int time)
Drying.addRecipe(IItemStack output, IIngredient input, int time)
Melting.addRecipe(ILiquidStack output, IIngredient input, @Optional int temp)
Melting.addEntityMelting(IEntityDefinition entity, ILiquidStack stack)
Melting.removeRecipe(ILiquidStack output, @Optional IItemStack input)
Fuel.registerFuel(ILiquidStack fluid, int duration)
Blacklist.addRecipe(ILiquidStack output, IIngredient input)
Blacklist.removeRecipe(IIngredient input)
Melter.removeByInput(IIngredient input)
Melter.removeByOutput(ILiquidStack output)
Melter.removeByInputAndOutput(IIngredient input, ILiquidStack output)
HighOven.addFuel(IIngredient fuel, int burnTime, int tempRate)
HighOven.removeFuel(IIngredient stack)
HighOven.addHeatRecipe(ILiquidStack output, ILiquidStack input, int temp)
HighOven.removeHeatRecipe(ILiquidStack output, @Optional ILiquidStack input)
```

### Inspirations and Thaumcraft signatures

```text
AnvilSmashing.add(IBlockState input, IBlockState output)
AnvilSmashing.add(IBlock input, IBlockState output)
AnvilSmashing.addBreaking(IBlockState input)
AnvilSmashing.removeByInput(IBlockState input)
AnvilSmashing.removeByOutput(IBlockState output)
Cauldron.addBrewingRecipe(String output, String input, IIngredient reagent)
Cauldron.removeBrewingRecipe(String output, @Optional String input, @Optional IIngredient reagent)
Cauldron.addPotionRecipe(IItemStack output, IIngredient input, String potion, @Optional int levels, @Optional Boolean boiling)
Cauldron.removePotionRecipe(IIngredient output, @Optional IIngredient input, @Optional String potion)
Cauldron.addDyeRecipe(IItemStack output, IIngredient input, String stringDye, @Optional int levels)
Cauldron.removeDyeRecipe(IIngredient output, @Optional IIngredient input, @Optional String stringDye)
Cauldron.addFluidRecipe(IItemStack output, IIngredient input, ILiquidStack fluid, @Optional int levels, @Optional Boolean boiling)
Cauldron.addFluidTransform(ILiquidStack output, IIngredient input, ILiquidStack fluid, @Optional int maxLevel, @Optional Boolean boiling)
Cauldron.addFluidMix(IItemStack output, ILiquidStack liquid1, ILiquidStack liquid2)
Cauldron.removeFluidRecipe(IIngredient output, @Optional IIngredient input, @Optional ILiquidStack fluid)
Cauldron.addFillRecipe(IIngredient input, ILiquidStack fluid, @Optional int amount, @Optional IItemStack container, @Optional Boolean boiling)
Cauldron.removeFillRecipe(IIngredient input, @Optional ILiquidStack fluid)

Research.addCategory(String key, String researchKey, CTAspectStack[] formula, String icon, String background, @Optional String background2)
Research.removeCategory(String key)
Research.removeAllCategories()
Research.addNode(String category, String key, String name, int column, int row, @Optional String[] parents)
Research.removeNode(String category, String key)
Research.removeAllNodes(String category)
Research.connectNodes(String category, String parent, String child)
Research.disconnectNodes(String category, String parent, String child)
Research.addResearchLocation(String mod, String location)
Research.addScannable(String researchKey, IItemStack item)
Research.addScannable(IBlock block)
Research.addScannable(String researchKey, IBlock block)
Crucible.registerRecipe(String name, String researchKey, IItemStack output, IIngredient input, CTAspectStack[] aspects)
ArcaneWorkbench.registerShapedRecipe(String name, String research, int vis, CTAspectStack[] aspects, IItemStack output, IIngredient[][] input)
Infusion.registerRecipe(String name, String research, IItemStack output, int instability, CTAspectStack[] aspects, IIngredient centralItem, IIngredient[] recipe)
DustTrigger.addSingleConversion(IBlock input, IItemStack output, @Optional String research)
LootBag.addLoot(WeightedItemStack stack, int[] bagTypes)
LootBag.removeLoot(IItemStack stack, int[] bagTypes)
SmeltingBonus.addSmeltingBonus(IIngredient input, WeightedItemStack output)
Warp.addWarp(IItemStack stack, int amount)
Warp.setWarp(IItemStack stack, int amount)
Warp.clearWarp(IItemStack stack)
```
