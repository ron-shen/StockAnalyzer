package webapp.stock;

import webapp.dto.StockDto;
import webapp.entities.Stock;

import java.util.ArrayList;
import java.util.List;

public class StockMapper {
    public static StockDto toDto(Stock stock){
        StockDto dto = new StockDto(stock.getSymbol(), stock.getSector(), stock.getIndustry());
        return dto;
    }
    public static List<StockDto> toDto(List<Stock> stocks){
        List<StockDto> res = new ArrayList<>();
        for(Stock stock : stocks){
            StockDto dto = new StockDto(stock.getSymbol(), stock.getSector(), stock.getIndustry());
            res.add(dto);
        }
        return res;
    }
}
