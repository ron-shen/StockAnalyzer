package service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.mockito.Mockito;
import org.openqa.selenium.chrome.ChromeDriver;
import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import org.mockito.junit.MockitoJUnitRunner;
import webapp.components.DateProvider;
import webapp.entities.Stock;
import webapp.repositories.StockRepository;
import webapp.services.StockChartsService;

@RunWith(MockitoJUnitRunner.class)
public class StockChartsServiceTest {
    @Mock
    private WebElement mockWebElement;
    @Mock
    private ChromeDriver mockDriver;
    @Mock
    private WebDriver.TargetLocator mockTargetLocator;
    @Mock
    private StockRepository mockStockRepository;
    @Mock
    private DateProvider mockDateProvider;
    private StockChartsService stockCharts;

    @Before
    public void setup(){
        this.stockCharts = new StockChartsService(mockDriver, mockStockRepository, mockDateProvider);
    }
    @Test
    public void testScan(){
        List<WebElement> webElements = Arrays.asList(mockWebElement);
        LocalDate dummyDate = LocalDate.of(2023, Month.JULY, 26);
        String[] industries = new String[] {"Commercial Vehicles & Trucks"};

        Mockito.doNothing().when(mockWebElement).click();
        Mockito.when(mockDriver.findElement(Mockito.any())).thenReturn(mockWebElement);

        Mockito.when(mockDriver.getWindowHandle()).thenReturn("originalWindow");
        Mockito.when(mockDriver.getWindowHandles()).thenReturn(new HashSet<>(Arrays.asList("originalWindow", "newWindow")));
        Mockito.when(mockDriver.switchTo()).thenReturn(mockTargetLocator);
        Mockito.when(mockDriver.switchTo().window(Mockito.any())).thenReturn(null);
        Mockito.when(mockDriver.findElements(By.xpath("//tbody/tr"))).thenReturn(webElements);

        Mockito.when(mockWebElement.findElement(By.xpath("td[6]"))).thenReturn(mockWebElement);
        Mockito.when(mockWebElement.findElement(By.xpath("td[5]"))).thenReturn(mockWebElement);
        Mockito.when(mockWebElement.findElement(By.xpath("td[2]/span[@class='symlink']")))
                .thenReturn(mockWebElement);
        Mockito.when(mockWebElement.findElement(By.xpath("td[6]")).getText())
                .thenReturn("Commercial Vehicles");
        Mockito.when(mockWebElement.findElement(By.xpath("td[2]/span[@class='symlink']")).getText())
                .thenReturn("CMI");
        Mockito.when(mockWebElement.findElement(By.xpath("td[5]")).getText())
                .thenReturn("Industrial");
        Mockito.when(mockDateProvider.getCurrentDate()).thenReturn(dummyDate);

        Set<Stock> stocks = stockCharts.scan(industries);
        Stock expectedStock = new Stock("CMI", "Industrial",
                "Commercial Vehicles", dummyDate, true);

        for (Stock stock : stocks) {
            assertEquals(expectedStock, stock);
        }
    }

    @Test
    public void testUpdateStockList(){
        //DB: TSLA AAPL AMD MSFT
        //NEW: TSLA AMD NVDA
        LocalDate dummyDate = LocalDate.of(2023, Month.JULY, 26);
        Stock tsla = new Stock("TSLA", "Discretionary",
                "Automobiles", dummyDate, true);
        Stock aapl = new Stock("AAPL", "Technology",
                "Computer Hardware", dummyDate, true);
        Stock amd = new Stock("AMD", "Technology",
                "Semiconductors", dummyDate, true);
        Stock msft = new Stock("MSFT", "Technology",
                "Software", dummyDate, true);

        List<Stock> savedStocks = new ArrayList<>();
        Mockito.when(mockStockRepository.findAll()).thenReturn(savedStocks);
        savedStocks.add(tsla);
        savedStocks.add(aapl);
        savedStocks.add(amd);
        savedStocks.add(msft);

        LocalDate dummyNextDate = LocalDate.of(2023, Month.AUGUST, 2);
        Stock newTsla = new Stock("TSLA", "Discretionary",
                "Automobiles", dummyNextDate, true);
        Stock newAmd = new Stock("AMD", "Technology",
                "Semiconductors", dummyNextDate, true);
        Stock newNvda = new Stock("NVDA", "Technology",
                "Semiconductors", dummyNextDate, true);

        Set<Stock> scannedStocks = new HashSet<>();
        scannedStocks.add(newTsla);
        scannedStocks.add(newAmd);
        scannedStocks.add(newNvda);

        List<Stock> updatedStocks = stockCharts.updateStockList(scannedStocks);
        tsla.setNewScanned(false);
        amd.setNewScanned(false);
        assertTrue(updatedStocks.contains(tsla));
        assertTrue(updatedStocks.contains(amd));
        assertTrue(updatedStocks.contains(newNvda));

        //test if there's no stocks in DB
        savedStocks.clear();
        scannedStocks.clear();
        scannedStocks.add(newTsla);
        scannedStocks.add(newAmd);
        scannedStocks.add(newNvda);
        updatedStocks = stockCharts.updateStockList(scannedStocks);
        assertTrue(updatedStocks.contains(newTsla));
        assertTrue(updatedStocks.contains(newAmd));
        assertTrue(updatedStocks.contains(newNvda));

    }
}
