package shop.holy.v3.ecommerce.service.biz;

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
import shop.holy.v3.ecommerce.api.dto.comment.ResponseComment;
import shop.holy.v3.ecommerce.persistence.entity.Comment;
import shop.holy.v3.ecommerce.persistence.repository.ICommentRepository;
import shop.holy.v3.ecommerce.shared.constant.BizErrors;
import shop.holy.v3.ecommerce.shared.mapper.CommentMapper;
import shop.holy.v3.ecommerce.shared.util.SecurityUtil;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final ICommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public ResponsePagination<ResponseComment> search(RequestCommentSearch searchReq) {
        Specification<Comment> specs = commentMapper.fromSearchRequestToSpec(searchReq);
        Pageable pageable = commentMapper.fromRequestPageableToPageable(searchReq.pageRequest());
        Page<Comment> comments = commentRepository.findAll(specs, pageable);
        var pageRes = comments
                .map(commentMapper::fromEntityToResponse);
        return ResponsePagination.fromPage(pageRes);
    }

    public ResponseComment getById(long id, boolean deleted) {
        Optional<Comment> optionalComment;
        if (deleted)
            optionalComment = commentRepository.findById(id);
        else
            optionalComment = commentRepository.findFirstByIdEqualsAndDeletedAtIsNull(id);

        return optionalComment.map(commentMapper::fromEntityToResponse)
                .orElseThrow(BizErrors.RESOURCE_NOT_FOUND::exception);
    }

    @Transactional
    public ResponseComment insert(RequestComment request) {
        AuthAccount authAccount = SecurityUtil.getAuthNonNull();
        Comment comment = commentMapper.fromCreateRequestToEntity(request);
        comment.setAuthorId(authAccount.getProfileId());

        return commentMapper.fromEntityToResponse(commentRepository.save(comment));

    }

    @Transactional
    public int deleteCategory(long id) {
        return commentRepository.updateDeletedAtById(id);
    }


}
