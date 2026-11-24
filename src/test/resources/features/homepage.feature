Feature: Product catalog

  Background:
    Given I am logged in

  Scenario: Inventory heading is Products
    Then the page heading is "Products"

  Scenario: The catalog lists every standard product
    Then the catalog shows 6 products

  Scenario: Each catalog card shows a name and a dollar price
    Then each product shows a name and a dollar price

  Scenario: The cart icon is visible and empty before anything is added
    Then the cart icon is displayed
    And the cart badge shows 0

  Scenario: The menu lists All Items, About, Logout, and Reset App State
    Then the menu items are "All Items, Dynamic Catalog, About, Logout, Reset App State"
    And the menu items are enabled
    When I close the menu
    Then the menu is closed

  Scenario: All Items keeps the shopper on the catalog
    When I open the menu
    And I click All Items
    And I close the menu
    Then the page heading is "Products"
    And I am on the inventory page

  Scenario: Name A to Z sorts the catalog ascending
    When I sort the catalog by "az"
    Then the sort option shown is "Name (A to Z)"
    And the first product is "Sauce Labs Backpack"
    And the products are sorted by name ascending

  Scenario: Name Z to A sorts the catalog descending
    When I sort the catalog by "za"
    Then the sort option shown is "Name (Z to A)"
    And the first product is "Test.allTheThings() T-Shirt (Red)"
    And the products are sorted by name descending

  Scenario: Price low to high sorts the catalog by ascending price
    When I sort the catalog by "lohi"
    Then the sort option shown is "Price (low to high)"
    And the first product is "Sauce Labs Onesie"
    And the products are sorted by price ascending

  Scenario: Price high to low sorts the catalog by descending price
    When I sort the catalog by "hilo"
    Then the sort option shown is "Price (high to low)"
    And the first product is "Sauce Labs Fleece Jacket"
    And the products are sorted by price descending

  Scenario: The footer shows the Sauce Labs copyright line
    Then the footer shows the Sauce Labs copyright line

  Scenario: Social links point at Twitter, Facebook, and LinkedIn
    Then the social links point to Twitter, Facebook and LinkedIn
