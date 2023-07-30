package webapp.controllers;

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
        Set<Stock> scannedStocks = stockChartsService.scan(industries);
        List<Stock> updateStockList = stockChartsService.updateStockList(scannedStocks);
        return updateStockList;
    }

    @GetMapping({"/screened-stocks"})
    public List<Stock> getScreenedStocks() {
        return null;
       // return stockRepository.findAll();
    }
}
