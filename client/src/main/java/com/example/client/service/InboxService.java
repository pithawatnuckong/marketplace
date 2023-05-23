package com.example.client.service;

import com.example.client.component.InboxComponent;
import com.example.client.entity.InboxEntity;
import com.example.client.exception.BadRequest;
import com.example.client.model.InboxModel;
import com.example.client.model.ResponseModel;
import com.example.client.repository.InboxNativeRepository;
import com.example.client.repository.InboxRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
            if(inboxModel.getMessage().isEmpty()){
                throw new BadRequest("Inbox message cannot be null");
            }
            this.insertInbox(inboxModel);
        }
        catch (BadRequest ex){
            ex.printStackTrace();
            result.setStatus(400);
            result.setDescription(ex.getMessage());
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

    public void insertInbox(InboxModel inboxModel){
        InboxEntity inboxEntity = new InboxEntity();
        this.inboxComponent.transformInboxModelToEntity(inboxModel, inboxEntity);
        this.inboxRepository.save(inboxEntity);
    }

    private List<InboxModel> findInboxByInbox(InboxModel inboxModel){
        return this.inboxNativeRepository.findInboxByInbox(inboxModel);
    }

    private void updateInboxStatus(List<InboxModel> inboxModels) {
        this.inboxNativeRepository.updateToReadStatus(inboxModels);
    }
}
