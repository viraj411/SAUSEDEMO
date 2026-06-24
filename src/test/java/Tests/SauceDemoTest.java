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
import utils.TestData;
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
        driver.get(TestData.BASE_URL);
        loginPage = new LoginPage(driver);
        productPage = new ProductPage(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);
        homepage = new Homepage(driver);
    }

    @Test(priority = 1)
    public void testLogin() {
        loginPage.validLogin(TestData.VALID_USERNAME, TestData.VALID_PASSWORD);
        Assert.assertEquals(driver.getTitle(), TestData.EXPECTED_HOME_TITLE);
        System.out.println("Login successful");
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
    }

    @Test(priority = 3)
    public void testAddToCart() {
        productPage.checkProductDetails();
        productPage.addSingleItemToCart();
        productPage.openCart();
        Assert.assertEquals(cartPage.getCartQuantity(), "1");
        System.out.println("Item added to cart successfully");
    }

    @Test(priority = 4)
    public void testCheckout() {
        cartPage.clickCheckout();
        checkoutPage.verifyCheckoutFields("", "", "");
        checkoutPage.enterShippingDetails(
                TestData.SHIPPING_FIRST_NAME,
                TestData.SHIPPING_LAST_NAME,
                TestData.SHIPPING_ZIP_CODE);
        Assert.assertTrue(checkoutPage.comparePrice());
        checkoutPage.completeOrder();
        Assert.assertTrue(checkoutPage.isOrderSuccessful(), "Order was not successful!");
        System.out.println("Order placed successfully");
        checkoutPage.clickBackToHome();
    }

    @Test(priority = 5)
    public void testAddingAndRemovingProducts() {
        productPage.addAllItemsToCart();
        productPage.openCart();
        productPage.removeAllItemsFromCart();
    }

    @AfterClass
    public void teardown() {
        WebDriverManager.quitDriver();
    }
}
