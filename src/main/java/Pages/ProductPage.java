package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ProductPage {

    WebDriver driver;
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    public ProductPage(WebDriver driver) {
        this.driver = driver;
    }

    // Locators
    private By addToCartBtn = By.id("add-to-cart-sauce-labs-backpack");
    private By cartIcon = By.id("shopping_cart_container");
    private By addToCartButton = By.xpath(".//button[starts-with(@id, 'add-to-cart')]"); // Dynamic 'Add to Cart' button using partial ID match
    private By removecartitems = By.xpath(".//button[starts-with(@id, 'remove')]");
    private By inventoryitems = By.className("inventory_item");
    private By itemName = By.className("inventory_item_label");
    private By itemPrice = By.className("inventory_item_price");
    private By cartQuantity = By.className("cart_item");
    private By continueShopping = By.id("continue-shopping");

    // Actions
    public void singleaddToCart() {
        driver.findElement(addToCartBtn).click();
    }

    public void openCart() {
        driver.findElement(cartIcon).click();
    }

    public void addAllItemsToCart() {
        List<WebElement> items = driver.findElements(inventoryitems);
        System.out.println("Total items found: " + items.size());

        for (WebElement item : items) {
            String name = item.findElement(itemName).getText();
            String newname = name.substring(0, 20);

            String price = item.findElement(itemPrice).getText();
            WebElement addToCart = item.findElement(addToCartButton);


            wait.until(ExpectedConditions.elementToBeClickable(addToCart));
            addToCart.click();

            System.out.println("Added to Cart: " + newname + " - " + price);
        }

    }

    public void removeAllItemsFromCart() {
        List<WebElement> items = driver.findElements(cartQuantity);
        System.out.println("Total items found in the cart: " + items.size());
        for (WebElement item : items) {
            WebElement remove = item.findElement(removecartitems);
            wait.until(ExpectedConditions.elementToBeClickable(remove));
            remove.click();


        }


    }


}


