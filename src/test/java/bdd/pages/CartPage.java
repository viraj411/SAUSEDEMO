package bdd.pages;

import com.microsoft.playwright.Page;

import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class CartPage extends BasePage {

    private static final String PAGE_TITLE = ".title";
    private static final String CART_ITEMS = ".cart_item";
    private static final String ITEM_NAMES = ".inventory_item_name";
    private static final String ITEM_PRICES = ".inventory_item_price";
    private static final String CART_QUANTITY = ".cart_quantity";
    private static final String REMOVE_BUTTONS = ".cart_item button[id^='remove-']";
    private static final String CHECKOUT_BUTTON = "#checkout";
    private static final String CONTINUE_SHOPPING_BUTTON = "#continue-shopping";

    public CartPage(Page page) {
        super(page);
    }

    public String getPageTitle() {
        waitForUrl("cart.html");
        return textOf(PAGE_TITLE);
    }

    public int getItemCount() {
        return page.locator(CART_ITEMS).count();
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void waitForItemCount(int expectedCount) {
        assertThat(page.locator(CART_ITEMS)).hasCount(expectedCount);
    }

    public List<String> getItemNames() {
        waitForUrl("cart.html");
        return textsOf(ITEM_NAMES);
    }

    public List<String> getItemPrices() {
        waitForUrl("cart.html");
        return textsOf(ITEM_PRICES);
    }

    public List<String> getQuantities() {
        waitForUrl("cart.html");
        return textsOf(CART_QUANTITY);
    }

    public void removeItem(String slug) {
        click("#remove-" + slug);
    }

    public void removeAllItems() {
        clickUntilGone(REMOVE_BUTTONS);
    }

    public void continueShopping() {
        click(CONTINUE_SHOPPING_BUTTON);
        waitForUrl("inventory.html");
    }

    public void clickCheckout() {
        click(CHECKOUT_BUTTON);
        waitForUrl("checkout-step-one");
    }

    public boolean isCheckoutDisplayed() {
        return isVisible(CHECKOUT_BUTTON);
    }
}
