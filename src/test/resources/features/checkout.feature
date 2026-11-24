Feature: Checkout

  Background:
    Given I am logged in

  Scenario: Checkout opens the customer information form
    Given I am at the checkout information with the backpack in my cart
    Then the page heading is "Checkout: Your Information"
    And the shipping fields are displayed
    And the finish button is not displayed

  Scenario: Continuing with every field blank asks for the first name
    Given I am at the checkout information with the backpack in my cart
    When I enter shipping details "", "" and ""
    Then the checkout error is "Error: First Name is required"
    And the page heading is "Checkout: Your Information"

  Scenario: A missing last name is reported after the first name is filled
    Given I am at the checkout information with the backpack in my cart
    When I enter shipping details "Viraj", "" and ""
    Then the checkout error is "Error: Last Name is required"
    And the first name is "Viraj"

  Scenario: A missing postal code is reported after the name fields are filled
    Given I am at the checkout information with the backpack in my cart
    When I enter shipping details "Viraj", "Abhang" and ""
    Then the checkout error is "Error: Postal Code is required"
    And the first name is "Viraj"
    And the last name is "Abhang"

  Scenario: Values already entered stay on the form when a later field is missing
    Given I am at the checkout information with the backpack in my cart
    When I enter shipping details "Viraj", "" and "422605"
    Then the checkout error is "Error: Last Name is required"
    And the first name is "Viraj"
    And the zip code is "422605"

  Scenario: Closing a checkout error hides it and stays on the information step
    Given I am at the checkout information with the backpack in my cart
    When I enter shipping details "", "" and ""
    Then the checkout error is "Error: First Name is required"
    When I dismiss the checkout error
    Then the checkout error is not displayed
    And the page heading is "Checkout: Your Information"

  Scenario: A postal code made of letters is accepted
    Given I am at the checkout information with the backpack in my cart
    When I enter shipping details "Viraj", "Abhang" and "ABCDE"
    Then the page heading is "Checkout: Overview"
    And the checkout error is not displayed

  Scenario: Valid customer information opens the overview
    Given I am at the checkout information with the backpack in my cart
    When I enter valid shipping details
    Then the page heading is "Checkout: Overview"
    And the finish button is displayed

  Scenario: Overview lists the cart item plus payment and shipping values
    Given I am at the checkout information with the backpack in my cart
    When I enter valid shipping details
    Then the overview shows the item "Sauce Labs Backpack"
    And the summary shows "SauceCard #31337" and "Free Pony Express Delivery!"

  Scenario: Overview subtotal matches the prices of the cart items
    Given the backpack and the bike light are in my cart
    When I open the cart
    And I start checkout
    And I enter valid shipping details
    Then the subtotal matches the item prices

  Scenario: Overview total equals subtotal plus tax
    Given I am at the checkout information with the backpack in my cart
    When I enter valid shipping details
    Then the tax is greater than zero
    And the total is consistent

  Scenario: An empty cart overview totals to zero before tax
    When I open the cart
    And I start checkout
    And I enter valid shipping details
    Then the subtotal is 0.0
    And the total is consistent

  Scenario: Cancel on the information step returns to the cart and keeps the item
    Given I am at the checkout information with the backpack in my cart
    When I cancel checkout
    Then the page heading is "Your Cart"
    And the cart lists only "Sauce Labs Backpack"

  Scenario: Cancel on the overview returns to the catalog and keeps the cart
    Given I am at the checkout overview with the backpack in my cart
    When I cancel checkout
    Then the page heading is "Products"
    And the cart badge shows 1

  Scenario: Finish shows the thank-you header
    Given I am at the checkout overview with the backpack in my cart
    When I finish the order
    Then the page heading is "Checkout: Complete!"
    And the success message is "Thank you for your order!"

  Scenario: The complete page shows the dispatch message
    Given I am at the checkout overview with the backpack in my cart
    When I finish the order
    Then the dispatch message is "Your order has been dispatched, and will arrive just as fast as the pony can get there!"

  Scenario: Back Home from the complete page returns to the catalog
    Given I am at the checkout overview with the backpack in my cart
    When I finish the order
    And I click back home
    Then the page heading is "Products"
    And I am on the inventory page

  Scenario: A completed order leaves the cart empty
    Given I am at the checkout overview with the backpack in my cart
    When I finish the order
    And I click back home
    Then the cart badge shows 0
    When I open the cart
    Then the cart is empty
