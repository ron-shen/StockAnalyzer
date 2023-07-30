package webapp.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String symbol;
    private String sector;
    private String industry;
    private LocalDate existFrom;
    private Boolean newScanned;
    protected Stock(){
    }

    public Stock(String symbol, String sector, String industry,
                 LocalDate existFrom, Boolean newScanned) {
        this.symbol = symbol;
        this.sector = sector;
        this.industry = industry;
        this.existFrom = existFrom;
        this.newScanned = newScanned;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return Objects.equals(symbol, stock.symbol)
                && Objects.equals(sector, stock.sector)
                && Objects.equals(industry, stock.industry);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, sector, industry);
    }

    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", symbol='" + symbol + '\'' +
                ", sector='" + sector + '\'' +
                ", industry='" + industry + '\'' +
                ", existFrom=" + existFrom +
                ", newScanned=" + newScanned +
                '}';
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public LocalDate getExistFrom() {
        return existFrom;
    }

    public void setExistFrom(LocalDate existFrom) {
        this.existFrom = existFrom;
    }

    public Boolean getNewScanned() {
        return newScanned;
    }

    public void setNewScanned(Boolean newScanned) {
        this.newScanned = newScanned;
    }
}
