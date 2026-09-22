package Tests;

import Pages.CartPage;
import Pages.CheckoutPage;
import Pages.Homepage;
import Pages.LoginPage;
import Pages.ProductPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.TestData;

import java.util.List;

public class SauceDemoTest extends BaseTest {

    private LoginPage loginPage;
    private ProductPage productPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;
    private Homepage homepage;

    @BeforeClass
    public void openApplication() {
        loginPage = new LoginPage(driver);
        productPage = new ProductPage(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);
        homepage = new Homepage(driver);
        driver.get(TestData.BASE_URL);
    }

    @Test(priority = 1, description = "A standard user can sign in")
    public void testLogin() {
        loginPage.login(TestData.VALID_USERNAME, TestData.VALID_PASSWORD);
        Assert.assertEquals(driver.getTitle(), TestData.EXPECTED_HOME_TITLE);
        Assert.assertTrue(loginPage.isOnInventoryPage());
    }

    @Test(priority = 2, description = "The catalog menu, sort order, social links, and footer are available")
    public void checkHomepageContent() {
        Assert.assertEquals(homepage.getMenuItemLabels(), TestData.EXPECTED_MENU_ITEMS);
        Assert.assertTrue(homepage.areMenuItemsEnabled());
        homepage.closeMenuIfOpen();

        Assert.assertEquals(homepage.sortByValue(TestData.SORT_A_TO_Z), TestData.SORT_A_TO_Z_LABEL);
        Assert.assertTrue(homepage.isSortedByName(true));
        Assert.assertEquals(homepage.sortByValue(TestData.SORT_Z_TO_A), TestData.SORT_Z_TO_A_LABEL);
        Assert.assertTrue(homepage.isSortedByName(false));
        Assert.assertEquals(homepage.sortByValue(TestData.SORT_PRICE_LOW_TO_HIGH), TestData.SORT_PRICE_LOW_TO_HIGH_LABEL);
        Assert.assertTrue(homepage.isSortedByPrice(true));
        Assert.assertEquals(homepage.sortByValue(TestData.SORT_PRICE_HIGH_TO_LOW), TestData.SORT_PRICE_HIGH_TO_LOW_LABEL);
        Assert.assertTrue(homepage.isSortedByPrice(false));

        List<String> socialLinks = homepage.getSocialLinkHrefs();
        Assert.assertEquals(socialLinks.size(), 3);
        Assert.assertTrue(socialLinks.get(0).contains("x.com") || socialLinks.get(0).contains("twitter.com"));
        Assert.assertTrue(homepage.getFooterText().matches(TestData.FOOTER_TEXT_PATTERN));
    }

    @Test(priority = 3, description = "A product can be reviewed and added to the cart")
    public void testAddToCart() {
        productPage.openProduct(TestData.FIRST_PRODUCT_NAME);
        Assert.assertEquals(productPage.getDetailsName(), TestData.FIRST_PRODUCT_NAME);
        Assert.assertEquals(productPage.getDetailsDescription(), TestData.FIRST_PRODUCT_DESCRIPTION);
        productPage.backToProducts();
        productPage.addItem(TestData.BACKPACK_SLUG);
        productPage.openCart();
        Assert.assertEquals(cartPage.getQuantities(), List.of("1"));
        Assert.assertEquals(cartPage.getItemNames(), List.of(TestData.FIRST_PRODUCT_NAME));
    }

    @Test(priority = 4, description = "Checkout rejects an empty form, then completes a valid order")
    public void testCheckout() {
        cartPage.clickCheckout();
        checkoutPage.enterShippingDetails("", "", "");
        Assert.assertEquals(checkoutPage.getErrorText(), TestData.CHECKOUT_FIRST_NAME_REQUIRED);

        checkoutPage.enterShippingDetails(
                TestData.SHIPPING_FIRST_NAME,
                TestData.SHIPPING_LAST_NAME,
                TestData.SHIPPING_ZIP_CODE);
        Assert.assertTrue(checkoutPage.doesSubtotalMatchItems());
        Assert.assertTrue(checkoutPage.isTotalConsistent());
        checkoutPage.completeOrder();
        Assert.assertEquals(checkoutPage.getSuccessMessage(), TestData.ORDER_SUCCESS_MESSAGE);
        checkoutPage.clickBackToHome();
        Assert.assertEquals(homepage.getPageTitle(), TestData.EXPECTED_PRODUCTS_TITLE);
    }

    @Test(priority = 5, description = "Every catalog product can be added and then removed")
    public void testAddingAndRemovingProducts() {
        productPage.addAllItemsToCart();
        Assert.assertEquals(productPage.getCartBadgeCount(), TestData.EXPECTED_PRODUCT_COUNT);
        productPage.openCart();
        Assert.assertEquals(cartPage.getItemCount(), TestData.EXPECTED_PRODUCT_COUNT);
        cartPage.removeAllItems();
        Assert.assertTrue(cartPage.isEmpty());
        Assert.assertEquals(productPage.getCartBadgeCount(), 0);
    }
}
