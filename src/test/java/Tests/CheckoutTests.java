package Tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import utils.TestData;

public class CheckoutTests extends InventoryTest {

    @Test(description = "Checkout from the cart opens the customer information form")
    public void testCheckoutOpensInformationForm() {
        addBackpackAndOpenCart();
        cartPage.clickCheckout();
        Assert.assertEquals(checkoutPage.getPageTitle(), TestData.CHECKOUT_INFO_TITLE);
        Assert.assertTrue(checkoutPage.areShippingFieldsDisplayed());
        Assert.assertFalse(checkoutPage.isFinishButtonDisplayed());
    }

    @Test(description = "An empty cart can still open checkout")
    public void testCheckoutFromEmptyCartOpensInformationForm() {
        productPage.openCart();
        Assert.assertTrue(cartPage.isEmpty());
        cartPage.clickCheckout();
        Assert.assertEquals(checkoutPage.getPageTitle(), TestData.CHECKOUT_INFO_TITLE);
        Assert.assertTrue(checkoutPage.areShippingFieldsDisplayed());
    }

    @Test(description = "Continuing with every field blank asks for the first name")
    public void testCheckoutRequiresFirstName() {
        addBackpackAndStartCheckout();
        checkoutPage.enterShippingDetails("", "", "");
        Assert.assertEquals(checkoutPage.getErrorText(), TestData.CHECKOUT_FIRST_NAME_REQUIRED);
        Assert.assertEquals(checkoutPage.getPageTitle(), TestData.CHECKOUT_INFO_TITLE);
    }

    @Test(description = "A missing last name is reported after the first name is filled")
    public void testCheckoutRequiresLastName() {
        addBackpackAndStartCheckout();
        checkoutPage.enterShippingDetails(TestData.SHIPPING_FIRST_NAME, "", "");
        Assert.assertEquals(checkoutPage.getErrorText(), TestData.CHECKOUT_LAST_NAME_REQUIRED);
        Assert.assertEquals(checkoutPage.getFirstNameValue(), TestData.SHIPPING_FIRST_NAME);
    }

    @Test(description = "A missing postal code is reported after the name fields are filled")
    public void testCheckoutRequiresPostalCode() {
        addBackpackAndStartCheckout();
        checkoutPage.enterShippingDetails(TestData.SHIPPING_FIRST_NAME, TestData.SHIPPING_LAST_NAME, "");
        Assert.assertEquals(checkoutPage.getErrorText(), TestData.CHECKOUT_POSTAL_CODE_REQUIRED);
        Assert.assertEquals(checkoutPage.getFirstNameValue(), TestData.SHIPPING_FIRST_NAME);
        Assert.assertEquals(checkoutPage.getLastNameValue(), TestData.SHIPPING_LAST_NAME);
    }

    @Test(description = "Values already entered stay on the form when a later field is missing")
    public void testEnteredValuesStayWhenValidationFails() {
        addBackpackAndStartCheckout();
        checkoutPage.enterShippingDetails(TestData.SHIPPING_FIRST_NAME, "", TestData.SHIPPING_ZIP_CODE);
        Assert.assertEquals(checkoutPage.getErrorText(), TestData.CHECKOUT_LAST_NAME_REQUIRED);
        Assert.assertEquals(checkoutPage.getFirstNameValue(), TestData.SHIPPING_FIRST_NAME);
        Assert.assertEquals(checkoutPage.getZipCodeValue(), TestData.SHIPPING_ZIP_CODE);
    }

    @Test(description = "Closing a checkout error hides it and stays on the information step")
    public void testDismissCheckoutError() {
        addBackpackAndStartCheckout();
        checkoutPage.enterShippingDetails("", "", "");
        Assert.assertTrue(checkoutPage.isErrorDisplayed());
        checkoutPage.dismissError();
        Assert.assertFalse(checkoutPage.isErrorDisplayed());
        Assert.assertEquals(checkoutPage.getPageTitle(), TestData.CHECKOUT_INFO_TITLE);
    }

    @Test(description = "A postal code made of letters is accepted")
    public void testAlphabeticPostalCodeIsAccepted() {
        addBackpackAndStartCheckout();
        checkoutPage.enterShippingDetails(TestData.SHIPPING_FIRST_NAME, TestData.SHIPPING_LAST_NAME, "ABCDE");
        Assert.assertEquals(checkoutPage.getPageTitle(), TestData.CHECKOUT_OVERVIEW_TITLE);
        Assert.assertFalse(checkoutPage.isErrorDisplayed());
    }

    @Test(description = "Valid customer information opens the overview")
    public void testValidInformationOpensOverview() {
        addBackpackAndStartCheckout();
        checkoutPage.enterShippingDetails(
                TestData.SHIPPING_FIRST_NAME,
                TestData.SHIPPING_LAST_NAME,
                TestData.SHIPPING_ZIP_CODE);
        Assert.assertEquals(checkoutPage.getPageTitle(), TestData.CHECKOUT_OVERVIEW_TITLE);
        Assert.assertTrue(checkoutPage.isFinishButtonDisplayed());
    }

