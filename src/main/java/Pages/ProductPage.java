package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ProductPage {

    private static final String FIRST_PRODUCT_NAME = "Sauce Labs Backpack";
    private static final String FIRST_PRODUCT_DESCRIPTION =
            "carry.allTheThings() with the sleek, streamlined Sly Pack that melds uncompromising style with unequaled laptop and tablet protection.";

    private final WebDriver driver;
    private final WebDriverWait wait;
    private double totalPrice;

    private final By productPageTitle = By.className("title");
    private final By addBackpackToCartButton = By.id("add-to-cart-sauce-labs-backpack");
    private final By cartIcon = By.id("shopping_cart_container");
    private final By addToCartButton = By.xpath(".//button[starts-with(@id, 'add-to-cart')]");
    private final By removeCartItemsButton = By.xpath(".//button[starts-with(@id, 'remove')]");
    private final By inventoryItems = By.className("inventory_item");
    private final By itemName = By.className("inventory_item_label");
    private final By itemPrice = By.className("inventory_item_price");
    private final By cartItems = By.className("cart_item");
    private final By backToProductsButton = By.id("back-to-products");
    private final By firstProductName = By.xpath("//div[normalize-space()='" + FIRST_PRODUCT_NAME + "']");
    private final By firstProductDescription = By.xpath("//div[normalize-space()='" + FIRST_PRODUCT_DESCRIPTION + "']");

    public ProductPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void addSingleItemToCart() {
        driver.findElement(addBackpackToCartButton).click();
    }

    public void openCart() {
        driver.findElement(cartIcon).click();
    }

    public void addAllItemsToCart() {
        List<WebElement> items = driver.findElements(inventoryItems);
        System.out.println("Total items found: " + items.size());
        totalPrice = 0;

        for (WebElement item : items) {
            String name = item.findElement(itemName).getText();
            String price = item.findElement(itemPrice).getText();
            WebElement addToCart = item.findElement(addToCartButton);

            wait.until(ExpectedConditions.elementToBeClickable(addToCart)).click();
            System.out.println("Added to Cart: " + name.substring(0, Math.min(name.length(), 20)) + " - " + price);

            try {
                totalPrice += Double.parseDouble(price.replaceAll("[^0-9.]", ""));
            } catch (NumberFormatException e) {
                System.out.println("Error parsing price: " + price);
            }
        }

        System.out.println("Total price of the products is " + totalPrice);
    }

    public void removeAllItemsFromCart() {
        List<WebElement> items = driver.findElements(cartItems);
        System.out.println("Total items found in the cart: " + items.size());

        for (WebElement item : items) {
            WebElement removeButton = item.findElement(removeCartItemsButton);
            wait.until(ExpectedConditions.elementToBeClickable(removeButton)).click();
        }
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void checkProductDetails() {
        wait.until(ExpectedConditions.elementToBeClickable(firstProductName)).click();

        String productName = wait.until(ExpectedConditions.visibilityOfElementLocated(firstProductName)).getText();
        String productDescription = wait.until(ExpectedConditions.visibilityOfElementLocated(firstProductDescription)).getText();

        System.out.println(productName);
        System.out.println(productDescription);

        if (FIRST_PRODUCT_NAME.equals(productName) && FIRST_PRODUCT_DESCRIPTION.equals(productDescription)) {
            wait.until(ExpectedConditions.elementToBeClickable(backToProductsButton)).click();
        } else {
            System.out.println("Product details do not match");
        }
    }

    public boolean verifyProductPageTitle() {
        String title = wait.until(ExpectedConditions.visibilityOfElementLocated(productPageTitle)).getText();
        return "Products".equals(title);
    }

    public boolean verifyCartItemCountAfterAddingProduct() {
        driver.findElement(addBackpackToCartButton).click();
        int cartItemCount = Integer.parseInt(driver.findElement(cartIcon).getText().trim());
        System.out.println("Cart item quantity is: " + cartItemCount);

        if (cartItemCount == 1) {
            driver.findElement(removeCartItemsButton).click();
        }

        return cartItemCount == 1;
    }
}
