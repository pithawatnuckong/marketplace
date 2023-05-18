package com.example.client.repository;

import com.example.client.entity.InboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InboxRepository extends JpaRepository<InboxEntity, Integer> {
}
