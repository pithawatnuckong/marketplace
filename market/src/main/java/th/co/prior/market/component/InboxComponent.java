package th.co.prior.market.component;

import org.springframework.stereotype.Component;
import th.co.prior.market.entity.InboxEntity;
import th.co.prior.market.model.InboxModel;

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
