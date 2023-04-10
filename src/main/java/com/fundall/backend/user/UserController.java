package com.fundall.backend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fundall.backend.card.CardRequest;
import com.fundall.backend.card.CardResponse;
import com.fundall.backend.interceptors.ApiResponse;
import com.fundall.backend.merchant.MerchantRequest;
import com.fundall.backend.merchant.MerchantResponse;
import com.fundall.backend.transaction.TransactionByMerchantResponse;
import com.fundall.backend.transaction.TransactionResponse;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> getAUser(@PathVariable Long userId) {
        try {
            UserResponse data = userService.getAUser(userId);
            ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                    .success(true)
                    .message("User retrieved successful")
                    .data(data)
                    .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                    .success(false)
                    .message("Something went wrong retrieving user")
                    .build();

            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/{userId}/cards")
    public ResponseEntity<ApiResponse<UserResponse>> makeACardRequest(@PathVariable Long userId,
            @RequestBody CardRequest request) {
        try {
            UserResponse data = userService.makeACardRequest(userId, request);
            ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                    .success(true)
                    .message("Card successful requested")
                    .data(data)
                    .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                    .success(false)
                    .message("Something went wrong requesting a card")
                    .build();

            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/{userId}/cards/{cardId}")
    public ResponseEntity<ApiResponse<CardResponse>> getACardRequest(@PathVariable Long userId,
            @PathVariable Long cardId) {
        try {
            CardResponse data = userService.getACardRequest(userId, cardId);
            ApiResponse<CardResponse> response = ApiResponse.<CardResponse>builder()
                    .success(true)
                    .message("Card successful retrieved")
                    .data(data)
                    .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<CardResponse> response = ApiResponse.<CardResponse>builder()
                    .success(false)
                    .message("Something went wrong getting a card")
                    .build();

            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/{userId}/transactions")
    public ResponseEntity<ApiResponse<TransactionResponse>> getAllTransactionsRequest(@PathVariable Long userId) {
        try {
            TransactionResponse data = userService.getAllTransactionsRequest(userId);
            ApiResponse<TransactionResponse> response = ApiResponse.<TransactionResponse>builder()
                    .success(true)
                    .message("Transactions successful retrieved")
                    .data(data)
                    .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<TransactionResponse> response = ApiResponse.<TransactionResponse>builder()
                    .success(false)
                    .message("Something went wrong getting all transactions")
                    .build();

            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/{userId}/transactions/merchant")
    public ResponseEntity<ApiResponse<TransactionByMerchantResponse>> getAllTransactionsByMerchant(
            @PathVariable Long userId) {
        try {
            TransactionByMerchantResponse data = userService.getAllTransactionsByMerchant(userId);
            ApiResponse<TransactionByMerchantResponse> response = ApiResponse.<TransactionByMerchantResponse>builder()
                    .success(true)
                    .message("Transactions by merchant successful retrieved")
                    .data(data)
                    .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<TransactionByMerchantResponse> response = ApiResponse.<TransactionByMerchantResponse>builder()
                    .success(false)
                    .message("Something went wrong getting all transactions by merchant")
                    .build();

            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/{userId}/merchants")
    public ResponseEntity<ApiResponse<MerchantResponse>> createAMerchantRequest(@PathVariable Long userId,
            @RequestBody MerchantRequest request) {
        try {
            MerchantResponse data = userService.createAMerchantRequest(userId, request);

            ApiResponse<MerchantResponse> response = ApiResponse.<MerchantResponse>builder()
                    .success(true)
                    .message("Merchant created successful")
                    .data(data)
                    .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<MerchantResponse> response = ApiResponse.<MerchantResponse>builder()
                    .success(false)
                    .message("Something went wrong getting all transactions by merchant")
                    .build();

            return ResponseEntity.ok(response);// TODO: handle exception
        }
    }
}
