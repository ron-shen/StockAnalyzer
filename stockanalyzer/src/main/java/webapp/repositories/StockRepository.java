package webapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import webapp.entities.Stock;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Long> {
    List<Stock> findBySector(String sector);
    List<Stock> findByIndustry(String industry);
}
