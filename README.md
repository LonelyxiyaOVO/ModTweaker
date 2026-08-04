ModTweaker2
==========
ModTweaker is an addon for MineTweaker 3. Minetweaker lets you adjust recipes, remove them entirely, or add new recipes. While it has decent mod support, there are many mods that use custom crafting handlers that are not supported natively. ModTweaker plans to provide additional support for as many of these mods over time as possible.


Stable Releases on Curse
----------
https://minecraft.curseforge.com/projects/modtweaker

Development build on jenkins
----------
http://ci.blamejared.com/job/Modtweaker/


Guidelines for Bugreporting
----------
https://github.com/jaredlll08/ModTweaker2/wiki/Bug-Reporting-Guidelines


Supported Mods
----------
- Actually Additions
- Applied Energistics 2
- Auracascade
- Blood Magic
- Botania
- Chisel
- ExNihilo
- ExtendedWorkbench
- Factorization (0.8.95+)
- Forestry (3.6.0+)
- Flaxbeard's Steam Power
- Mariculture
- Mekanism 8
- Metallurgy
- PneumaticCraft
- Railcraft
- Tinkers Construct
- Terrafirmacraft
- Thaumcraft
- Thermal Expansion

ImmersiveTech compatibility has been removed because the original ImmersiveTech
mod no longer provides CraftTweaker (CRT) support. The unofficial MCT Immersive
Technology version provides more complete features and native CRT support:
https://www.curseforge.com/minecraft/mc-mods/mct-immersive-technology


GroovyScript Feature Ports
----------
Selected features from GroovyScript have been ported to CRT for mods already
supported by ModTweaker. These ports do not add support for new mods.

- Thaumcraft item aspect and warp helpers, including add, remove, clear, and set methods
- Extra Utilities 2 Furnace and Enchanter recipe support with ingredient and ore dictionary inputs
- Thermal Expansion machine-wide recipe removal and ingredient input support for selected machines
- Thermal Expansion Centrifuge output-based removal and mob recipe support
- Botania Brew input-based removal and Magnet blacklist management
- Forestry Moistener fuel removal by input/output and full fuel clearing
- Inspirations Anvil Smashing registration, targeted removal, reflection-backed registry access, and full clearing

CRT API reference for the ports
----------
The following methods are available from CRT. `IIngredient` accepts normal item
stacks and ore-dictionary ingredients where the underlying machine supports it.
`removeAll()` clears the complete native registry for that machine.

- `mods.betterwithmods.FilteredHopper`: `removeFilter(name)`, `removeByFilter(filter)`, `removeByFiltered(filtered)` and `removeAll()` are available alongside filter registration
- `mods.betterwithmods.Mill`: `removeByInput(input)` is available alongside output removal and `removeAll()`

