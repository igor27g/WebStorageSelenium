import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public class WebStorageTest {

    ChromeDriver driver;
    WebDriverWait wait;

    String airly = "https://airly.eu/map/pl/#50.06237,19.93898";

    @BeforeEach
    public void driverSetup(){

        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().pageLoadTimeout(25, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.navigate().to("https://fakestore.testelka.pl/product/fuerteventura-sotavento/");

        wait = new WebDriverWait(driver,10);
    }

    @AfterEach
    public void driverClose()
    {
        driver.close();
        driver.quit();
    }

    @Test
    public void initialLocalStorageStateTest() {
        Assertions.assertEquals(1,driver.getLocalStorage().size(), "Number of keys in Local Storage is not 1.");
    }

    @Test
    public void initialSessionStorageStateTest() {
        Assertions.assertEquals(2, driver.getSessionStorage().size(), "Number of keys in Session Storage is not 2.");
    }

    @Test
    public void cartCreatedKeyTest() {
        //wc_cart_created
        driver.findElement(By.cssSelector("button[name='add-to-cart']")).click();
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(d ->driver.getSessionStorage().size() ==3);
        Assertions.assertTrue(driver.getSessionStorage().keySet().contains("wc_cart_created"), "wc_cart_created was not add to Session" +
                "Storage affter adding product to the cart");
    }

    @Test
    public void wcFragmentsKeyRemovedTest() {
        Set<String> keys = driver.getSessionStorage().keySet();
        String wcFragmentKey = "";
        for (String key: keys) {
            if(key.contains("wc_fragments")) {
                wcFragmentKey = key;
            }
        }
        String removed = driver.getSessionStorage().removeItem(wcFragmentKey); //jak wpiszemy głupotę to zwraca nam null
        Assertions.assertTrue(removed!=null,"wc_fragment was not removed.");
    }
}
