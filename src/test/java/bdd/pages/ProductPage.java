package bdd.pages;

import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ProductPage extends BasePage {

    private static final String CART_ICON = "#shopping_cart_container";
    private static final String CART_TITLE = ".title";
    private static final String CART_BADGE = ".shopping_cart_badge";
    private static final String INVENTORY_ADD_BUTTONS = ".inventory_item button[id^='add-to-cart']";
    private static final String DETAILS_NAME = ".inventory_details_name";
    private static final String DETAILS_DESCRIPTION = ".inventory_details_desc";
    private static final String DETAILS_PRICE = ".inventory_details_price";
    private static final String DETAILS_ADD_BUTTON = "#add-to-cart";
    private static final String DETAILS_REMOVE_BUTTON = "#remove";
    private static final String BACK_TO_PRODUCTS_BUTTON = "#back-to-products";
    private static final String CART_PAGE_TITLE = "Your Cart";

    public ProductPage(Page page) {
        super(page);
    }

    public void openProduct(String productName) {
        page.getByText(productName, new Page.GetByTextOptions().setExact(true)).first().click();
    }

    public String getDetailsName() {
        return textOf(DETAILS_NAME);
    }

    public String getDetailsDescription() {
        return textOf(DETAILS_DESCRIPTION);
    }

    public String getDetailsPrice() {
        return textOf(DETAILS_PRICE);
    }

    public void backToProducts() {
        click(BACK_TO_PRODUCTS_BUTTON);
        waitForUrl("inventory.html");
    }

    public void addItem(String slug) {
        click("#add-to-cart-" + slug);
    }

    public void addItemFromDetails() {
        click(DETAILS_ADD_BUTTON);
    }

    public void removeItem(String slug) {
        click("#remove-" + slug);
    }

    public void addAllItemsToCart() {
        clickUntilGone(INVENTORY_ADD_BUTTONS);
    }

    public void waitForCatalogButton(String slug, String label) {
        assertThat(page.locator(catalogButton(slug))).hasText(label);
    }

    public void waitForDetailsButton(String label) {
        assertThat(page.locator(DETAILS_ADD_BUTTON + ", " + DETAILS_REMOVE_BUTTON)).hasText(label);
    }

    public void openCart() {
        click(CART_ICON);
        waitForUrl("cart.html");
        assertThat(page.locator(CART_TITLE)).hasText(CART_PAGE_TITLE);
    }

    public void waitForCartBadgeCount(int expectedCount) {
        if (expectedCount == 0) {
            assertThat(page.locator(CART_BADGE)).not().isVisible();
        } else {
            assertThat(page.locator(CART_BADGE)).hasText(String.valueOf(expectedCount));
        }
    }

    public int getCartBadgeCount() {
        if (!isDisplayedNow(CART_BADGE)) {
            return 0;
        }
        String badgeText = page.locator(CART_BADGE).first().innerText().trim();
        return badgeText.isEmpty() ? 0 : Integer.parseInt(badgeText);
    }

    private static String catalogButton(String slug) {
        return "#add-to-cart-" + slug + ", #remove-" + slug;
    }
}
