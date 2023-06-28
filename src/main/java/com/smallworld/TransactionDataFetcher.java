package com.smallworld;

import com.smallworld.data.Transaction;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TransactionDataFetcher {

    private List<Transaction> transactions;

    public TransactionDataFetcher(List<Transaction> transactions) {
        //assigning mapped data to transactions reference instance
        this.transactions = transactions;
    }

    /**
     * Returns the sum of the amounts of all transactions
     */
    public double getTotalTransactionAmount() {
        //streaming over transaction, grouping data based on unique mtn ids. Streaming over grouped values to get total unique transaction amount
        return transactions.stream().collect(Collectors.groupingBy(Transaction::getMtn)).values().stream().mapToDouble(transaction -> transaction.get(0).getAmount()).sum();
    }

    /**
     * Returns the sum of the amounts of all transactions sent by the specified client
     */
    public double getTotalTransactionAmountSentBy(String senderFullName) {
        //streaming over transaction, grouping data based on unique mtn ids. Streaming over grouped values to get total amount sent by a sender
        return transactions.stream().collect(Collectors.groupingBy(Transaction::getMtn)).values().stream().filter(transaction -> transaction.get(0).getSenderFullName().equalsIgnoreCase(senderFullName)).mapToDouble(transaction1 -> transaction1.get(0).getAmount()).sum();
    }

    /**
     * Returns the highest transaction amount
     */
    public double getMaxTransactionAmount() {
        //streaming over transaction, grouping data based on unique mtn ids. Streaming over grouped values to get total max transaction amount
        return transactions.stream().collect(Collectors.groupingBy(Transaction::getMtn)).values().stream().mapToDouble(transaction -> transaction.get(0).getAmount()).max().getAsDouble();
    }

    /**
     * Counts the number of unique clients that sent or received a transaction
     */
    public long countUniqueClients() {
        //streaming over transaction, mapping distinct sender`s full name
        return transactions.stream().flatMap(transaction -> Stream.of(transaction.getSenderFullName(), transaction.getBeneficiaryFullName())).distinct().count();
    }

    /**
     * Returns whether a client (sender or beneficiary) has at least one transaction with a compliance
     * issue that has not been solved
     */
    public boolean hasOpenComplianceIssues(String clientFullName) {
        //streaming over transaction, filtering based on sender and beneficiary name having non-null issue id and unsolved issue
        return transactions.stream().filter(transaction -> transaction.getSenderFullName().equalsIgnoreCase(clientFullName) || transaction.getBeneficiaryFullName().equalsIgnoreCase(clientFullName))
                .anyMatch(transaction -> transaction.getIssueId()!=null && !transaction.getIssueSolved());
    }

    /**
     * Returns all transactions indexed by beneficiary name
     */
    public Map<String, Set<Transaction>> getTransactionsByBeneficiaryName() {
        //streaming over transaction, grouping transaction by beneficiary name adding them in to set in order to avoid redundancy
        return transactions.stream().collect(Collectors.groupingBy(Transaction::getBeneficiaryFullName, Collectors.toSet()));
    }

    /**
     * Returns the identifiers of all open compliance issues
     */
    public Set<Integer> getUnsolvedIssueIds() {
        //streaming over transaction, filtering unsolved issue transactions collecting their ids in to set to avoid redundancy
        return transactions.stream().filter(transaction -> transaction.getIssueSolved() == false).map(Transaction::getIssueId).filter(issueId -> issueId!=null).collect(Collectors.toSet());
    }

    /**
     * Returns a list of all solved issue messages
     */
    public List<String> getAllSolvedIssueMessages() {
        //streaming over transaction, filtering solved issue transactions collecting their messages in to set to avoid redundancy
        return transactions.stream().filter(transaction -> transaction.getIssueSolved() == true).map(Transaction::getIssueMessage).filter(message -> message!=null).collect(Collectors.toList());
    }

    /**
     * Returns the 3 transactions with the highest amount sorted by amount descending
     */
    public List<Transaction> getTop3TransactionsByAmount() {
        //streaming over transaction, grouping data based on unique mtn ids. Streaming over grouped values to get top 3 transaction amounts
        return transactions.stream().collect(Collectors.groupingBy(Transaction::getMtn)).values().stream().map(transaction1 -> transaction1.get(0)).sorted(Comparator.comparingDouble(Transaction::getAmount).reversed()).limit(3).collect(Collectors.toList());
    }

    /**
     * Returns the senderFullName of the sender with the most total sent amount
     */
    public Optional<String> getTopSender() {
        //streaming over transaction, grouping data based on unique mtn ids. Streaming over grouped values to get top sender of transactions based on amount
        return transactions.stream().collect(Collectors.groupingBy(Transaction::getMtn)).values().stream().map(transactions1 -> transactions1.get(0)).collect(Collectors.toMap(Transaction::getSenderFullName, Transaction::getAmount, Double::sum)).entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey);
    }

}
