package shop.holy.v3.ecommerce.service.biz;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.holy.v3.ecommerce.api.dto.AuthAccount;
import shop.holy.v3.ecommerce.api.dto.comment.RequestComment;
import shop.holy.v3.ecommerce.api.dto.comment.RequestCommentSearch;
import shop.holy.v3.ecommerce.api.dto.comment.ResponseComment;
import shop.holy.v3.ecommerce.persistence.entity.Comment;
import shop.holy.v3.ecommerce.persistence.repository.ICommentRepository;
import shop.holy.v3.ecommerce.shared.constant.BizErrors;
import shop.holy.v3.ecommerce.shared.exception.ResourceNotFoundException;
import shop.holy.v3.ecommerce.shared.mapper.CommentMapper;
import shop.holy.v3.ecommerce.shared.util.SecurityUtil;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final ICommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public List<ResponseComment> search(RequestCommentSearch searchReq) {
        List<Comment> comments;
        if (searchReq.deleted())
            comments = commentRepository.findAllByIdIn(searchReq.ids());
        else
            comments = commentRepository.findAllByIdInAndDeletedAtIsNull(searchReq.ids());

        return comments.stream()
                .map(commentMapper::fromEntityToResponse)
                .toList();
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

    @Transactional(timeout = 15)
    public ResponseComment insert(RequestComment request) {
        AuthAccount authAccount = SecurityUtil.getAuthNonNull();
        Comment comment = commentMapper.fromCreateRequestToEntity(request);
        comment.setAuthorId(authAccount.getId());
        return commentMapper.fromEntityToResponse(commentRepository.save(comment));

    }

    @Transactional(timeout = 15)
    public int deleteCategory(long id) {
        return commentRepository.updateDeletedAtById(id);
    }


}
