package com.katedra.biller.app.service;

import com.katedra.biller.app.entity.AccountEntity;
import com.katedra.biller.app.model.TicketAccess;
import com.katedra.biller.app.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public void createAccount(AccountEntity account) {
        accountRepository.save(account);
    }

    public List<AccountEntity> findAll() {
        return (List<AccountEntity>) accountRepository.findAll();
    }

    public AccountEntity findByCuit(Long cuit) {
        return accountRepository.findByCuit(cuit);
    }

    public AccountEntity updateSession(AccountEntity account, TicketAccess ta) {
        account.setToken(ta.getToken());
        account.setSign(ta.getSign());
        account.setExpirationTime(ta.getExpirationTime());
        return accountRepository.save(account);
    }

}
