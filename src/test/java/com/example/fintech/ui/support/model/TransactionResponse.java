package com.example.fintech.ui.support.model;

import java.math.BigDecimal;

public record TransactionResponse(
    String transactionId,
    String fromAccountId,
    String toAccountId,
    BigDecimal amount,
    String status
) {}
