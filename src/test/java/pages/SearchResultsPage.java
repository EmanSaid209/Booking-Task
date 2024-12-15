package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.Iterator;
import java.util.Set;
import org.openqa.selenium.WebDriver.Window;

public class SearchResultsPage {
    private WebDriver driver;
    private WebDriverWait wait;
    private JavascriptExecutor js;
    

    //private By hotelLocator = By.xpath("//div[contains(., 'Tolip Hotel Alexandria') and @data-testid='title']");
   private By hotelLocator = By.xpath("//a[@data-testid='availability-cta-btn' and contains(@href, 'royal-tulip-alexandria')]");
    

    public SearchResultsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.js = (JavascriptExecutor) driver;
    }

    public void clickSeeAvailabilityBtn() {
        try {
            String originalWindow = driver.getWindowHandle();
            Thread.sleep(2000);
            wait.until(ExpectedConditions.refreshed(
                ExpectedConditions.elementToBeClickable(hotelLocator)
            ));
            
            // Scroll the element into view
            WebElement hotelLink = driver.findElement(hotelLocator);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", hotelLink);
            Thread.sleep(1000);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", hotelLink);
            
            wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        
        // Switch to the new window/tab
            for (String windowHandle : driver.getWindowHandles()) {
            if (!originalWindow.equals(windowHandle)) {
                driver.switchTo().window(windowHandle);
                driver.manage().window().maximize();
                break;
                }
            }
        }
            
         catch (Exception e) {
            throw new RuntimeException("Failed to select hotel: " + e.getMessage());
        }
    }
}

