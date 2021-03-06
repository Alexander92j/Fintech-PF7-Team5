package gr.codelearn.service.impl;

import gr.codelearn.domain.Account;
import gr.codelearn.service.AccountService;
import gr.codelearn.service.PostingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class PostingServiceImpl implements PostingService {

    private AccountService accountService;

    public Map<String, Object> makeTransaction(Map<String, Object> payload) {
        log.info("Performing transaction.");

        // preparing fee
        String feeAmountStr = (String) payload.get("feeAmount");
        BigDecimal feeAmount = new BigDecimal(feeAmountStr);

        // 1) Subtract fee from debtor
        log.info("Subtracting fee from debtor.");
        String debtorIBAN = (String) payload.get("debtorIBAN");
        // supposedly we have already checked if they exist
        Optional<Account> debtorOptional = accountService.findByIban(debtorIBAN);
        Account debtor = debtorOptional.get();
        BigDecimal balanceAfterFeeSubtraction = debtor.getBalance().subtract(feeAmount);
        debtor.setBalance(balanceAfterFeeSubtraction);
        //accountService.save(debtor);

        // 2) Add fee to bank
        log.info("Adding fee to bank.");
        Optional<Account> bankOptional = accountService.findByIban("GR00000000000001");
        if (bankOptional.isPresent()) {
            Account bank = bankOptional.get();
            BigDecimal balanceAfterFeeAddition = bank.getBalance().add(feeAmount);
            bank.setBalance(balanceAfterFeeAddition);
            accountService.save(bank);
        }

        // preparing amount
        String paymentAmountStr = (String) payload.get("paymentAmount");
        BigDecimal paymentAmount = new BigDecimal(paymentAmountStr);

        // 3) Subtract amount from debtor
        log.info("Subtracting amount from debtor.");
        BigDecimal balanceAfterAmountSubtraction = debtor.getBalance().subtract(paymentAmount);
        debtor.setBalance(balanceAfterAmountSubtraction);
        accountService.save(debtor);

        // 4) Add amount to creditor
        log.info("Adding amount to creditor.");
        String creditorIBAN = (String) payload.get("creditorIBAN");
        // supposedly we have already checked if they exist
        Optional<Account> creditorOptional = accountService.findByIban(creditorIBAN);
        Account creditor = creditorOptional.get();
        BigDecimal balanceAfterAmountAddition = creditor.getBalance().add(paymentAmount);
        creditor.setBalance(balanceAfterAmountAddition);
        accountService.save(creditor);

        log.info("Performing wallet transaction has finished successfully.");
        payload.put("transactionComplete", Boolean.TRUE);
        return payload;
    }

    @Override
    public Map<String, Object> makeWalletTransaction(Map<String, Object> payload) {
        log.info("Performing wallet transaction.");
        // preparing fee
        String feeAmountStr = (String) payload.get("feeAmount");
        BigDecimal feeAmount = new BigDecimal(feeAmountStr);

        // 1) Subtract fee from debtor
        log.info("Subtracting fee from debtor.");
        String debtorIBAN = (String) payload.get("debtorIBAN");
        // supposedly we have already checked if they exist
        Optional<Account> debtorOptional = accountService.findByIban(debtorIBAN);
        Account debtor = debtorOptional.get();
        BigDecimal debtorBalanceAfterFeeSubtraction = debtor.getBalance().subtract(feeAmount.divide(BigDecimal.valueOf(2)));
        debtor.setBalance(debtorBalanceAfterFeeSubtraction.setScale(2));
        //accountService.save(debtor);

        // 2) Subtract fee from creditor
        log.info("Subtracting fee from creditor.");
        String creditorIBAN = (String) payload.get("creditorIBAN");
        // supposedly we have already checked if they exist
        Optional<Account> creditorOptional = accountService.findByIban(creditorIBAN);
        Account creditor = creditorOptional.get();
        BigDecimal creditorBalanceAfterFeeSubtraction = creditor.getBalance().subtract(feeAmount.divide(BigDecimal.valueOf(2)));
        creditor.setBalance(creditorBalanceAfterFeeSubtraction.setScale(2));
        //accountService.save(debtor);

        // 3) Add fee to bank
        log.info("Adding fee to bank.");
        Optional<Account> bankOptional = accountService.findByIban("GR00000000000001");
        if (bankOptional.isPresent()) {
            Account bank = bankOptional.get();
            BigDecimal balanceAfterFeeAddition = bank.getBalance().add(feeAmount);
            bank.setBalance(balanceAfterFeeAddition);
            accountService.save(bank);
        }

        // preparing amount
        String paymentAmountStr = (String) payload.get("paymentAmount");
        BigDecimal paymentAmount = new BigDecimal(paymentAmountStr);

        // 4) Subtract amount from debtor
        log.info("Subtracting amount from debtor.");
        BigDecimal balanceAfterAmountSubtraction = debtor.getBalance().subtract(paymentAmount);
        debtor.setBalance(balanceAfterAmountSubtraction);
        accountService.save(debtor);

        // 5) Add amount to creditor
        log.info("Adding amount to creditor.");
        BigDecimal balanceAfterAmountAddition = creditor.getBalance().add(paymentAmount);
        creditor.setBalance(balanceAfterAmountAddition);
        accountService.save(creditor);


        log.info("Performing wallet transaction has finished successfully.");
        payload.put("transactionComplete", Boolean.TRUE);
        return payload;
    }
}
