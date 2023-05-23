package com.example.client.component;

import com.example.client.entity.AccountEntity;
import com.example.client.model.AccountModel;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AccountComponent {

    public void accountTransformModelToEntity(AccountEntity accountEntity, AccountModel accountModel) {
        double cashAmount = 0.0;
        if(accountModel.getCashAmount() != null) {
            cashAmount = accountModel.getCashAmount() > 0.0 ? accountModel.getCashAmount() : 0.0;
        }
        accountEntity.setId(accountModel.getId());
        accountEntity.setAccountDate(LocalDateTime.now());
        accountEntity.setName(accountModel.getName());
        accountEntity.setCashAmount(cashAmount);
    }

    public AccountModel accountTransformEntityToModel(AccountEntity accountEntity){
        AccountModel accountModel = new AccountModel();
        accountModel.setAccountDate(accountEntity.getAccountDate());
        accountModel.setId(accountEntity.getId());
        accountModel.setName(accountEntity.getName());
        accountModel.setCashAmount(accountEntity.getCashAmount());
        return accountModel;
    }
}
