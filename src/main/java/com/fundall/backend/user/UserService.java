package com.fundall.backend.user;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fundall.backend.card.Card;
import com.fundall.backend.card.CardRepository;
import com.fundall.backend.card.CardRequest;
import com.fundall.backend.card.CardResponse;
import com.fundall.backend.merchant.Merchant;
import com.fundall.backend.merchant.MerchantRepository;
import com.fundall.backend.merchant.MerchantRequest;
import com.fundall.backend.merchant.MerchantResponse;
import com.fundall.backend.transaction.Transaction;
import com.fundall.backend.transaction.TransactionByMerchantResponse;
import com.fundall.backend.transaction.TransactionResponse;
import com.fundall.backend.transaction.TransactionType;
import com.fundall.backend.transaction.TransactionTypeSummaryResponse;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private MerchantRepository merchantRepository;

    private User findAUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("User not found with id: {}", userId);
                    return new EntityNotFoundException("User could not be found");
                });
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
        .orElseThrow(() -> {
                logger.error("User not found with email: {}", email);
                return new EntityNotFoundException("User could bot be found");
        });
    }

    public UserResponse makeACardRequest(Long userId, CardRequest request) {
        User user = findAUserById(userId);

        var card = Card.builder()
                .name(request.getName())
                .cardType(request.getCardType())
                .balance(BigDecimal.ZERO)
                .creditLimit(new BigDecimal("500000"))
                .user(user)
                .build();

        Card savedCard = cardRepository.save(card);

        List<Card> cards = user.getCards();
        cards.add(savedCard);
        user.setCards(cards);
        userRepository.save(user);
        List<Transaction> transactions = user.getTransactions();

        logger.info("Card request was made successfully");

        return UserResponse.builder()
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .phone(user.getPhone())
                .email(user.getEmail())
                .cards(cards)
                .transactions(transactions)
                .build();
    }

    public UserResponse getAUser(Long userId) {
        User user = findAUserById(userId);

        List<Card> cards = user.getCards();
        List<Transaction> transactions = user.getTransactions();

        logger.info("User response was retrieved successfully");

        return UserResponse.builder()
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .phone(user.getPhone())
                .email(user.getEmail())
                .cards(cards)
                .transactions(transactions)
                .build();
    }

    public CardResponse getACardRequest(Long userId, Long cardId) {
        User user = findAUserById(userId);

        Card card = user.getCards().stream()
                .filter(c -> c.getId().equals(cardId))
                .findFirst()
                .orElseThrow(EntityNotFoundException::new);

        logger.info("Card response was retrieved successfully");

        return CardResponse.builder()
                .name(card.getName())
                .balance(card.getBalance())
                .creditLimit(card.getCreditLimit())
                .cardType(card.getCardType())
                .transactions(card.getTransactions())
                .user(card.getUser())
                .build();
    }

    public TransactionResponse getAllTransactionsRequest(Long userId) {
        User user = findAUserById(userId);

        List<Transaction> transactions = user.getTransactions();

        logger.info("All transactions was retrieved successfully");

        return TransactionResponse.builder()
                .transactions(transactions)
                .build();
    }

    public TransactionByMerchantResponse getAllTransactionsByMerchant(Long userId) {
        User user = findAUserById(userId);

        Map<Merchant, List<Transaction>> transactionsByMerchant = user.getTransactions().stream()
                .collect(Collectors.groupingBy(Transaction::getMerchant));

        int numMerchants = transactionsByMerchant.keySet().size();

        logger.info("All transactions by merchants was etrieved successfully");

        return TransactionByMerchantResponse.builder()
                .transactionsByMerchant(transactionsByMerchant)
                .numMerchants(numMerchants)
                .build();
    }

    public TransactionTypeSummaryResponse getTransactionTypeSummary(Long userId) {
        User user = findAUserById(userId);

        Map<TransactionType, List<Transaction>> transactionsByType = user.getTransactions().stream()
                .collect(Collectors.groupingBy(Transaction::getType));

        double totalIncome = 0;
        double totalExpense = 0;

        if (transactionsByType.containsKey(TransactionType.INCOME)) {
            totalIncome = transactionsByType.get(TransactionType.INCOME).stream()
                    .mapToDouble(Transaction::getAmount)
                    .sum();
        }

        if (transactionsByType.containsKey(TransactionType.EXPENSE)) {
            totalExpense = transactionsByType.get(TransactionType.EXPENSE).stream()
                    .mapToDouble(Transaction::getAmount)
                    .sum();
        }

        double totalCardBalance = user.getCards().stream()
                .mapToDouble(card -> card.getBalance().doubleValue())
                .sum();

        return TransactionTypeSummaryResponse.builder()
                .transactionsByType(transactionsByType)
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .totalCardBalance(totalCardBalance)
                .build();
    }

    public MerchantResponse createAMerchantRequest(Long userId, MerchantRequest request) {
        User user = findAUserById(userId);

        var merchant = Merchant.builder()
                .name(request.getName())
                .user(user)
                .build();

        Merchant savedMerchant = merchantRepository.save(merchant);

        List<Merchant> merchants = user.getMerchants();
        merchants.add(savedMerchant);
        user.setMerchants(merchants);
        userRepository.save(user);
        List<Transaction> transactions = merchant.getTransactions();

        // logger.info("Card request was made successfully");

        return MerchantResponse.builder()
                .name(merchant.getName())
                .user(user)
                .transactions(transactions)
                .build();
    }

}
