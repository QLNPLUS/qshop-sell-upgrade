# QShop Sell Upgrade

QShop Sell Upgrade 是精妙背包（Sophisticated Backpacks）的自动出售升级插件。

当精妙背包吸入物品时，插件会读取 QShop Sell Box 的价格规则。物品有有效售价，并且符合升级的过滤模式时，物品不会进入背包，而是直接转换为 QShop 提供的货币。

## 功能

- 自动出售有价格的拾取物品。
- 支持白名单和黑名单模式。
- 普通售卖升级提供 3x3 个筛选格。
- 高级售卖升级提供 4x4 共 16 个筛选格。
- 两个升级均加入精妙背包的创造模式物品分页。
- 支持 Forge 1.20.1、NeoForge 1.21.1 和 NeoForge 26.1.2 分支。

## 使用

1. 将售卖升级或高级售卖升级安装到精妙背包。
2. 在升级设置页面选择白名单或黑名单模式。
3. 将物品放入筛选格，并让背包吸入物品。
4. 符合价格和过滤条件的物品会被自动出售，不再占用背包空间。

默认模式为黑名单模式。黑名单为空时，所有有有效售价的物品都可以自动出售。

## 依赖

- QShop
- QShop Sell Box
- Sophisticated Backpacks
- Sophisticated Core（最低要求按对应版本分支配置；Forge 1.20.1 最低为 1.3.6.1514）

## 合成

售卖升级使用自动售货箱、空白升级和铁锭合成。高级售卖升级使用箱子、售卖升级和铁锭合成。具体配方以对应版本 JAR 内的配方文件为准。

## 问题反馈

请在 GitHub Issues（https://github.com/QLNPLUS/qshop-sell-upgrade/issues）提交问题，并附上 Minecraft 版本、加载器版本、相关依赖版本和日志片段。

## 许可

All Rights Reserved（ARR）。未经版权所有者书面许可，不得复制、修改、再发布、再许可或商业使用本项目及其源代码。
