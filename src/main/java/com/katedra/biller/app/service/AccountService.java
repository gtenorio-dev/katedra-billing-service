package com.katedra.biller.app.service;

import com.katedra.biller.app.payload.AccountPayload;
import com.katedra.biller.app.entity.AccountEntity;
import com.katedra.biller.app.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final SessionService sessionService;

    public AccountService(AccountRepository accountRepository, SessionService sessionService) {
        this.accountRepository = accountRepository;
        this.sessionService = sessionService;
    }

    public void createAccount(AccountPayload accountPayload) {
        AccountEntity account = new AccountEntity();
        account.setCuit(accountPayload.getCuit());
        account.setPuntoVenta(accountPayload.getPuntoVenta());
        account.setTipoFactura(accountPayload.getTipoFactura());
        account.setConcepto(accountPayload.getConcepto());
        account.setRazonSocial(accountPayload.getRazonSocial());
        account.setInicioActividad(accountPayload.getInicioActividad());
        account.setCondicionDeVenta(accountPayload.getCondicionDeVenta());
        account.setCondicionFrenteAlIva(accountPayload.getCondicionFrenteAlIva());
        account.setActivo(accountPayload.getActivo());
        account = accountRepository.save(account);
        sessionService.createSession(account, accountPayload);
    }

    public List<AccountEntity> findAll() {
        return (List<AccountEntity>) accountRepository.findAll();
    }

    public AccountEntity findByCuit(Long cuit) {
        return accountRepository.findByCuit(cuit);
    }

}
