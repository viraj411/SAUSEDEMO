package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.TestData;

import java.text.DecimalFormat;
import java.time.Duration;
import java.util.List;

public class CheckoutPage {

    private static final DecimalFormat PRICE_FORMAT = new DecimalFormat("0.00");

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By firstNameField = By.id("first-name");
    private final By lastNameField = By.id("last-name");
    private final By zipCodeField = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By finishButton = By.id("finish");
    private final By successMessage = By.className("complete-header");
    private final By backToHome = By.id("back-to-products");
    private final By totalCartPrice = By.className("inventory_item_price");
    private final By totalCartSummary = By.className("summary_subtotal_label");
    private final By totalTax = By.className("summary_tax_label");
    private final By totalLabel = By.className("summary_total_label");
    private final By errorMessage = By.cssSelector("h3[data-test='error']");

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    private double parsePrice(String priceText) {
        try {
            return Double.parseDouble(priceText.replaceAll("[^0-9.]", ""));
        } catch (NumberFormatException e) {
            System.err.println("Error: Could not parse price - '" + priceText + "'");
            return 0.0;
        }
    }

    public void verifyCheckoutFields(String firstName, String lastName, String zip) {
        enterShippingDetails(firstName, lastName, zip);
        String validationMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).getText();
        if (validationMessage.equals(TestData.CHECKOUT_FIRST_NAME_REQUIRED)) {
            System.out.println("Checkout fields validation passed");
        }
    }

    public void enterShippingDetails(String firstName, String lastName, String zip) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField)).sendKeys(firstName);
        wait.until(ExpectedConditions.visibilityOfElementLocated(lastNameField)).sendKeys(lastName);
        wait.until(ExpectedConditions.visibilityOfElementLocated(zipCodeField)).sendKeys(zip);
        wait.until(ExpectedConditions.elementToBeClickable(continueButton)).click();
    }

    public boolean comparePrice() {
        List<WebElement> items = driver.findElements(totalCartPrice);
        double totalPrice = 0.0;

        System.out.println("Total items found: " + items.size());

        for (WebElement item : items) {
            String priceText = item.getText().trim();
            System.out.println("Raw price text: '" + priceText + "'");
            totalPrice += parsePrice(priceText);
        }

        System.out.println("Calculated Total Price: $ " + PRICE_FORMAT.format(totalPrice));

        double subtotal = parsePrice(wait.until(ExpectedConditions.visibilityOfElementLocated(totalCartSummary)).getText());
        double tax = parsePrice(wait.until(ExpectedConditions.visibilityOfElementLocated(totalTax)).getText());
        double displayedTotal = parsePrice(wait.until(ExpectedConditions.visibilityOfElementLocated(totalLabel)).getText());

        double calculatedTotal = subtotal + tax;
        System.out.println("Expected Total: $" + PRICE_FORMAT.format(calculatedTotal));
        System.out.println("Displayed Total: $" + PRICE_FORMAT.format(displayedTotal));

        return Math.abs(calculatedTotal - displayedTotal) < 0.01;
    }

    public void completeOrder() {
        wait.until(ExpectedConditions.elementToBeClickable(finishButton)).click();
    }

    public boolean isOrderSuccessful() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage))
                .getText().equalsIgnoreCase(TestData.ORDER_SUCCESS_MESSAGE);
    }

    public void clickBackToHome() {
        wait.until(ExpectedConditions.elementToBeClickable(backToHome)).click();
    }
}
