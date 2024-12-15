package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ExcelUtils;
import java.time.Duration;
import java.util.List;
import java.util.ArrayList;

public class BookingHomePage {
    private WebDriver driver;
    private WebDriverWait wait;
    private List<String[]> testData;

    private By searchBox = By.cssSelector("input[name='ss']");
    private By searchButton = By.cssSelector("button[type='submit']");
    private By signInPopup = By.cssSelector("[aria-label='Dismiss sign in information.']");
    private By datePickerButton = By.cssSelector("[data-testid='date-display-field-start']");
    private By calendarMonth = By.cssSelector("[data-testid='searchbox-datepicker-calendar']");

    public BookingHomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        try {
            this.testData = ExcelUtils.readTestData("src/test/resources/testData.xlsx");
        } catch (Exception e) {
            System.out.println("Failed to read Excel file: " + e.getMessage());
            List<String[]> defaultData = new ArrayList<>();
            defaultData.add(new String[]{"Alexandria", "2024-12-22", "2024-12-26"});
            this.testData = defaultData;
        }
    }

    public void searchLocation(int rowIndex) {
        try {
            dismissSignInPopup();
            WebElement searchInput = wait.until(ExpectedConditions.elementToBeClickable(searchBox));
            searchInput.clear();
            String location = testData.get(rowIndex)[0];
            System.out.println("Searching for location: " + location);
            searchInput.sendKeys(location);
            Thread.sleep(1000);
        } catch (Exception e) {
            throw new RuntimeException("Failed to enter location: " + e.getMessage());
        }
    }

    public void selectDates(int rowIndex) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(datePickerButton)).click();
            Thread.sleep(1000);

            wait.until(ExpectedConditions.visibilityOfElementLocated(calendarMonth));

            String checkInDate = testData.get(rowIndex)[1];
            String checkOutDate = testData.get(rowIndex)[2];

            selectDate(checkInDate);
            Thread.sleep(500);
            selectDate(checkOutDate);

        } catch (Exception e) {
            throw new RuntimeException("Failed to select dates: " + e.getMessage());
        }
    }

    private void selectDate(String date) {
        try {
            String dateSelector = String.format("[data-date='%s']", date);
            
            while (true) {
                try {
                    WebElement dateElement = driver.findElement(By.cssSelector(dateSelector));
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dateElement);
                    break;
                } catch (NoSuchElementException e) {
                    WebElement nextButton = driver.findElement(
                        By.cssSelector("[data-testid='searchbox-datepicker-calendar'] button[aria-label*='Next month']"));
                    nextButton.click();
                    Thread.sleep(500);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to select date " + date + ": " + e.getMessage());
        }
    }

    public void dismissSignInPopup() {
        try {
            WebElement popup = wait.until(ExpectedConditions.presenceOfElementLocated(signInPopup));
            popup.click();
        } catch (Exception e) {
            System.out.println("Sign-in popup not found or already dismissed");
        }
    }

    public void clickSearch() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
        } catch (Exception e) {
            throw new RuntimeException("Failed to click search button: " + e.getMessage());
        }
    }

     public void clickSeeAvailabilityBtn() {
        try {
            // Wait for any dynamic content to load
            Thread.sleep(2000);
            
            // Find and click the See Availability button
            WebElement seeAvailabilityBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@data-testid='availability-cta-btn']")
            ));
            
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", seeAvailabilityBtn);
            Thread.sleep(1000);
            seeAvailabilityBtn.click();
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to click See Availability button: " + e.getMessage());
        }
    }

    public List<String[]> getTestData() {
        return testData;
    }
} 