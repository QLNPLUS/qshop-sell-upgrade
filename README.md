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
- Sophisticated Core（最低 `1.3.6.1514`）

兼容说明：精妙背包本身也必须使用声明支持 Sophisticated Core 1.3.6 系列的版本；较新的 3.26.x 版本要求 Core 1.5.x，不能与旧 Core 组合使用。

过滤器默认是黑名单模式：空黑名单表示所有有价格的物品都可自动出售。打开精妙背包的升级设置页可切换白名单/黑名单并放入过滤物品。
