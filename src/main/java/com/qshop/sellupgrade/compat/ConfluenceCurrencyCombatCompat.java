package com.qshop.sellupgrade.compat;

import com.mojang.logging.LogUtils;
import com.qshop.sellbox.PriceQuote;
import com.qshop.sellbox.SellBoxPrices;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import org.slf4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/** Optional integration with Confluence and the QShop Confluence Currency Bridge. */
public final class ConfluenceCurrencyCombatCompat {
    private static final String CONFLUENCE_MOD_ID = "confluence";
    private static final String BRIDGE_MOD_ID = "qshop_confluence";
    private static final String PRICE_RESOLVER_CLASS =
            "com.qshop.confluence.ConfluenceSellBoxPrices";
    private static final Logger LOGGER = LogUtils.getLogger();

    private static volatile Method resolveForSale;
    private static volatile boolean resolverLookupComplete;
    private static volatile boolean failureLogged;

    private ConfluenceCurrencyCombatCompat() {
    }

    /**
     * Uses the bridge's SellBox-first resolver when both optional mods are loaded.
     * Otherwise, it preserves the existing SellBox-only behavior.
     */
    public static PriceQuote resolve(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return null;
        }

        if (!ModList.get().isLoaded(CONFLUENCE_MOD_ID)
                || !ModList.get().isLoaded(BRIDGE_MOD_ID)) {
            return SellBoxPrices.resolve(stack);
        }

        Method resolver = getResolver();
        if (resolver == null) {
            return null;
        }

        try {
            Object result = resolver.invoke(null, stack);
            if (result == null || result instanceof PriceQuote) {
                return (PriceQuote) result;
            }
            logFailure(new IllegalStateException("Unexpected return type from "
                    + PRICE_RESOLVER_CLASS + ".resolveForSale: " + result.getClass().getName()));
        } catch (IllegalAccessException | InvocationTargetException | LinkageError exception) {
            logFailure(exception);
        }
        return null;
    }

    private static Method getResolver() {
        if (resolverLookupComplete) {
            return resolveForSale;
        }

        synchronized (ConfluenceCurrencyCombatCompat.class) {
            if (!resolverLookupComplete) {
                try {
                    Class<?> resolverClass = Class.forName(PRICE_RESOLVER_CLASS, false,
                            ConfluenceCurrencyCombatCompat.class.getClassLoader());
                    resolveForSale = resolverClass.getMethod("resolveForSale", ItemStack.class);
                    if (!PriceQuote.class.isAssignableFrom(resolveForSale.getReturnType())) {
                        throw new NoSuchMethodException("resolveForSale does not return PriceQuote");
                    }
                } catch (ReflectiveOperationException | LinkageError exception) {
                    logFailure(exception);
                } finally {
                    resolverLookupComplete = true;
                }
            }
        }
        return resolveForSale;
    }

    private static void logFailure(Throwable exception) {
        if (failureLogged) {
            return;
        }
        synchronized (ConfluenceCurrencyCombatCompat.class) {
            if (!failureLogged) {
                LOGGER.warn("QShop Sell Upgrade could not use the optional Confluence price resolver; "
                        + "automatic selling is skipped for safety.", exception);
                failureLogged = true;
            }
        }
    }
}
