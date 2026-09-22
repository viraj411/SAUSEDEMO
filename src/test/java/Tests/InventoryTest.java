package Tests;

import Pages.CartPage;
import Pages.CheckoutPage;
import Pages.Homepage;
import Pages.LoginPage;
import Pages.ProductPage;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import utils.SessionState;
import utils.TestData;

public abstract class InventoryTest extends BaseTest {

    protected LoginPage loginPage;
    protected Homepage homepage;
    protected ProductPage productPage;
    protected CartPage cartPage;
    protected CheckoutPage checkoutPage;

    @BeforeClass
    public void loginToInventory() {
        loginPage = new LoginPage(driver);
        homepage = new Homepage(driver);
        productPage = new ProductPage(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);
        driver.get(TestData.BASE_URL);
        loginPage.login(TestData.VALID_USERNAME, TestData.VALID_PASSWORD);
    }

    @BeforeMethod
    public void resetInventoryState() {
        driver.get(TestData.INVENTORY_URL);
        if (!loginPage.isOnInventoryPage()) {
            driver.get(TestData.BASE_URL);
            loginPage.login(TestData.VALID_USERNAME, TestData.VALID_PASSWORD);
            driver.get(TestData.INVENTORY_URL);
        }
        SessionState.clearCart(driver);
        driver.navigate().refresh();
        homepage.waitUntilLoaded();
        homepage.closeMenuIfOpen();
    }
}
