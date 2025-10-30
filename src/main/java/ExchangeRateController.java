import java.util.List;

public class ExchangeRateController {
    private final CurrencyExchangeService currencyExchangeService;
    private final HtmlRenderer htmlRenderer;

    public ExchangeRateController(CurrencyExchangeService currencyExchangeService, HtmlRenderer htmlRenderer) {
        this.currencyExchangeService = currencyExchangeService;
        this.htmlRenderer = htmlRenderer;
    }

    public void addMoney(double amount) {
        currencyExchangeService.addMoney(amount);
    }

    public double getCurrentMoney() {
        return currencyExchangeService.getCurrentMoney();
    }

    public void removeMoney(double amount) {
        currencyExchangeService.removeMoney(amount);
    }

    public double convert(Currency currency) {
        Money converted = currencyExchangeService.convertToCurrency(currency);
        return converted.getAmount();
    }

    public Currency getCurrentCurrency() {
        return currencyExchangeService.getCurrentCurrency();
    }

    public List<Money> getExchanges() {
        return currencyExchangeService.convertToAllCurrencies();
    }

    public String getExchangesHtmlTable() {
        List<Money> exchanges = getExchanges();
        double amount = currencyExchangeService.getCurrentMoney();
        return htmlRenderer.generateCurrenciesTable(amount, exchanges, currencyExchangeService.getCurrentCurrency());
    }
}
