package br.com.fiap.tech.sub_tech_challenge_management_api_fase4.application.vehicle.ports;

import br.com.fiap.tech.sub_tech_challenge_management_api_fase4.application.vehicle.entities.VehicleEntity;

import java.util.List;

public interface IListVehiclesUseCase {

    List<VehicleEntity> listVehicles();

}