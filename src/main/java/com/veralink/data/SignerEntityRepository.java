package com.veralink.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.veralink.model.SignerEntity;

@Component
@Repository
public interface SignerEntityRepository extends JpaRepository<SignerEntity, Long> {
	SignerEntity findByName(String name);
}
