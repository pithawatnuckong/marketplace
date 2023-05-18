@startuml

actor Buyer as buyer_actor
entity AccountAPI as account_entity
entity InventoryAPI as inventory_entity
entity InventoryHistoryAPI as inventory_history_entity
actor Seller as seller_actor
entity MarketPlaceAPI as market_place_entity
queue Kafka as kafka_queue
database Store as store_database
entity InboxAPI as inbox_entity


seller_actor -> account_entity: POST /api/account create account
activate seller_actor
activate account_entity
account_entity -> account_entity: Validate text
alt successful case
    account_entity -> store_database: Insert into database
    activate store_database
        alt successful case
            store_database -> account_entity: Response OK
            account_entity -> seller_actor: Response OK
        else failed case
            store_database -> account_entity: Response error
            deactivate store_database
            account_entity -> seller_actor: Response error
        end
else failed case
    account_entity -> seller_actor: Response error
deactivate account_entity
deactivate seller_actor
end



buyer_actor -> account_entity: POST /api/account create account
activate buyer_actor
activate account_entity
account_entity -> account_entity: Validate text
alt successful case
    account_entity -> store_database: Insert account into database
    activate store_database
    alt successful case
        store_database -> account_entity: Response OK
        account_entity -> buyer_actor: Response OK
    else failed case
        store_database -> account_entity: Response error
        deactivate store_database
        account_entity -> buyer_actor: Response error
    end
else failed case
    account_entity -> buyer_actor: Response error
deactivate account_entity
deactivate buyer_actor
end
        

seller_actor -> inventory_entity: POST /api/inventory insert item
activate seller_actor
activate inventory_entity
inventory_entity -> inventory_entity: Validate text
alt successful case
    inventory_entity -> store_database: Insert items into database
    activate store_database
    alt successful case
        store_database -> inventory_entity: Response OK
        inventory_entity -> seller_actor: Response OK
    else failed case
        store_database -> inventory_entity: Response error 
        deactivate store_database
        inventory_entity -> seller_actor: Response error
    end
else failed case
    inventory_entity -> seller_actor: Response error
deactivate inventory_entity
deactivate seller_actor
end


buyer_actor -> inventory_entity: POST /api/inventory insert item
activate buyer_actor
activate inventory_entity
inventory_entity -> inventory_entity: Validate text
alt successful case
    inventory_entity -> store_database: Insert item into database
    activate store_database
    alt successful case
        store_database -> inventory_entity: Response OK
        inventory_entity -> buyer_actor: Response OK
    else failed case
        store_database -> inventory_entity: Response error
        deactivate store_database
        inventory_entity -> buyer_actor: Response error
    end
else failed case
    inventory_entity -> buyer_actor: Repsonse error
deactivate inventory_entity
deactivate buyer_actor
end


buyer_actor -> inventory_entity: POST /api/find/inventory get items from inventory
activate buyer_actor
activate inventory_entity
inventory_entity -> store_database: Fetch item from databases
activate store_database
alt successful case
    store_database -> inventory_entity: Response list of item from database
    inventory_entity -> buyer_actor: Response list of item to buyer
else failed case
    store_database -> inventory_entity: Response error
    inventory_entity -> buyer_actor: Response error
deactivate buyer_actor
deactivate store_database
deactivate inventory_entity
end

seller_actor -> inventory_entity: POST /api/find/inventory get items from inventory
activate seller_actor
activate inventory_entity
inventory_entity -> store_database: Fetch item from databases
activate store_database
alt successful case
    store_database -> inventory_entity: Response list of item from database
    inventory_entity -> seller_actor: Response list of item to seller
else failed case
    store_database -> inventory_entity: Response error
    inventory_entity -> seller_actor: Response error
deactivate seller_actor
deactivate store_database
deactivate inventory_entity
end


seller_actor -> market_place_entity: POST /api/marketplace to sell item
activate seller_actor
activate market_place_entity
market_place_entity -> market_place_entity: Validate text
alt successful case
    market_place_entity -> store_database: Insert items to sell into database
    activate store_database
    alt successful case
        store_database -> market_place_entity: Response OK
        market_place_entity --> kafka_queue: Send message to kafka
        activate kafka_queue
        kafka_queue --> inbox_entity: Tell inbox to create a message to the seller_actor
        deactivate kafka_queue
        activate inbox_entity
        inbox_entity -> inbox_entity: Acknowledge then create inbox model to save
        alt successful case
                inbox_entity -> store_database: Insert inbox into database
            else failed case
                store_database -> inbox_entity: Response error
                inbox_entity -> inbox_entity: Show error
            end
            deactivate inbox_entity
    else failed case
        store_database -> market_place_entity: Response error
        deactivate store_database
        market_place_entity -> buyer_actor: Response error
    end
else failed case
    market_place_entity -> seller_actor: Response error
deactivate market_place_entity
deactivate seller_actor
end


buyer_actor -> market_place_entity: POST /api/find/marketplace list item on market place
activate buyer_actor
activate market_place_entity
market_place_entity -> store_database: Find all item in marketplace that is selling on database
alt successful case
    store_database -> market_place_entity: Response list of item in market place as a model
    market_place_entity -> buyer_actor: Response list of item as a model 
else failed case
    store_database -> market_place_entity: Response error
    market_place_entity -> buyer_actor: Response error
deactivate buyer_actor
deactivate market_place_entity
end

