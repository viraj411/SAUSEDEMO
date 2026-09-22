package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ProductPage extends BasePage {

    private static final By CART_ICON = By.id("shopping_cart_container");
    private static final By CART_TITLE = By.className("title");
    private static final By CART_BADGE = By.className("shopping_cart_badge");
    private static final By INVENTORY_ADD_BUTTONS = By.cssSelector(".inventory_item button[id^='add-to-cart']");
    private static final By DETAILS_NAME = By.className("inventory_details_name");
    private static final By DETAILS_DESCRIPTION = By.className("inventory_details_desc");
    private static final By DETAILS_PRICE = By.className("inventory_details_price");
    private static final By DETAILS_ADD_BUTTON = By.id("add-to-cart");
    private static final By DETAILS_REMOVE_BUTTON = By.id("remove");
    private static final By BACK_TO_PRODUCTS_BUTTON = By.id("back-to-products");
    private static final String CART_PAGE_TITLE = "Your Cart";

    public ProductPage(WebDriver driver) {
        super(driver);
    }

    public void openProduct(String productName) {
        click(By.linkText(productName));
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
        waitForUrlToContain("inventory.html");
    }

    public void addItem(String slug) {
        click(By.id("add-to-cart-" + slug));
    }

    public void addItemFromDetails() {
        click(DETAILS_ADD_BUTTON);
    }

    public void removeItem(String slug) {
        click(By.id("remove-" + slug));
    }

    public void addAllItemsToCart() {
        int remaining = driver.findElements(INVENTORY_ADD_BUTTONS).size();
        while (remaining > 0) {
            click(INVENTORY_ADD_BUTTONS);
            int previous = remaining;
            remaining = countDecreased(INVENTORY_ADD_BUTTONS, previous);
        }
    }

    public String getCartButtonText(String slug) {
        return buttonText(By.id("remove-" + slug), By.id("add-to-cart-" + slug));
    }

    public String getDetailsCartButtonText() {
        return buttonText(DETAILS_REMOVE_BUTTON, DETAILS_ADD_BUTTON);
    }

    public void openCart() {
        click(CART_ICON);
        waitForUrlToContain("cart.html");
        wait.until(driver -> {
            try {
                return CART_PAGE_TITLE.equals(driver.findElement(CART_TITLE).getText().trim());
            } catch (StaleElementReferenceException e) {
                return false;
            }
        });
    }

    public void waitForCartBadgeCount(int expectedCount) {
        wait.until(driver -> getCartBadgeCount() == expectedCount);
    }

    public int getCartBadgeCount() {
        List<WebElement> badges = driver.findElements(CART_BADGE);
        if (badges.isEmpty() || !badges.get(0).isDisplayed()) {
            return 0;
        }
        String badgeText = badges.get(0).getText().trim();
        return badgeText.isEmpty() ? 0 : Integer.parseInt(badgeText);
    }

    private String buttonText(By removeButton, By addButton) {
        if (isDisplayed(removeButton)) {
            return textOf(removeButton);
        }
        return textOf(addButton);
    }
}
