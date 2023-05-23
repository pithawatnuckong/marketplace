package com.example.client.controller.rest;

import com.example.client.model.InboxModel;
import com.example.client.model.ResponseModel;
import com.example.client.service.InboxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
