package Tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import utils.TestData;

public class CartTests extends InventoryTest {

    @Test(description = "Opening a product shows its name, description, and price")
    public void testProductDetailsMatchCatalog() {
        productPage.openProduct(TestData.FIRST_PRODUCT_NAME);
        Assert.assertEquals(productPage.getDetailsName(), TestData.FIRST_PRODUCT_NAME);
        Assert.assertEquals(productPage.getDetailsDescription(), TestData.FIRST_PRODUCT_DESCRIPTION);
        Assert.assertEquals(productPage.getDetailsPrice(), TestData.FIRST_PRODUCT_PRICE);
    }

    @Test(description = "Back to products returns to the catalog")
    public void testBackToProducts() {
        productPage.openProduct(TestData.FIRST_PRODUCT_NAME);
        productPage.backToProducts();
        Assert.assertEquals(homepage.getPageTitle(), TestData.EXPECTED_PRODUCTS_TITLE);
        Assert.assertEquals(homepage.getProductCount(), TestData.EXPECTED_PRODUCT_COUNT);
    }

    @Test(description = "Adding a product from its details page updates the cart badge")
    public void testAddToCartFromProductDetails() {
        productPage.openProduct(TestData.FIRST_PRODUCT_NAME);
        productPage.addItemFromDetails();
        Assert.assertEquals(productPage.getCartBadgeCount(), 1);
        Assert.assertEquals(productPage.getDetailsCartButtonText(), TestData.REMOVE_LABEL);
        productPage.backToProducts();
        Assert.assertEquals(productPage.getCartBadgeCount(), 1);
    }

    @Test(description = "Add to cart switches the button to Remove and shows a badge of 1")
    public void testAddSingleItemUpdatesBadgeAndButton() {
        productPage.addItem(TestData.BACKPACK_SLUG);
        Assert.assertEquals(productPage.getCartBadgeCount(), 1);
        Assert.assertEquals(productPage.getCartButtonText(TestData.BACKPACK_SLUG), TestData.REMOVE_LABEL);
    }

    @Test(description = "The same product cannot be added a second time while it is in the cart")
    public void testAddButtonTogglesInsteadOfDuplicating() {
        productPage.addItem(TestData.BACKPACK_SLUG);
        Assert.assertEquals(productPage.getCartBadgeCount(), 1);
        Assert.assertEquals(productPage.getCartButtonText(TestData.BACKPACK_SLUG), TestData.REMOVE_LABEL);
    }

    @Test(description = "Removing a product from the catalog clears the badge")
    public void testRemoveFromCatalogClearsBadge() {
        productPage.addItem(TestData.BACKPACK_SLUG);
        productPage.removeItem(TestData.BACKPACK_SLUG);
        Assert.assertEquals(productPage.getCartBadgeCount(), 0);
        Assert.assertEquals(productPage.getCartButtonText(TestData.BACKPACK_SLUG), TestData.ADD_TO_CART_LABEL);
    }

    @Test(description = "The cart lists the added product with quantity 1 and its price")
    public void testCartShowsAddedProduct() {
        productPage.addItem(TestData.BACKPACK_SLUG);
        productPage.openCart();
        Assert.assertEquals(cartPage.getPageTitle(), TestData.CART_TITLE);
        Assert.assertEquals(cartPage.getItemCount(), 1);
        Assert.assertEquals(cartPage.getItemNames(), java.util.List.of(TestData.FIRST_PRODUCT_NAME));
        Assert.assertEquals(cartPage.getQuantities(), java.util.List.of("1"));
        Assert.assertEquals(cartPage.getItemPrices(), java.util.List.of(TestData.FIRST_PRODUCT_PRICE));
        Assert.assertTrue(cartPage.isCheckoutDisplayed());
    }

    @Test(description = "Removing the only cart line leaves the cart empty")
    public void testRemoveItemFromCart() {
        productPage.addItem(TestData.BACKPACK_SLUG);
        productPage.openCart();
        cartPage.removeItem(TestData.BACKPACK_SLUG);
        Assert.assertTrue(cartPage.isEmpty());
        Assert.assertEquals(productPage.getCartBadgeCount(), 0);
    }

