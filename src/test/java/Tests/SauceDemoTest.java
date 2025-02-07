package Tests;

import Pages.*;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.WebDriverManager;

public class SauceDemoTest {

    WebDriver driver;
    LoginPage loginPage;
    ProductPage productPage;
    CartPage cartPage;
    CheckoutPage checkoutPage;
    Homepage homepage;

    @BeforeClass
    public void setup() {
        driver = WebDriverManager.getDriver();
        driver.get("https://www.saucedemo.com/");
        loginPage = new LoginPage(driver);
        productPage = new ProductPage(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);
        homepage = new Homepage(driver);
    }

    @Test(priority = 1)
    public void testLogin() throws InterruptedException {
        loginPage.validLogin("standard_user", "secret_sauce");
        Assert.assertEquals(driver.getTitle(), "Swag Labs");
        System.out.println("Login successful");
    }

    @Test(priority = 2)
    public void checkhomepagecontent() {
        homepage.clickMenu();
        homepage.printMenuItemsAndCheckClickable();
        homepage.clickclose();
        Assert.assertTrue(homepage.issortingmenuDisplayed());
        homepage.checksortingByAlphabets();
        homepage.checkSortingByZtoA();
        homepage.checkPriceSortingLowToHigh();
        homepage.checkPriceSortingHighToLow();
        homepage.printSocialMediaIconsAndCheckClickable();
        Assert.assertEquals(homepage.isFooterDisplayed(), "© 2025 Sauce Labs. All Rights Reserved. Terms of Service | Privacy Policy");
    }

    @Test(priority = 3)
    public void testAddToCart() {
        productPage.singleaddToCart();
        productPage.openCart();
        Assert.assertEquals(cartPage.getCartQuantity(), "1");
        System.out.println("Item added to cart successfully");
    }

    @Test(priority = 4)
    public void testCheckout() {
        cartPage.clickCheckout();
        checkoutPage.enterShippingDetails("Viraj", "Abhang", "422605");
       Assert.assertTrue(checkoutPage.comparePrice());
        checkoutPage.completeOrder();
        Assert.assertTrue(checkoutPage.isOrderSuccessful(), "Order was not successful!");
        System.out.println("Order placed successfully");
        checkoutPage.clickBackToHome();
    }

    @Test(priority = 5)
    public void testaddingandremovingtheproducts() {
        productPage.addAllItemsToCart();
        productPage.openCart();
        productPage.removeAllItemsFromCart();
    }

    @AfterClass
    public void teardown() {
        WebDriverManager.quitDriver();
    }
}
