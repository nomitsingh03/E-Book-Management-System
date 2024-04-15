package com.google.ebook.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.google.ebook.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Integer> {

	
}
