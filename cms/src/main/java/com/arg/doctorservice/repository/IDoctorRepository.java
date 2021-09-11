package com.arg.doctorservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.arg.doctorservice.entity.Doctor;

@Repository
public interface IDoctorRepository extends JpaRepository<Doctor, Long> {

}
