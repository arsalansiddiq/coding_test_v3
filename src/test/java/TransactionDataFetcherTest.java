import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smallworld.TransactionDataFetcher;

import com.smallworld.data.Transaction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import java.io.File;
import java.io.IOException;
import java.util.*;

public class TransactionDataFetcherTest {

    private static TransactionDataFetcher transactionDataFetcher;

    @BeforeAll
    static void setup() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Transaction> transactions = objectMapper.readValue(new File("transactions.json"), new TypeReference<List<Transaction>>() {
            });
            transactionDataFetcher = new TransactionDataFetcher(transactions);
        } catch (IOException e) {
            System.out.println("Exception occurred while loading transaction data " + e);
        }
    }

    @Test
    void getTotalTransactionAmount() {
        double expectedValue = 2889.17;
        double actualValue = transactionDataFetcher.getTotalTransactionAmount();
        assertEquals(expectedValue, actualValue);
    }

    @Test
    void getTotalTransactionAmountSentBy() {
        double expectedValue = 678.06;
        double actualValue = transactionDataFetcher.getTotalTransactionAmountSentBy("Tom Shelby");
        assertEquals(expectedValue, actualValue);
    }

    @Test
    void getMaxTransactionAmount() {
        double expectedValue = 985.0;
        double actualValue = transactionDataFetcher.getMaxTransactionAmount();
        assertEquals(expectedValue, actualValue);
    }


    @Test
    void countUniqueClients() {
        int expectedValue = 14;
        long actualValue = transactionDataFetcher.countUniqueClients();
        assertEquals(expectedValue, actualValue);
    }


    @Test
    void hasOpenComplianceIssues() {
        boolean expected = true;
        boolean actualValue = transactionDataFetcher.hasOpenComplianceIssues("Arthur Shelby");
        assertEquals(expected, actualValue);
    }

    @Test
    void getTransactionsByBeneficiaryName() {
        Map<String, Set<Transaction>> expectedValues = new HashMap<>();
//        expectedValues.put("Luca Changretta", new HashSet<>().add(new Transaction()))
        Map<String, Set<Transaction>> actualValue = transactionDataFetcher.getTransactionsByBeneficiaryName();
        assertEquals(expectedValues, actualValue);
    }

    @Test
    void getUnsolvedIssueIds() {
        Set<Integer> expectedValues = new HashSet<>();
        expectedValues.add(1);
        expectedValues.add(3);
        expectedValues.add(15);
        expectedValues.add(54);
        expectedValues.add(99);
        Set<Integer> actualValue = transactionDataFetcher.getUnsolvedIssueIds();
        assertEquals(expectedValues, actualValue);
    }

    @Test
    void getAllSolvedIssueMessages() {
        List<String> expectedValues = new ArrayList<>();
        expectedValues.add("Never gonna give you up");
        expectedValues.add("Never gonna let you down");
        expectedValues.add("Never gonna run around and desert you");
        List<String> actualValue = transactionDataFetcher.getAllSolvedIssueMessages();
        assertEquals(expectedValues, actualValue);
    }

    @Test
    void getTop3TransactionsByAmount() {
        List<Transaction> expectedValues = new ArrayList<>();
        expectedValues.add(new Transaction(5465465, 985.0, "Arthur Shelby", 60, "Ben Younger", 47, 15, false, "Something's fishy"));
        expectedValues.add(new Transaction(32612651, 666.0, "Grace Burgess", 31, "Michael Gray", 58, 54, false, "Something ain't right"));
        expectedValues.add(new Transaction(663458, 430.2, "Tom Shelby", 22, "Alfie Solomons", 33, 1, false, "Looks like money laundering"));
        List<Transaction> actualValue = transactionDataFetcher.getTop3TransactionsByAmount();
        assertEquals(expectedValues, actualValue);
    }

    @Test
    void getTopSender() {
        String expectedValue = "Arthur Shelby";
        Optional<String> actualValue = transactionDataFetcher.getTopSender();
        assertEquals(expectedValue, actualValue.get());
    }
}