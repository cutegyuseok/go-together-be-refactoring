package com.example.gotogether.product.repository;

import com.example.gotogether.category.entity.Category;
import com.example.gotogether.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ProductRepositoryCustom {
    Page<Product> searchByKeywordAndSorting(Pageable pageable, String keyword, String sort, LocalDate startDate, LocalDate endDate, int people);

    List<Product> findPopular(List<Category> categoryList);

    Page<Product> searchByCategories(Pageable pageable ,List<Category> categoryList, String sort,LocalDate startDate, LocalDate endDate,int people);
}
