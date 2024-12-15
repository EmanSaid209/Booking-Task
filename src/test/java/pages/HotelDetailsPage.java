package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import org.openqa.selenium.JavascriptExecutor;

public class HotelDetailsPage {

    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;

    private By seeAvailabilityButton = By.xpath("//a[@data-testid='availability-cta-btn' and contains(@href, 'royal-tulip-alexandria')]");
    private By checkInDateDisplay = By.xpath("(//button[@data-testid='date-display-field-start']//span[text()='Wed 25 Dec'])[2]");
    private By checkOutDateDisplay = By.xpath("(//button[@data-testid='date-display-field-end']//span[text()='Sat 28 Dec'])[2]");
    private By reserveButton = By.xpath("//span[contains(@class, 'js-reservation-button__text')]");
    private By hotelNameDisplay = By.xpath("//h2[contains(@class, 'pp-header__title') and text()='Tolip Hotel Alexandria']");

    public HotelDetailsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.js = (JavascriptExecutor) driver;
    }

    private void scrollUntilElementVisible(WebElement element) {
        try {
            int maxScrollAttempts = 10;
            int scrollAttempt = 0;
            WebElement checkInAvailability = driver.findElement(checkInDateDisplay);
            
            while (!isElementVisible(checkInAvailability) && scrollAttempt < maxScrollAttempts) {
                js.executeScript("window.scrollBy(0, 300)");
                Thread.sleep(500);
                scrollAttempt++;
            }
            
            if (!isElementVisible(checkInAvailability)) {
                js.executeScript("arguments[0].scrollIntoView(false)", checkInAvailability);
                Thread.sleep(500);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to scroll to element: " + e.getMessage());
        }
    }

    private boolean isElementVisible(WebElement element) {
        try {
            return element.isDisplayed() && element.getLocation().getY() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public String getDisplayedCheckInDate() {
        try {
            WebElement checkInAvailability = driver.findElement(checkInDateDisplay);
            scrollUntilElementVisible(checkInAvailability);
            return checkInAvailability.getText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get check-in date: " + e.getMessage());
        }
    }


    public String getDisplayedCheckOutDate() {
        try {
            WebElement checkOutAvailability = driver.findElement(checkOutDateDisplay);
            scrollUntilElementVisible(checkOutAvailability);
            return checkOutAvailability.getText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get check-out date: " + e.getMessage());
        }
    }

    public void selectRoomTypeAndAmount(String roomType, String amount) {
        try {

            WebElement dropdownMenu = driver.findElement(By.xpath("//select[contains(@class, 'js-hprt-nos-select')]"));
            Select dropdown = new Select(dropdownMenu);
            dropdown.selectByIndex(1);
            dropdownMenu.click();
        } catch (Exception e) {
            throw new RuntimeException("Failed to select room: " + e.getMessage());
        }
    }

    public void clickReserveButton() {
        try {
            WebElement reservationBtn = driver.findElement(reserveButton);
            scrollUntilElementVisible(reservationBtn);
            reservationBtn.click();
        } catch (Exception e) {
            throw new RuntimeException("Failed to click reserve button: " + e.getMessage());
        }
    }

    public boolean isHotelNameDisplayed(String hotelName) {
        try {
            WebElement hotelName = driver.findElement(hotelNameDisplay);
            scrollUntilElementVisible(hotelName);
            String displayedName = hotelName.getText();
            return displayedName.contains(hotelName);
        } catch (Exception e) {
            throw new RuntimeException("Failed to check hotel name: " + e.getMessage());
        }
    }
}

