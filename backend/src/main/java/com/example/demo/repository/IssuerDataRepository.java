package com.example.demo.repository;

import com.example.demo.model.IssuerData;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface IssuerDataRepository extends JpaRepository<IssuerData, Integer>{
}
