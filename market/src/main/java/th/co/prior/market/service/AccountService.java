package th.co.prior.market.service;

import org.springframework.stereotype.Service;
import th.co.prior.market.component.AccountComponent;
import th.co.prior.market.entity.AccountEntity;
import th.co.prior.market.model.AccountModel;
import th.co.prior.market.model.ResponseModel;
import th.co.prior.market.repository.AccountRepository;

import java.util.Optional;

@Service
public class AccountService {

    public AccountService(AccountRepository accountRepository, AccountComponent accountComponent) {
        this.accountRepository = accountRepository;
        this.accountComponent = accountComponent;
    }

    private AccountRepository accountRepository;
    private AccountComponent accountComponent;

    public ResponseModel<Void> insertAccountAndUpdateThenResponse(AccountModel accountModel) {
        ResponseModel<Void> result = new ResponseModel<>();
        result.setStatus(200);
        result.setDescription("ok");
        try {
            AccountEntity accountEntity =  new AccountEntity();
            this.accountComponent.accountTransformModelToEntity(accountEntity, accountModel);
            this.insertAndUpdateAccount(accountEntity);
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

    private void insertAndUpdateAccount(AccountEntity accountEntity){
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
