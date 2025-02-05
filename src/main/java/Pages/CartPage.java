package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CartPage {
    WebDriver driver;

    public CartPage(WebDriver driver) {
        this.driver = driver;
    }

    // Locators
    private By cartQuantity = By.className("cart_quantity");
    private By checkoutButton = By.id("checkout");

    // Actions
    public String getCartQuantity() {
        return driver.findElement(cartQuantity).getText();
    }

    public void clickCheckout() {
        driver.findElement(checkoutButton).click();
        System.out.println("Checkout Started");

    }
}

