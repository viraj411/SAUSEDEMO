package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class CheckoutPage extends BasePage {

    private static final double PRICE_TOLERANCE = 0.01;

    private final By firstNameField = By.id("first-name");
    private final By lastNameField = By.id("last-name");
    private final By zipCodeField = By.id("postal-code");
    private final By continueButton = By.id("continue");
    private final By finishButton = By.id("finish");
    private final By cancelButton = By.id("cancel");
    private final By errorMessage = By.cssSelector("h3[data-test='error']");
    private final By errorCloseButton = By.className("error-button");
    private final By pageTitle = By.className("title");
    private final By itemPrices = By.className("inventory_item_price");
    private final By itemNames = By.className("inventory_item_name");
    private final By subtotalLabel = By.className("summary_subtotal_label");
    private final By taxLabel = By.className("summary_tax_label");
    private final By totalLabel = By.className("summary_total_label");
    private final By summaryValues = By.className("summary_value_label");
    private final By successMessage = By.className("complete-header");
    private final By dispatchMessage = By.className("complete-text");
    private final By backToHome = By.id("back-to-products");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public void enterShippingDetails(String firstName, String lastName, String zip) {
        type(firstNameField, firstName);
        type(lastNameField, lastName);
        type(zipCodeField, zip);
        click(continueButton);
        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(errorMessage),
                ExpectedConditions.urlContains("checkout-step-two")));
    }

    public String getPageTitle() {
        return stableText(pageTitle);
    }

    public boolean areShippingFieldsDisplayed() {
        return isDisplayed(firstNameField)
                && isDisplayed(lastNameField)
                && isDisplayed(zipCodeField)
                && isDisplayed(continueButton);
    }

    public String getErrorText() {
        return textOf(errorMessage);
    }

    public boolean isErrorDisplayed() {
        return isDisplayed(errorMessage);
    }

    public void dismissError() {
        click(errorCloseButton);
    }

    public String getFirstNameValue() {
        return valueOf(firstNameField);
    }

    public String getLastNameValue() {
        return valueOf(lastNameField);
    }

    public String getZipCodeValue() {
        return valueOf(zipCodeField);
    }

    public void clickCancel() {
        click(cancelButton);
    }

    public List<String> getOverviewItemNames() {
        waitForUrlToContain("checkout-step-two");
        wait.until(ExpectedConditions.visibilityOfElementLocated(itemNames));
        return visibleTexts(itemNames);
    }

    public List<String> getSummaryValues() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(summaryValues));
        return visibleTexts(summaryValues);
    }

    public double getItemsTotal() {
        double total = 0.0;
        for (WebElement item : driver.findElements(itemPrices)) {
            total += parsePrice(item.getText());
        }
        return total;
    }

    public double getSubtotal() {
        return parsePrice(textOf(subtotalLabel));
    }

    public double getTax() {
        return parsePrice(textOf(taxLabel));
    }

    public double getDisplayedTotal() {
        return parsePrice(textOf(totalLabel));
    }

    public boolean doesSubtotalMatchItems() {
        return Math.abs(getItemsTotal() - getSubtotal()) < PRICE_TOLERANCE;
    }

    public boolean isTotalConsistent() {
        return Math.abs((getSubtotal() + getTax()) - getDisplayedTotal()) < PRICE_TOLERANCE;
    }

    public boolean isFinishButtonDisplayed() {
        return isDisplayed(finishButton);
    }

    public void completeOrder() {
        click(finishButton);
        waitForUrlToContain("checkout-complete");
    }

    public String getSuccessMessage() {
        return textOf(successMessage);
    }

    public String getDispatchMessage() {
        return textOf(dispatchMessage);
    }

    public void clickBackToHome() {
        click(backToHome);
        waitForUrlToContain("inventory.html");
    }

    private String valueOf(By field) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(field)).getAttribute("value");
    }

    private double parsePrice(String priceText) {
        String numeric = priceText.replaceAll("[^0-9.]", "");
        if (numeric.isEmpty()) {
            throw new IllegalArgumentException("No price found in '" + priceText + "'");
        }
        return Double.parseDouble(numeric);
    }
}