- `mods.extrautils2.Furnace`: `add(output, input)`, `remove(output)`, `removeAll()`
- `mods.extrautils2.Enchanter`: `add(output, input, lapis, energy, time, enchantName)`, `remove(output)`, `removeAll()`
- `mods.extrautils2.Resonator`: `add(output, input, energy, addOwnerTag)`, `remove(output)`
- `mods.thermalexpansion.Brewer`: `addRecipe(input, fluidInput, fluidOutput, energy)`, `removeRecipeByInput(input)`, `removeRecipeByOutput(output)`, `removeAll()`
- `mods.thermalexpansion.XpCollector`: `add(catalyst, xp, factor)`, `addRecipe(catalyst, xp, factor)`, `remove(catalyst)`, `removeAll()`
- `mods.thermalexpansion.Charger`: `addRecipe(input, output, energy)`, `removeRecipeByInput(input)`, `removeRecipeByOutput(output)`, `removeAll()`
- `mods.thermalexpansion.Fisher`: `addRecipe(fish, weight)`, `removeRecipe(fish)`, `removeAll()`
- `mods.thermalexpansion.FisherBait`: `addRecipe(bait, multiplier)`, `removeRecipe(bait)`, `removeAll()`
- `mods.thermalexpansion.Precipitator`: `addRecipe(output, water, energy)`, `removeRecipeByInput(input)`, `removeRecipeByOutput(output)`, `removeAll()`
- `mods.thermalexpansion.Furnace`: `addRecipe(input, output, energy)`, `addFood(input)`, `removeFood(input)`, `removeRecipeByInput(input)`, `removeRecipeByOutput(output)`, `removeAllFood()`, `removeAll()`
- `mods.thermalexpansion.FurnacePyrolysis`: `addRecipe(input, output, energy, creosote)`, `removeRecipeByInput(input)`, `removeRecipeByOutput(output)`, `removeAll()`
- `mods.thermalexpansion.Smelter`: `addRecipe(primaryInput, secondaryInput, primaryOutput, secondaryOutput, chance, energy)`, `addFlux(input)`, `removeFlux(input)`, `removeRecipeByInput(input)`, `removeRecipeByOutput(output)`, `removeAll()`
- `mods.thermalexpansion.TapperFertilizer`: `addRecipe(bait, multiplier)`, `removeRecipe(bait)`, `removeAll()`
- `mods.thermalexpansion.TapperTree`: `addRecipe(log, leaf)`, `removeRecipeByLog(log)`, `removeRecipeByLeaf(leaf)`, `removeAll()`
- `mods.thermalexpansion.Tapper`: `addItem(input, fluidOutput)`, `addBlock(input, fluidOutput)`, `removeItemByInput(input)`, `removeBlockByInput(input)`, `removeAllItems()`, `removeAllBlocks()`, `removeAll()`
- `mods.thermalexpansion.RefineryPotion`: `addRecipe(input, output, outputItem, chance, energy)`, `removeRecipeByInput(input)`, `removeRecipeByOutput(output)`, `removeAll()`
- `mods.thermalexpansion.Extruder`: `addRecipe(output, fluidHot, fluidCold, energy, sedimentary)`, `removeRecipeByInput(input)`, `removeRecipeByOutput(output)`, `removeByType(sedimentary)`, `removeAll()`
- `mods.thermalexpansion.Diffuser`: `addRecipe(input, amplifier, duration)`, `removeRecipe(input)`, `removeAll()`
- `mods.thermalexpansion.Coolant`: `addRecipe(fluid, rf, factor)`, `removeRecipe(fluid)`, `removeAll()`
- `mods.thermalexpansion.Lapidary`: `addFuel(input, energy)`, `removeFuel(input)`, `removeAll()`
- Thermal Expansion dynamos: `CompressionDynamo.removeAll()`, `EnervationDynamo.removeAll()`, `MagmaticDynamo.removeAll()`, `NumisticDynamo.removeAll()`, `SteamDynamo.removeAll()`
- `mods.thermalexpansion.Pulverizer`: `addRecipe(output, input, energy, secondaryOutput, secondaryChance)`, `removeRecipe(output)`, `removeAll()`
- `mods.thermalexpansion.RedstoneFurnace`: `addRecipe(output, input, energy)`, `addPyrolysisRecipe(output, input, energy, creosote)`, `removeRecipe(output)`, `removePyrolysisRecipe(output)`, `removeAll()`
- `mods.thermalexpansion.InductionSmelter`: `addRecipe(primaryOutput, primaryInput, secondaryInput, energy, secondaryOutput, secondaryChance)`, `removeRecipe(output)`, `removeAll()`
- `mods.thermalexpansion.Centrifuge`: `addRecipe(...)`, `addRecipeMob(...)`, `removeRecipe(input)`, `removeRecipeMob(entity)`, `removeRecipeByOutput(output)`, `removeRecipeMobByOutput(output)`, `removeAll()`
- Other Thermal Expansion machines with full clearing: `Crucible.removeAll()`, `Compactor.removeAll()`, `Enchanter.removeAll()`, `Insolator.removeAll()` and `SawMill.removeAll()`.
- `mods.thermalexpansion.Factorizer`: `addRecipeSplit(in, out)`, `addRecipeCombine(in, out)`, `addRecipeBoth(combined, split)`, `removeRecipeSplit(in)`, `removeRecipeCombine(in)`, `removeByType(split)`, `removeAll()`
- `mods.thermalexpansion.Refinery`: `addRecipe(...)`, `addRecipePotion(...)`, `removeRecipe(input)`, `removeRecipePotion(input)`, `addFossilFuel(name/fluid)`, `removeFossilFuel(name/fluid)`, `addBioFuel(name/fluid)`, `removeBioFuel(name/fluid)`, `removeAllFossilFuels()`, `removeAllBioFuels()`, `removeAll()`
- `mods.thermalexpansion.Transposer`: `addExtractRecipe(...)`, `removeExtractRecipe(input)`, `addFillRecipe(...)`, `removeFillRecipe(input, fluid)`, `removeAllExtractRecipes()`, `removeAllFillRecipes()`, `removeAll()`
- `mods.thermalexpansion.Enchanter`: `addArcana(input)`, `removeArcana(input)` and ingredient-based primary/secondary inputs are supported; `removeAll()` also clears validation and arcana lock sets.
- `mods.thermalexpansion.ReactantDynamo`: `addReaction(ingredient, fluid, energy)`, `addReactionElemental(...)`, `removeReaction(...)`, `removeReactionElemental(...)`, `addElementalReactant()`, `removeElementalReactant()`, `addElementalFluid()`, `removeElementalFluid()`, `removeAll()`
- `mods.thermalexpansion.Compactor`: adds ingredient-based `removeByInput(mode, input)` and `removeByOutput(mode, output)`; `Crucible`, `Insolator`, `Pulverizer` and `SawMill` now also expose ingredient-based input/output removal, with Insolator fertilizer controls.
- `EnervationDynamo`, `NumisticDynamo` and `SteamDynamo` accept `IIngredient` for fuel removal.
- `mods.botania.Brew`: `addRecipe(inputItems, brewName)`, `removeRecipe(brewName)`, `removeRecipeByInput(inputs)`, `removeAll()`
- `mods.botania.ManaInfusion`: `addInfusion(output, input, mana)`, `addAlchemy(output, input, mana)`, `addConjuration(output, input, mana)`, `removeRecipe(output)`, `removeRecipeByInput(input)`, `removeRecipeByCatalyst(catalyst)`, `removeAll()`
- `mods.botania.RuneAltar`: `addRecipe(output, inputs, mana)`, `removeRecipe(output)`, `removeRecipeByInput(inputs)`, `removeRecipeByInputs(inputs)`, `removeAll()`
- `mods.botania.ElvenTrade`: `addRecipe(outputs, inputs)`, `removeRecipe(output)`, `removeRecipeByInput(inputs)`, `removeRecipeByInputs(inputs)`, `removeAll()`
- `mods.botania.Magnet`: `addItem(item)`, `addBlock(block)`, `addBlockState(state)`, `isBlacklisted(item)`, `removeItem(item)`, `removeBlock(block)`, `removeBlockState(state)`, `removeAll()`
- `mods.forestry.Moistener`: `addFuel(item, product, value, stage)`, `removeFuel(item)`, `removeFuelByProduct(product)`, `removeAllFuel()`
- `mods.forestry.BeeMutations`: `add(output, first, second, chance)`, `remove(output, first, second)`, `removeByOutput(output)`, `removeAll()`
- `mods.forestry.BeeProduce`: `add(species, output, chance[, specialty])`, `removeProduct(species, output)`, `removeSpecialty(species, output)`, `removeAll(species)`, `removeAll()`
- Forestry recipe registries: `Carpenter.removeAll()`, `Centrifuge.removeAll()`, `Fermenter.removeAll()`, `Squeezer.removeAll()`, `Still.removeAll()`, `ThermionicFabricator.removeAll()`
- Forestry precise removal: `Carpenter.removeByFluidInput(fluid)`, `removeByBox(box)`, `removeByInput(inputs)`; `Centrifuge.removeByOutput(output)`, `removeByOutputs(outputs)`; `Fermenter.removeByInput(fluid)`, `removeByCatalyst(item)`, `removeByOutput(fluid)`; `Squeezer.removeByOutput(fluid)`, `removeByInput(inputs)`, `removeByInputs(inputs)`; and `ThermionicFabricator.removeByFluid(fluid)`, `removeByCatalyst(item)`, `removeByInput(input)`, `removeSmeltingByOutput(fluid)`
- `mods.inspirations.AnvilSmashing`: `add(input, output)`, `addBreaking(input)`, `removeByInput(input)`, `removeByOutput(output)`, `removeAll()`
- `mods.bloodmagic.Meteor`: `addRecipe(catalyst, components, weights, explosionStrength, radius, cost)`, `removeRecipe(catalyst)`, `removeRecipeByInput(catalyst)`, `removeRecipeByCatalyst(catalyst)`, `removeAll()`
- `mods.bloodmagic.Sacrificial`: `add(entity, value)`, `remove(entity)`, `removeAll()`
- `mods.bloodmagic.Tranquility`: `add(block, type, value)`, `add(blockstate, type, value)`, `remove(block, type)`, `remove(blockstate, type)`, `removeAll()`
- `mods.thaumcraft.Research`: `addNode(category, key, name, column, row[, parents])`, `removeNode(category, key)`, `removeAllNodes(category)`, `connectNodes(category, parent, child)`, `disconnectNodes(category, parent, child)`
- `mods.thaumcraft.Research`: `addCategory(...)`, `removeCategory(key)`, `removeAllCategories()`, `addResearchLocation(...)`, and item/block `addScannable(...)` overloads
- Thaumcraft handlers: `addAspect(stack, aspect, amount)`, `clearAspects(stack)`, `addWarp(stack, amount)`, `clearWarp(stack)`

