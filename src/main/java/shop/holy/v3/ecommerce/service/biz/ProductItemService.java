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
import shop.holy.v3.ecommerce.persistence.projection.ProQ_ProductId_Quantity;
import shop.holy.v3.ecommerce.persistence.projection.ProQ_ProductId_AcceptedKey;
import shop.holy.v3.ecommerce.persistence.repository.IProductItemRepository;
import shop.holy.v3.ecommerce.persistence.repository.IProductRepository;
import shop.holy.v3.ecommerce.shared.constant.BizErrors;
import shop.holy.v3.ecommerce.shared.constant.DefaultValues;
import shop.holy.v3.ecommerce.shared.mapper.ProductItemMapper;
import shop.holy.v3.ecommerce.shared.util.MappingUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductItemService {
    private final IProductItemRepository productItemRepository;
    private final IProductRepository productRepository;
    private final ProductItemMapper mapper;

    public ResponsePagination<ResponseProductItem> search(RequestProductItemSearch searchReq) {
        Specification<ProductItem> specs = mapper.fromRequestSearchToSpec(searchReq);
        Pageable pageable = MappingUtils.fromRequestPageableToPageable(searchReq.pageRequest());
        Page<ProductItem> productItems = productItemRepository.findAll(specs, pageable);
        Page<ResponseProductItem> responses = productItems.map(mapper::fromEntityToResponse);
        return ResponsePagination.fromPage(responses);
    }

    public ResponseProductItem getByIdentifier(long id, String productKey, boolean deleted) {
        if (id == DefaultValues.ID && productKey == null)
            return null;
        ProductItem rs;
        if (id != DefaultValues.ID)
            if (deleted)
                rs = productItemRepository.findById(id).orElseThrow(BizErrors.RESOURCE_NOT_FOUND::exception);
            else
                rs = productItemRepository.findFirstByIdAndDeletedAtIsNull(id).orElseThrow(BizErrors.RESOURCE_NOT_FOUND::exception);
        else if (deleted)
            rs = productItemRepository.findFirstByProductKey(productKey).orElseThrow(BizErrors.RESOURCE_NOT_FOUND::exception);
        else
            rs = productItemRepository.findFirstByProductKeyAndDeletedAtIsNull(productKey).orElseThrow(BizErrors.RESOURCE_NOT_FOUND::exception);

        return mapper.fromEntityToResponse(rs);
    }

    public void testInsert() {
        long[] prod_ids = new long[]{9L, 9L, 10L};
        String[] prod_keys = {"1", "2", "3"};
        String[] regions = {"1", "2", "3"};
//        List<String> accepted = productItemRepository.insertProductItems(prod_ids, prod_keys, regions);
//        System.out.println(accepted);
    }

    @Transactional
    public ResponseProductItemCreate inserts(RequestProductItemCreate[] requests) {
        ProductItem[] productItems = Arrays.stream(requests).map(mapper::fromRequestToEntity)
                .filter(item -> item.getProductKey() != null)
                .toArray(ProductItem[]::new);

        long[] prod_ids = new long[productItems.length];
        String[] prod_keys = new String[productItems.length];
        String[] regions = new String[productItems.length];
        for (int i = 0; i < productItems.length; i++) {
            prod_ids[i] = productItems[i].getProductId();
            prod_keys[i] = productItems[i].getProductKey();
            regions[i] = productItems[i].getRegion();
        }

        ///  MAY BE THE ACCEPTED?
        List<ProQ_ProductId_AcceptedKey> accepted = productItemRepository.insertProductItems(prod_ids, prod_keys, regions);
        accepted.stream().collect(
                Collectors.groupingBy(ProQ_ProductId_AcceptedKey::getProductId,
                        Collectors.reducing(0, _ -> 1, Integer::sum))
        ).forEach(productRepository::updateAddProductItemCountsByProductIdEquals);

        return new ResponseProductItemCreate(accepted.stream().map(ProQ_ProductId_AcceptedKey::getAcceptedKey).toArray(String[]::new));
    }

    @Transactional
    public int update(RequestProductItemUpdate request) {
        ProductItem productItem = mapper.fromUpdateRequestToEntity(request);
        int changes = productItemRepository.updateProductItemById(productItem);
        return changes;
    }

    @Transactional
    public int deleteProductItems(long[] ids) {
        List<ProQ_ProductId_Quantity> id_qty_pair = productItemRepository.deleteProductItemsByIdIn(ids);
        for (ProQ_ProductId_Quantity id_qty : id_qty_pair) {
            productRepository.updateAddProductItemCountsByProductIdEquals(id_qty.getProductId(), -id_qty.getQuantity());
        }

        return id_qty_pair.size();
    }


}