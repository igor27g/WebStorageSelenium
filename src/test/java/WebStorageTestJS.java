import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;


public class WebStorageTestJS {

        WebDriver driver;
        WebDriverWait wait;

        String airly = "https://airly.eu/map/pl/#50.06237,19.93898";

        @BeforeEach
        public void driverSetup(){

            System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
            driver = new ChromeDriver();
            driver.manage().timeouts().pageLoadTimeout(25, TimeUnit.SECONDS);
            driver.manage().window().maximize();
            driver.navigate().to("https://fakestore.testelka.pl/product/fuerteventura-sotavento/");
            driver.findElement(By.cssSelector("a[class='woocommerce-store-notice__dismiss-link']")).click();

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
            long size = (long) ((JavascriptExecutor)driver).executeScript("return localStorage.length;");
            Assertions.assertEquals(1,size , "Number of keys in Local Storage is not 1.");
        }

        @Test
        public void initialSessionStorageStateTest() {
            long size = (long) ((JavascriptExecutor)driver).executeScript("return sessionStorage.length;");
            Assertions.assertEquals(2, size, "Number of keys in Session Storage is not 2.");
        }

        @Test
        public void cartCreatedKeyTest() {
            //wc_cart_created
            driver.findElement(By.cssSelector("button[name='add-to-cart']")).click();
            WebDriverWait wait = new WebDriverWait(driver, 5);
            wait.until(d ->(long) ((JavascriptExecutor)driver).executeScript("return sessionStorage.length;") == 3);

            Set<String> keys = new HashSet<>();

            for(int i = 0; i < keys.size(); i++) {
                String key = (String) ((JavascriptExecutor)driver).executeScript("return localStorage.key(arguments[0]);",i);
                keys.add(key);
            }


            Assertions.assertTrue(keys.contains("wc_cart_created"), "wc_cart_created was not add to Session" +
                    "Storage affter adding product to the cart");
        }

        @Test
        public void wcFragmentsKeyRemovedTest() {

            String wcFragmentKey = "";
            for(int i = 0; i < 2; i++) {
                String key = (String) ((JavascriptExecutor)driver).executeScript("return localStorage.key(arguments[0]);",i);  //sprawdza kazdy key
                if(key.contains("wc_fragments")) {                                  // jesli zawiera wc_fragments to przypisuje
                    wcFragmentKey = key;
            }

                ((JavascriptExecutor)driver).executeScript("localStorage.removeItem(arguments[0]);",wcFragmentKey);
                long size = (long) ((JavascriptExecutor) driver).executeScript("return sessionStorage.length");
            Assertions.assertEquals(1,size,wcFragmentKey + " key was not removed");
        }
    }

}
