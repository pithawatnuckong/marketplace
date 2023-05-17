@startuml

actor Buyer as buyer_actor
entity AccountAPI as account_entity
entity InventoryAPI as inventory_entity
actor Seller as seller_actor
entity MarketPlaceAPI as market_place_entity
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
        market_place_entity -> inbox_entity: Tell inbox to create a message to the seller_actor
        inbox_entity -> store_database: Insert inbox into database
        activate inbox_entity
            alt successful case
                store_database -> inbox_entity: Response OK
                inbox_entity -> market_place_entity: Response OK
                market_place_entity -> seller_actor: Response OK
            else failed case
                store_database -> inbox_entity: Response error
                inbox_entity -> market_place_entity: Response error
                deactivate inbox_entity
                market_place_entity -> seller_actor: Response error
            end
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
        market_place_entity -> inventory_entity: Tell inventory to update new owner
        activate inventory_entity
        inventory_entity -> store_database: Update owner status on database
        alt successful case
            store_database -> inventory_entity: Response OK
            inventory_entity -> market_place_entity: Response OK
            market_place_entity -> inbox_entity: Tell inbox to create message for buyer_actor
            activate inbox_entity
            inbox_entity -> store_database: Insert inbox into database
            alt successful case
                store_database -> inbox_entity: Response OK
                market_place_entity -> buyer_actor: Response OK
            else failed case
                store_database -> inbox_entity: Response error
                inbox_entity -> market_place_entity: Response error
                market_place_entity -> buyer_actor: Response error
            deactivate inbox_entity
            end
        else failed case
            store_database -> inventory_entity: Response error
            inventory_entity -> market_place_entity: Response error
            deactivate inventory_entity
            market_place_entity -> buyer_actor: Response error
        end
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