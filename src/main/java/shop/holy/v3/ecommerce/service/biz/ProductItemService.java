package shop.holy.v3.ecommerce.service.biz;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import shop.holy.v3.ecommerce.api.dto.ResponsePagination;
import shop.holy.v3.ecommerce.api.dto.product.item.*;
import shop.holy.v3.ecommerce.persistence.entity.ProductItem;
import shop.holy.v3.ecommerce.persistence.entity.ProductItemUsed;
import shop.holy.v3.ecommerce.persistence.projection.ProQ_ProductId_AcceptedKey;
import shop.holy.v3.ecommerce.persistence.projection.ProQ_ProductId_Quantity;
import shop.holy.v3.ecommerce.persistence.repository.IProductItemRepository;
import shop.holy.v3.ecommerce.persistence.repository.IProductItemUsedRepository;
import shop.holy.v3.ecommerce.persistence.repository.IProductRepository;
import shop.holy.v3.ecommerce.shared.constant.BizErrors;
import shop.holy.v3.ecommerce.shared.constant.DefaultValues;
import shop.holy.v3.ecommerce.shared.mapper.ProductItemMapper;
import shop.holy.v3.ecommerce.shared.util.MappingUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductItemService {
    private final IProductItemRepository productItemRepository;
    private final IProductItemUsedRepository usedRepository;
    private final IProductRepository productRepository;
    private final ProductItemMapper mapper;

    public ResponsePagination<ResponseProductItem> search(RequestProductItemSearch searchReq) {
        boolean used = searchReq.used();
        Pageable pageable = MappingUtils.fromRequestPageableToPageable(searchReq.pageRequest());
        if (used) {
            Specification<ProductItemUsed> specs = mapper.fromRequestSearchToSpec(searchReq);
            Page<ProductItemUsed> usedProductItems = usedRepository.findAll(specs, pageable);
            Page<ResponseProductItem> responses = usedProductItems.map(mapper::fromUsedEntityToResponse);
            return ResponsePagination.fromPage(responses);
        }

        Specification<ProductItem> specs = mapper.fromRequestSearchToSpec(searchReq);
        Page<ProductItem> productItems = productItemRepository.findAll(specs, pageable);
        Page<ResponseProductItem> responses = productItems.map(mapper::fromEntityToResponse);
        return ResponsePagination.fromPage(responses);
    }

    public ResponseProductItem[] getByIdentifier(long id, long productId, long orderDetailId, String productKey) {
        if (id == DefaultValues.ID && productKey == null)
            return null;
        if (id != DefaultValues.ID) {
            var rs = productItemRepository.findById(id).orElseThrow(BizErrors.RESOURCE_NOT_FOUND::exception);
            return new ResponseProductItem[]{mapper.fromEntityToResponse(rs)};

        } else if (productId != DefaultValues.ID)
            return usedRepository.findAllByOrderDetailIdEquals(orderDetailId).stream()
                    .map(mapper::fromUsedEntityToResponse)
                    .toArray(ResponseProductItem[]::new);

        else if (StringUtils.hasLength(productKey)) {
            var rs = productItemRepository.findFirstByProductKey(productKey).orElseThrow(BizErrors.RESOURCE_NOT_FOUND::exception);
            return new ResponseProductItem[]{mapper.fromEntityToResponse(rs)};
        }

        var rs = productItemRepository.findAllByProductIdEquals(productId, Pageable.unpaged(Sort.by(Sort.Direction.DESC, "createdAt"))).getContent();
        return rs.stream().map(mapper::fromEntityToResponse)
                .toArray(ResponseProductItem[]::new);
    }

    @Transactional
    public ResponseProductItemCreate inserts(RequestProductItemCreate[] requests, boolean used) {
        if (used) {
            ProductItemUsed[] usedItems = Arrays.stream(requests).map(mapper::from_Request_ToUsedEntity)
                    .filter(item -> item.getProductKey() != null)
                    .toArray(ProductItemUsed[]::new);
            var tri = mapper.from_UsedEntity_To_Tri_Arrays(usedItems);
            var accepted = usedRepository.insertProductItems(tri.getLeft(), tri.getMiddle(), tri.getRight());
            return new ResponseProductItemCreate(accepted.stream().map(ProQ_ProductId_AcceptedKey::getAcceptedKey).toArray(String[]::new));
        }

        ProductItem[] productItems = Arrays.stream(requests).map(mapper::fromRequestToEntity)
                .filter(item -> item.getProductKey() != null)
                .toArray(ProductItem[]::new);
        var tri = mapper.from_Entity_To_Tri_Arrays(productItems);

        ///  MAY BE THE ACCEPTED?
        List<ProQ_ProductId_AcceptedKey> accepted = productItemRepository.insertProductItems(tri.getLeft(), tri.getMiddle(), tri.getRight());
        ///  TO UPDATE PRODUCT'S QUANTITY
        accepted.stream().collect(
                Collectors.groupingBy(ProQ_ProductId_AcceptedKey::getProductId,
                        Collectors.reducing(0, _ -> 1, Integer::sum))
        ).forEach(productRepository::updateAddProductItemCountsByProductIdEquals);

        return new ResponseProductItemCreate(accepted.stream().map(ProQ_ProductId_AcceptedKey::getAcceptedKey).toArray(String[]::new));
    }


    @Transactional
    public int update(RequestProductItemUpdate request) {
        ProductItem productItem = mapper.fromUpdateRequestToEntity(request);
        return productItemRepository.updateProductItemById(productItem);
    }

    @Transactional
    public int deleteProductItems(long[] ids) {
        List<ProQ_ProductId_Quantity> id_qty_pair = productItemRepository.deleteProductItemsByIdIn(ids);
        for (ProQ_ProductId_Quantity id_qty : id_qty_pair) {
            productRepository.updateAddProductItemCountsByProductIdEquals(id_qty.getProductId(), -id_qty.getQuantity());
        }
        return id_qty_pair.size();
    }

    @Transactional
    public int makeProductUsed(long[] ids) {
        var productItems = productItemRepository.deleteProductItemsByIdInAndReturnAll(ids);
        var useds = productItems.stream().map(mapper::from_NonUsedEntity_ToUsedEntity).toList();
        var groupByProdIdCount = useds.stream()
                .collect(Collectors.groupingBy(ProductItemUsed::getProductId, Collectors.counting()));
        for (var e : groupByProdIdCount.entrySet()) {
            productRepository.updateAddProductItemCountsByProductIdEquals(e.getKey(), -e.getValue());
        }

        var addedToUseds = usedRepository.saveAll(useds);
        return addedToUseds.size();
    }


}