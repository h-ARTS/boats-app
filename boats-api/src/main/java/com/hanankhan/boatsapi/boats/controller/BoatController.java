package com.hanankhan.boatsapi.boats.controller;

import com.hanankhan.boatsapi.boats.dto.BoatDto;
import com.hanankhan.boatsapi.boats.dto.BoatUpsertDto;
import com.hanankhan.boatsapi.boats.service.BoatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boats")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class BoatController {
    private final BoatService boatService;

    @GetMapping
    public ResponseEntity<Page<BoatDto>> getAll(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        Page<BoatDto> boats = boatService.getAll(pageable);
        return ResponseEntity.ok(boats);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoatDto> getById(@PathVariable Long id) {
        BoatDto boat = boatService.getById(id);
        return ResponseEntity.ok(boat);
    }

    @PostMapping
    public ResponseEntity<BoatDto> create(@Valid @RequestBody BoatUpsertDto in) {
        BoatDto createdBoat = boatService.createBoat(in);
        return ResponseEntity.status(201).body(createdBoat);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoatDto> update(@PathVariable Long id, @Valid @RequestBody BoatUpsertDto in) {
        BoatDto updatedBoat = boatService.updateBoat(id, in);
        return ResponseEntity.ok(updatedBoat);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boatService.deleteBoat(id);
        return ResponseEntity.noContent().build();
    }
}
