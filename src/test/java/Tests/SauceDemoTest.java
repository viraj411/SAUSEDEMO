package Tests;

import Pages.CartPage;
import Pages.CheckoutPage;
import Pages.Homepage;
import Pages.LoginPage;
import Pages.ProductPage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.WebDriverManager;

public class SauceDemoTest {

    private WebDriver driver;
    private LoginPage loginPage;
    private ProductPage productPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;
    private Homepage homepage;

    @BeforeClass
    public void setup() {
        driver = WebDriverManager.getDriver();
        driver.get(WebDriverManager.BASE_URL);
        loginPage = new LoginPage(driver);
        productPage = new ProductPage(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);
        homepage = new Homepage(driver);
    }

    @Test(priority = 1)
    public void testLogin() {
        loginPage.validLogin("standard_user", "secret_sauce");
        Assert.assertEquals(driver.getTitle(), "Swag Labs");
        System.out.println("Login successful");
        WebDriverManager.takeScreenshot("testLogin");
    }

    @Test(priority = 2)
    public void checkHomepageContent() {
        homepage.clickMenu();
        homepage.printMenuItemsAndCheckClickable();
        homepage.clickCloseMenu();
        Assert.assertTrue(homepage.isSortingMenuDisplayed());
        homepage.checkSortingByAlphabets();
        homepage.checkSortingByZtoA();
        homepage.checkPriceSortingLowToHigh();
        homepage.checkPriceSortingHighToLow();
        homepage.printSocialMediaIconsAndCheckClickable();
        Assert.assertTrue(homepage.isFooterDisplayed());
        WebDriverManager.takeScreenshot("checkhomepagecontent");
    }

    @Test(priority = 3)
    public void testAddToCart() {
        productPage.checkProductDetails();
        productPage.addSingleItemToCart();
        productPage.openCart();
        Assert.assertEquals(cartPage.getCartQuantity(), "1");
        System.out.println("Item added to cart successfully");
        WebDriverManager.takeScreenshot("testAddToCart");
    }

    @Test(priority = 4)
    public void testCheckout() {
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
    public void testAddingAndRemovingProducts() {
        productPage.addAllItemsToCart();
        productPage.openCart();
        productPage.removeAllItemsFromCart();
        WebDriverManager.takeScreenshot("testaddingandremovingtheproducts");
    }

    @AfterClass
    public void teardown() {
        WebDriverManager.quitDriver();
    }
}
