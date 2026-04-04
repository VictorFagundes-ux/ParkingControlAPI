package VictorFagundes_ux.ParkingControlAPI.Springboot.controller;

import VictorFagundes_ux.ParkingControlAPI.Springboot.dto.ParkingSpotDto;
import VictorFagundes_ux.ParkingControlAPI.Springboot.model.ParkingSpotModel;
import VictorFagundes_ux.ParkingControlAPI.Springboot.service.ParkingSpotService;

import jakarta.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/parking-spot")
public class ParkingSpotController {

    final ParkingSpotService parkingSpotService;

    public ParkingSpotController(ParkingSpotService parkingSpotService) {
        this.parkingSpotService = parkingSpotService;
    }

    @PostMapping
    public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid ParkingSpotDto dto) {

        if (parkingSpotService.existsByLicensePlateCar(dto.getLicensePlateCar())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("License Plate already in use!");
        }

        var model = new ParkingSpotModel();
        BeanUtils.copyProperties(dto, model);
        model.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(parkingSpotService.save(model));
    }

    @GetMapping
    public ResponseEntity<Page<ParkingSpotModel>> getAll(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {

        return ResponseEntity.ok(parkingSpotService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOne(@PathVariable UUID id) {

        Optional<ParkingSpotModel> opt = parkingSpotService.findById(id);

        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found");
        }

        return ResponseEntity.ok(opt.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable UUID id) {

        Optional<ParkingSpotModel> opt = parkingSpotService.findById(id);

        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found");
        }

        parkingSpotService.delete(opt.get());
        return ResponseEntity.ok("Deleted successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable UUID id,
                                         @RequestBody @Valid ParkingSpotDto dto) {

        Optional<ParkingSpotModel> opt = parkingSpotService.findById(id);

        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found");
        }

        var model = opt.get();
        BeanUtils.copyProperties(dto, model);

        return ResponseEntity.ok(parkingSpotService.save(model));
    }
}