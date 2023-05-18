package com.example.client.component;

import com.example.client.entity.InboxEntity;
import com.example.client.model.InboxModel;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class InboxComponent {

    public void transformInboxModelToEntity(InboxModel inboxModel, InboxEntity inboxEntity){
        inboxEntity.setStatus(false);
        inboxEntity.setCreateDate(LocalDateTime.now());
        inboxEntity.setMessage(inboxModel.getMessage());
        inboxEntity.setFromAccountId(inboxModel.getFromAccountId());
        inboxEntity.setToAccountId(inboxModel.getToAccountId());
    }
}
