package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutPage {
    WebDriver driver;

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
    private By backtohome = By.id("back-to-products");

    // Actions
    public void enterShippingDetails(String firstName, String lastName, String zip) {
        driver.findElement(firstNameField).sendKeys(firstName);
        driver.findElement(lastNameField).sendKeys(lastName);
        driver.findElement(zipCodeField).sendKeys(zip);
        driver.findElement(continueButton).click();
    }

    public void completeOrder() {
        driver.findElement(finishButton).click();
    }

    public boolean isOrderSuccessful() {
        return driver.findElement(successMessage).getText().equals("Thank you for your order!");
    }

    public void clickBacktohome() {
        driver.findElement(backtohome).click();
    }
}

