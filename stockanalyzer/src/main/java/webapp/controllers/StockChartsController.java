package webapp.controllers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import webapp.services.StockChartsService;
import webapp.entities.Stock;
import webapp.repositories.StockRepository;

@RestController
@RequestMapping({"/stock-charts"})
public class StockChartsController {
    private StockChartsService stockChartsService;

    public StockChartsController(StockChartsService stockChartsService) {
        this.stockChartsService = stockChartsService;
    }

    @GetMapping({"/run-scan"})
    public List<Stock> runScan(@RequestParam String[] industries) {
        String filePath = "/home/ron/Desktop/account";
        String username = null;
        String password = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            username = reader.readLine();
            password = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stockChartsService.login(username, password);
        Set<Stock> scannedStocks = stockChartsService.scan(industries);
        List<Stock> updateStockList = stockChartsService.updateStockList(scannedStocks);
        return updateStockList;
    }

    @GetMapping({"/screened-stocks"})
    public List<Stock> getScreenedStocks() {
        return stockChartsService.getAllStocks();
    }
}
