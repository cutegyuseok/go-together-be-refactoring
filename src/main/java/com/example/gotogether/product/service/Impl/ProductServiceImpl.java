package com.example.gotogether.product.service.Impl;

import com.example.gotogether.auth.entity.Grouping;
import com.example.gotogether.auth.repository.GroupingRepository;
import com.example.gotogether.category.entity.Category;
import com.example.gotogether.category.repository.CategoryRepository;
import com.example.gotogether.global.response.PageResponseDTO;
import com.example.gotogether.product.dto.ProductDTO;
import com.example.gotogether.product.dto.ProductOptionDTO;
import com.example.gotogether.product.entity.Product;
import com.example.gotogether.product.entity.ProductCategory;
import com.example.gotogether.product.entity.ProductOption;
import com.example.gotogether.product.repository.ProductCategoryRepository;
import com.example.gotogether.product.repository.ProductRepository;
import com.example.gotogether.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.example.gotogether.global.config.PageSizeConfig.*;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final GroupingRepository groupingRepository;

    public ResponseEntity<?> createProduct(ProductDTO.ProductCreateReqDTO productCreateReqDTO) {
        try {
            if (productRepository.existsByName(productCreateReqDTO.getName()))return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            if (productCreateReqDTO.getType()!=null && !groupingRepository.existsByGroup(productCreateReqDTO.getType())){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            //상품 엔티티 생성
            Product product = productCreateReqDTO.toEntity();
            //상품 카테고리 엔티티 생성 후 상품엔티티의 카테고리 리스트에 넣기
            List<Category> categoryList = categoryRepository.findAllByCategoryIdIn(productCreateReqDTO.getCategoryIdList());
            if (categoryList.size() != productCreateReqDTO.getCategoryIdList().size())
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            for (Category category : categoryList) {
                product.getCategories().add(ProductCategory.builder()
                        .category(category)
                        .product(product)
                        .build());
            }
            //상품 옵션 생성 후, 상품 엔티티의 옵션 리스트에 넣기
            for (ProductOptionDTO.ProductOptionReqDTO createDto : productCreateReqDTO.getOptions()) {
                product.getProductOptions().add(ProductOption.builder()
                        .product(product)
                        .startDate(createDto.getStartDate())
                        .endDate(createDto.getEndDate())
                        .maxPeople(createDto.getMaxPeople())
                        .maxSingleRoom(createDto.getMaxSingleRoom())
                        .presentPeopleNumber(0)
                        .presentSingleRoomNumber(0)
                        .build());
            }
            productRepository.save(product);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteProduct(Long productId) {
        try {
            Product product = productRepository.findById(productId).orElseThrow(NoSuchElementException::new);
            product.changeStatusHiding(product);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateProduct(Long productId, ProductDTO.ProductUpdateReqDTO productUpdateReqDTO) {
        try {
            Product product = productRepository.findById(productId).orElseThrow(NoSuchElementException::new);
            if (product == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            productCategoryRepository.deleteAllByProduct(product);

            List<Category> categoryList = categoryRepository.findAllByCategoryIdIn(productUpdateReqDTO.getCategoryIdList());
            if (categoryList.size() != productUpdateReqDTO.getCategoryIdList().size())
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            if (productUpdateReqDTO.getType()!=null&&!groupingRepository.existsByGroup(productUpdateReqDTO.getType())){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            List<ProductCategory> productCategories = new ArrayList<>();
            for (Category category : categoryList) {
                productCategories.add(ProductCategory.builder()
                        .category(category)
                        .product(product)
                        .build());
            }
            product.update(productUpdateReqDTO,productCategories);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> getAllProducts(int page) {
        try {
            if (page < 1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            PageRequest pageable = PageRequest.of(page - 1, Product_List_By_Admin);
            Page<Product> productList = productRepository.findAll(pageable);
            if (productList.getContent().size()<1){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            PageResponseDTO pageResponseDTO = new PageResponseDTO(productList);
            pageResponseDTO.setContent(
                    pageResponseDTO
                            .getContent()
                            .stream()
                            .map(e -> new ProductDTO.ProductListResDTO((Product)e))
                            .collect(Collectors.toList())
            );
            return new ResponseEntity<>(pageResponseDTO, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> findDetailProduct(Long productId) {
        try {
            Product product = productRepository.findById(productId).orElseThrow(NoSuchElementException::new);
            return new ResponseEntity<>(new ProductDTO.ProductDetailResDTO(product),HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @Override
    public ResponseEntity<?> findProductByCategory(Long categoryId, int page, String sort,String dateOption,String endDateOption,int people) {
        LocalDate startDate;
        LocalDate endDate;
        try {
            if (dateOption!=null) {
                startDate = LocalDate.parse(dateOption).minusDays(1);
            }else {
                startDate = null;
            }
            if (endDateOption!=null){
                endDate = LocalDate.parse(endDateOption).plusDays(1);
            }else {
                endDate = null;
            }
        }catch (DateTimeParseException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            if (page < 1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            PageRequest pageable = PageRequest.of(page - 1, Product_List_By_Category);
            //카테고리 검색
            Category category = categoryRepository.findById(categoryId).orElseThrow(IllegalArgumentException::new);
            Page<Product> products = productRepository.searchByCategories(pageable,listOfCategory(category),sort,startDate,endDate,people);
            PageResponseDTO pageResponseDTO = new PageResponseDTO(products);
            pageResponseDTO.setContent(products.getContent().stream().map(ProductDTO.ProductListResDTO::new).collect(Collectors.toList()));
            if(pageResponseDTO.getContent().size()<1){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(pageResponseDTO, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> findProductByKeyword(String keyword, int page, String sort, String dateOption,String endDateOption, int people) {
        LocalDate startDate;
        LocalDate endDate;
        try {
            if (dateOption!=null) {
                startDate = LocalDate.parse(dateOption).minusDays(1);
            }else {
                startDate = null;
            }
            if (endDateOption!=null){
                endDate = LocalDate.parse(endDateOption).plusDays(1);
            }else {
                endDate = null;
            }
        }catch (DateTimeParseException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            if (page < 1) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            PageRequest pageable = PageRequest.of(page - 1, Product_List_By_Keyword);
            Page<Product> productPage = productRepository
                    .searchByKeywordAndSorting(pageable,keyword,sort, startDate,endDate, people);
            if (productPage.getTotalElements()<1){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            PageResponseDTO pageResponseDTO = new PageResponseDTO(productPage);
            pageResponseDTO
                    .setContent(pageResponseDTO.getContent()
                            .stream()
                            .map(e -> new ProductDTO.ProductListResDTO((Product) e))
                            .collect(Collectors.toList()));
            return new ResponseEntity<>(pageResponseDTO,HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> findPopularProducts(Long categoryId) {
        try {
            Category category = null;
            if (categoryId!=null) {
                category = categoryRepository.findById(categoryId).orElseThrow(IllegalArgumentException::new);
            }
            List<Category> categoryList = null;
            if (category!=null) {
                categoryList = listOfCategory(category);
            }
            List<Product> productList = productRepository.findPopular(categoryList);
            if (productList.size() < 1) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(productList.stream().map(ProductDTO.ProductListResDTO::new).collect(Collectors.toList()), HttpStatus.OK);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<Category> listOfCategory(Category category) {
        List<Category> categoryList = new ArrayList<>(category.getChildren());
        categoryList.add(category);
        for (int i = 0; i < category.getChildren().size(); i++) {
            Category insideCategory = category.getChildren().get(i);
            if (insideCategory.getChildren().size() > 0)
                categoryList.addAll(listOfCategory(insideCategory));
        }
        return categoryList;
    }


    @Override
    public ResponseEntity<?> getProductByType(Long productId) {
        try {
            Product product = productRepository.findById(productId).orElseThrow(NoSuchElementException::new);
            List<Product> productTypeList= productRepository.findAllByType(product.getType());
            if (productTypeList.size()<1)return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(productTypeList.stream().map(ProductDTO.ProductDetailResDTO::new).collect(Collectors.toList()), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}