Thaumcraft research node connections are stored as the child node's `parents` list.
The CRT Research API edits the native `ResearchCategory.research` registry during
the normal late-action phase; adding a node creates its position, display name and
parent links, but does not create research pages or rewards.

The reflection-backed methods intentionally target the native registries of the
installed mod, so they also work for private registries that have no public CRT
API. Recipe removal is applied during the normal ModTweaker late-action phase.

CRT examples by mod
----------
The following examples use CRT syntax for the APIs in this project. They are
intended as copyable starting points; item, fluid and ore names can be replaced
with entries from the modpack.

### Actually Additions

```zenscript
mods.actuallyadditions.AtomicReconstructor.addRecipe(<minecraft:diamond>, <minecraft:coal>, 5000);
mods.actuallyadditions.AtomicReconstructor.removeRecipe(<minecraft:diamond>);
mods.actuallyadditions.AtomicReconstructor.removeAll();
mods.actuallyadditions.MiningLens.addStoneOre(<ore:oreCopper>, 10);
```

### Better With Mods

```zenscript
mods.betterwithmods.HeatRegistry.addHeatSource(<ore:logWood>, 5);
mods.betterwithmods.FilteredHopper.addFilter("my_filter", <minecraft:iron_ingot>);
mods.betterwithmods.FilteredHopper.addFilteredItem("my_filter", <minecraft:gold_ingot>);
mods.betterwithmods.FilteredHopper.removeByFilter(<minecraft:iron_ingot>);
mods.betterwithmods.Mill.addRecipe([<minecraft:wheat>], [<minecraft:flour>]);
mods.betterwithmods.Mill.removeByInput(<minecraft:wheat>);
mods.betterwithmods.Mill.removeAll();
```

