package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
    WebDriver driver;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    private By usernameField = By.id("user-name");
    private By passwordField = By.id("password");
    private By loginButton = By.id("login-button");
    private By errorMessage = By.xpath("//*[@id='login_button_container']/div/form/div[3]/h3");

    public void enterUsername(String username) {
        driver.findElement(usernameField).sendKeys(username);
    }

    public void enterPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }

    public void clickLogin() {
        driver.findElement(loginButton).click();
    }

    public boolean invalidlogin() {
        driver.findElement(usernameField).sendKeys("viraj ");
        driver.findElement(passwordField).sendKeys("12345");
        driver.findElement(loginButton).click();

        String errorMessageText = driver.findElement(errorMessage).getText();
        System.out.println(errorMessageText);

        return (errorMessageText.equals("Epic sadface: Username and password do not match any user in this service"));

    }


}
