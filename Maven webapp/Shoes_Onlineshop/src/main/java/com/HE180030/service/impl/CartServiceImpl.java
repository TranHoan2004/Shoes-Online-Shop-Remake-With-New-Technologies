package com.HE180030.service.impl;

import com.HE180030.dto.CartDTO;
import com.HE180030.repository.CartRepository;
import com.HE180030.service.CartService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;

    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public void create(int accountID, int productID, int amount, String size) {

    }

    @Override
    public void updateAmountCartDTO(int accountID, int productID, int amount) {

    }

    @Override
    public void deleteCartDTOByProductID(int productID) {

    }

    @Override
    public void deleteCartDTOByAccountID(int id) {

    }

    @Override
    public List<CartDTO> getCartDTOByAccountID(int accountID) {
        return null;
    }

    @Override
    public CartDTO getCartDTOByAccountIDAndProductID(int accountID, int productID, String size) {
        return null;
    }

    @Override
    public void createAmountAndSize(int accountID, int productID, int amount, String size) {

    }

    @Override
    public void editAmountAndSize(int accountID, int productID, int amount, String size) {

    }

    @Override
    public void editAmount(int accountID, int productID, int amount) {

    }
}
