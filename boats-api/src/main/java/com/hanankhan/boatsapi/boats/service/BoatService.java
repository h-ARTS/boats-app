package com.hanankhan.boatsapi.boats.service;

import com.hanankhan.boatsapi.boats.dto.BoatDto;
import com.hanankhan.boatsapi.boats.dto.BoatUpsertDto;
import com.hanankhan.boatsapi.boats.entity.Boat;
import com.hanankhan.boatsapi.boats.exception.NotFoundException;
import com.hanankhan.boatsapi.boats.repository.BoatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class BoatService {
    private final BoatRepository boatRepository;

    public Page<BoatDto> getAll(Pageable pageable) {
        return boatRepository.findAll(pageable)
                .map(boat -> new BoatDto(
                        boat.getId(),
                        boat.getName(),
                        boat.getType(),
                        boat.getLength(),
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
                        boat.getType(),
                        boat.getLength(),
                        boat.getDescription(),
                        boat.getCreatedAt(),
                        boat.getUpdatedAt()
                ))
                .orElseThrow(() -> new NotFoundException("Boat not found"));
    }

    public BoatDto createBoat(BoatUpsertDto boatDto) {
        var boat = new Boat();
        boat.setName(boatDto.name());
        boat.setType(boatDto.type());
        boat.setLength(boatDto.length());
        boat.setDescription(boatDto.description());
        var savedBoat = boatRepository.save(boat);
        return new BoatDto(
                savedBoat.getId(),
                savedBoat.getName(),
                savedBoat.getType(),
                savedBoat.getLength(),
                savedBoat.getDescription(),
                savedBoat.getCreatedAt(),
                savedBoat.getUpdatedAt()
        );
    }

    @Transactional
    public BoatDto updateBoat(Long id, BoatUpsertDto in) {
        var boat = boatRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Boat not found"));

        boolean changed = false;

        changed |= setIfDifferent(boat::getName, boat::setName, in.name());
        changed |= setIfDifferent(boat::getDescription, boat::setDescription, in.description());
        changed |= setIfDifferent(boat::getType, boat::setType, in.type());
        changed |= setIfDifferent(boat::getLength, boat::setLength, in.length());

        if (changed) {
            boat = boatRepository.save(boat);
        }
        return new BoatDto(
                boat.getId(),
                boat.getName(),
                boat.getType(),
                boat.getLength(),
                boat.getDescription(),
                boat.getCreatedAt(),
                boat.getUpdatedAt()
        );
    }

    private <T> boolean setIfDifferent(Supplier<T> getter, Consumer<T> setter, T newVal) {
        T cur = getter.get();
        if (!Objects.equals(cur, newVal)) {
            setter.accept(newVal);
            return true;
        }
        return false;
    }

    public void deleteBoat(Long id) {
        if (!boatRepository.existsById(id)) {
            throw new NotFoundException("Boat not found");
        }
        boatRepository.deleteById(id);
    }
}
