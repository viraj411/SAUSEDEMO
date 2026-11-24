Feature: Cart and product details

  Background:
    Given I am logged in

  Scenario: Opening a product shows its name, description, and price
    When I open the product "Sauce Labs Backpack"
    Then the product details name is "Sauce Labs Backpack"
    And the product details description is "carry.allTheThings() with the sleek, streamlined Sly Pack that melds uncompromising style with unequaled laptop and tablet protection."
    And the product details price is "$29.99"

  Scenario: Back to products returns to the catalog
    When I open the product "Sauce Labs Backpack"
    And I go back to products
    Then the page heading is "Products"
    And the catalog shows 6 products

  Scenario: Adding a product from its details page updates the cart badge
    When I open the product "Sauce Labs Backpack"
    And I add to cart from the product details
    Then the cart badge shows 1
    And the details button shows "Remove"
    When I go back to products
    Then the cart badge shows 1

  Scenario: Add to cart switches the button to Remove and shows a badge of 1
    When I add the backpack to the cart
    Then the cart badge shows 1
    And the backpack button shows "Remove"

  Scenario: The same product cannot be added a second time while it is in the cart
    When I add the backpack to the cart
    Then the cart badge shows 1
    And the backpack button shows "Remove"

  Scenario: Removing a product from the catalog clears the badge
    When I add the backpack to the cart
    And I remove the backpack from the catalog
    Then the cart badge shows 0
    And the backpack button shows "Add to cart"

  Scenario: The cart lists the added product with quantity 1 and its price
    When I add the backpack to the cart
    And I open the cart
    Then the page heading is "Your Cart"
    And the cart has 1 item
    And the cart lists only "Sauce Labs Backpack"
    And the cart item quantities are "1"
    And the cart prices are "$29.99"
    And the checkout button is displayed

  Scenario: Removing the only cart line leaves the cart empty
    When I add the backpack to the cart
    And I open the cart
    And I remove the backpack from the cart
    Then the cart is empty
    And the cart badge shows 0

  Scenario: Adding two products sets the badge and cart lines to 2
    When I add the backpack to the cart
    And I add the bike light to the cart
    Then the cart badge shows 2
    When I open the cart
    Then the cart has 2 items
    And the cart includes "Sauce Labs Backpack"
    And the cart includes "Sauce Labs Bike Light"
    And the cart item quantities are "1, 1"

  Scenario: Removing one of two cart lines leaves the other product
    When I add the backpack to the cart
    And I add the bike light to the cart
    And I open the cart
    And I remove the backpack from the cart
    Then the cart has 1 item
    And the cart lists only "Sauce Labs Bike Light"
    And the cart badge shows 1

  Scenario: Every catalog product can be added and then removed from the cart
    When I add every product to the cart
    Then the cart badge shows 6
    When I open the cart
    Then the cart has 6 items
    When I remove all items from the cart
    Then the cart is empty
    And the cart badge shows 0

  Scenario: The cart still holds an item after returning to the catalog
    When I add the backpack to the cart
    And I open the cart
    And I continue shopping
    Then the page heading is "Products"
    And the cart badge shows 1
    When I open the cart
    Then the cart lists only "Sauce Labs Backpack"

  Scenario: Changing the catalog sort does not drop items already in the cart
    When I add the backpack to the cart
    And I sort the catalog by "za"
    Then the cart badge shows 1
    When I open the cart
    Then the cart lists only "Sauce Labs Backpack"

  Scenario: Reset App State clears the cart
    When I add the backpack to the cart
    Then the cart badge shows 1
    When I reset the app state
    Then the cart badge shows 0
    When I open the cart
    Then the cart is empty

  Scenario: Continue Shopping from the cart returns to the catalog
    When I open the cart
    And I continue shopping
    Then the page heading is "Products"
    And I am on the inventory page

  Scenario: The cart is still populated after logout and a new login
    When I add the backpack to the cart
    Then the cart badge shows 1
    When I log out
    And I log in with valid credentials
    Then the cart badge shows 1

  Scenario: An empty cart can still open checkout
    When I open the cart
    Then the cart is empty
    When I start checkout
    Then the page heading is "Checkout: Your Information"
    And the shipping fields are displayed
