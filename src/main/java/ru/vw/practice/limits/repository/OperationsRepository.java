package ru.vw.practice.limits.repository;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import ru.vw.practice.limits.entity.OperationEntity;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface OperationsRepository extends ListCrudRepository<OperationEntity, Long> {
  OperationEntity getById(long id);

  List<OperationEntity> getByClientIdAndOpStampGreaterThan(long clientId, OffsetDateTime opStamp);
}