    @Test(description = "Adding two products sets the badge and cart lines to 2")
    public void testAddMultipleProducts() {
        productPage.addItem(TestData.BACKPACK_SLUG);
        productPage.addItem(TestData.BIKE_LIGHT_SLUG);
        Assert.assertEquals(productPage.getCartBadgeCount(), 2);
        productPage.openCart();
        Assert.assertEquals(cartPage.getItemCount(), 2);
        Assert.assertTrue(cartPage.getItemNames().contains(TestData.FIRST_PRODUCT_NAME));
        Assert.assertTrue(cartPage.getItemNames().contains(TestData.SECOND_PRODUCT_NAME));
        Assert.assertEquals(cartPage.getQuantities(), java.util.List.of("1", "1"));
    }

    @Test(description = "Removing one of two cart lines leaves the other product")
    public void testRemoveOneOfMultipleCartItems() {
        productPage.addItem(TestData.BACKPACK_SLUG);
        productPage.addItem(TestData.BIKE_LIGHT_SLUG);
        productPage.openCart();
        cartPage.removeItem(TestData.BACKPACK_SLUG);
        Assert.assertEquals(cartPage.getItemCount(), 1);
        Assert.assertEquals(cartPage.getItemNames(), java.util.List.of(TestData.SECOND_PRODUCT_NAME));
        Assert.assertEquals(productPage.getCartBadgeCount(), 1);
    }

    @Test(description = "Every catalog product can be added and then removed from the cart")
    public void testAddAllThenRemoveAll() {
        productPage.addAllItemsToCart();
        Assert.assertEquals(productPage.getCartBadgeCount(), TestData.EXPECTED_PRODUCT_COUNT);
        productPage.openCart();
        Assert.assertEquals(cartPage.getItemCount(), TestData.EXPECTED_PRODUCT_COUNT);
        cartPage.removeAllItems();
        Assert.assertTrue(cartPage.isEmpty());
        Assert.assertEquals(productPage.getCartBadgeCount(), 0);
    }

    @Test(description = "The cart still holds an item after returning to the catalog")
    public void testCartPersistsWhenReturningToProducts() {
        productPage.addItem(TestData.BACKPACK_SLUG);
        productPage.openCart();
        cartPage.continueShopping();
        Assert.assertEquals(homepage.getPageTitle(), TestData.EXPECTED_PRODUCTS_TITLE);
        Assert.assertEquals(productPage.getCartBadgeCount(), 1);
        productPage.openCart();
        Assert.assertEquals(cartPage.getItemNames(), java.util.List.of(TestData.FIRST_PRODUCT_NAME));
    }

    @Test(description = "Changing the catalog sort does not drop items already in the cart")
    public void testCartPersistsAfterSorting() {
        productPage.addItem(TestData.BACKPACK_SLUG);
        homepage.sortByValue(TestData.SORT_Z_TO_A);
        Assert.assertEquals(productPage.getCartBadgeCount(), 1);
        productPage.openCart();
        Assert.assertEquals(cartPage.getItemNames(), java.util.List.of(TestData.FIRST_PRODUCT_NAME));
    }

    @Test(description = "Reset App State clears the cart")
    public void testResetAppStateClearsCart() {
        productPage.addItem(TestData.BACKPACK_SLUG);
        Assert.assertEquals(productPage.getCartBadgeCount(), 1);
        homepage.resetAppState();
        Assert.assertEquals(productPage.getCartBadgeCount(), 0);
        productPage.openCart();
        Assert.assertTrue(cartPage.isEmpty());
    }

    @Test(description = "Continue Shopping from the cart returns to the catalog")
    public void testContinueShopping() {
        productPage.openCart();
        cartPage.continueShopping();
        Assert.assertEquals(homepage.getPageTitle(), TestData.EXPECTED_PRODUCTS_TITLE);
        Assert.assertTrue(loginPage.isOnInventoryPage());
    }

    @Test(description = "The cart is still populated after logout and a new login")
    public void testCartPersistsAfterLogoutAndLogin() {
        productPage.addItem(TestData.BACKPACK_SLUG);
        Assert.assertEquals(productPage.getCartBadgeCount(), 1);
        loginPage.logout();
        loginPage.login(TestData.VALID_USERNAME, TestData.VALID_PASSWORD);
        productPage.waitForCartBadgeCount(1);
        Assert.assertEquals(productPage.getCartBadgeCount(), 1);
    }
}
