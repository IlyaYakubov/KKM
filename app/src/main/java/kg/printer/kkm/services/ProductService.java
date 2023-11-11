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
     * @param product - номенклатура
     */
    public void createProduct(Product product) {
        productDAO.createProduct(product);
    }

    /**
     * Метод изменяет запись номенклатуры
     *
     * @param product - номенклатура
     * @param listIndex - индекс номенклатуры в списке
     */
    public void updateProduct(Product product, int listIndex) {
        productDAO.updateProduct(product, listIndex);
    }

    /**
     * @return единицы измерения из базы
     */
    public ArrayList<String> findAllUnits() {
        return productDAO.findAllUnits();
    }

}
