Feature: End to end shopping journey

  Scenario: A standard user can sign in
    Given I am on the SauceDemo login page
    When I log in with valid credentials
    Then the page title is "Swag Labs"
    And I am on the inventory page

  Scenario: The catalog menu, sort order, social links, and footer are available
    Given I am logged in
    Then the menu items are "All Items, Dynamic Catalog, About, Logout, Reset App State"
    And the menu items are enabled
    When I close the menu
    And I sort the catalog by "az"
    Then the sort option shown is "Name (A to Z)"
    And the products are sorted by name ascending
    When I sort the catalog by "za"
    Then the sort option shown is "Name (Z to A)"
    And the products are sorted by name descending
    When I sort the catalog by "lohi"
    Then the sort option shown is "Price (low to high)"
    And the products are sorted by price ascending
    When I sort the catalog by "hilo"
    Then the sort option shown is "Price (high to low)"
    And the products are sorted by price descending
    Then the social links point to Twitter, Facebook and LinkedIn
    And the footer shows the Sauce Labs copyright line

  Scenario: A product can be reviewed and added to the cart
    Given I am logged in
    When I open the product "Sauce Labs Backpack"
    Then the product details name is "Sauce Labs Backpack"
    And the product details description is "carry.allTheThings() with the sleek, streamlined Sly Pack that melds uncompromising style with unequaled laptop and tablet protection."
    When I go back to products
    And I add the backpack to the cart
    And I open the cart
    Then the cart lists only "Sauce Labs Backpack"
    And the cart item quantities are "1"

  Scenario: Checkout rejects an empty form, then completes a valid order
    Given I am logged in
    When I add the backpack to the cart
    And I open the cart
    And I start checkout
    When I enter shipping details "", "" and ""
    Then the checkout error is "Error: First Name is required"
    When I enter valid shipping details
    Then the subtotal matches the item prices
    And the total is consistent
    When I finish the order
    Then the success message is "Thank you for your order!"
    When I click back home
    Then the page heading is "Products"

  Scenario: Every catalog product can be added and then removed
    Given I am logged in
    When I add every product to the cart
    Then the cart badge shows 6
    When I open the cart
    Then the cart has 6 items
    When I remove all items from the cart
    Then the cart is empty
    And the cart badge shows 0
