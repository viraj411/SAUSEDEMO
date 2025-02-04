package Tests;

import Pages.CartPage;
import Pages.CheckoutPage;
import Pages.LoginPage;
import Pages.ProductPage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.WebDriverManager;

public class SauceDemoTest  extends WebDriverManager{

    WebDriver driver;
    LoginPage loginPage;
    ProductPage productPage;
    CartPage cartPage;
    CheckoutPage checkoutPage;

    @BeforeClass
    public void setup() {
        driver = WebDriverManager.getDriver();
        driver.get("https://www.saucedemo.com/");
        loginPage = new LoginPage(driver);
        productPage = new ProductPage(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);
    }

    @Test(priority = 1)
    public void testLogin() {
        loginPage.enterUsername("standard_user");
        loginPage.enterPassword("secret_sauce");
        loginPage.clickLogin();
        Assert.assertEquals(driver.getTitle(), "Swag Labs");
        System.out.println("Login successful");
    }

    @Test(priority = 2)
    public void testAddToCart() {
        productPage.addToCart();
        productPage.openCart();
        Assert.assertEquals(cartPage.getCartQuantity(), "1");
        System.out.println("Item added to cart successfully");
    }

    @Test(priority = 3)
    public void testCheckout() {
        cartPage.clickCheckout();
        checkoutPage.enterShippingDetails("Viraj", "Abhang", "422605");
        checkoutPage.completeOrder();
        Assert.assertTrue(checkoutPage.isOrderSuccessful(), "Order was not successful!");
        System.out.println("Order placed successfully");
    }

    @AfterClass
    public void teardown() {
        WebDriverManager.quitDriver();
    }
}
