package shop.holy.v3.ecommerce.service.biz.product.tag;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.holy.v3.ecommerce.persistence.repository.product.IProductTagRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductTagQuery {

    private final IProductTagRepository productTagRepository;


    public List<String> getAll() {
        return productTagRepository.findALlDistinctName();
    }

}
