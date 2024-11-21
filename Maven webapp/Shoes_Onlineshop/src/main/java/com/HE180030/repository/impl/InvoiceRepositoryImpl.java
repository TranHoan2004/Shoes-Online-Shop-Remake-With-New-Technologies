package com.HE180030.repository.impl;

import com.HE180030.model.Invoice;
import com.HE180030.repository.InvoiceRepository;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(propagation = Propagation.REQUIRED)
@DependsOn("sessionFactory")
@Repository
public class InvoiceRepositoryImpl implements InvoiceRepository {
    private final SessionFactory sessionFactory;

    public InvoiceRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void deleteByAccountID(long id) {
        sessionFactory.getCurrentSession().createQuery("delete from Invoice i where i.account.id=:id", Invoice.class)
                .setParameter("id", id)
                .executeUpdate();
    }

//    @Override
//    public void insertInvoice(long accountID, double totalPrice, String context, int phone, String delivery, String name, String typePay) {
//
//    }

//    @Override
//    public List<Invoice> getByID(long accountID) {
//        return null;
//    }

    @Override
    public List<Invoice> getAllInvoiceByID(long id) {
        return sessionFactory
                .getCurrentSession()
                .createQuery("from Invoice i where i.account.id=:id", Invoice.class)
                .setParameter("id", id)
                .getResultList();
    }
}
