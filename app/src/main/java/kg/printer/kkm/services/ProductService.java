package kg.printer.kkm.services;

import java.util.ArrayList;

import kg.printer.kkm.domains.Product;
import kg.printer.kkm.repositories.DatabaseDAO;
import kg.printer.kkm.repositories.ProductDAO;
import kg.printer.kkm.view.ProductActivity;
import kg.printer.kkm.view.ProductsActivity;

public class ProductService {

    private final ProductDAO productDAO;

    public ProductService(ProductsActivity productsActivity) {
        productDAO = new ProductDAO(new DatabaseDAO(productsActivity));
    }

    public ProductService(ProductActivity productActivity) {
        productDAO = new ProductDAO(new DatabaseDAO(productActivity));
    }

    /**
     * @return вся номенклатура
     */
    public ArrayList<Product> readProducts() {
        return productDAO.readProducts();
    }

    /**
     * @param listIndex - последний индекс записи номенклатуры
     * @return номенклатура
     */
    public Product findProductByListIndex(int listIndex) {
        return productDAO.findProductByListIndex(listIndex);
    }

    /**
     * Метод создает запись номенклатуры
     *
     * @param name - наименоване
     * @param unit - единица измерения
     * @param price - цена
     */
    public void createProduct(String name, String unit, String price) {
        productDAO.createProduct(name, unit, price);
    }

    /**
     * Метод изменяет запись номенклатуры
     *
     * @param name - новое наименование
     * @param unit - новая единица измерения
     * @param price - новая цена
     * @param listIndex - индекс номенклатуры в списке
     */
    public void updateProduct(String name, String unit, String price, int listIndex) {
        productDAO.updateProduct(name, unit, price, listIndex);
    }

    /**
     * @return единицы измерения из базы
     */
    public ArrayList<String> findAllUnits() {
        return productDAO.findAllUnits();
    }

}
