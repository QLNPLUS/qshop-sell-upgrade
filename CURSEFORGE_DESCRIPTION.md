# QShop Sell Upgrade

QShop Sell Upgrade adds automatic selling upgrades for Sophisticated Backpacks.

When Sophisticated Backpacks picks up an item, the mod checks the price rules provided by QShop Sell Box. If the item has a valid price and matches the upgrade filter mode, it is converted directly into QShop currency instead of being inserted into the backpack.

## Features

- Automatically sells picked-up items with a valid price.
- Supports whitelist and blacklist modes.
- Sell Upgrade provides a 3x3 filter grid.
- Advanced Sell Upgrade provides a 4x4 filter grid with 16 slots.
- Both upgrades are listed in the Sophisticated Backpacks creative tab.
- Separate branches for Forge 1.20.1, NeoForge 1.21.1, and NeoForge 26.1.2.

## Usage

1. Install Sell Upgrade or Advanced Sell Upgrade into a Sophisticated Backpack.
2. Select whitelist or blacklist mode in the upgrade settings.
3. Place items in the filter slots and let the backpack pick up items.
4. Items matching the price and filter rules are sold automatically instead of occupying backpack space.

The default mode is blacklist mode. With an empty blacklist, every item with a valid price can be sold automatically.

## Dependencies

- QShop
- QShop Sell Box
- Sophisticated Backpacks
- Sophisticated Core (the minimum version depends on the Minecraft branch; Forge 1.20.1 requires at least 1.3.6.1514)

## Recipes

Sell Upgrade is crafted with an Automatic Sell Box, Blank Upgrade, and iron ingots. Advanced Sell Upgrade is crafted with a chest, Sell Upgrade, and iron ingots. The exact recipes are included in the corresponding mod JAR.

## Issues

Report issues on [GitHub Issues](https://github.com/QLNPLUS/qshop-sell-upgrade/issues) and include the Minecraft version, loader version, dependency versions, and relevant log output.

## License

All Rights Reserved (ARR). The project and its source code may not be copied, modified, redistributed, sublicensed, or commercially used without prior written permission.
