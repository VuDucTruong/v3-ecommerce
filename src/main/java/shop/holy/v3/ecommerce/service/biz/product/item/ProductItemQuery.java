package shop.holy.v3.ecommerce.service.biz.product.item;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import shop.holy.v3.ecommerce.api.dto.ResponsePagination;
import shop.holy.v3.ecommerce.api.dto.product.item.RequestProductItemSearch;
import shop.holy.v3.ecommerce.api.dto.product.item.ResponseProductItems_Indetails;
import shop.holy.v3.ecommerce.persistence.entity.product.ProductItem;
import shop.holy.v3.ecommerce.persistence.entity.product.ProductItemUsed;
import shop.holy.v3.ecommerce.persistence.projection.ProQ_ProductMetadata;
import shop.holy.v3.ecommerce.persistence.repository.product.IProductItemRepository;
import shop.holy.v3.ecommerce.persistence.repository.product.IProductItemUsedRepository;
import shop.holy.v3.ecommerce.shared.constant.BizErrors;
import shop.holy.v3.ecommerce.shared.mapper.product.ProductItemMapper;
import shop.holy.v3.ecommerce.shared.util.MappingUtils;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductItemQuery {
    private final IProductItemRepository productItemRepository;
    private final IProductItemUsedRepository usedRepository;
    private final ProductItemMapper mapper;


    public ResponsePagination<ResponseProductItems_Indetails> search(RequestProductItemSearch searchReq) {
        boolean used = searchReq.used();
        Pageable pageable = MappingUtils.fromRequestPageableToPageable(searchReq.pageRequest());
        if (used) {
            Specification<ProductItemUsed> specs = mapper.fromRequestSearchToSpec(searchReq);
            var usedProdItems = usedRepository.findBy(specs, q -> q.as(ProQ_ProductMetadata.class).page(pageable));
            var responses = usedProdItems.map(item ->
                    mapper.fromProjection_InDetail_ToResponse(item, used));
            return ResponsePagination.fromPage(responses);
        }
        Specification<ProductItem> specs = mapper.fromRequestSearchToSpec(searchReq);
        var productItems = productItemRepository.findBy(specs, q -> q.as(ProQ_ProductMetadata.class).page(pageable));

        var responses = productItems.map(item ->
                mapper.fromProjection_InDetail_ToResponse(item, used));
        return ResponsePagination.fromPage(responses);
    }

    public ResponseProductItems_Indetails[] getByIdentifier(Long id, Long productId, Long orderDetailId, String productKey, boolean used) {
//        List<ProQ_ProductMetadata> results;
        if (id != null) {
            Optional<ProQ_ProductMetadata> optItem;
            if (used)
                optItem = usedRepository.findFirstById(id);
            else
                optItem = productItemRepository.findFirstById(id);
            boolean finalUsed1 = used;
            return new ResponseProductItems_Indetails[]{optItem
                    .map(item -> mapper.fromProjection_InDetail_ToResponse(item, finalUsed1))
                    .orElseThrow(BizErrors.RESOURCE_NOT_FOUND::exception)};

        }
        if (StringUtils.hasLength(productKey)) {
            Optional<ProQ_ProductMetadata> optItem;
            if (used)
                optItem = usedRepository.findFirstByProductKey(productKey);
            else
                optItem = productItemRepository.findFirstByProductKey(productKey);
            boolean finalUsed2 = used;
            return new ResponseProductItems_Indetails[]{optItem
                    .map(item -> mapper.fromProjection_InDetail_ToResponse(item, finalUsed2))
                    .orElseThrow(BizErrors.RESOURCE_NOT_FOUND::exception)};
        }
        List<ProQ_ProductMetadata> results;
        if (orderDetailId != null) {
            results = usedRepository.findAllByOrderDetailIdEquals(orderDetailId);
            used = true;
        } else ///  ELSE PRODUCT_id
            results = productItemRepository.findAllByProductIdEquals(productId);
        boolean finalUsed = used;
        return results.stream().map(item -> mapper.fromProjection_InDetail_ToResponse(item, finalUsed))
                .toArray(ResponseProductItems_Indetails[]::new);
    }


}
