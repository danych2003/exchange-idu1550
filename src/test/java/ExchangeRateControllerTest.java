import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExchangeRateControllerTest {

    private ExchangeRateRepository rateRepo;
    private MoneyTotal moneyTotal;
    private CurrencyExchangeService exchangeService;
    private HtmlRenderer htmlRenderer;
    private ExchangeRateController controller;

    private static Currency EUR = new Currency("EUR", "EURo");
    private static Currency USD = new Currency("USD", "US Dollar");
    private static Currency JPY = new Currency("JPY", "Japanese Yen");
    private static Currency GBP = new Currency("GBP", "British Pound");

    @BeforeEach
    void setUp() {
        moneyTotal = new MoneyTotal(BigDecimal.valueOf(100), EUR);
        rateRepo = new ExchangeRateRepository();

        rateRepo.save(new ExchangeRate(EUR, USD, 1.1));
        rateRepo.save(new ExchangeRate(EUR, JPY, 130.0));

        exchangeService = new CurrencyExchangeService(rateRepo, moneyTotal);
        htmlRenderer = new HtmlRenderer();
        controller = new ExchangeRateController(exchangeService, htmlRenderer);
    }

    @Test
    void testAddAndRemoveMoney() {
        controller.addMoney(50);
        assertEquals(150, controller.getCurrentMoney(), 0.01);

        controller.addMoney(-200);
        assertEquals(0, controller.getCurrentMoney(), 0.01);

        controller.addMoney(100);
        controller.removeMoney(30);
        assertEquals(70, controller.getCurrentMoney(), 0.01);
    }

    @Test
    void testAddNewCurrency() {
        rateRepo.save(new ExchangeRate(EUR, GBP, 0.85));
        ExchangeRate GBPRate = rateRepo.findAllByFromAndTo(EUR, GBP);
        assertNotNull(GBPRate);
        assertEquals(0.85, GBPRate.getRate(), 0.01);
    }

    @Test
    void testChangeExchangeRate() {
        ExchangeRate newRate = new ExchangeRate(EUR, USD, 1.2);
        exchangeService.saveExchangeRate(newRate);

        ExchangeRate updated = rateRepo.findAllByFromAndTo(EUR, USD);
        assertEquals(1.2, updated.getRate(), 0.01);

        ExchangeRate reverse = rateRepo.findAllByFromAndTo(USD, EUR);
        assertEquals(1.0 / 1.2, reverse.getRate(), 0.01);
    }

    @Test
    void testConvertMoney() {
        double USDAmount = controller.convert(USD);
        assertEquals(110.0, USDAmount, 0.01);

        double JPYAmount = controller.convert(JPY);
        assertEquals(13000.0, JPYAmount, 0.01);

        List<Money> allExchanges = controller.getExchanges();
        assertEquals(2, allExchanges.size());
    }

    @Test
    void testHtmlTable() {
        String html = controller.getExchangesHtmlTable();
        assertTrue(html.contains("EUR"));
        assertTrue(html.contains("USD"));
        assertTrue(html.contains("JPY"));
        assertTrue(html.contains("100"));
    }

    @Test
    void testConvertWithoutRate() {
        Currency cad = new Currency("CAD", "Canadian Dollar");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> controller.convert(cad));
        assertTrue(exception.getMessage().contains("No exchange rate"));
    }
}