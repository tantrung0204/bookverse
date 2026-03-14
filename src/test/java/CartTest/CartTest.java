/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CartTest;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 *
 * @author TrungNT - CE200064
 */
public class CartTest {

    public static void main(String[] args) throws InterruptedException {

        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        WebDriver driver = new ChromeDriver();

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

        Thread.sleep(2000);

        //  mở product
        driver.get("http://localhost:8080/bookverse/product-detail?id=5");
        System.out.println("Current page: " + driver.getCurrentUrl());

        Thread.sleep(2000);

        System.out.println("Trying to click add to cart");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.id("add-to-cart")));

        Thread.sleep(1000);
        //  add to cart
        driver.findElement(By.id("add-to-cart")).click();
        System.out.println("Clicked add to cart");

        System.out.println("Add to cart executed");

        driver.get("http://localhost:8080/bookverse/cart");

        Thread.sleep(3000);

        driver.quit();
    }
}