### Blood Magic

```zenscript
mods.bloodmagic.BloodAltar.addRecipe(<minecraft:gold_ingot>, <minecraft:iron_ingot>, 2, 5000, 20, 20);
mods.bloodmagic.BloodAltar.removeRecipe(<ore:ingotGold>);
mods.bloodmagic.AlchemyArray.addRecipe(<minecraft:diamond>, <minecraft:coal>, <minecraft:redstone>);
mods.bloodmagic.Meteor.removeAll();
```

### Botania

```zenscript
mods.botania.ManaInfusion.addInfusion(<minecraft:diamond>, <minecraft:coal>, 500);
mods.botania.ManaInfusion.removeRecipe(<minecraft:diamond>);
mods.botania.ManaInfusion.removeAll();
mods.botania.Orechid.addOre("oreCopper", 20);
mods.botania.Orechid.removeAll();
```

### Chisel

```zenscript
mods.chisel.Carving.addGroup("my_group");
mods.chisel.Carving.addVariation("my_group", <minecraft:stonebrick>);
mods.chisel.Carving.removeVariation("my_group", <minecraft:stonebrick>);
mods.chisel.Carving.removeAll();
```

### Extra Utilities 2

```zenscript
mods.extrautils2.Furnace.add(<minecraft:iron_ingot>, <minecraft:iron_ore>);
mods.extrautils2.Furnace.remove(<ore:oreIron>);
mods.extrautils2.Furnace.removeAll();
mods.extrautils2.Generator.remove("extrautils2:generator", <minecraft:coal>);
mods.extrautils2.Generator.removeAll();
```

