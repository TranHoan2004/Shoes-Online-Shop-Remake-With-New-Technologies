package com.HE180030.controller;

import com.HE180030.enumerate.CartStatus;
import com.HE180030.dto.request.DeleteRequest;
import com.HE180030.dto.response.ApiResponse;
import com.HE180030.dto.response.CartResponse;
import com.HE180030.dto.response.ProductResponse;
import com.HE180030.security.utils.SecurityUtils;
import com.HE180030.service.AccountService;
import com.HE180030.service.CartService;
import com.HE180030.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/cart")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CartController {
    ProductService pSrv;
    CartService cSrv;
    AccountService aSrv;
    Logger logger = LoggerFactory.getLogger(CartController.class);

    @GetMapping("/c")
    public ResponseEntity<?> getCartByUser() {
        logger.info("getCartByUser");
        int id = aSrv.getIDByEmail(SecurityUtils.getCurrentUser().getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.builder()
                        .code(HttpStatus.OK.value())
                        .message(HttpStatus.OK.getReasonPhrase())
                        .data(cSrv.getCartByAccountID(id))
        );
    }

    @GetMapping("/manage")
    public ResponseEntity<?> manageCart() {
        logger.info("manageCart");
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.builder()
                        .code(HttpStatus.OK.value())
                        .message(HttpStatus.OK.getReasonPhrase())
                        .data(displayContent(0.1))
        );
    }

    /*Tested*/
    @GetMapping("/amount")
    public ResponseEntity<?> loadAmountCart() {
        logger.info("loadAmountCart");
        int accountID = aSrv.getIDByEmail(SecurityUtils.getCurrentUser().getUsername());
        List<CartResponse> list = cSrv.getCartByAccountID(accountID);
        int totalAmountCart = list.size();
        return ResponseEntity.ok(ApiResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Total amount of cart is: " + totalAmountCart)
                .data(totalAmountCart)
                .build()
        );
    }

    @PostMapping("/add_to_cart")
    public ResponseEntity<?> addToCart(
            @RequestBody int productID,
            @RequestBody int size) {
        logger.info("addToCart");
        int accountID = aSrv.getIDByEmail(SecurityUtils.getCurrentUser().getUsername());
        String message;

        CartResponse existedCart = cSrv.getCartByAccountIDAndProductID(accountID, productID, size);
        if (existedCart != null) {
            int existedAmount = existedCart.getAmount();
            cSrv.createAmountAndSize(
                    accountID,
                    productID,
                    existedAmount > 5 ? (existedAmount + 1) : (existedAmount + 2),
                    size
            );
            message = "Number of products increase successfully";
        } else {
            cSrv.create(accountID, productID, 2, size);
            message = "Added product into cart";
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.builder()
                        .code(HttpStatus.CONTINUE.value())
                        .message("Redirect to manage cart")
                        .data(message)
        );
    }

    @GetMapping("")
    public ResponseEntity<?> getCart() {
        logger.info("getCart");
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.builder()
                        .code(HttpStatus.OK.value())
                        .message(HttpStatus.OK.getReasonPhrase())
                        .data(Map.of(
                                "content", displayContent(1.1),
                                "status", CartStatus.class.getEnumConstants()
                        ))
        );
    }

    @PatchMapping("/change_amount")
    public ResponseEntity<?> changeAmountCart(
            @RequestBody int productID,
            @RequestBody int amount,
            @RequestBody CartStatus status) {
        logger.info("changeAmountCart");
        int accountID = aSrv.getIDByEmail(SecurityUtils.getCurrentUser().getUsername());
        String message = "";
        if (status == CartStatus.Add) {
            amount++;
            message = "Increased amount!";
        } else if (status == CartStatus.Sub) {
            amount--;
            message = "Decreased amount!";
        }
        cSrv.updateAmountCart(accountID, productID, amount);
        return ResponseEntity.status(HttpStatus.CONTINUE).body(
                ApiResponse.builder()
                        .code(HttpStatus.CONTINUE.value())
                        .message(message)
        );
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCartByAccount(
            @RequestBody DeleteRequest request) {
        logger.info("delete");
        int id = aSrv.getIDByEmail(SecurityUtils.getCurrentUser().getUsername());
        switch (request.getCode()) {
            case 100 -> {
                logger.info("delete cart by account");
                cSrv.deleteCartByAccountID(id);
            }
            case 101 -> {
                logger.info("delete cart by product");
                cSrv.deleteCartByProductID(request.getId());
            }
            case 102 -> {
                logger.info("delete cart");
                cSrv.deleteCart(request.getId());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponse.builder()
                        .code(HttpStatus.OK.value())
                        .message("Delete successfully!")
        );
    }

    @GetMapping("/total_money")
    public ResponseEntity<?> totalMoneyCart() {
        logger.info("totalMoneyCart");
        int id = aSrv.getIDByEmail(SecurityUtils.getCurrentUser().getUsername());
        List<CartResponse> carts = cSrv.getCartByAccountID(id);
        List<ProductResponse> products = pSrv.getAllProducts();
        double totalMoney = 0;
        for (CartResponse c : carts) {
            for (ProductResponse p : products) {
                if (Objects.equals(c.getProductId(), p.getId())) {
                    totalMoney = totalMoney + p.getPrice() * c.getAmount();
                }
            }
        }

        double totalMoneyVAT = totalMoney * 1.1;
        totalMoneyVAT = Math.round(totalMoneyVAT);
        return ResponseEntity.status(HttpStatus.CONTINUE).body(
                ApiResponse.builder()
                        .code(HttpStatus.CONTINUE.value())
                        .message(
                                "Total amount of VAT is: " + totalMoneyVAT +
                                        "\nTotal amount of money is: " + totalMoney
                        )
                        .data(Map.of(
                                "VAT", totalMoneyVAT,
                                "total money", totalMoneyVAT
                        ))
        );
    }

    private @NotNull @Unmodifiable Map<String, Object> displayContent(double number) {
        AtomicReference<Double> totalMoney = new AtomicReference<>((double) 0);
        int id = aSrv.getIDByEmail(SecurityUtils.getCurrentUser().getUsername());
        List<CartResponse> carts = cSrv.getCartByAccountID(id);
        List<ProductResponse> products = pSrv.getAllProducts();
        carts.forEach(cartDTO -> products.forEach(productDTO -> {
            if (Objects.equals(cartDTO.getProductId(), productDTO.getId())) {
                totalMoney.updateAndGet(v -> (v + (productDTO.getPrice() * cartDTO.getAmount())));
            }
        }));
        double totalMoneyVAT = totalMoney.get() * number;
        return Map.of(
                "total money", totalMoney.get(),
                "total money VAT", totalMoneyVAT,
                "list of carts", carts,
                "list of products", products
        );
    }
}
