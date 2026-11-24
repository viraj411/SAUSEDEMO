package utils;

import java.util.List;

public final class TestData {

    private TestData() {
    }

    public static final String BASE_URL = "https://www.saucedemo.com/";

    public static final String INVENTORY_URL = BASE_URL + "inventory.html";

    public static final String VALID_USERNAME = "standard_user";
    public static final String VALID_PASSWORD = "secret_sauce";
    public static final String INVALID_USERNAME = "Admin@123";
    public static final String INVALID_PASSWORD = "Wigzo@123";
    public static final String LOCKED_OUT_USERNAME = "locked_out_user";
    public static final String WRONG_PASSWORD = "wrong_password";

    public static final String EXPECTED_HOME_TITLE = "Swag Labs";
    public static final String EXPECTED_PRODUCTS_TITLE = "Products";
    public static final String CART_TITLE = "Your Cart";
    public static final String CHECKOUT_INFO_TITLE = "Checkout: Your Information";
    public static final String CHECKOUT_OVERVIEW_TITLE = "Checkout: Overview";
    public static final String CHECKOUT_COMPLETE_TITLE = "Checkout: Complete!";

    public static final String FIRST_PRODUCT_NAME = "Sauce Labs Backpack";
    public static final String SECOND_PRODUCT_NAME = "Sauce Labs Bike Light";
    public static final String FIRST_PRODUCT_DESCRIPTION =
            "carry.allTheThings() with the sleek, streamlined Sly Pack that melds uncompromising style with unequaled laptop and tablet protection.";
    public static final String FIRST_PRODUCT_PRICE = "$29.99";
    public static final String BACKPACK_SLUG = "sauce-labs-backpack";
    public static final String BIKE_LIGHT_SLUG = "sauce-labs-bike-light";
    public static final int EXPECTED_PRODUCT_COUNT = 6;

    public static final String SORT_A_TO_Z = "az";
    public static final String SORT_Z_TO_A = "za";
    public static final String SORT_PRICE_LOW_TO_HIGH = "lohi";
    public static final String SORT_PRICE_HIGH_TO_LOW = "hilo";
    public static final String SORT_A_TO_Z_LABEL = "Name (A to Z)";
    public static final String SORT_Z_TO_A_LABEL = "Name (Z to A)";
    public static final String SORT_PRICE_LOW_TO_HIGH_LABEL = "Price (low to high)";
    public static final String SORT_PRICE_HIGH_TO_LOW_LABEL = "Price (high to low)";
    public static final String SORT_A_TO_Z_FIRST_PRODUCT = "Sauce Labs Backpack";
    public static final String SORT_Z_TO_A_FIRST_PRODUCT = "Test.allTheThings() T-Shirt (Red)";
    public static final String SORT_PRICE_LOW_FIRST_PRODUCT = "Sauce Labs Onesie";
    public static final String SORT_PRICE_HIGH_FIRST_PRODUCT = "Sauce Labs Fleece Jacket";

    public static final String ADD_TO_CART_LABEL = "Add to cart";
    public static final String REMOVE_LABEL = "Remove";
    public static final String PAYMENT_INFORMATION = "SauceCard #31337";
    public static final String SHIPPING_INFORMATION = "Free Pony Express Delivery!";
    public static final String ORDER_DISPATCH_MESSAGE =
            "Your order has been dispatched, and will arrive just as fast as the pony can get there!";

    public static final String SHIPPING_FIRST_NAME = "Viraj";
    public static final String SHIPPING_LAST_NAME = "Abhang";
    public static final String SHIPPING_ZIP_CODE = "422605";

    public static final String INVALID_CREDENTIALS_MESSAGE =
            "Epic sadface: Username and password do not match any user in this service";
    public static final String LOCKED_OUT_MESSAGE = "Epic sadface: Sorry, this user has been locked out.";
    public static final String USERNAME_REQUIRED_MESSAGE = "Epic sadface: Username is required";
    public static final String PASSWORD_REQUIRED_MESSAGE = "Epic sadface: Password is required";
    public static final String CHECKOUT_FIRST_NAME_REQUIRED = "Error: First Name is required";
    public static final String CHECKOUT_LAST_NAME_REQUIRED = "Error: Last Name is required";
    public static final String CHECKOUT_POSTAL_CODE_REQUIRED = "Error: Postal Code is required";
    public static final String ORDER_SUCCESS_MESSAGE = "Thank you for your order!";

    public static final List<String> EXPECTED_MENU_ITEMS = List.of(
            "All Items",
            "Dynamic Catalog",
            "About",
            "Logout",
            "Reset App State");
    public static final String FOOTER_TEXT_PATTERN =
            "© \\d{4} Sauce Labs\\. All Rights Reserved\\. Terms of Service \\| Privacy Policy";
}