### Forestry

```zenscript
mods.forestry.Centrifuge.removeByOutput(<minecraft:honeycomb>);
mods.forestry.Centrifuge.removeAll();
mods.forestry.BeeMutations.add("forestry.speciesForest", "forestry.speciesMeadows", "forestry.speciesCommon", 10);
mods.forestry.BeeMutations.removeByOutput("forestry.speciesForest");
mods.forestry.BeeProduce.add("forestry.speciesForest", <minecraft:apple>, 0.25);
mods.forestry.BeeProduce.removeProduct("forestry.speciesForest", <minecraft:apple>);
mods.forestry.ThermionicFabricator.removeByFluid(<liquid:glass>);
mods.forestry.ThermionicFabricator.removeSmeltingByOutput(<liquid:glass>);
```

### Inspirations

```zenscript
mods.inspirations.AnvilSmashing.add(<minecraft:iron_block>, <minecraft:iron_ingot>);
mods.inspirations.AnvilSmashing.removeByOutput(<minecraft:iron_ingot>);
mods.inspirations.AnvilSmashing.removeAll();
mods.inspirations.Cauldron.addBrewingRecipe("minecraft:water", "minecraft:awkward", <minecraft:nether_wart>);
```

### Tinkers' Construct

```zenscript
mods.tconstruct.Melting.addRecipe(<liquid:molten_iron>, <minecraft:iron_ingot>, 800);
mods.tconstruct.Melting.removeRecipe(<liquid:molten_iron>, <minecraft:iron_ingot>);

mods.tconstruct.Alloy.addRecipe(
    <liquid:molten_bronze>, [<liquid:molten_copper>, <liquid:molten_tin>]
);
mods.tconstruct.Casting.addTableRecipe(
    <minecraft:iron_ingot>, <minecraft:iron_block>, <liquid:molten_iron>, 144
);
```

### Tinkers' Complement

```zenscript
mods.tcomplement.Melter.removeByInput(<minecraft:iron_ingot>);
mods.tcomplement.Melter.removeByOutput(<liquid:molten_iron>);
mods.tcomplement.Blacklist.addRecipe(<liquid:molten_gold>, <ore:ingotGold>);
mods.tcomplement.Blacklist.removeAll();
```

### Thaumcraft

```zenscript
mods.thaumcraft.Crucible.registerRecipe("example_crucible", "UNLOCKALCHEMY@3", <minecraft:diamond>, <ore:ingotGold>, [<aspect:metallum> * 5]);
mods.thaumcraft.Crucible.removeRecipe(<minecraft:diamond>);
mods.thaumcraft.Research.addCategory("EXAMPLE", "", [<aspect:ordo> * 5], "thaumcraft:textures/aspects/ordo.png", "thaumcraft:textures/gui/gui_research_back_1.jpg");
mods.thaumcraft.Research.addNode("EXAMPLE", "EXAMPLE_NODE", "example.node", 1, 1);
mods.thaumcraft.Research.connectNodes("EXAMPLE", "BASICS", "EXAMPLE_NODE");
mods.thaumcraft.Research.addResearchLocation("examplemod", "research/example.json");
```

### Thermal Expansion

```zenscript
mods.thermalexpansion.Furnace.addRecipe(<minecraft:iron_ingot>, <minecraft:iron_ore>, 2000);
mods.thermalexpansion.Furnace.removeRecipeByInput(<ore:oreIron>);
mods.thermalexpansion.Furnace.removeAll();
mods.thermalexpansion.Smelter.removeRecipeByOutput(<minecraft:iron_ingot>);
mods.thermalexpansion.Transposer.removeAll();
mods.thermalexpansion.CompressionDynamo.removeAll();
```

GroovyScript-only features such as Groovy `Closure` callbacks, internal recipe
objects, RecipeBuilder objects, virtualized registries, reload rollback and ASM
accessors are intentionally not represented by these CRT examples.
