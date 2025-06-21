package shop.holy.v3.ecommerce.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.holy.v3.ecommerce.api.dto.RequestPageable;
import shop.holy.v3.ecommerce.api.dto.blog.genre.RequestGenre1Upsert;
import shop.holy.v3.ecommerce.api.dto.blog.genre.ResponseGenre1;
import shop.holy.v3.ecommerce.api.dto.blog.genre.ResponseGenre1Blogs;
import shop.holy.v3.ecommerce.service.biz.blog.BlogQuery;
import shop.holy.v3.ecommerce.service.biz.blog.GenreService;

import java.util.Collection;
import java.util.HashSet;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
@Tag(name = "Genres", description = "May perform get only, as I may hard code this")
public class ControllerGenre {
    private final GenreService genreService;
    private final BlogQuery blogQuery;

    @PutMapping
    public ResponseEntity<ResponseGenre1> upsert(@RequestBody RequestGenre1Upsert request) {

        var x = genreService.upsert(request);
        return ResponseEntity.ok(x);
    }

    @GetMapping("identifier")
    public ResponseEntity<ResponseGenre1> getByIdentifier(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) boolean deleted) {
        var x = genreService.getByIdentifier(id, name, deleted);
        return ResponseEntity.ok(x);
    }

    @GetMapping("blogs")
    @Operation(summary = "given array ids[] -> genre1s -> blogs group by genre")
    public ResponseEntity<Collection<ResponseGenre1Blogs>> getPartitionBy(@RequestParam HashSet<Long> ids, @RequestParam(defaultValue = "10") Integer size) {
        var rs = blogQuery.findPartitionByGenre1Ids(ids, size);
        return ResponseEntity.ok(rs);
    }

    @GetMapping
    public ResponseEntity<ResponseGenre1[]> getAllGenres() {
        var x = genreService.getAllGenres();
        return ResponseEntity.ok(x);
    }


    @DeleteMapping("{id}")
    public ResponseEntity<ResponseGenre1> delete(long id) {
        genreService.deleteGenre(id);
        return ResponseEntity.ok().build();
    }


}
