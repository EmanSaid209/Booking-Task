package test.booking;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.BookingHomePage;
import pages.SearchResultsPage;
import pages.HotelDetailsPage;
import pages.ReservationPage;

import java.time.Duration;

public class BookingTest {
    private WebDriver driver;
    private BookingHomePage bookingPage;
    private SearchResultsPage searchResultsPage;
    private HotelDetailsPage hotelDetailsPage;
    private ReservationPage reservationPage;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-notifications");
        options.addArguments("--start-maximized");

        // Add these options to prevent browser from closing
        options.setExperimentalOption("detach", true);
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);
        driver.get("https://www.booking.com");
        
        // Initialize page objects
        bookingPage = new BookingHomePage(driver);
        searchResultsPage = new SearchResultsPage(driver);
        hotelDetailsPage = new HotelDetailsPage(driver);
        reservationPage = new ReservationPage(driver);
    }

    @Test
    public void testBookingFlow() {
        try {

            Thread.sleep(2000);

            //Dismiss sign in popup
            bookingPage.dismissSignInPopup();

            // Step 1: Search for location from Excel
            bookingPage.searchLocation(0);
            
            // Step 2: Select dates from Excel and search
            bookingPage.selectDates(0);
            bookingPage.clickSearch();
            Thread.sleep(1000);

            // Step 3: Select Tolip Hotel and click See Availability
            bookingPage.clickSeeAvailabilityBtn();
            hotelDetailsPage.getDisplayedCheckInDate();
            hotelDetailsPage.getDisplayedCheckOutDate();
            
            // Step 5: Assert dates
            String actualCheckIn = hotelDetailsPage.getDisplayedCheckInDate();
            String actualCheckOut = hotelDetailsPage.getDisplayedCheckOutDate();
            
            // Get expected dates from Excel data
           /* String expectedCheckIn = bookingPage.getTestData().get(0)[1];
            String expectedCheckOut = bookingPage.getTestData().get(0)[2];*/
            
            Assert.assertTrue(actualCheckIn.contains(expectedCheckIn) && 
                            actualCheckOut.contains(expectedCheckOut), 
                            "Dates are not displayed correctly");
            
            // Step 6: Select room and reserve
            hotelDetailsPage.selectRoomTypeAndAmount("Standard Room", "1");
            hotelDetailsPage.clickReserveButton();

            // Step 7: Verify hotel name in reservation
            hotelDetailsPage.verifyHotelName("Tolip Hotel Alexandria");
            
            
            Assert.assertTrue(reservationPage.verifyHotelName("Tolip Hotel Alexandria"), 
                "Hotel name is not correct in reservation");
                
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Test failed: " + e.getMessage());
        }
    }

   /* @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }*/
}
