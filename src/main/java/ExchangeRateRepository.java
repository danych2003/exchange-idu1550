import java.util.ArrayList;
import java.util.List;

public class ExchangeRateRepository {
    private final List<ExchangeRate> exchangeRates = new ArrayList<>();

    public List<ExchangeRate> findAllByFrom(Currency currency) {
        return exchangeRates.stream()
                .filter(exchangeRate -> exchangeRate.getFrom().equals(currency)).toList();
    }

    public ExchangeRate findAllByFromAndTo(Currency from, Currency to) {
        return exchangeRates.stream()
                .filter(exchangeRate -> exchangeRate.getFrom().equals(from) && exchangeRate.getTo().equals(to))
                .findFirst()
                .orElse(null);
    }

    public void remove(ExchangeRate exchangeRate) {
        exchangeRates.remove(exchangeRate);
    }

    public void save(ExchangeRate exchangeRate) {
        ExchangeRate existing = exchangeRates.stream()
                .filter(er -> er.getFrom().equals(exchangeRate.getFrom()) &&
                        er.getTo().equals(exchangeRate.getTo()))
                .findFirst()
                .orElse(null);

        if (existing != null) {
            exchangeRates.remove(existing);
            exchangeRates.add(exchangeRate);
        } else {
            exchangeRates.add(exchangeRate);
        }
    }
}
