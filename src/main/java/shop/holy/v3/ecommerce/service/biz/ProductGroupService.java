package shop.holy.v3.ecommerce.service.biz;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.holy.v3.ecommerce.api.dto.product.group.RequestProductGroupCreate;
import shop.holy.v3.ecommerce.api.dto.product.group.RequestProductGroupUpdate;
import shop.holy.v3.ecommerce.api.dto.product.group.ResponseProductGroup;
import shop.holy.v3.ecommerce.persistence.entity.ProductGroup;
import shop.holy.v3.ecommerce.persistence.repository.IProductGroupRepository;
import shop.holy.v3.ecommerce.shared.constant.BizErrors;

@Service
@RequiredArgsConstructor
public class ProductGroupService {
    private final IProductGroupRepository productGroupRepository;

    public ResponseProductGroup[] getAllProductGroups() {
        return productGroupRepository.findAll().stream()
                .map(group -> new ResponseProductGroup(group.getId(), group.getName()))
                .toArray(ResponseProductGroup[]::new);
    }

    public ResponseProductGroup insertProductGroup(RequestProductGroupCreate request) {
        ProductGroup productGroup = new ProductGroup();
        productGroup.setName(request.name());
        productGroupRepository.save(productGroup);
        return new ResponseProductGroup(productGroup.getId(), productGroup.getName());
    }

    public ResponseProductGroup updateProductGroup(RequestProductGroupUpdate request) {
        ProductGroup productGroup = productGroupRepository.findById(request.id()).orElseThrow(BizErrors.PRODUCT_NOT_FOUND::exception);
        productGroup.setName(request.name());
        productGroupRepository.save(productGroup);
        return new ResponseProductGroup(productGroup.getId(), productGroup.getName());
    }


    public ResponseProductGroup getProductGroupById(long id, boolean deleted) {
        ProductGroup productGroup;
        if (deleted)
            productGroup = productGroupRepository.findById(id).orElseThrow();
        else
            productGroup = productGroupRepository.findFirstByIdAndDeletedAtIsNull(id).orElseThrow(BizErrors.PRODUCT_NOT_FOUND::exception);
        return new ResponseProductGroup(productGroup.getId(), productGroup.getName());
    }

    public void deleteProductGroup(long id) {
        productGroupRepository.deleteById(id);
    }


}
