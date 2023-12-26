package th.co.prior.market.controller.rest;

import org.springframework.web.bind.annotation.*;
import th.co.prior.market.model.AccountModel;
import th.co.prior.market.model.ResponseModel;
import th.co.prior.market.service.AccountService;

@RestController
@RequestMapping("/api")
public class AccountRestController {

    public AccountRestController(AccountService accountService) {
        this.accountService = accountService;
    }

    private final AccountService accountService;

    @PostMapping("/account")
    public ResponseModel<Void> insertAndUpdateAccount(@RequestBody AccountModel accountModel) {
        return this.accountService.insertAccountAndUpdateThenResponse(accountModel);
    }

    @GetMapping("/account/{id}")
    public ResponseModel<AccountModel> findAccountById(@PathVariable("id") int id) {
        return this.accountService.findAccountIdThenResponse(id);
    }
}
