package shop.holy.v3.ecommerce.service.biz;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.holy.v3.ecommerce.api.dto.ResponsePagination;
import shop.holy.v3.ecommerce.api.dto.product.item.*;
import shop.holy.v3.ecommerce.persistence.entity.ProductItem;
import shop.holy.v3.ecommerce.persistence.repository.IProductItemRepository;
import shop.holy.v3.ecommerce.shared.constant.BizErrors;
import shop.holy.v3.ecommerce.shared.constant.DefaultValues;
import shop.holy.v3.ecommerce.shared.mapper.ProductItemMapper;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class ProductItemService {
    private final IProductItemRepository productItemRepository;
    private final ProductItemMapper mapper;

    public ResponsePagination<ResponseProductItem> search(RequestProductItemSearch searchReq) {
        Specification<ProductItem> specs = mapper.fromRequestSearchToSpec(searchReq);
        Pageable pageable = mapper.fromRequestPageableToPageable(searchReq.pageRequest());
        Page<ProductItem> productItems = productItemRepository.findAll(specs, pageable);
        Page<ResponseProductItem> responses = productItems.map(mapper::fromEntityToResponse);
        return ResponsePagination.fromPage(responses);
    }

    public ResponseProductItem getByIdentifier(long id, String productKey, boolean deleted) {
        if (id == DefaultValues.ID && productKey == null)
            return null;
        ProductItem rs;
        if (id != DefaultValues.ID) {
            if (deleted)
                rs = productItemRepository.findById(id).orElseThrow(BizErrors.RESOURCE_NOT_FOUND::exception);
            else
                rs = productItemRepository.findFirstByIdAndDeletedAtIsNull(id).orElseThrow(BizErrors.RESOURCE_NOT_FOUND::exception);
        } else {
            if (deleted)
                rs = productItemRepository.findFirstByProductKey(productKey).orElseThrow(BizErrors.RESOURCE_NOT_FOUND::exception);
            else
                rs = productItemRepository.findFirstByProductKeyAndDeletedAtIsNull(productKey).orElseThrow(BizErrors.RESOURCE_NOT_FOUND::exception);
        }
        return mapper.fromEntityToResponse(rs);
    }

    public ResponseProductItemCreate inserts(RequestProductItemCreate[] requests) {
        ProductItem[] productItems = Arrays.stream(requests).map(mapper::fromRequestToEntity)
                .filter(item -> item.getProductKey() != null)
                .toArray(ProductItem[]::new);
        long[] productIds = new long[productItems.length];
        String[] productKeys = new String[productItems.length];
        String[] regions = new String[productItems.length];
        for (int i = 0; i < productItems.length; i++) {
            productIds[i] = productItems[i].getProductId();
            productKeys[i] = productItems[i].getProductKey();
            regions[i] = productItems[i].getRegion();
        }
        String[] rejected = productItemRepository.insertProductWithOnConflictReturning(productIds, productKeys, regions);
        String[] inserted = Arrays.stream(productItems).map(ProductItem::getProductKey).toArray(String[]::new);
        return new ResponseProductItemCreate(inserted, rejected);
    }

    public ResponseProductItem update(RequestProductItemUpdate request) {
        ProductItem productItem = mapper.fromUpdateRequestToEntity(request);
        ProductItem rs = productItemRepository.save(productItem);
        return mapper.fromEntityToResponse(rs);
    }

    @Transactional
    public int deleteProductItems(long[] ids) {
        return productItemRepository.deleteProductItemsByIdIn(ids);
    }


}