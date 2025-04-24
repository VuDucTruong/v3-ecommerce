package shop.holy.v3.ecommerce.service.biz;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.holy.v3.ecommerce.api.dto.product.group.RequestProductGroupCreate;
import shop.holy.v3.ecommerce.api.dto.product.group.RequestProductGroupUpdate;
import shop.holy.v3.ecommerce.api.dto.product.group.ResponseProductGroup;
import shop.holy.v3.ecommerce.persistence.entity.ProductGroup;
import shop.holy.v3.ecommerce.persistence.repository.IProductGroupRepository;
import shop.holy.v3.ecommerce.persistence.repository.IProductRepository;
import shop.holy.v3.ecommerce.shared.constant.BizErrors;
import shop.holy.v3.ecommerce.shared.mapper.ProductGroupMapper;

@Service
@RequiredArgsConstructor
public class ProductGroupService {
    private final IProductGroupRepository productGroupRepository;
    private final ProductGroupMapper mapper;

    public ResponseProductGroup[] getAllProductGroups() {
        return productGroupRepository.findAll().stream()
                .map(group -> new ResponseProductGroup(group.getId(), group.getName()))
                .toArray(ResponseProductGroup[]::new);
    }

    @Transactional
    public ResponseProductGroup insertProductGroup(RequestProductGroupCreate request) {
        ProductGroup productGroup = mapper.fromCreateRequestToEntity(request);
        productGroupRepository.save(productGroup);

        return mapper.fromEntityToResponse(productGroup);
    }

    @Transactional
    public ResponseProductGroup updateProductGroup(RequestProductGroupUpdate request) {
        ProductGroup productGroup = mapper.fromUpdateRequestToEntity(request);
        productGroupRepository.updateProductGroupNameById(productGroup.getName(), productGroup.getId());
        return mapper.fromEntityToResponse(productGroup);
    }


    public ResponseProductGroup getProductGroupById(long id, boolean deleted) {
        ProductGroup productGroup;
        if (deleted)
            productGroup = productGroupRepository.findById(id).orElseThrow();
        else
            productGroup = productGroupRepository.findFirstByIdAndDeletedAtIsNull(id).orElseThrow(BizErrors.PRODUCT_NOT_FOUND::exception);
        return mapper.fromEntityToResponse(productGroup);
    }

    @Transactional
    public void deleteProductGroup(long id) {
        productGroupRepository.deleteById(id);
    }


}
