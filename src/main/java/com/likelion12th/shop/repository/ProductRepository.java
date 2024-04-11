package com.likelion12th.shop.repository;

import com.likelion12th.shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
public interface ProductRepository extends JpaRepository<Product, Long>{
}
