package ru.vw.practice.limits.repository;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import ru.vw.practice.limits.entity.LimitEntity;

@Repository
public interface LimitsRepository extends ListCrudRepository<LimitEntity, Long> {
  LimitEntity getByClientId(long clientId);
}
