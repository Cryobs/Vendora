package com.vendora.catalog_service.service;


import com.vendora.catalog_service.DTO.ProductDTO;
import com.vendora.catalog_service.entity.Product;
import com.vendora.catalog_service.repository.elasticsearch.ProductSearchRepo;
import com.vendora.catalog_service.repository.postgres.ProductsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductsRepo productsRepo;
    @Autowired
    private ProductSearchRepo productSearchRepo;
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    public Product registerProduct(ProductDTO product, Jwt jwt){
        Product productEntity = new Product(UUID.fromString(jwt.getSubject()), product.getName(), product.getDescription(), product.getBasePrice(), product.getCategory(), product.getCharacteristics());
        Product finalProduct = productsRepo.save(productEntity);
        productSearchRepo.save(productEntity);
        return finalProduct;
    }

    public Product addPurchasesCount(UUID productId){
        Product productEntity = productsRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        productEntity.addPurchasesCount();
        productSearchRepo.save(productEntity);
        return productsRepo.save(productEntity);
    }

    public String deleteProduct(UUID productId, Jwt jwt) throws IllegalAccessException {
        Product productEntity = productsRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        if (productEntity.getUserId().equals(jwt.getSubject())){
            productsRepo.delete(productEntity);
            productSearchRepo.delete(productEntity);
            return "Product deleted";
        } else {
            throw new IllegalAccessException("Unauthorized");
        }
    }

    public String deleteAllProducts(){
        productsRepo.deleteAll();
        productSearchRepo.deleteAll();
        return "All products deleted";
    }

    public Product getProduct(UUID productId){
        return productsRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public List<Product> search(
            String keyword,
            int page,
            int psize,
            String sort,
            String category,
            Double minPrice,
            Double maxPrice) {

        Sort.Direction direction = sort.split("_")[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        String sortBy = sort.split("_")[0];
        Pageable pageable = PageRequest.of(page, psize, Sort.by(direction, sortBy));

        NativeQueryBuilder nativeQueryBuilder = NativeQuery.builder();

        //search by name
        if (keyword != null && !keyword.isEmpty()){
            nativeQueryBuilder
                    .withQuery(
                            q -> q.match(
                                    m -> m.field("name").query(keyword)));
        }
        //filter by category
        if (category != null && !category.isEmpty()){
            nativeQueryBuilder
                    .withFilter(
                            q -> q.term(
                                    t -> t.field("category").value(category)));
        }

        //filter by range price
        if (minPrice != null && maxPrice != null){
            nativeQueryBuilder
                    .withFilter(
                            q -> q.range(
                                    r -> r.number(
                                            n -> n.field("basePrice").gte(minPrice).lte(maxPrice))));
        } else {
            if (minPrice != null){
                nativeQueryBuilder
                        .withFilter(
                                q -> q.range(
                                        r -> r.number(
                                                n -> n.field("basePrice").gte(minPrice))));
            }

            if (maxPrice != null){
                nativeQueryBuilder
                        .withFilter(
                                q -> q.range(
                                        r -> r.number(
                                                n -> n.field("basePrice").lte(maxPrice))));
            }
        }


        NativeQuery nativeQuery = nativeQueryBuilder.withPageable(pageable).build();

        SearchHits<Product> searchHits = elasticsearchOperations.search(nativeQuery, Product.class);
        return searchHits.stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }


    public Iterable<Product> productListAll(){
        return productsRepo.findAll();
    }
}