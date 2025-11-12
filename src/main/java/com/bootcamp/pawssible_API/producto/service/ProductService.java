package com.bootcamp.pawssible_API.producto.service;

import com.bootcamp.pawssible_API.producto.dto.ProductCreateRequest;
import com.bootcamp.pawssible_API.producto.dto.ProductResponse;
import com.bootcamp.pawssible_API.producto.dto.ProductUpdateRequest;
import com.bootcamp.pawssible_API.producto.model.Product;
import com.bootcamp.pawssible_API.producto.model.ProductCategory;
import com.bootcamp.pawssible_API.producto.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repo;

    public ProductResponse create(ProductCreateRequest req) {
        if (repo.existsBySlug(req.slug())) throw new IllegalArgumentException("slug ya existe");
        if (req.sku()!=null && repo.existsBySku(req.sku())) throw new IllegalArgumentException("sku ya existe");

        Product entity = toEntity(req);
        entity = repo.save(entity);
        return toResponse(entity);
    }

    public Page<ProductResponse> list(String q, Integer categoryCode, Pageable pageable) {
        Page<Product> page = repo.findAll(pageable); // simple; puedes añadir Specification/Query más adelante
        return page.map(this::toResponse);
    }

    public ProductResponse get(Long id) {
        return toResponse(find(id));
    }

    public ProductResponse update(Long id, ProductUpdateRequest req) {
        Product p = find(id);

        // reglas de unicidad
        repo.findBySlug(req.slug()).ifPresent(other -> {
            if (!other.getId().equals(id)) throw new IllegalArgumentException("slug ya existe");
        });
        if (req.sku()!=null) {
            repo.findAll().stream()
                    .filter(o -> req.sku().equals(o.getSku()) && !o.getId().equals(id))
                    .findFirst()
                    .ifPresent(o -> { throw new IllegalArgumentException("sku ya existe"); });
        }

        // map
        p.setName(req.name());
        p.setSlug(req.slug());
        p.setDescription(req.description());
        p.setPrice(req.price());
        p.setStock(req.stock());
        p.setSku(req.sku());
        p.setSize(req.size());
        p.setCategory(ProductCategory.from(req.categoryCode()));

        return toResponse(repo.save(p));
    }

    public void delete(Long id) {
        Product p = find(id);
        repo.delete(p);
    }

    private Product find(Long id){
        return repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Product "+id+" no existe"));
    }

    private Product toEntity(ProductCreateRequest r){
        return Product.builder()
                .name(r.name())
                .slug(r.slug())
                .description(r.description())
                .price(r.price())
                .stock(r.stock())
                .sku(r.sku())
                .size(r.size())
                .category(ProductCategory.from(r.categoryCode()))
                .build();
    }

    private ProductResponse toResponse(Product p){
        return new ProductResponse(
                p.getId(), p.getName(), p.getSlug(), p.getDescription(),
                p.getPrice(), p.getStock(), p.getSku(), p.getSize(),
                p.getCategory().getCode()
        );
    }
}
