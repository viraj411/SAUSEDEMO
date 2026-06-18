package Tests;

import Pages.*;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.WebDriverManager;
import java.lang.reflect.Method;

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
       // driver.get(homepage.BASE_URL);
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
        WebDriverManager.takeScreenshot("testLogin");
    }

    @Test(priority = 2)
    public void checkhomepagecontent() {
       // Assert.assertTrue(homepage.is_burger_menu_present());
        homepage.clickMenu();
        homepage.printMenuItemsAndCheckClickable();
        homepage.clickclose();
        Assert.assertTrue(homepage.issortingmenuDisplayed());
        homepage.checksortingByAlphabets();
        homepage.checkSortingByZtoA();
        homepage.checkPriceSortingLowToHigh();
        homepage.checkPriceSortingHighToLow();
        homepage.printSocialMediaIconsAndCheckClickable();
        Assert.assertTrue(homepage.isFooterDisplayed());
        WebDriverManager.takeScreenshot("checkhomepagecontent");
    }

    @Test(priority = 3)
    public void testAddToCart() {
        //Assert.assertTrue(homepage.is_burger_menu_present());
        //Assert.assertTrue(productPage.verifyproductpagetitle());
        //Assert.assertTrue(productPage.verifyCartItemCountAfterAddingProduct());
        productPage.checkproductdetails();
        productPage.singleaddToCart();
        productPage.openCart();
        Assert.assertEquals(cartPage.getCartQuantity(), "1");
        System.out.println("Item added to cart successfully");
        WebDriverManager.takeScreenshot("testAddToCart");
    }

    @Test(priority = 4)
    public void testCheckout() {
       // Assert.assertTrue(homepage.is_burger_menu_present());
        cartPage.clickCheckout();
        checkoutPage.verifyCheckoutFields("", "", "");
        checkoutPage.enterShippingDetails("Viraj", "Abhang", "422605");
        Assert.assertTrue(checkoutPage.comparePrice());
        checkoutPage.completeOrder();
        Assert.assertTrue(checkoutPage.isOrderSuccessful(), "Order was not successful!");
        System.out.println("Order placed successfully");
        checkoutPage.clickBackToHome();
        WebDriverManager.takeScreenshot("testCheckout");
    }

    @Test(priority = 5)
    public void testaddingandremovingtheproducts() {
        //Assert.assertTrue(homepage.is_burger_menu_present());
        productPage.addAllItemsToCart();
        productPage.openCart();
        productPage.removeAllItemsFromCart();
        WebDriverManager.takeScreenshot("testaddingandremovingtheproducts");
    }

    @AfterMethod
    public void afterEachTest(Method method) {
        // Additional screenshot for test completion (this will be taken regardless of pass/fail)
        System.out.println("Test completed: " + method.getName());
    }

    @AfterClass
    public void teardown() {
        WebDriverManager.quitDriver();
    }
}
