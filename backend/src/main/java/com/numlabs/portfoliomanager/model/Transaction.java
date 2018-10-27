package com.numlabs.portfoliomanager.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;

    @Column(name="source_account")
    private Account sourceAccount;

    @Column(name="dest_account")
    private Account destAccount;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column
    private BigDecimal amount;

    @Column(name="transaction_date")
    private Date transactionDate;

    @Column
    private BigDecimal commision;
}
