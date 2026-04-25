package br.com.fiap.postech.adapter.input.vehicle.controller;

import br.com.fiap.postech.adapter.input.api.model.PaginatedVehicleResponse;
import br.com.fiap.postech.adapter.input.api.model.VehicleData;
import br.com.fiap.postech.adapter.input.api.model.VehicleRequest;
import br.com.fiap.postech.port.api.VehiclesApi;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VehicleController implements VehiclesApi {
    
    @Override
    public ResponseEntity<VehicleData> createVehicle(VehicleRequest veiculo) {
        VehicleData responseBody = new VehicleData();

        return ResponseEntity.ok(responseBody);
    }

    @Override
    public ResponseEntity<Void> deleteVehicle(Long id) {
        return ResponseEntity.accepted().build();
    }

    @Override
    public ResponseEntity<VehicleData> getVehicleById(Long id) {
        VehicleData responseBody = new VehicleData();

        return ResponseEntity.ok(responseBody);
    }

    @Override
    public ResponseEntity<PaginatedVehicleResponse> listVehicles(Long id, String licensePlate, Integer pageSize, String cursor) {
        PaginatedVehicleResponse responseBody = new PaginatedVehicleResponse();

        return ResponseEntity.ok(responseBody);
    }

    @Override
    public ResponseEntity<VehicleData> updateVehicle(Long id, VehicleData veiculo) {
        VehicleData responseBody = new VehicleData();

        return ResponseEntity.ok(responseBody);
    }
}
