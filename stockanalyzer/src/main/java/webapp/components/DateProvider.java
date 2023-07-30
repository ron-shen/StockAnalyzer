package webapp.components;

import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;

@Component
public class DateProvider {
    private Clock clock;
    public DateProvider(Clock clock) {
        this.clock = clock;
    }
    public LocalDate getCurrentDate(){
        return LocalDate.now(clock);
    }
}
