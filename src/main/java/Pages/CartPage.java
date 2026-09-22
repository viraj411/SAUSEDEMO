package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class CartPage extends BasePage {

    private static final By PAGE_TITLE = By.className("title");
    private static final By CART_ITEMS = By.className("cart_item");
    private static final By ITEM_NAMES = By.className("inventory_item_name");
    private static final By ITEM_PRICES = By.className("inventory_item_price");
    private static final By CART_QUANTITY = By.className("cart_quantity");
    private static final By REMOVE_BUTTONS = By.cssSelector(".cart_item button[id^='remove-']");
    private static final By CHECKOUT_BUTTON = By.id("checkout");
    private static final By CONTINUE_SHOPPING_BUTTON = By.id("continue-shopping");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public String getPageTitle() {
        waitForUrlToContain("cart.html");
        return stableText(PAGE_TITLE);
    }

    public int getItemCount() {
        return driver.findElements(CART_ITEMS).size();
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public List<String> getItemNames() {
        waitForUrlToContain("cart.html");
        return visibleTexts(ITEM_NAMES);
    }

    public List<String> getItemPrices() {
        waitForUrlToContain("cart.html");
        return visibleTexts(ITEM_PRICES);
    }

    public List<String> getQuantities() {
        waitForUrlToContain("cart.html");
        return visibleTexts(CART_QUANTITY);
    }

    public void removeItem(String slug) {
        click(By.id("remove-" + slug));
    }

    public void removeAllItems() {
        int remaining = driver.findElements(REMOVE_BUTTONS).size();
        while (remaining > 0) {
            click(REMOVE_BUTTONS);
            int previous = remaining;
            remaining = countDecreased(REMOVE_BUTTONS, previous);
        }
    }

    public void continueShopping() {
        click(CONTINUE_SHOPPING_BUTTON);
        waitForUrlToContain("inventory.html");
    }

    public void clickCheckout() {
        click(CHECKOUT_BUTTON);
        waitForUrlToContain("checkout-step-one");
    }

    public boolean isCheckoutDisplayed() {
        return isDisplayed(CHECKOUT_BUTTON);
    }
}
