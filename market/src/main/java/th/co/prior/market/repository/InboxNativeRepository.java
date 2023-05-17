package th.co.prior.market.repository;

import th.co.prior.market.model.InboxModel;

import java.util.List;

public interface InboxNativeRepository {
    List<InboxModel> findInboxByInbox(InboxModel inboxModel);

    void updateToReadStatus(List<InboxModel> inboxModels);
}
