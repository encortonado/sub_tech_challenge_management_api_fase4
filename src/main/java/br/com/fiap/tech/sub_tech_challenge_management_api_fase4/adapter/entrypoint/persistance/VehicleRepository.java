package br.com.fiap.tech.sub_tech_challenge_management_api_fase4.adapter.entrypoint.persistance;

import br.com.fiap.tech.sub_tech_challenge_management_api_fase4.application.vehicle.entities.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<VehicleEntity, Long> {

    List<VehicleEntity> findAllByOrderByPriceAsc();

}