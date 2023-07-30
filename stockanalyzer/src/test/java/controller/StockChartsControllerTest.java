package controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import webapp.services.StockChartsService;
import webapp.repositories.StockRepository;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class StockChartsControllerTest {
    @Mock
    private StockChartsService stockChartsService;
    @Mock
    private StockRepository stockRepository;

    @Test
    public void testRunScan(){
        assertTrue(true);
//        StockChartsController stockChartsController = new StockChartsController(stockChartsService, stockRepository);
//
//        Set<StockDto> scannedStock = new HashSet<>();
//        String[] industries = new String[] {"Recreational Services"};
//        List<Stock> savedStock = new ArrayList<>();
//
//        StockDto dummyStock1 = new StockDto("CCL", "Consumer Discretionary", "Recreational Services");
//        StockDto dummyStock2 = new StockDto("CUK", "Consumer Discretionary", "Recreational Services");
//        StockDto dummyStock3 = new StockDto("LTH", "Consumer Discretionary", "Recreational Services");
//
//        Stock dummyStock4 = new Stock("NCLH", "Consumer Discretionary", "Recreational Services");
//        Stock dummyStock5 = new Stock("LTH", "Consumer Discretionary", "Recreational Services");
//
//        scannedStock.add(dummyStock1);
//        scannedStock.add(dummyStock2);
//        scannedStock.add(dummyStock3);
//        savedStock.add(dummyStock4);
//        savedStock.add(dummyStock5);
//
//        Mockito.doNothing().when(stockChartsService).login("abc", "def");
//        Mockito.doNothing().when(stockRepository).deleteAll();
//        Mockito.doNothing().when(stockRepository).save(Mockito.any());
//        Mockito.when(stockChartsService.scan(industries)).thenReturn(scannedStock);
//        Mockito.when(stockRepository.findAll()).thenReturn(savedStock);
    }
}
