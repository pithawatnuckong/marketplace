package com.example.marketplace.component;

import com.example.marketplace.entity.AccountEntity;
import com.example.marketplace.model.AccountModel;
import org.springframework.stereotype.Component;

@Component
public class AccountComponent {

    public AccountModel accountTransformEntityToModel(AccountEntity accountEntity){
        AccountModel accountModel = new AccountModel();
        accountModel.setAccountDate(accountEntity.getAccountDate());
        accountModel.setId(accountEntity.getId());
        accountModel.setName(accountEntity.getName());
        accountModel.setCashAmount(accountEntity.getCashAmount());
        return accountModel;
    }
}
