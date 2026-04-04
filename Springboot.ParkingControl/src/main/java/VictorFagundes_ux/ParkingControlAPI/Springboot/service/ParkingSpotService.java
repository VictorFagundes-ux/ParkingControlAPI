package VictorFagundes_ux.ParkingControlAPI.Springboot.service;

import VictorFagundes_ux.ParkingControlAPI.Springboot.model.ParkingSpotModel;
import VictorFagundes_ux.ParkingControlAPI.Springboot.repository.ParkingSpotRepository;

import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ParkingSpotService {

    private final ParkingSpotRepository repository;

    public ParkingSpotService(ParkingSpotRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ParkingSpotModel save(ParkingSpotModel model) {
        return repository.save(model);
    }

    public boolean existsByLicensePlateCar(String licensePlateCar) {
        return repository.existsByLicensePlateCar(licensePlateCar);
    }

    public boolean existsByApartmentAndBlock(String apartment, String block) {
        return repository.existsByApartmentAndBlock(apartment, block);
    }

    public Page<ParkingSpotModel> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Optional<ParkingSpotModel> findById(UUID id) {
        return repository.findById(id);
    }

    @Transactional
    public void delete(ParkingSpotModel model) {
        repository.delete(model);
    }
}