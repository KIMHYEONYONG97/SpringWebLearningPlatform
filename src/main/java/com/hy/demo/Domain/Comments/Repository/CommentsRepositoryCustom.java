package com.hy.demo.Domain.Comments.Repository;

import com.hy.demo.Domain.Comments.Dto.CommentsDto;
import com.hy.demo.Domain.Comments.Entity.Comments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CommentsRepositoryCustom {
    Page<CommentsDto> findByCourseBoardId(Long courseBoardId, Pageable pageable);
    Page<CommentsDto> findReplyByIds(Long id, Pageable pageable);
    Optional<Comments> findByIdAndUser(Long id, String username);
}
