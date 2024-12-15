package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ReservationPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By hotelNameDisplay = By.cssSelector(".hotel-name");

    public ReservationPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean verifyHotelName(String expectedHotelName) {
        String actualHotelName = driver.findElement(hotelNameDisplay).getText();
        return actualHotelName.contains(expectedHotelName);
    }
} 