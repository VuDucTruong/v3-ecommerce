package shop.holy.v3.ecommerce.service.biz.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.holy.v3.ecommerce.api.dto.AuthAccount;
import shop.holy.v3.ecommerce.api.dto.ResponsePagination;
import shop.holy.v3.ecommerce.api.dto.comment.RequestComment;
import shop.holy.v3.ecommerce.api.dto.comment.RequestCommentSearch;
import shop.holy.v3.ecommerce.api.dto.comment.RequestCommentUpdate;
import shop.holy.v3.ecommerce.api.dto.comment.ResponseComment;
import shop.holy.v3.ecommerce.persistence.entity.Comment;
import shop.holy.v3.ecommerce.persistence.repository.ICommentRepository;
import shop.holy.v3.ecommerce.persistence.repository.IProfileRepository;
import shop.holy.v3.ecommerce.shared.constant.BizErrors;
import shop.holy.v3.ecommerce.shared.mapper.CommentMapper;
import shop.holy.v3.ecommerce.shared.util.MappingUtils;
import shop.holy.v3.ecommerce.shared.util.SecurityUtil;

import java.util.Date;
import java.util.Optional;
import java.util.function.BooleanSupplier;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final ICommentRepository commentRepository;
    private final IProfileRepository profileRepository;
    private final CommentMapper commentMapper;

    public ResponsePagination<ResponseComment.Flattened> search(RequestCommentSearch searchReq) {
        Specification<Comment> specs = commentMapper.fromSearchRequestToSpec(searchReq);
        AuthAccount authAccount = SecurityUtil.getAuthNonNull();
        if (authAccount.isCustomer())
            specs.and((Specification<Comment>) (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("authorId"), authAccount.getProfileId()));
        Pageable pageable = MappingUtils.fromRequestPageableToPageable(searchReq.pageRequest());
        Page<Comment> comments = commentRepository.findAll(specs, pageable);
        Page<ResponseComment.Flattened> pageRes = comments.map(commentMapper::fromEntityToResponseFlattened);
        return ResponsePagination.fromPage(pageRes);
    }

    public ResponseComment getById(long id, boolean deleted) {
        Optional<Comment> queryRs;
        if (deleted)
            queryRs = commentRepository.findById(id);
        else
            queryRs = commentRepository.findByIdAndDeletedAtIsNull(id);
        return queryRs.map(commentMapper::fromEntityToResponse).orElseThrow(BizErrors.RESOURCE_NOT_FOUND::exception);
    }

    public ResponsePagination<ResponseComment.Light> getSelfComments(boolean deleted, Pageable pageable) {
        Page<Comment> queryRs;
        AuthAccount authAccount = SecurityUtil.getAuthNonNull();

        if (deleted)
            queryRs = commentRepository.findAllByAuthorIdAndParentCommentIdIsNull(authAccount.getProfileId(), pageable);
        else
            queryRs = commentRepository.findAllByAuthorIdAndParentCommentIdIsNullAndDeletedAtIsNull(authAccount.getProfileId(), pageable);
        return ResponsePagination.fromPage(queryRs, commentMapper::fromEntityToResponseLight);
    }


    public ResponsePagination<ResponseComment.Light> getCommentsByProductId(long productId, boolean deleted, Pageable pageable) {
        var comments = commentRepository.findAllByProductIdAndParentCommentIdIsNull(productId, pageable);
        Page<ResponseComment.Light> rs;
        if (deleted)
            rs = comments.map(commentMapper::fromEntityToResponseLight);
        else
            rs = comments.map(c -> {
                if (c.getDeletedAt() != null)
                    c.setContent(null);
                return commentMapper.fromEntityToResponseLight(c);
            });
        return ResponsePagination.fromPage(rs);
    }

    @Transactional
    public ResponseComment insert(RequestComment request) {
        AuthAccount authAccount = SecurityUtil.getAuthNonNull();
        Comment comment = commentMapper.fromCreateRequestToEntity(request);
        comment.setAuthorId(authAccount.getProfileId());
        Comment result = commentRepository.save(comment);
        profileRepository.findById(authAccount.getProfileId()).ifPresent(result::setAuthor);
        result.setCreatedAt(new Date());

        return commentMapper.fromEntityToResponse(result);
    }

    public ResponseComment update(RequestCommentUpdate request) {
        String content = request.content();
        Comment comment = commentRepository.updateContentById(content, request.id());
        return commentMapper.fromEntityToResponse(comment);
    }

    @Transactional
    public int deleteComment(long id) {
        AuthAccount authAccount = SecurityUtil.getAuthNonNull();
        var optionalDeletedItem = commentRepository.updateDeletedAtById(id);
        if (optionalDeletedItem.isEmpty())
            return 0;
        var deletedItem = optionalDeletedItem.get();

        BooleanSupplier isOwned = () -> deletedItem.getId() == authAccount.getId();
        SecurityUtil.validateBizResources(authAccount.getRole(), deletedItem.getRole(), isOwned);
        return 1;
    }

    @Transactional
    public int deleteComments(long[] ids) {
        if (ids == null || ids.length == 0)
            return 0;
        ///  TODO: VALIDATE HERE AS WELL
        return commentRepository.updateDeletedAtByIdIn(ids);
    }


}
