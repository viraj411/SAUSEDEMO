package utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public final class SessionState {

    private static final String CART_STORAGE_KEY = "cart-contents";

    private SessionState() {
    }

    /**
     * Sauce Demo keeps the cart in local storage, so a page load restores it.
     * Tests clear that entry when they need an empty cart.
     */
    public static void clearCart(WebDriver driver) {
        ((JavascriptExecutor) driver).executeScript(
                "window.localStorage.removeItem(arguments[0]);",
                CART_STORAGE_KEY);
    }
}
