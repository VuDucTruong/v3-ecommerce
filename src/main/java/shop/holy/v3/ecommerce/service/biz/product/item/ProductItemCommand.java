package shop.holy.v3.ecommerce.service.biz.product.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.holy.v3.ecommerce.api.dto.product.item.*;
import shop.holy.v3.ecommerce.persistence.entity.product.ProductItem;
import shop.holy.v3.ecommerce.persistence.entity.product.ProductItemUsed;
import shop.holy.v3.ecommerce.persistence.projection.ProQ_Id_ProductId_AcceptedKey;
import shop.holy.v3.ecommerce.persistence.projection.ProQ_ProductId_Quantity;
import shop.holy.v3.ecommerce.persistence.repository.product.IProductItemRepository;
import shop.holy.v3.ecommerce.persistence.repository.product.IProductItemUsedRepository;
import shop.holy.v3.ecommerce.persistence.repository.product.IProductRepository;
import shop.holy.v3.ecommerce.shared.mapper.CommonMapper;
import shop.holy.v3.ecommerce.shared.mapper.JsonMapper;
import shop.holy.v3.ecommerce.shared.mapper.product.ProductItemMapper;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ProductItemCommand {
    private final IProductItemRepository productItemRepository;
    private final IProductItemUsedRepository usedRepository;
    private final IProductRepository productRepository;
    private final ProductItemMapper mapper;
    private final JsonMapper jsonMapper;

    @Transactional
    public ResponseProductItemCreate inserts(List<RequestProductItemCreate> requests, boolean used, boolean ignoreDeleted) {

        Set<Long> groupedProductIds = requests.stream().map(RequestProductItemCreate::productId).collect(Collectors.toSet());
        Set<Long> existingProdIds;
        if (ignoreDeleted)
            existingProdIds = productRepository.findExistingProductIds(groupedProductIds);
        else
            existingProdIds = productRepository.findExistingProductIdsAndDeletedAtIsNull(groupedProductIds);

        Stream<RequestProductItemCreate> insertable = requests.stream()
                .filter(s -> existingProdIds.contains(s.productId()));

        List<ProQ_Id_ProductId_AcceptedKey> accepted;
        if (used) {
            ProductItemUsed[] usedItems = insertable.map(mapper::from_Request_ToUsedEntity)
                    .toArray(ProductItemUsed[]::new);
            var batch = mapper.fromUsedEntityToBatchInsertDTO(usedItems);
            var batchAccounts = Arrays.stream(batch.accounts()).map(jsonMapper::writeJson).toArray(String[]::new);
            accepted = usedRepository.insertProductItems(batch.productIds(),batch.productKeys(), batchAccounts, batch.regions());
        } else {

            ProductItem[] productItems = insertable.map(mapper::fromRequestToEntity)
                    .toArray(ProductItem[]::new);
            var batch = mapper.fromEntityToBatchInsertDTO(productItems);

            ///  MAY BE THE ACCEPTED?
            var batchAccounts = Arrays.stream(batch.accounts()).map(jsonMapper::writeJson).toArray(String[]::new);
            accepted = productItemRepository.insertProductItems(batch.productIds(),batch.productKeys(), batchAccounts, batch.regions());
            ///  TO UPDATE PRODUCT'S QUANTITY
            accepted.stream().collect(
                    Collectors.groupingBy(ProQ_Id_ProductId_AcceptedKey::getProductId,
                            Collectors.reducing(0, _ -> 1, Integer::sum))
            ).forEach(productRepository::updateAddProductItemCountsByProductIdEquals);
        }


        var results = accepted.stream().map(a -> new ResponseProductItemCreate.ResponseAccepted(a.getId(), a.getProductId(), a.getAcceptedKey(), a.getAccount()))
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
    public int markItemUsed(long[] ids) {
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