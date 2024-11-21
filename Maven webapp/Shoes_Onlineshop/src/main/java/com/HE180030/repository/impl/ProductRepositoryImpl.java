package com.HE180030.repository.impl;

import com.HE180030.model.Account;
import com.HE180030.model.Category;
import com.HE180030.model.Product;
import com.HE180030.repository.ProductRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(propagation = Propagation.REQUIRED)
@DependsOn("sessionFactory")
@Repository
public class ProductRepositoryImpl implements ProductRepository {
    private final SessionFactory sessionFactory;

    public ProductRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
//    @Override
//    public int countAllProduct() {
//        return 0;
//    }

    @Override
    public Product getLastProduct() {
        return sessionFactory.getCurrentSession()
                .createQuery(
                        "select p from Product p " +
                        "order by p.id desc", Product.class)
                .setMaxResults(1)
                .uniqueResult();
    }

    @Override
    public List<Product> getAllProducts() {
        return sessionFactory
                .getCurrentSession()
                .createQuery("from Product", Product.class)
                .getResultList();
    }

    @Override
    public List<Product> getAllProductsbyCategoryID(long categoryID) {
        return sessionFactory
                .getCurrentSession()
                .createQuery("from Product p " +
                             "where p.category.id=:categoryID " +
                             "order by p.id desc", Product.class)
                .setParameter("categoryID", categoryID)
                .getResultList();
    }

    @Override
    // getAllproductbyTxt
    public List<Product> searchProductsByName(String txt) {
        return sessionFactory
                .getCurrentSession()
                .createQuery("from Product p " +
                             "where p.name like :name " +
                             "order by p.id desc", Product.class)
                .setParameter("name", "%" + txt + "%")
                .getResultList();
    }

    @Override
    public Product getByID(long id) {
        return sessionFactory.getCurrentSession().get(Product.class, id);
    }

    @Override
    public List<Product> getListByPage(List<Product> list, int start, int end) {
//        List<Product> arr = new ArrayList<>();
//        for (int i = start; i < end; i++) {
//            arr.add(list.get(i));
//        }
        return sessionFactory
                .getCurrentSession()
                .createQuery("from Product p ", Product.class)
                .setFirstResult(start)
                .setMaxResults(end)
                .getResultList();
    }

    @Override
    // getAllproductbySellID
    public List<Product> getAllProductsByAccountID(long id) {
        return sessionFactory
                .getCurrentSession()
                .createQuery("from Product p " +
                             "where p.account.id=:id " +
                             "order by p.id desc", Product.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    // deleteProductBySellID
    public void deleteProductByAccountID(long id) {
        sessionFactory.getCurrentSession()
                .createQuery("delete from Product p where p.account.id=:id", Product.class)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public void deleteByID(long id) {
        sessionFactory.getCurrentSession()
                .createQuery("delete from Product p where p.id=:id", Product.class)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public void editProduct(String name, String image, double price,
                            String title, String description, long categoryId,
                            String model, String color, String delivery,
                            String image2, String image3, String image4, long id) throws Exception {
        Session session = sessionFactory.getCurrentSession();
        Product product = session.createQuery("from Product p where p.id=:id", Product.class)
                .setParameter("id", id).uniqueResult();
        Category category = session.createQuery("from Category c where c.id=:id", Category.class)
                .setParameter("id", categoryId).uniqueResult();
        if (product == null) {
            throw new Exception("There is no product has id " + id);
        }
        product = Product.builder()
                .name(name)
                .image(image)
                .price(price)
                .title(title)
                .description(description)
                .category(category)
                .model(model)
                .color(color)
                .delivery(delivery)
                .image2(image2)
                .image3(image3)
                .image4(image4)
                .build();
        session.merge(product);
    }

    @Override
    public void insertProduct(String name, String image, double price, String title,
                              String description, long categoryId, int accountId,
                              String model, String color, String delivery, String image2, String image3, String image4) {
        Session session = sessionFactory.getCurrentSession();
        Category category = session.createQuery("from Category c where c.id=:id", Category.class)
                .setParameter("id", categoryId).uniqueResult();
        Account account = session.createQuery("from Account where id=:id", Account.class)
                .setParameter("id", accountId).uniqueResult();
        Product product = Product.builder()
                .name(name)
                .image(image)
                .price(price)
                .title(title)
                .description(description)
                .category(category)
                .account(account)
                .model(model)
                .color(color)
                .delivery(delivery)
                .image2(image2)
                .image3(image3)
                .image4(image4)
                .build();
        session.merge(product);
    }

//    @Override
//    public List<Product> getProductBySellIDAndPageIndex(long id, int pageIndex) {
//        return null;
//    }

//    @Override
//    public List<Product> getProductByPageIndex(int pageIndex) {
//        return null;
//    }
}