package com.example.gotogether.product.repository;

import com.example.gotogether.category.entity.Category;
import com.example.gotogether.product.entity.Product;
import com.example.gotogether.product.entity.ProductStatus;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.LocalDate;
import java.util.List;

import static com.example.gotogether.product.entity.QProduct.product;
import static com.example.gotogether.product.entity.QProductCategory.productCategory;
import static com.example.gotogether.product.entity.QProductOption.productOption;
import static com.example.gotogether.reservation.entity.QReservationDetail.reservationDetail;

public class ProductRepositoryCustomImpl extends QuerydslRepositorySupport implements ProductRepositoryCustom {

    @Autowired
    private JPAQueryFactory queryFactory;

    public ProductRepositoryCustomImpl() {
        super(Product.class);
    }

    @Override
    public Page<Product> searchByKeywordAndSorting(Pageable pageable, String keyword, String sort, LocalDate startDate, LocalDate endDate, int people) {
        JPQLQuery<Product> query = queryFactory.selectFrom(product)
                .leftJoin(product.productOptions, productOption)
                .groupBy(product.productId)
                .where(containsName(keyword), isAvailableProduct(),isAvailablePeople(people),isStartDateAfter(startDate),isEndDateBefore(endDate))
                .orderBy(sort(sort));
        List<Product> productList = this.getQuerydsl().applyPagination(pageable, query).fetch();
        return new PageImpl<Product>(productList, pageable, query.fetchCount());
    }

    @Override
    public List<Product> findPopular(List<Category> categoryList) {
        return queryFactory
                .select(product)
                .from(product)
                .leftJoin(product.reservationDetails, reservationDetail)
                .leftJoin(product.categories, productCategory)
                .groupBy(product.productId)
                .where(containCategory(categoryList), isAvailableProduct())
                .limit(10)
                .orderBy(reservationDetail.count().desc())
                .fetch();
    }

    @Override
    public Page<Product> searchByCategories(Pageable pageable, List<Category> categoryList, String sort,LocalDate startDate, LocalDate endDate,int people) {
        JPQLQuery<Product> query =  queryFactory
                .select(product)
                .from(product)
                .leftJoin(product.categories,productCategory)
                .leftJoin(product.productOptions,productOption)
                .groupBy(product.productId)
                .where(containCategory(categoryList),isAvailableProduct(),isStartDateAfter(startDate),isEndDateBefore(endDate),isAvailablePeople(people))
                .orderBy(sort(sort));
        List<Product> productList = this.getQuerydsl().applyPagination(pageable,query).fetch();
        return new PageImpl<Product>(productList,pageable,query.fetchCount());
    }

    private BooleanExpression containsName(String keyword) {
        if (keyword.isEmpty()) {
            return null;
        }
        return product.name.contains(keyword).or(containsSummary(keyword)).or(containsArea(keyword)).or(containsFeature(keyword)).or(containsDetail(keyword));
    }

    private BooleanExpression containsSummary(String keyword) {
        if (keyword.isEmpty()) {
            return null;
        }
        return product.summary.contains(keyword);
    }

    private BooleanExpression containsArea(String keyword) {
        if (keyword.isEmpty()) {
            return null;
        }
        return product.area.contains(keyword);
    }

    private BooleanExpression containsFeature(String keyword) {
        if (keyword.isEmpty()) {
            return null;
        }
        return product.feature.contains(keyword);
    }

    private BooleanExpression containsDetail(String keyword) {
        if (keyword.isEmpty()) {
            return null;
        }
        return product.detail.contains(keyword);
    }

    private OrderSpecifier<?> sort(String sort) {
        switch (sort) {
            case "recent":
                return new OrderSpecifier<>(Order.DESC, product.productId);
            case "asc":
                return new OrderSpecifier<>(Order.ASC, product.price);
            case "desc":
                return new OrderSpecifier<>(Order.DESC, product.price);
        }
        return null;
    }

    private BooleanExpression containCategory(List<Category> categoryList) {
        if (categoryList == null) {
            return null;
        }
        return productCategory.category.in(categoryList);
    }

    private BooleanExpression isAvailableProduct() {
        return product.productStatus.eq(ProductStatus.FOR_SALE);
    }

    private BooleanExpression isStartDateAfter(LocalDate startDate) {
        if (startDate==null){
            return null;
        }
        return productOption.startDate.after(startDate);
    }

    private BooleanExpression isEndDateBefore(LocalDate endDate) {
        if (endDate==null){
            return null;
        }
        return productOption.endDate.before(endDate);
    }

    private BooleanExpression isAvailablePeople(int people) {
        if (people < 1) {
            return null;
        }
        return productOption.maxPeople.subtract(productOption.PresentPeopleNumber).goe(people);
    }
}
