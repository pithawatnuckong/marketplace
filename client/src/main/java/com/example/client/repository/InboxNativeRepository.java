package com.example.client.repository;


import com.example.client.model.InboxModel;

import java.util.List;

public interface InboxNativeRepository {
    List<InboxModel> findInboxByInbox(InboxModel inboxModel);

    void updateToReadStatus(List<InboxModel> inboxModels);
}
