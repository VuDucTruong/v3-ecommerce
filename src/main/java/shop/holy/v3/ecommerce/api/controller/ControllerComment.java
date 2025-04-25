package shop.holy.v3.ecommerce.api.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.holy.v3.ecommerce.api.dto.ResponsePagination;
import shop.holy.v3.ecommerce.api.dto.comment.RequestComment;
import shop.holy.v3.ecommerce.api.dto.comment.RequestCommentSearch;
import shop.holy.v3.ecommerce.api.dto.comment.ResponseComment;
import shop.holy.v3.ecommerce.persistence.entity.Comment;
import shop.holy.v3.ecommerce.service.biz.CommentService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("comments")
@Tag(name = "Comments")
public class ControllerComment {

    private final CommentService commentService;

    @PostMapping("/searches")
    public ResponseEntity<ResponsePagination<ResponseComment>> search(
            @RequestBody RequestCommentSearch searchReq
    ) {
        return ResponseEntity.ok(commentService.search(searchReq));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseComment> getCommentById(
            long id,
            boolean deleted
    ) {
        return ResponseEntity.ok(commentService.getById(id, deleted));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCommentById(
            long id
    ) {
        return ResponseEntity.ok(commentService.deleteCategory(id));
    }

    @PostMapping("")
    public ResponseEntity<ResponseComment> createComment(
            @RequestBody RequestComment request
    ) {
        return ResponseEntity.ok(commentService.insert(request));
    }


}
