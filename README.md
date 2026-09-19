# QShop Sell Upgrade

精妙背包升级插件：当精妙背包吸入物品时，调用 QShop Sell Box 的价格规则。物品有有效售价且通过升级上的白名单/黑名单过滤时，物品不会进入背包，而是按售价兑换为 QShop 钱币。

## 版本工作树

- forge-1.20.1：Forge 1.20.1，当前实现分支。
- neoforge-1.21.1：NeoForge 1.21.1，预留分支。
- neoforge-1.26.1.2：NeoForge 26.1.2，预留分支。

## 运行依赖

- QShop
- QShop Sell Box
- Sophisticated Backpacks
- Sophisticated Core

过滤器默认是黑名单模式：空黑名单表示所有有价格的物品都可自动出售。打开精妙背包的升级设置页可切换白名单/黑名单并放入过滤物品。