buyer_actor -> market_place_entity: PUT /api/marketplace buy item from market place
activate buyer_actor
activate market_place_entity
market_place_entity -> market_place_entity: Validate duplicate items
alt successful case
    market_place_entity -> store_database: Update status sell item on database
    activate store_database
    alt successful case
        store_database -> market_place_entity: Response OK
        market_place_entity --> kafka_queue: Send message to kafka
        activate kafka_queue
        kafka_queue --> inventory_entity: Tell inventory to update new owner
        activate inventory_entity
        inventory_entity -> inventory_entity: Acknowledge then create inventory model to insert inventory
        alt successful case
            inventory_entity -> store_database: Update owner status on database
            inventory_entity -> inventory_history_entity: Tell inventory history to insert history
            activate inventory_history_entity
            alt successful case
                inventory_history_entity -> store_database: Insert new history into database
            else failed case
                store_database -> inventory_history_entity: Response error
                inventory_history_entity -> inventory_history_entity: Log error
            end
            deactivate inventory_history_entity
        else failed case
            store_database -> inventory_entity: Response error
            inventory_entity -> inventory_entity: Log error
        end
        deactivate inventory_entity

        kafka_queue --> inbox_entity: Tell inbox to create message for buyer_actor
        activate inbox_entity
        inbox_entity -> inbox_entity: Acknowledge then create inbox model to insert inbox
        alt successful case
            inbox_entity -> store_database: insert new inbox into database
        else failed case
            store_database -> inbox_entity: Response error
            inbox_entity -> inbox_entity: Log error
        end
        deactivate inbox_entity
        kafka_queue --> account_entity: Tell account to update decrease cash amount for buyer_actor
        activate account_entity
        account_entity -> account_entity: Acknowledge then create an account model to update account
        alt successful case
            account_entity -> store_database: Update new value of buyer account into database
        else failed case
            store_database -> account_entity: Response error
            account_entity -> account_entity: Log error
        end
        deactivate account_entity
        kafka_queue --> account_entity: Tell account to update increase cash amount for seller_actor
        deactivate kafka_queue
        activate account_entity
        account_entity -> account_entity: Acknowledge then create an account model to update account
        alt successful case
            account_entity -> store_database: Update new value of seller account into database
        else failed case
            store_database -> account_entity: Respose error
            account_entity -> account_entity: Log error
        end
        deactivate account_entity
    else failed case
        store_database -> market_place_entity: Response error
        deactivate store_database
        market_place_entity -> buyer_actor: Response error
    end
else failed case
    market_place_entity -> buyer_actor: Response error
deactivate market_place_entity
deactivate buyer_actor
end


buyer_actor -> inbox_entity: POST /api/inbox send message to someone
activate buyer_actor
activate inbox_entity
inbox_entity -> inbox_entity: Validate
alt successful case
    inbox_entity -> store_database: Insert message into database
    activate store_database
    alt successful case
        store_database -> inbox_entity: Response OK
        inbox_entity -> buyer_actor: Response OK
    else failed case
        store_database -> inbox_entity: Response error
        deactivate store_database
        inbox_entity -> buyer_actor: Response error
    end
else failed case
    inbox_entity -> buyer_actor: Resopnse error
deactivate inbox_entity
deactivate buyer_actor
end


seller_actor -> inbox_entity: POST /api/inbox send message to someone
activate seller_actor
activate inbox_entity
inbox_entity -> inbox_entity: Validate
alt successful case
    inbox_entity -> store_database: Insert message into database
    activate store_database
    alt successful clase
        store_database -> inbox_entity: Response OK
        inbox_entity -> seller_actor: Response OK
    else failed case
        store_database -> inbox_entity: Response error
        deactivate store_database
        inbox_entity -> seller_actor: Response error
    end
else failed case
    inbox_entity -> seller_actor: Resopnse error
deactivate inbox_entity
deactivate seller_actor
end


buyer_actor -> inbox_entity: POST /api/inbox get messages from inbox
activate buyer_actor
activate inbox_entity
inbox_entity -> store_database: Fetch message from database
alt successful case
    store_database -> inbox_entity: Response list of message from database
    inbox_entity -> store_database: Update inbox status into database
    alt successful case
        store_database -> inbox_entity: Response OK
        inbox_entity -> buyer_actor: Response list of message to actor
    else failed case
        store_database -> inbox_entity: Response error
        inbox_entity -> buyer_actor: Response error
    end
else failed casee
    store_database -> inbox_entity: Response error
    inbox_entity -> buyer_actor: Response error
deactivate store_database
deactivate inbox_entity
deactivate buyer_actor
end


seller_actor -> inbox_entity: POST /api/inbox get messages from inbox
activate seller_actor
activate inbox_entity
inbox_entity -> store_database: Fetch message from database
alt successful case
    store_database -> inbox_entity: Response list of message from database
    inbox_entity -> store_database: Update inbox status into database
    alt successful case
        store_database -> inbox_entity: Response OK
        inbox_entity -> seller_actor: Response list of message to actor
    else failed case
        store_database -> inbox_entity: Response error
        inbox_entity -> seller_actor: Response error
    end
else failed casee
    store_database -> inbox_entity: Response error
    deactivate store_database
    inbox_entity -> seller_actor: Response error
deactivate inbox_entity
deactivate seller_actor
end

@enduml