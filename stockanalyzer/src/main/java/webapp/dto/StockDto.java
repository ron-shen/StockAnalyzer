package webapp.dto;

public class StockDto {
    private String symbol;
    private String sector;
    private String industry;

    public StockDto(String symbol, String sector, String industry) {
        this.symbol = symbol;
        this.sector = sector;
        this.industry = industry;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this){
            return true;
        }
        if (!(obj instanceof StockDto)) {
            return false;
        }

        StockDto stock = (StockDto) obj;
        return this.sector == stock.getSector()
                && this.symbol == stock.getSymbol()
                && this.industry == stock.getIndustry();
    }

    public String getSymbol() {
        return symbol;
    }

    public String getSector() {
        return sector;
    }

    public String getIndustry() {
        return industry;
    }
}
