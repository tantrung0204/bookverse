    package CartTest;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test các trường hợp Đúng, Sai, Lỗi cho chức năng Giỏ Hàng.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CartTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private final String BASE_URL = "http://localhost:8080/bookverse";

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    public void tearDown() throws InterruptedException {
        // Dừng lại một lúc để bản kịp quan sát giao diện trước khi đóng trình duyệt
        Thread.sleep(3500);
        if (driver != null) {
            System.out.println("Closing browser.");
            driver.quit();
        }
        // Dừng thêm một chút trước khi tự động gọi bài Test tiếp theo
        Thread.sleep(1500);
    }

    /**
     * Đăng nhập bằng tài khoản Customer
     */
    private void loginAsCustomer() {
        driver.get(BASE_URL + "/signin");
        WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));
        usernameInput.sendKeys("user1");
        WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("password")));
        passwordInput.sendKeys("123456789");
        driver.findElement(By.id("signin-btn")).click();

        wait.until(ExpectedConditions.urlContains("/home"));
    }

    /**
     * Thêm sản phẩm vào giỏ
     */
    private void addItemToCart(String productId) throws InterruptedException {
        driver.get(BASE_URL + "/product-detail?id=" + productId);
        WebElement addBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("add-to-cart")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", addBtn);
        Thread.sleep(500); // Đợi scroll
        addBtn.click();
        Thread.sleep(1500); // Chờ insert vào db
    }

    // ==========================================
    // CÁC TEST CASES: ĐÚNG, SAI, LỖI
    // ==========================================
    /**
     * 1. TRƯỜNG HỢP LỖI: Cố tình truy cập giỏ hàng khi chưa đăng nhập. Bị chặn
     * bởi logic phân quyền, phải bị điều hướng về signin.
     */
    @Test
    @Order(1)
    public void testAccessCartWithoutLogin_ShouldRedirectToSignin() {
        System.out.println("--- Test 1: Access Cart Without Login ---");
        driver.get(BASE_URL + "/cart");

        wait.until(ExpectedConditions.urlContains("/signin"));
        assertTrue(driver.getCurrentUrl().contains("/signin"), "User is redirected to the login page when no active session exists.");
    }

    /**
     * 2. TRƯỜNG HỢP ĐÚNG: Thêm sản phẩm vào giỏ hàng thành công
     */
    @Test
    @Order(2)
    public void testAddToCart_Success() throws InterruptedException {
        System.out.println("--- Test 2: Valid Add to Cart ---");
        loginAsCustomer();

        addItemToCart("5");

        driver.get(BASE_URL + "/cart");

        boolean isCartTablePresent = driver.findElements(By.cssSelector(".cart-table")).size() > 0;
        assertTrue(isCartTablePresent, "Product added successfully. Cart table is displayed.");
    }

    /**
     * 3. TRƯỜNG HỢP ĐÚNG: Cập nhật số lượng sản phẩm (+1) và kiểm tra tổng tiền
     * đổi.
     */
    @Test
    @Order(3)
    public void testUpdateQuantity_Valid() throws InterruptedException {
        System.out.println("--- Test 3: Update Quantity Correctly ---");
        loginAsCustomer();
        addItemToCart("5"); // Thêm để đảm bảo có hàng

        driver.get(BASE_URL + "/cart");

        if (driver.findElements(By.cssSelector(".cart-table")).isEmpty()) {
            fail("No items in the cart to test quantity update.");
        }

        // Lưu tổng tiền ban đầu lại
        WebElement grandTotalSpan = driver.findElement(By.id("grand-total"));
        String oldTotal = grandTotalSpan.getText();

        // Nhấn nút '+'
        WebElement increaseQtyBtn = driver.findElement(By.cssSelector(".qty-btn-custom:last-child"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", increaseQtyBtn);
        Thread.sleep(500);
        increaseQtyBtn.click();

        // Đợi Ajax update
        Thread.sleep(2000);
        String newTotal = driver.findElement(By.id("grand-total")).getText();

        assertNotEquals(oldTotal, newTotal, "After increasing the quantity, the grand total must be updated");
    }

    /**
     * 4. TRƯỜNG HỢP SAI/LỖI: Nhập số lượng vượt tồn kho. UI sẽ validate và hiển
     * thị Toast nhắc nhở.
     */
    @Test
    @Order(4)
    public void testUpdateQuantity_ExceedStockLimits() throws InterruptedException {
        System.out.println("--- Test 4: Update Quantity Exceeding Stock ---");
        loginAsCustomer();
        addItemToCart("5");

        driver.get(BASE_URL + "/cart");

        if (driver.findElements(By.cssSelector(".cart-table")).isEmpty()) {
            fail("Cart is empty. Cannot test stock validation.");
        }

        // Cố tình gõ số lượng siêu lớn
        WebElement qtyInput = driver.findElement(By.cssSelector(".qty-input-custom"));
        qtyInput.clear();
        qtyInput.sendKeys("999999");

        // Trigger Javascript onchange thay vì chỉ dùng phím TAB
        qtyInput.sendKeys(Keys.ENTER);
        ((JavascriptExecutor) driver).executeScript("arguments[0].dispatchEvent(new Event('change'));", qtyInput);

        // Tăng thời gian chờ xử lý Toast của JS và Ajax
        Thread.sleep(3000);

        // Dò xem khối toast có hiện lên không
        boolean notificationTriggered = driver.getPageSource().contains("Only");
        if (!notificationTriggered) {
            notificationTriggered = !driver.findElements(By.cssSelector(".toast-error")).isEmpty();
        }
        assertTrue(notificationTriggered, "Quantity exceeds stock → Error displayed.");
    }

    /**
     * 5. TRƯỜNG HỢP ĐÚNG: Xóa sản phẩm khỏi giỏ hàng
     */
    @Test
    @Order(5)
    public void testRemoveItem_Success() throws InterruptedException {
        System.out.println("--- Test 5: Remove Items from Cart ---");
        loginAsCustomer();
        // Đảm bảo có sẵn đồ để xóa
        addItemToCart("5");

        driver.get(BASE_URL + "/cart");

        if (driver.findElements(By.cssSelector(".cart-table")).isEmpty()) {
            fail("Cart is empty. Nothing to remove.");
        }

        // Nhấn nút xóa
        WebElement removeBtn = driver.findElement(By.className("btn-remove-item"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", removeBtn);
        Thread.sleep(500);
        removeBtn.click();

        // Cấp quyền Alert
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        alert.accept();

        // Đợi tải lại trang giỏ hàng
        Thread.sleep(2000);

        // Giỏ trống sẽ hiện "Your cart is currently empty."
        // Hoặc tối thiểu là bảng `cart-table` biến mất / có phần tử giảm
        boolean isCartEmpty = driver.getPageSource().contains("Your cart is currently empty.")
                || driver.findElements(By.cssSelector(".cart-table")).isEmpty();

        assertTrue(isCartEmpty, "All items removed → Cart is empty / table hidden.");
    }
}
