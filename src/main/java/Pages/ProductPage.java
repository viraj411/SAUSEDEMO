package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ProductPage {

    WebDriver driver;
    WebDriverWait wait;
    private double totalPrice = 0.0; // Declare totalPrice as an instance variable


    public ProductPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Locators
    private By Productpagetitle = By.className("title");
    private By addToCartBtn = By.id("add-to-cart-sauce-labs-backpack");
    private By cartIcon = By.id("shopping_cart_container");
    private By addToCartButton = By.xpath(".//button[starts-with(@id, 'add-to-cart')]"); // Dynamic 'Add to Cart' button using partial ID match
    private By removecartitems = By.xpath(".//button[starts-with(@id, 'remove')]");
    private By inventoryitems = By.className("inventory_item");
    private By itemName = By.className("inventory_item_label");
    private By itemPrice = By.className("inventory_item_price");
    private By cartQuantity = By.className("cart_item");
    private By continueShopping = By.id("continue-shopping");
    private By Backtoproductbutton = By.id("back-to-products");
    private By Firstproductname = By.xpath("//div[normalize-space()='Sauce Labs Backpack']");
    private By Firstproductdisc = By.xpath("//div[normalize-space()='carry.allTheThings() with the sleek, streamlined Sly Pack that melds uncompromising style with unequaled laptop and tablet protection.']");

    // Actions
    public void singleaddToCart() {
        driver.findElement(addToCartBtn).click();
    }

    public void openCart() {
        driver.findElement(cartIcon).click();
    }

    public void addAllItemsToCart() {
        List<WebElement> items = driver.findElements(inventoryitems);
        System.out.println("Total items found: " + items.size());

        double pricevalue = 0;


        for (WebElement item : items) {
            String name = item.findElement(itemName).getText();
            String newname = name.substring(0, 20);

            String price = item.findElement(itemPrice).getText();
            WebElement addToCart = item.findElement(addToCartButton);


            wait.until(ExpectedConditions.elementToBeClickable(addToCart));
            addToCart.click();

            System.out.println("Added to Cart: " + newname + " - " + price);

            try {
                double priceValue = Double.parseDouble(price.replaceAll("[^0-9.]", ""));
                totalPrice += priceValue; // Accumulate total price
            } catch (NumberFormatException e) {
                System.out.println("Error parsing price: " + price);
            }
        }
        System.out.println("Total price of the products is " + totalPrice);

    }

    public void removeAllItemsFromCart() {
        List<WebElement> items = driver.findElements(cartQuantity);
        System.out.println("Total items found in the cart: " + items.size());
        for (WebElement item : items) {
            WebElement remove = item.findElement(removecartitems);
            wait.until(ExpectedConditions.elementToBeClickable(remove));
            remove.click();

        }

    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void checkproductdetails() {
        wait.until(ExpectedConditions.elementToBeClickable(Firstproductname)).click();
        String firstproductnametext = wait.until(ExpectedConditions.elementToBeClickable(Firstproductname)).getText();
        String firstproductdisctext = wait.until(ExpectedConditions.elementToBeClickable(Firstproductdisc)).getText();

        System.out.println(firstproductnametext);
        System.out.println(firstproductdisctext);

        if ((firstproductnametext.equals("Sauce Labs Backpack")) && firstproductdisctext.equals("carry.allTheThings() with the sleek, streamlined Sly Pack that melds uncompromising style with unequaled laptop and tablet protection.")) {
            wait.until(ExpectedConditions.elementToBeClickable(Backtoproductbutton)).click();
        } else {
            System.out.println("Product details does not match");
        }


    }

    public boolean verifyproductpagetitle() {
        String productpagetitletext = wait.until(ExpectedConditions.visibilityOfElementLocated(Productpagetitle)).getText();
        return (productpagetitletext.equals("Products"));
    }

    public boolean verifyCartItemCountAfterAddingProduct() {
        driver.findElement(addToCartBtn).click();
        String cartItemText = driver.findElement(cartIcon).getText().trim();
        int cartItemCount = Integer.parseInt(cartItemText);
        System.out.println("Cart item quantity is: " + cartItemCount);
        if (cartItemCount == 1) {
            driver.findElement(removecartitems).click();
        }
        return (cartItemCount==1);
    }

}






