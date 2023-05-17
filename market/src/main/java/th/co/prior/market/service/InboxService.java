package th.co.prior.market.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import th.co.prior.market.component.InboxComponent;
import th.co.prior.market.entity.InboxEntity;
import th.co.prior.market.model.InboxModel;
import th.co.prior.market.model.ResponseModel;
import th.co.prior.market.repository.InboxNativeRepository;
import th.co.prior.market.repository.InboxRepository;

import java.util.List;

@Service
@Slf4j
public class InboxService {


    public InboxService(InboxRepository inboxRepository, InboxComponent inboxComponent, InboxNativeRepository inboxNativeRepository) {
        this.inboxRepository = inboxRepository;
        this.inboxComponent = inboxComponent;
        this.inboxNativeRepository = inboxNativeRepository;
    }

    private final InboxRepository inboxRepository;
    private final InboxComponent inboxComponent;
    private final InboxNativeRepository inboxNativeRepository;

    public ResponseModel<Void> insertInboxThenResponse(InboxModel inboxModel){
        ResponseModel<Void> result = new ResponseModel<>();
        result.setStatus(201);
        result.setDescription("created");
        try {
            InboxEntity inboxEntity = new InboxEntity();
            this.inboxComponent.transformInboxModelToEntity(inboxModel, inboxEntity);
            this.insertInbox(inboxEntity);
        }
         catch (Exception ex) {
            ex.printStackTrace();
            result.setStatus(500);
            result.setDescription(ex.getMessage());
         }

        return result;
    }

    public ResponseModel<List<InboxModel>> findInboxByInboxModelThenResponse(InboxModel inboxModel){
        ResponseModel<List<InboxModel>> result = new ResponseModel<>();
        result.setStatus(200);
        result.setDescription("ok");
        try{
            List<InboxModel> inboxModels = this.findInboxByInbox(inboxModel);
            List<InboxModel> modelsToUpdate = inboxModels.stream().filter(inbox -> !inbox.getStatus()).toList();
            this.updateInboxStatus(modelsToUpdate);
            result.setData(inboxModels);
        } catch (Exception ex) {
            ex.printStackTrace();
            result.setStatus(500);
            result.setDescription(ex.getMessage());
        }
        return result;
    }

    private void insertInbox(InboxEntity inboxEntity){
        this.inboxRepository.save(inboxEntity);
    }

    private List<InboxModel> findInboxByInbox(InboxModel inboxModel){
        return this.inboxNativeRepository.findInboxByInbox(inboxModel);
    }

    private void updateInboxStatus(List<InboxModel> inboxModels) {
        this.inboxNativeRepository.updateToReadStatus(inboxModels);
    }
}
