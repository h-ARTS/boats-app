package com.hanankhan.boatsapi.boats.service;

import com.hanankhan.boatsapi.boats.dto.BoatDto;
import com.hanankhan.boatsapi.boats.dto.BoatUpsertDto;
import com.hanankhan.boatsapi.boats.entity.Boat;
import com.hanankhan.boatsapi.boats.repository.BoatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoatService {
    private final BoatRepository boatRepository;

    public Page<BoatDto> getAll(Pageable pageable) {
        return boatRepository.findAll(pageable)
                .map(boat -> new BoatDto(
                        boat.getId(),
                        boat.getName(),
                        boat.getDescription(),
                        boat.getCreatedAt(),
                        boat.getUpdatedAt()
                ));
    }

    public BoatDto getById(Long id) {
        return boatRepository.findById(id)
                .map(boat -> new BoatDto(
                        boat.getId(),
                        boat.getName(),
                        boat.getDescription(),
                        boat.getCreatedAt(),
                        boat.getUpdatedAt()
                ))
                .orElseThrow(() -> new RuntimeException("Boat not found"));
    }

    public BoatDto createBoat(BoatUpsertDto boatDto) {
        var boat = new Boat();
        boat.setName(boatDto.name());
        boat.setDescription(boatDto.description());
        var savedBoat = boatRepository.save(boat);
        return new BoatDto(
                savedBoat.getId(),
                savedBoat.getName(),
                savedBoat.getDescription(),
                savedBoat.getCreatedAt(),
                savedBoat.getUpdatedAt()
        );
    }

    public BoatDto updateBoat(Long id, BoatUpsertDto boatDto) {
        var boat = boatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Boat not found"));
        boat.setName(boatDto.name());
        boat.setDescription(boatDto.description());
        var updatedBoat = boatRepository.save(boat);
        return new BoatDto(
                updatedBoat.getId(),
                updatedBoat.getName(),
                updatedBoat.getDescription(),
                updatedBoat.getCreatedAt(),
                updatedBoat.getUpdatedAt()
        );
    }

    public void deleteBoat(Long id) {
        if (!boatRepository.existsById(id)) {
            throw new RuntimeException("Boat not found");
        }
        boatRepository.deleteById(id);
    }
}
