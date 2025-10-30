import java.util.List;

public class HtmlRenderer {
    public String generateCurrenciesTable(double from, List<Money> moneyList, Currency currency) {
        StringBuilder sb = new StringBuilder();
        sb.append("<table border='1'>");
        sb.append("<tr><th>From</th><th>Amount</th><th>To</th><th>Converted Amount</th></tr>");

        for (Money money : moneyList) {
            sb.append("<tr>");
            sb.append("<td>").append(currency.getCode()).append("</td>");
            sb.append("<td>").append(from).append("</td>");
            sb.append("<td>").append(money.getCurrency().getCode()).append("</td>");
            sb.append("<td>").append(String.format("%.2f", money.getAmount())).append("</td>");
            sb.append("</tr>");
        }

        sb.append("</table>");
        return sb.toString();
    }
}
