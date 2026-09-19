# QShop Sell Upgrade - multi-version worktree rules

This repository uses one Git repository with one worktree per loader/version. The primary implementation is developed on the forge-1.20.1 branch; the NeoForge branches contain loader-specific ports of the same feature.

## Branch Matrix

| Branch | Worktree | Loader | Minecraft | JDK | Gradle | Build plugin | Status |
|---|---|---|---|---|---|---|---|
| forge-1.20.1 | D:\projects\q_shop_sell_upgrade\forge-1.20.1 | Forge | 1.20.1 | 17 | 8.1.1 | ForgeGradle 6.0.54 | primary, implemented first |
| neoforge-1.21.1 | D:\projects\q_shop_sell_upgrade\neoforge-1.21.1 | NeoForge | 1.21.1 | 21 | 8.8 | ModDevGradle 2.0.141 | build verified; runtime smoke pending |
| neoforge-1.26.1.2 | D:\projects\q_shop_sell_upgrade\neoforge-1.26.1.2 | NeoForge | 26.1.2 (branch name: 1.26.1.2) | 25 | 9.2.1 | ModDevGradle 2.0.146 | build verified; runtime smoke pending |

- The Forge worktree owns the .git directory. The other two are linked worktrees whose .git files point back to it.
- All three branches were created from the same bootstrap commit. A branch is considered build-ready only after its loader-specific build passes; runtime smoke status is recorded in the matrix.
- Local worktree paths, local branch names, and future remote branch names must remain identical.

## Runtime Dependency Matrix

| Branch | Sophisticated Core | Sophisticated Backpacks | QShop | QShop SellBox |
|---|---|---|---|---|
| forge-1.20.1 | minimum 1.3.6.1514 | 3.23.x line | Forge 1.20.1 / 1.8.0 | Forge 1.20.1 / 1.5.0 |
| neoforge-1.21.1 | 1.21.1-1.5.1.2341; mod range [1.21.1-1.5.1,1.21.2) | 1.21.1-3.26.3.2158 | NeoForge 1.21.1 / 1.8.1 | NeoForge 1.21.1 / 1.5.0 |
| neoforge-1.26.1.2 | 26.1.2-1.5.0.2334; mod range [26.1.2-1.5.0,26.2) | 26.1.2-3.26.2.2156 | NeoForge 26.1.2 / 1.8.1 | NeoForge 26.1.2 / 1.5.0 |

## Cross-Version Changes

- Develop and test one version first, then commit it before propagation.
- Cross-version propagation uses only git cherry-pick -x <sha>; do not hand-rewrite the same change on another branch.
- A user confirmation such as "测试通过", "可以了", or "同步到其他版本" triggers the propagation review.
- For each target, classify the change as applies, does not apply because it is loader-specific, or needs adaptation because of an API rename. State the conclusion before acting.
- Fixes found on a non-primary branch return to forge-1.20.1 first, then move onward.

## Known Platform Gaps

| Area | Forge 1.20.1 | NeoForge 1.21.1 / 26.1.2 |
|---|---|---|
| Metadata | META-INF/mods.toml | META-INF/neoforge.mods.toml |
| QShop wallet | Forge QShop addon API | NeoForge QShop addon API, verify signatures per branch |
| SellBox prices | SellBoxPrices.resolve(ItemStack) | verify the NeoForge package/API before porting |
| Item data | NBT APIs | 1.21+ DataComponents and loader-specific shims may be required |
| Upgrade registration | Forge registry and ForgeGradle | NeoForge registry and ModDevGradle |
| Runtime dependency | Forge Sophisticated Backpacks/Core artifacts | NeoForge Sophisticated Backpacks/Core artifacts |

These differences are real platform boundaries. Do not make a blind copy across loaders; cherry-pick first, then adapt the conflict/API boundary explicitly.

## Release Tags

Use v<mod-version>-<loader>-<mcversion>, for example v0.1.0-forge-1.20.1. Do not create loader-less tags for new releases.

## Toolchain

Use the version-specific Gradle wrapper and JDK paths:

- JDK 17: C:\Program Files\Java\jdk-17
- JDK 21: C:\Program Files\Java\jdk-21
- JDK 25: C:\Program Files\Java\jdk-25.0.4.1

Build version worktrees sequentially and keep each worktree's build, run, and generated output local to that worktree.
