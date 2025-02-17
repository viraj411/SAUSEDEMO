package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.text.DecimalFormat;
import java.util.List;

public class CheckoutPage {
    WebDriver driver;
    private static final DecimalFormat df = new DecimalFormat("0.00"); // Formatting for price comparisons

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
    }

    // Locators
    private By firstNameField = By.id("first-name");
    private By lastNameField = By.id("last-name");
    private By zipCodeField = By.id("postal-code");
    private By continueButton = By.id("continue");
    private By finishButton = By.id("finish");
    private By successMessage = By.className("complete-header");
    private By backToHome = By.id("back-to-products");
    private By totalCartPrice = By.className("inventory_item_price");
    private By totalCartSummary = By.className("summary_subtotal_label");
    private By totalTax = By.className("summary_tax_label");
    private By totalLabel = By.className("summary_total_label");
    private By errormessage = By.xpath("//*[@id=\"checkout_info_container\"]/div/form/div[1]/div[4]/h3");

    // Helper function to parse prices
    private double parsePrice(String priceText) {
        try {
            return Double.parseDouble(priceText.replaceAll("[^0-9.]", ""));
        } catch (NumberFormatException e) {
            System.err.println("Error: Could not parse price - '" + priceText + "'");
            return 0.0;
        }
    }

    public void verifyCheckoutFields(String firstName, String lastName, String zip) {
        driver.findElement(firstNameField).sendKeys(firstName);
        driver.findElement(lastNameField).sendKeys(lastName);
        driver.findElement(zipCodeField).sendKeys(zip);
        driver.findElement(continueButton).click();
        String Errormessage = driver.findElement(errormessage).getText();
        if (Errormessage.equals("Error: First Name is required")) {
            System.out.println("Every CheckoutField has validation ");
        }


    }


    public void enterShippingDetails(String firstName, String lastName, String zip) {
        driver.findElement(firstNameField).sendKeys(firstName);
        driver.findElement(lastNameField).sendKeys(lastName);
        driver.findElement(zipCodeField).sendKeys(zip);
        driver.findElement(continueButton).click();
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

        System.out.println("Calculated Total Price: $ " + df.format(totalPrice));

        double total = parsePrice(driver.findElement(totalCartSummary).getText());
        double tax = parsePrice(driver.findElement(totalTax).getText());
        double displayedTotal = parsePrice(driver.findElement(totalLabel).getText());

        double calculatedTotal = total + tax;
        System.out.println("Expected Total: $" + df.format(calculatedTotal));
        System.out.println("Displayed Total: $" + df.format(displayedTotal));

        // Allow minor floating-point discrepancies
        return Math.abs(calculatedTotal - displayedTotal) < 0.01;
    }

    public void completeOrder() {
        driver.findElement(finishButton).click();
    }

    public boolean isOrderSuccessful() {
        return driver.findElement(successMessage).getText().equalsIgnoreCase("Thank you for your order!");
    }

    public void clickBackToHome() {
        driver.findElement(backToHome).click();
    }
}