    @Test(description = "Overview lists the cart item plus payment and shipping values")
    public void testOverviewShowsItemAndDeliveryDetails() {
        addBackpackAndStartCheckout();
        submitValidInformation();
        Assert.assertEquals(checkoutPage.getOverviewItemNames(), java.util.List.of(TestData.FIRST_PRODUCT_NAME));
        Assert.assertEquals(
                checkoutPage.getSummaryValues(),
                java.util.List.of(TestData.PAYMENT_INFORMATION, TestData.SHIPPING_INFORMATION));
    }

    @Test(description = "Overview subtotal matches the prices of the cart items")
    public void testSubtotalMatchesItemPrices() {
        productPage.addItem(TestData.BACKPACK_SLUG);
        productPage.addItem(TestData.BIKE_LIGHT_SLUG);
        productPage.openCart();
        cartPage.clickCheckout();
        submitValidInformation();
        Assert.assertTrue(checkoutPage.doesSubtotalMatchItems());
    }

    @Test(description = "Overview total equals subtotal plus tax")
    public void testTotalEqualsSubtotalPlusTax() {
        addBackpackAndStartCheckout();
        submitValidInformation();
        Assert.assertTrue(checkoutPage.getTax() > 0);
        Assert.assertTrue(checkoutPage.isTotalConsistent());
    }

    @Test(description = "An empty cart overview totals to zero before tax")
    public void testEmptyCartOverviewSubtotalIsZero() {
        productPage.openCart();
        cartPage.clickCheckout();
        submitValidInformation();
        Assert.assertEquals(checkoutPage.getSubtotal(), 0.0);
        Assert.assertTrue(checkoutPage.isTotalConsistent());
    }

    @Test(description = "Cancel on the information step returns to the cart and keeps the item")
    public void testCancelFromInformationReturnsToCart() {
        addBackpackAndStartCheckout();
        checkoutPage.clickCancel();
        Assert.assertEquals(cartPage.getPageTitle(), TestData.CART_TITLE);
        Assert.assertEquals(cartPage.getItemNames(), java.util.List.of(TestData.FIRST_PRODUCT_NAME));
    }

    @Test(description = "Cancel on the overview returns to the catalog and keeps the cart")
    public void testCancelFromOverviewKeepsCart() {
        addBackpackAndStartCheckout();
        submitValidInformation();
        checkoutPage.clickCancel();
        Assert.assertEquals(homepage.getPageTitle(), TestData.EXPECTED_PRODUCTS_TITLE);
        Assert.assertEquals(productPage.getCartBadgeCount(), 1);
    }

    @Test(description = "Finish shows the thank-you header")
    public void testFinishShowsThankYouHeader() {
        addBackpackAndStartCheckout();
        submitValidInformation();
        checkoutPage.completeOrder();
        Assert.assertEquals(checkoutPage.getPageTitle(), TestData.CHECKOUT_COMPLETE_TITLE);
        Assert.assertEquals(checkoutPage.getSuccessMessage(), TestData.ORDER_SUCCESS_MESSAGE);
    }

    @Test(description = "The complete page shows the dispatch message")
    public void testCompletePageShowsDispatchMessage() {
        addBackpackAndStartCheckout();
        submitValidInformation();
        checkoutPage.completeOrder();
        Assert.assertEquals(checkoutPage.getDispatchMessage(), TestData.ORDER_DISPATCH_MESSAGE);
    }

    @Test(description = "Back Home from the complete page returns to the catalog")
    public void testBackHomeReturnsToProducts() {
        addBackpackAndStartCheckout();
        submitValidInformation();
        checkoutPage.completeOrder();
        checkoutPage.clickBackToHome();
        Assert.assertEquals(homepage.getPageTitle(), TestData.EXPECTED_PRODUCTS_TITLE);
        Assert.assertTrue(loginPage.isOnInventoryPage());
    }

    @Test(description = "A completed order leaves the cart empty")
    public void testCartIsEmptyAfterCompletedOrder() {
        addBackpackAndStartCheckout();
        submitValidInformation();
        checkoutPage.completeOrder();
        checkoutPage.clickBackToHome();
        Assert.assertEquals(productPage.getCartBadgeCount(), 0);
        productPage.openCart();
        Assert.assertTrue(cartPage.isEmpty());
    }

    private void addBackpackAndOpenCart() {
        productPage.addItem(TestData.BACKPACK_SLUG);
        productPage.openCart();
    }

    private void addBackpackAndStartCheckout() {
        addBackpackAndOpenCart();
        cartPage.clickCheckout();
    }

    private void submitValidInformation() {
        checkoutPage.enterShippingDetails(
                TestData.SHIPPING_FIRST_NAME,
                TestData.SHIPPING_LAST_NAME,
                TestData.SHIPPING_ZIP_CODE);
    }
}
