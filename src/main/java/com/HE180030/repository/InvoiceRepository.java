package com.HE180030.repository;

import com.HE180030.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    //    void insert(int accountID, double totalPrice, String context, int phone, String delivery, String name, String typePay);
//
//    List<Invoice> getAllInvoiceByID(int id);
    List<Invoice> findByAccountId(int accountId);
}
