package shop.holy.v3.ecommerce.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nonnull;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNullApi;
import org.springframework.web.bind.annotation.*;
import shop.holy.v3.ecommerce.api.dto.ResponsePagination;
import shop.holy.v3.ecommerce.api.dto.comment.RequestComment;
import shop.holy.v3.ecommerce.api.dto.comment.RequestCommentSearch;
import shop.holy.v3.ecommerce.api.dto.comment.RequestCommentUpdate;
import shop.holy.v3.ecommerce.api.dto.comment.ResponseComment;
import shop.holy.v3.ecommerce.service.biz.CommentService;

@RequiredArgsConstructor
@RestController
@RequestMapping("comments")
@Tag(name = "Comments")
public class ControllerComment {

    private final CommentService commentService;

    @GetMapping("{id}")
    @Operation(summary = "Get 1 Comment-> not commonly used", description = """
            ==> Vẫn trả về comment kể cả đã delete \n
            ==> phần comment nào bị delete sẽ bị content = null \n
            ====> if deleted = true, trả về comment và không bị content = null
            """)
    public ResponseEntity<ResponseComment> getResponseCommentById(@PathVariable Long id,
                                                                  @Parameter(description = "if true => will return content even deletedAt!=null (mark deleted)")
                                                                  @RequestParam(required = false) boolean deleted) {
        var rs = commentService.getById(id, deleted);
        return ResponseEntity.ok(rs);
    }

    @Operation(summary = "get many")
    @GetMapping("")
    public ResponseEntity<ResponsePagination<ResponseComment.Light>> getCommentsByProductIds(@RequestParam long productId,
                                                                                             @RequestParam(required = false) boolean deleted,
                                                                                             @ParameterObject Pageable pageable) {
        var cmt = commentService.getCommentsByProductId(productId, deleted, pageable);
        return ResponseEntity.ok(cmt);
    }


    @PostMapping("/searches")
    public ResponseEntity<ResponsePagination<ResponseComment>> search(
            @RequestBody RequestCommentSearch searchReq
    ) {
        return ResponseEntity.ok(commentService.search(searchReq));
    }


    @Operation(summary = "delete 1")
    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteCommentById(@PathVariable long id) {
        return ResponseEntity.ok(commentService.deleteComment(id));
    }

    @Operation(summary = "delete many")
    @DeleteMapping("")
    public ResponseEntity<Integer> deleteComments(@RequestParam long[] ids) {
        return ResponseEntity.ok(commentService.deleteComments(ids));
    }

    @Operation(summary = "create 1")
    @PostMapping("")
    public ResponseEntity<ResponseComment> createComment(
            @RequestBody RequestComment request
    ) {
        return ResponseEntity.ok(commentService.insert(request));
    }

    @Operation(summary = "update 1")
    @PutMapping("")
    public ResponseEntity<ResponseComment> updateComment(
            @RequestBody @NotNull(message = "Invalid Request must not be null") @Valid RequestCommentUpdate request){
        var cmt = commentService.update(request);
        return ResponseEntity.ok(cmt);
    }


}
