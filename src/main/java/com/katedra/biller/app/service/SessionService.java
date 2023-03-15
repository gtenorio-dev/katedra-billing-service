package com.katedra.biller.app.service;

import com.katedra.biller.app.payload.AccountPayload;
import com.katedra.biller.app.entity.AccountEntity;
import com.katedra.biller.app.entity.SessionEntity;
import com.katedra.biller.app.repository.SessionRepository;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    public SessionService(SessionRepository sessionRepository){
        this.sessionRepository = sessionRepository;
    }

    public void createSession(AccountEntity account, AccountPayload accountPayload) {
        SessionEntity session = new SessionEntity();
        session.setAccount(account);
        session.setCertName(accountPayload.getCertName());
        session.setCertSigner(accountPayload.getCertSigner());
        session.setCertPassword(accountPayload.getCertPassword());
        sessionRepository.save(session);
    }

    public SessionEntity getSession(Long cuit) {
        return sessionRepository.findSessionByCuit(cuit);
    }

    public void updateSession(SessionEntity session) {
        sessionRepository.save(session);
    }

}
