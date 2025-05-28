package shop.holy.v3.ecommerce.service.biz.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import shop.holy.v3.ecommerce.api.dto.product.RequestProductCreate;
import shop.holy.v3.ecommerce.api.dto.product.RequestProductUpdate;
import shop.holy.v3.ecommerce.api.dto.product.ResponseProduct;
import shop.holy.v3.ecommerce.persistence.entity.product.Product;
import shop.holy.v3.ecommerce.persistence.entity.product.ProductDescription;
import shop.holy.v3.ecommerce.persistence.entity.product.ProductTag;
import shop.holy.v3.ecommerce.persistence.repository.product.IProductDescriptionRepository;
import shop.holy.v3.ecommerce.persistence.repository.product.IProductRepository;
import shop.holy.v3.ecommerce.persistence.repository.product.IProductTagRepository;
import shop.holy.v3.ecommerce.service.cloud.CloudinaryFacadeService;
import shop.holy.v3.ecommerce.shared.constant.BizErrors;
import shop.holy.v3.ecommerce.shared.mapper.product.ProductMapper;
import shop.holy.v3.ecommerce.shared.mapper.product.ProductTagMapper;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductCommand {

    private final ProductMapper productMapper;
    private final ProductTagMapper productTagMapper;

    private final IProductTagRepository tagRepository;
    private final IProductRepository productRepository;
    private final IProductDescriptionRepository descriptionRepository;
    private final CloudinaryFacadeService cloudinaryFacadeService;

    @Transactional
    public ResponseProduct insert(RequestProductCreate request) {
        Product product = productMapper.fromCreateRequestToEntity(request);

        ///  UPLOAD images
        this.uploadSingleImageAndSetEntity(product, request.image());
        ProductDescription prod_Desc = productMapper.fromRequestToDescription(request.productDescription());
        var desc = descriptionRepository.save(prod_Desc);

        /// SAVE PRODUCT_DESC
        product.setProDescId(desc.getId());
        Product rs = productRepository.save(product);

        /// SAVE PRODUCT TAGS
        if (!CollectionUtils.isEmpty(request.tags())) {
            Set<ProductTag> tags = productTagMapper.fromStringTagsToProductTagsEntity(rs.getId(), request.tags());
            tagRepository.saveAll(tags);
        }

        ///  SAVE CATEGORIES
        request.categoryIds().forEach(catId -> {
            productRepository.insertProductCategories(rs.getId(), catId);
        });
        return productMapper.fromEntityToResponse_Light(rs, request.tags().toArray(String[]::new), false);
    }

    @Transactional
    public ResponseProduct update(RequestProductUpdate request) throws IOException {
        Product product = productMapper.fromRequestUpdateToEntity(request);
        long productId = request.id();

        this.uploadSingleImageAndSetEntity(product, request.image());
        ///  append-delete Categories
        if (!CollectionUtils.isEmpty(request.categoryIds())) {
            List<Long> commingIds = request.categoryIds();
            Set<Long> currentCatIds = productRepository.findCategoryIdsByProductId(productId);
            List<Long> toDeletes = currentCatIds.stream()
                    .filter(id -> !commingIds.contains(id))
                    .toList();
            List<Long> toInserts = commingIds.stream()
                    .filter(id -> !currentCatIds.contains(id))
                    .toList();
            toDeletes.forEach(catId -> {
                productRepository.deleteProductCategories(productId, catId);
            });
            toInserts.forEach(catId -> {
                productRepository.insertProductCategories(productId, catId);
            });
        }

        ///  update product && update description as well
        ProductDescription prod_Desc = productMapper.fromRequestToDescription(request.productDescription());
        if (prod_Desc != null) {
            var pd = descriptionRepository.updateProductDescriptionByProductId(request.id(), prod_Desc).orElseThrow(BizErrors.PRODUCT_NOT_FOUND::exception);
            product.setProDescId(pd.getId());
            product.setProductDescription(pd);
        }

        /// SAVE TAGS
        if (!CollectionUtils.isEmpty(request.tags())) {
            tagRepository.deleteAllByProductId(productId);
            Set<ProductTag> tags = productTagMapper.fromStringTagsToProductTagsEntity(request.id(), request.tags());
            tagRepository.saveAll(tags);
        }

        int changes = productRepository.updateProductById(product);
        if (changes == 0)
            throw BizErrors.PRODUCT_NOT_FOUND.exception();

        /// TODO: use Redis cache -> get Profile -> favoriteProductIds -> if match -> true
        return productMapper.fromEntityToResponse_Light(product, request.tags().toArray(String[]::new), false);
    }

    @Transactional
    public int deleteProductById(Long id) {
        if (id == null)
            return 0;
        return productRepository.updateProductDeletedAt(id);
    }

    @Transactional
    public int deleteProductByIdIn(long[] ids) {
        if (ids == null || ids.length == 0)
            return 0;
        return productRepository.updateProductDeletedAtByIdIn(ids);
    }


    private void uploadSingleImageAndSetEntity(Product product, MultipartFile file) {
        if (file == null)
            return;
        String imageUrl = cloudinaryFacadeService.uploadProductBlob(file);
        product.setImageUrlId(imageUrl);
    }


}
