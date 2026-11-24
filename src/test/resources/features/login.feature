Feature: Login

  Background:
    Given I am on the SauceDemo login page

  Scenario: Login form shows username, password, and the login button
    Then the login form is displayed
    And the page title is "Swag Labs"

  Scenario: Password characters are masked
    Then the password field is masked

  Scenario: Valid credentials land on the inventory page
    When I log in with valid credentials
    Then the page title is "Swag Labs"
    And I am on the inventory page

  Scenario: Refreshing the inventory page keeps the logged-in session
    Given I am logged in
    When I refresh the page
    Then I am on the inventory page

  Scenario: Logout returns the user to the login form
    Given I am logged in
    When I log out
    Then the login form is displayed
    And I am not on the inventory page

  Scenario: Inventory cannot be opened without a session
    When I open the inventory page directly
    Then the login form is displayed
    And I am not on the inventory page

  Scenario Outline: Invalid login attempts show the matching error
    When I log in as "<username>" with password "<password>"
    Then I see the login error "<error>"
    And I am not on the inventory page

    Examples:
      | username        | password       | error                                                                     |
      | Admin@123       | Wigzo@123      | Epic sadface: Username and password do not match any user in this service |
      | standard_user   | wrong_password | Epic sadface: Username and password do not match any user in this service |
      | Admin@123       | secret_sauce   | Epic sadface: Username and password do not match any user in this service |
      | locked_out_user | secret_sauce   | Epic sadface: Sorry, this user has been locked out.                       |
      | standard_user   |                | Epic sadface: Password is required                                        |
      |                 | secret_sauce   | Epic sadface: Username is required                                        |
      |                 |                | Epic sadface: Username is required                                       |

  Scenario: Whitespace around the username is not trimmed into a valid login
    When I log in as " standard_user " with password "secret_sauce"
    Then I see the login error "Epic sadface: Username and password do not match any user in this service"
    And I am not on the inventory page

  Scenario: Closing the login error hides it and stays on login
    When I log in as "Admin@123" with password "Wigzo@123"
    Then I see the login error "Epic sadface: Username and password do not match any user in this service"
    When I dismiss the login error
    Then the login error is not displayed
    And the login form is displayed
