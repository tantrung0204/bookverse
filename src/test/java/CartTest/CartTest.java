/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CartTest;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.Alert;

/**
 *
 * @author TrungNT - CE200064
 */
public class CartTest {

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        try {
            // open login page
            driver.get("http://localhost:8080/bookverse/signin");
            System.out.println("Current page: " + driver.getCurrentUrl());

            Thread.sleep(2000);

            // nhập username
            driver.findElement(By.name("username")).sendKeys("user1");

            // nhập password
            driver.findElement(By.name("password")).sendKeys("123456789");

            //  click login
            driver.findElement(By.id("signin-btn")).click();
            System.out.println("Logged in successfully");

            Thread.sleep(2000);

            // 1. ADD ITEM TO CART
            System.out.println("==== TEST: ADD ITEM ====");
            driver.get("http://localhost:8080/bookverse/product-detail?id=5");
            System.out.println("Current page: " + driver.getCurrentUrl());

            Thread.sleep(2000);

            System.out.println("Trying to click add to cart");
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.id("add-to-cart")));

            Thread.sleep(1000);
            driver.findElement(By.id("add-to-cart")).click();
            System.out.println("Clicked add to cart");
            Thread.sleep(2000);

            // 2. VIEW CART
            System.out.println("==== TEST: VIEW CART ====");
            driver.get("http://localhost:8080/bookverse/cart");
            System.out.println("Navigated to Cart: " + driver.getCurrentUrl());
            Thread.sleep(2000);

            // Verify if cart is not empty
            if (driver.findElements(By.cssSelector(".cart-table")).size() > 0) {
                System.out.println("Cart table found, proceeding to test edit and remove.");
                
                // 3. EDIT QUANTITY
                System.out.println("==== TEST: EDIT QUANTITY ====");
                WebElement increaseQtyBtn = driver.findElement(By.cssSelector(".qty-btn-custom:last-child")); // + button
                js.executeScript("arguments[0].scrollIntoView(true);", increaseQtyBtn);
                Thread.sleep(500);
                increaseQtyBtn.click();
                System.out.println("Clicked '+' to increase quantity.");
                
                // Wait for AJAX update
                Thread.sleep(3000);
                
                // Decrease quantity
                WebElement decreaseQtyBtn = driver.findElement(By.cssSelector(".qty-btn-custom:first-child")); // - button
                decreaseQtyBtn.click();
                System.out.println("Clicked '-' to decrease quantity.");
                
                // Wait for AJAX update
                Thread.sleep(3000);

                // 4. REMOVE ITEM
                System.out.println("==== TEST: REMOVE ITEM ====");
                WebElement removeBtn = driver.findElement(By.className("btn-remove-item"));
                js.executeScript("arguments[0].scrollIntoView(true);", removeBtn);
                Thread.sleep(500);
                removeBtn.click();
                System.out.println("Clicked remove item button.");
                
                Thread.sleep(1000);
                
                // Handle native confirm alert
                Alert alert = driver.switchTo().alert();
                System.out.println("Alert text: " + alert.getText());
                alert.accept();
                System.out.println("Accepted the remove confirmation alert.");
                
                Thread.sleep(3000);
                System.out.println("Item should be removed now.");
            } else {
                System.out.println("Cart is empty. Make sure product 5 is available in stock and add to cart succeeds.");
            }
        } catch (Exception e) {
            System.out.println("Test Failed: " + e.getMessage());
        } finally {
            // Close browser
            System.out.println("Closing browser.");
            driver.quit();
        }
    }
}
