package com.hy.demo.Domain.Board.Dto;

import lombok.*;

import java.sql.Date;
import java.sql.Timestamp;


@Data
@NoArgsConstructor
public class CourseBoardDto {

    private Long id;
    private String title;
    private String contents;
    private Long views;
    private Date createDate;
    private String teachName;
    private String courseName;

    public CourseBoardDto(Long id, String title, Long views, Timestamp createDate, String contents, String teachName, String courseName) {
        this.id = id;
        this.title = title;
        this.views = views;
        this.createDate = new Date(createDate.getTime());
        this.contents = contents;
        this.teachName = teachName;
        this.courseName = courseName;
    }
}
