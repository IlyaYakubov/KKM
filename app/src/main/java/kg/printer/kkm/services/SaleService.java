package kg.printer.kkm.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kg.printer.kkm.domains.Product;
import kg.printer.kkm.repositories.DatabaseDAO;
import kg.printer.kkm.repositories.SaleDAO;
import kg.printer.kkm.view.CashActivity;
import kg.printer.kkm.view.ProductSelectionActivity;
import kg.printer.kkm.view.SaleActivity;

public class SaleService {

    private final SaleDAO saleDAO;

    public SaleService(ProductSelectionActivity productSelectionActivity) {
        saleDAO = new SaleDAO(new DatabaseDAO(productSelectionActivity));
    }

    public SaleService(CashActivity cashActivity) {
        saleDAO = new SaleDAO(new DatabaseDAO(cashActivity));
    }

    public SaleService(SaleActivity saleActivity) {
        saleDAO = new SaleDAO(new DatabaseDAO(saleActivity));
    }

    /**
     * @param contributedParam - внесено наличными
     * @param sum - значение суммы
     * @return структура с расчитанной сдачей
     */
    @SuppressWarnings("rawtypes")
    public Map toCount(String contributedParam, double sum) {
        double contributed = Double.parseDouble(contributedParam);
        double change = contributed - sum;

        Map result = new HashMap();
        result.put("contributed", contributed);
        result.put("change", change);

        return result;
    }

    /**
     * @param quantityText - количество
     * @param priceText - цена
     * @return строка, результат умножения количества на цену
     */
    public String toMultiply(String quantityText, String priceText) {
        if (quantityText.equals("") || priceText.equals("")) {
            return "";
        }

        double quantity = Double.parseDouble(quantityText);
        double coast = Double.parseDouble(priceText);
        double sum = quantity * coast;

        return String.format("%.2f", sum);
    }

    /**
     * @param listIndex - индекс в списке
     * @return структура с наименованием номенклатуры и ценой
     */
    public Map findProductByListIndex(int listIndex) {
        return saleDAO.findProductByListIndex(listIndex);
    }

    /**
     * @return список номенлатуры
     */
    public ArrayList<Product> readProducts() {
        return saleDAO.readProducts();
    }

}
