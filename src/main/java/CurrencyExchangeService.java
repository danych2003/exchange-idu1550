import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class CurrencyExchangeService {
    private final ExchangeRateRepository exchangeRateRepository;
    private final MoneyTotal moneyTotal;

    public CurrencyExchangeService(ExchangeRateRepository exchangeRateRepository, MoneyTotal moneyTotal) {
        this.exchangeRateRepository = exchangeRateRepository;
        this.moneyTotal = moneyTotal;
    }

    public void addMoney(double amount) {
        moneyTotal.add(amount);
    }

    public void removeMoney(double amount) {
        moneyTotal.remove(amount);
    }

    public Currency getCurrentCurrency() {
        return moneyTotal.getCurrency();
    }

    public double getCurrentMoney() {
        return moneyTotal.getAmount().doubleValue();
    }


    public ExchangeRate getExchangeRate(Currency from, Currency to) {
        return exchangeRateRepository.findAllByFromAndTo(from, to);
    }

    public void saveExchangeRate(ExchangeRate exchangeRate) {
        exchangeRateRepository.save(exchangeRate);
        ExchangeRate reverse = new ExchangeRate(exchangeRate.getTo(), exchangeRate.getFrom(),
                1.0 / exchangeRate.getRate());
        exchangeRateRepository.save(reverse);
    }

    public Money convertToCurrency(Currency currency) {
        ExchangeRate rate = getExchangeRate(moneyTotal.getCurrency(), currency);
        if (rate == null) {
            throw new IllegalArgumentException("No exchange rate from " + moneyTotal.getCurrency().getCode() + " to " + currency.getCode());
        }
        double convertedAmount = moneyTotal.getAmount().doubleValue() * rate.getRate();
        return new Money(convertedAmount, currency);
    }

    public List<Money> convertToAllCurrencies() {
        return exchangeRateRepository.findAllByFrom(moneyTotal.getCurrency()).stream()
                .map(rate -> new Money(moneyTotal.getAmount().doubleValue() * rate.getRate(), rate.getTo()))
                .collect(Collectors.toList());
    }
}
