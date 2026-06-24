package utils;

public final class TestData {

    private TestData() {
    }

    public static final String BASE_URL = "https://www.saucedemo.com/";

    public static final String VALID_USERNAME = "standard_user";
    public static final String VALID_PASSWORD = "secret_sauce";
    public static final String INVALID_USERNAME = "Admin@123";
    public static final String INVALID_PASSWORD = "Wigzo@123";

    public static final String EXPECTED_HOME_TITLE = "Swag Labs";
    public static final String EXPECTED_PRODUCTS_TITLE = "Products";

    public static final String FIRST_PRODUCT_NAME = "Sauce Labs Backpack";
    public static final String FIRST_PRODUCT_DESCRIPTION =
            "carry.allTheThings() with the sleek, streamlined Sly Pack that melds uncompromising style with unequaled laptop and tablet protection.";

    public static final String SHIPPING_FIRST_NAME = "Viraj";
    public static final String SHIPPING_LAST_NAME = "Abhang";
    public static final String SHIPPING_ZIP_CODE = "422605";

    public static final String INVALID_CREDENTIALS_MESSAGE =
            "Epic sadface: Username and password do not match any user in this service";
    public static final String CHECKOUT_FIRST_NAME_REQUIRED = "Error: First Name is required";
    public static final String ORDER_SUCCESS_MESSAGE = "Thank you for your order!";
    public static final String FOOTER_TEXT_PATTERN =
            "© \\d{4} Sauce Labs\\. All Rights Reserved\\. Terms of Service \\| Privacy Policy";
}
