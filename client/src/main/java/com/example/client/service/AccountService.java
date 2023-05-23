package com.example.client.service;

import com.example.client.component.AccountComponent;
import com.example.client.entity.AccountEntity;
import com.example.client.model.AccountModel;
import com.example.client.model.ResponseModel;
import com.example.client.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class AccountService {

    public AccountService(AccountRepository accountRepository, AccountComponent accountComponent) {
        this.accountRepository = accountRepository;
        this.accountComponent = accountComponent;
    }

    private final AccountRepository accountRepository;
    private final AccountComponent accountComponent;

    @Transactional()
    public ResponseModel<Void> insertAccountAndUpdateThenResponse(AccountModel accountModel) {
        ResponseModel<Void> result = new ResponseModel<>();
        result.setStatus(200);
        result.setDescription("ok");
        try {
            this.insertAndUpdateAccount(accountModel);
        } catch (Exception ex) {
            ex.printStackTrace();
            result.setStatus(500);
            result.setDescription(ex.getMessage());
        }

        return result;
    }

    public ResponseModel<AccountModel> findAccountIdThenResponse(Integer id){
        ResponseModel<AccountModel> result = new ResponseModel<>();
        result.setStatus(200);
        result.setDescription("ok");
        try {
            AccountModel accountModel = this.findAccountById(id);
            result.setData(accountModel);
        } catch (Exception ex){
            ex.printStackTrace();
            result.setStatus(500);
            result.setDescription(ex.getMessage());
        }
        return result;
    }

    public void insertAndUpdateAccount(AccountModel accountModel){
        AccountEntity accountEntity =  new AccountEntity();
        this.accountComponent.accountTransformModelToEntity(accountEntity, accountModel);
        this.accountRepository.save(accountEntity);
    }

    public AccountModel findAccountById(Integer id) {
        Optional<AccountEntity> accountEntityOptional = this.accountRepository.findById(id);
        if(accountEntityOptional.isPresent()){
            AccountModel accountModel = this.accountComponent.accountTransformEntityToModel(accountEntityOptional.get());
            return accountModel;
        }
        return null;
    }
}
