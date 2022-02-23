package com.hy.demo.Domain.Board.Repository;

import com.hy.demo.Domain.Board.Dto.CourseDto;
import com.hy.demo.Domain.Board.Entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseRepositoryCustom {

    public Page<Course> findByCourseNameAndUser(String CourseName, Pageable pageable);
    public Page<CourseDto> findByCourseNameAndUserDTO(String courseName, Pageable pageable);
}
