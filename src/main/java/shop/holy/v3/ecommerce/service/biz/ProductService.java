package shop.holy.v3.ecommerce.service.biz;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import shop.holy.v3.ecommerce.api.dto.ResponsePagination;
import shop.holy.v3.ecommerce.api.dto.product.RequestProductCreate;
import shop.holy.v3.ecommerce.api.dto.product.RequestProductSearch;
import shop.holy.v3.ecommerce.api.dto.product.RequestProductUpdate;
import shop.holy.v3.ecommerce.api.dto.product.ResponseProduct;
import shop.holy.v3.ecommerce.persistence.entity.Product;
import shop.holy.v3.ecommerce.persistence.repository.IProductRepository;
import shop.holy.v3.ecommerce.service.cloud.CloudinaryFacadeService;
import shop.holy.v3.ecommerce.shared.exception.ResourceNotFoundException;
import shop.holy.v3.ecommerce.shared.mapper.ProductMapper;
import shop.holy.v3.ecommerce.shared.util.SecurityUtil;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductMapper productMapper;
    private final IProductRepository productRepository;
    private final CloudinaryFacadeService cloudinaryFacadeService;

    @SneakyThrows
    public ResponsePagination<ResponseProduct> search(RequestProductSearch searchReq) {
        Pageable pageable = productMapper.fromRequestPageableToPageable(searchReq.pageRequest());
        Specification<Product> spec = productMapper.fromRequestSearchToSpec(searchReq);
        Page<Product> products = productRepository.findAll(spec, pageable);
        Page<ResponseProduct> responses = products.map(productMapper::fromEntityToResponse);
        return ResponsePagination.fromPage(responses);
    }

    public ResponseProduct findById(long id, boolean deleted) {
        Product product;
        if (deleted)
            product = productRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("PRODUCT NOT FOUND"));
        else
            product = productRepository.findFirstByIdEqualsAndDeletedAtIsNull(id)
                    .orElseThrow(() -> new ResourceNotFoundException("PRODUCT NOT FOUND"));
        return productMapper.fromEntityToResponse(product);

    }

    @Transactional
    public ResponseProduct insert(RequestProductCreate request) throws IOException {
        Product product = productMapper.fromCreateRequestToEntity(request);
        this.uploadSingleImageAndSetEntity(product, request.image());
        productRepository.save(product);
        return productMapper.fromEntityToResponse(product);
    }

    @Transactional
    public ResponseProduct update(RequestProductUpdate request) throws IOException {
        Product product = productMapper.fromRequestUpdateToEntity(request);
        this.uploadSingleImageAndSetEntity(product, request.image());
        productRepository.save(product);
        return productMapper.fromEntityToResponse(product);
    }

    public int deleteProductById(Long id) {
        return productRepository.updateProductDeletedAt(id);
    }

    public void addFavorite(Long productId) {
        long accountId = SecurityUtil.getAuthNonNull().getId();
        productRepository.insertFavoriteProduct(accountId, productId);
    }

    public void removeFavorite(Long productId) {
        long accountId = SecurityUtil.getAuthNonNull().getId();
        productRepository.deleteFavoriteProduct(accountId, productId);
    }

    public ResponsePagination<ResponseProduct> findFavorites(Pageable pageable) {
        long accountId = SecurityUtil.getAuthNonNull().getId();
        Page<Product> products = productRepository.findFavorites(accountId, pageable);
        Page<ResponseProduct> responses = products.map(productMapper::fromEntityToResponse);
        return ResponsePagination.fromPage(responses);
    }

    private void uploadSingleImageAndSetEntity(Product product, MultipartFile file) throws IOException {
        if (file == null)
            return;
        String imageUrl = cloudinaryFacadeService.uploadProductBlob(file);
        product.setImageUrlId(imageUrl);
    }


}
