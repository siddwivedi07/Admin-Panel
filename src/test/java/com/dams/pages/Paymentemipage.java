import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class PaymentEmiTest {

    private static WebDriver driver;
    private static WebDriverWait wait;

    public static void main(String[] args) throws InterruptedException {
        // --- Setup ---
        ChromeOptions options = new ChromeOptions();
        // options.addArguments("--headless"); // Uncomment for headless mode
        options.addArguments("--start-maximized");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            // Open your application URL
            driver.get("https://your-application-url.com"); // <-- Replace with your actual URL
            Thread.sleep(2000);

            // =========================================================
            // STEP 1: Click on "Payment Emi" link in the sidebar/menu
            // =========================================================
            System.out.println("Step 1: Clicking on Payment Emi...");
            WebElement paymentEmiLink = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='/payment-emi']"))
            );
            paymentEmiLink.click();
            Thread.sleep(1500);
            System.out.println("Step 1: Done - Navigated to Payment EMI page.");

            // =========================================================
            // STEP 2: Click on "Emi Installment List" card
            // =========================================================
            System.out.println("Step 2: Clicking on Emi Installment List...");
            WebElement emiInstallmentCard = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class,'textData') and normalize-space(text())='Emi Installment List']")
                )
            );
            emiInstallmentCard.click();
            Thread.sleep(1500);
            System.out.println("Step 2: Done - Navigated to Emi Installment List page.");

            // =========================================================
            // STEP 3: Click "Today" button
            // =========================================================
            System.out.println("Step 3: Clicking Today button...");
            WebElement todayBtn = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[.//span[normalize-space(text())='Today']]")
                )
            );
            todayBtn.click();
            Thread.sleep(1500);
            System.out.println("Step 3: Done - Today filter applied.");

            // =========================================================
            // STEP 4: Click "Weekly" button
            // =========================================================
            System.out.println("Step 4: Clicking Weekly button...");
            WebElement weeklyBtn = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[.//span[normalize-space(text())='Weekly']]")
                )
            );
            weeklyBtn.click();
            Thread.sleep(1500);
            System.out.println("Step 4: Done - Weekly filter applied.");

            // =========================================================
            // STEP 5: Click "Monthly" button
            // =========================================================
            System.out.println("Step 5: Clicking Monthly button...");
            WebElement monthlyBtn = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[.//span[normalize-space(text())='Monthly']]")
                )
            );
            monthlyBtn.click();
            Thread.sleep(1500);
            System.out.println("Step 5: Done - Monthly filter applied.");

            // =========================================================
            // STEP 6: Click "Yearly" button
            // =========================================================
            System.out.println("Step 6: Clicking Yearly button...");
            WebElement yearlyBtn = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[.//span[normalize-space(text())='Yearly']]")
                )
            );
            yearlyBtn.click();
            Thread.sleep(1500);
            System.out.println("Step 6: Done - Yearly filter applied.");

            // =========================================================
            // STEP 7: Click "Date Range" button
            // =========================================================
            System.out.println("Step 7: Clicking Date Range button...");
            WebElement dateRangeBtn = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[.//span[normalize-space(text())='Date Range']]")
                )
            );
            dateRangeBtn.click();
            Thread.sleep(2000); // Wait 2 seconds for the date picker to appear
            System.out.println("Step 7: Done - Date Range selected, waiting for calendar...");

            // =========================================================
            // STEP 8: Enter Start Date and End Date in the date range picker
            // =========================================================
            System.out.println("Step 8: Entering start date...");
            WebElement startDateInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//input[@placeholder='Start date' and @date-range='start']")
                )
            );
            // Clear and enter start date (format: YYYY-MM-DD or MM/DD/YYYY depending on your app)
            startDateInput.click();
            startDateInput.clear();
            startDateInput.sendKeys("2025-01-01"); // <-- Adjust date format if needed
            Thread.sleep(1000);

            System.out.println("Step 8: Entering end date...");
            WebElement endDateInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//input[@placeholder='End date' and @date-range='end']")
                )
            );
            endDateInput.click();
            endDateInput.clear();
            endDateInput.sendKeys("2025-03-31"); // <-- Adjust date format if needed
            Thread.sleep(1000);

            // Press Enter or Tab to confirm the date selection
            endDateInput.sendKeys(org.openqa.selenium.Keys.ENTER);
            Thread.sleep(1500);
            System.out.println("Step 8: Done - Date range entered: 2025-01-01 to 2025-03-31.");

            System.out.println("\n✅ All steps completed successfully!");

        } catch (Exception e) {
            System.err.println("❌ Test failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Thread.sleep(3000); // Brief pause to observe final state
            driver.quit();
            System.out.println("Browser closed.");
        }
    }
}
