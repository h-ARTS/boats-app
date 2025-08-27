package com.hanankhan.boatsapi.boats.repository;

import com.hanankhan.boatsapi.boats.entity.Boat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoatRepository extends JpaRepository<Boat, Long> {
}
