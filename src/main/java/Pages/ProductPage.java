package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductPage {

    WebDriver driver;

    public ProductPage(WebDriver driver) {
        this.driver = driver;
    }

    // Locators
    private By addToCartBtn = By.id("add-to-cart-sauce-labs-backpack");
    private By cartIcon = By.id("shopping_cart_container");

    // Actions
    public void addToCart() {
        driver.findElement(addToCartBtn).click();
    }

    public void openCart() {
        driver.findElement(cartIcon).click();
    }
}
