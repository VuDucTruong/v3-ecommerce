package shop.holy.v3.ecommerce.service.biz;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import shop.holy.v3.ecommerce.api.dto.ResponsePagination;
import shop.holy.v3.ecommerce.api.dto.product.item.*;
import shop.holy.v3.ecommerce.persistence.entity.ProductItem;
import shop.holy.v3.ecommerce.persistence.entity.ProductItemUsed;
import shop.holy.v3.ecommerce.persistence.projection.ProQ_Id_ProductId_AcceptedKey;
import shop.holy.v3.ecommerce.persistence.projection.ProQ_ProductId_Quantity;
import shop.holy.v3.ecommerce.persistence.projection.ProQ_ProductMetadata;
import shop.holy.v3.ecommerce.persistence.repository.IProductItemRepository;
import shop.holy.v3.ecommerce.persistence.repository.IProductItemUsedRepository;
import shop.holy.v3.ecommerce.persistence.repository.IProductRepository;
import shop.holy.v3.ecommerce.shared.constant.BizErrors;
import shop.holy.v3.ecommerce.shared.mapper.ProductItemMapper;
import shop.holy.v3.ecommerce.shared.util.MappingUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ProductItemService {
    private final IProductItemRepository productItemRepository;
    private final IProductItemUsedRepository usedRepository;
    private final IProductRepository productRepository;
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

    @Transactional
    public ResponseProductItemCreate inserts(List<RequestProductItemCreate> requests, boolean used) {
        Set<Long> groupedProductIds = requests.stream().map(RequestProductItemCreate::productId).collect(Collectors.toSet());
        Set<Long> existingProdIds = productRepository.findExistingProductIds(groupedProductIds);
        Stream<RequestProductItemCreate> insertable = requests.stream()
                .filter(s -> existingProdIds.contains(s.productId()));

        List<ProQ_Id_ProductId_AcceptedKey> accepted;
        if (used) {
            ProductItemUsed[] usedItems = insertable.map(mapper::from_Request_ToUsedEntity)
                    .toArray(ProductItemUsed[]::new);
            var tri = mapper.from_UsedEntity_To_Tri_Arrays(usedItems);
            accepted = usedRepository.insertProductItems(tri.getLeft(), tri.getMiddle(), tri.getRight());
        } else {

            ProductItem[] productItems = insertable.map(mapper::fromRequestToEntity)
                    .toArray(ProductItem[]::new);
            var tri = mapper.from_Entity_To_Tri_Arrays(productItems);

            ///  MAY BE THE ACCEPTED?
            accepted = productItemRepository.insertProductItems(tri.getLeft(), tri.getMiddle(), tri.getRight());
            ///  TO UPDATE PRODUCT'S QUANTITY
            accepted.stream().collect(
                    Collectors.groupingBy(ProQ_Id_ProductId_AcceptedKey::getProductId,
                            Collectors.reducing(0, _ -> 1, Integer::sum))
            ).forEach(productRepository::updateAddProductItemCountsByProductIdEquals);
        }
        var results = accepted.stream().map(a -> new ResponseProductItemCreate.ResponseAccepted(a.getId(),a.getProductId(), a.getAcceptedKey()))
                .toList();
        return new ResponseProductItemCreate(results);
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

    ///  FIX THIS
    @Transactional
    public int makeProductUsed(long[] ids) {
        var productItems = productItemRepository.deleteProductItemsByIdInAndReturnAll(ids);
//        productItemRepository.deleteByIdIn(ids);

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