package shop.holy.v3.ecommerce.service.biz;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.holy.v3.ecommerce.api.dto.ResponsePagination;
import shop.holy.v3.ecommerce.api.dto.category.RequestCategoryCreate;
import shop.holy.v3.ecommerce.api.dto.category.RequestCategorySearch;
import shop.holy.v3.ecommerce.api.dto.category.RequestCategoryUpdate;
import shop.holy.v3.ecommerce.api.dto.category.ResponseCategory;
import shop.holy.v3.ecommerce.persistence.entity.Category;
import shop.holy.v3.ecommerce.persistence.repository.ICategoryRepository;
import shop.holy.v3.ecommerce.service.cloud.CloudinaryFacadeService;
import shop.holy.v3.ecommerce.shared.exception.ResourceNotFoundException;
import shop.holy.v3.ecommerce.shared.mapper.CategoryMapper;
import shop.holy.v3.ecommerce.shared.util.MappingUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final ICategoryRepository categoryRepository;
    private final CloudinaryFacadeService cloudinaryFacadeService;
    private final CategoryMapper categoryMapper;

    public ResponsePagination<ResponseCategory> search(RequestCategorySearch searchReq) {
        var spec = categoryMapper.fromRequestSearchToSpec(searchReq);
        Pageable pageable = MappingUtils.fromRequestPageableToPageable(searchReq.pageRequest());
        Page<Category> categories = categoryRepository.findAll(spec, pageable);
        Page<ResponseCategory> responses = categories.map(categoryMapper::fromEntityToResponse);
        return ResponsePagination.fromPage(responses);
    }

    public ResponseCategory getCategoryByCode(long id, boolean deleted) {
        Optional<Category> optionalCategory;

        if (deleted)
            optionalCategory = categoryRepository.findById(id);
        else
            optionalCategory = categoryRepository.findFirstByIdAndDeletedAtIsNull(id);
        return optionalCategory.map(categoryMapper::fromEntityToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("CATEGORY NOT FOUND BY ID " + id));
    }

    public ResponseCategory insert(RequestCategoryCreate request) {
        Category category = categoryMapper.fromCreateRequestToEntity(request);

        if (request.image() != null) {
            String imageUrl = cloudinaryFacadeService.uploadCategoryBlob(request.image());
            category.setImageUrlId(imageUrl);
        }
        return this.upsert(category);
    }

    public ResponseCategory update(RequestCategoryUpdate request) {
        Category category = categoryMapper.fromUpdateRequestToEntity(request);
        if (request.image() != null) {
            cloudinaryFacadeService.uploadCategoryBlob(request.image());
            String imageUrl = cloudinaryFacadeService.uploadBlogBlob(request.image());
            category.setImageUrlId(imageUrl);
        }
        return this.upsert(category);
    }

    public ResponseCategory upsert(Category category) {
        return categoryMapper.fromEntityToResponse(categoryRepository.save(category));
    }

    @Transactional(timeout = 15)
    public void deleteCategory(long id, boolean isHard) {
        categoryRepository.deleteProductCategoryByCategoryIdEquals(id);
        if (isHard)
            categoryRepository.deleteById(id);
        else
            categoryRepository.updateDeletedAtById(id);

    }


}
