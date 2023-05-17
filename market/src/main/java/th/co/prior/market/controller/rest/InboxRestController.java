package th.co.prior.market.controller.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import th.co.prior.market.model.InboxModel;
import th.co.prior.market.model.ResponseModel;
import th.co.prior.market.service.InboxService;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class InboxRestController {

    public InboxRestController(InboxService inboxService) {
        this.inboxService = inboxService;
    }

    private final InboxService inboxService;


    @PostMapping("/inbox")
    public ResponseModel<Void> insertInbox(@RequestBody InboxModel inboxModel){
        return this.inboxService.insertInboxThenResponse(inboxModel);
    }

    @PostMapping("/find/inbox")
    public ResponseModel<List<InboxModel>> findInboxByInboxMode(@RequestBody InboxModel inboxModel){
        return this.inboxService.findInboxByInboxModelThenResponse(inboxModel);
    }
}
