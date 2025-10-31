import java.math.BigDecimal;
import java.util.Objects;

public class MoneyTotal {
    private BigDecimal amount;

    private Currency currency;

    public MoneyTotal(BigDecimal amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public void add(double amount) {
        BigDecimal result = this.amount.add(BigDecimal.valueOf(amount));
        this.amount = result.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : result;
    }

    public void remove(double amount) {
        BigDecimal result = this.amount.subtract(BigDecimal.valueOf(amount));
        this.amount = result.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : result;
    }

    public double getAmount() {
        return amount.doubleValue();
    }

    public Currency getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoneyTotal that = (MoneyTotal) o;
        return Objects.equals(amount, that.amount) && Objects.equals(currency, that.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }
}
