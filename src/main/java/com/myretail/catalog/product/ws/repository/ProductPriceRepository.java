package com.myretail.catalog.product.ws.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.myretail.catalog.product.ws.model.Price;

@Repository
public interface ProductPriceRepository extends MongoRepository<Price, Long> {}