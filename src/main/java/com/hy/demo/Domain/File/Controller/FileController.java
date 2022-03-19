package com.hy.demo.Domain.File.Controller;

import com.hy.demo.Config.Auth.PrincipalDetails;
import com.hy.demo.Domain.Board.Dto.CourseBoardDto;
import com.hy.demo.Domain.Board.Entity.CourseBoard;
import com.hy.demo.Domain.Board.Service.CourseBoardService;
import com.hy.demo.Domain.Course.Dto.CourseDto;
import com.hy.demo.Domain.Course.Entity.Course;
import com.hy.demo.Domain.Course.Service.CourseService;
import com.hy.demo.Domain.File.Dto.FileDto;
import com.hy.demo.Domain.File.Service.FileService;
import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Service.UserService;
import com.hy.demo.Utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

;

@Controller
@RequestMapping("/file/*")
public class FileController {

 

    @Autowired
    private FileService fileService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());





    @GetMapping("/download/{courseId}/{courseBoardId}/{fileId}")
    @PreAuthorize("@authorizationChecker.isFile(#fileId,#courseId,#courseBoardId)")
    public ResponseEntity<Object> BoardManagement(@PathVariable Long fileId, @PathVariable Long courseId,@PathVariable Long courseBoardId) throws IOException {

        Map<String, Object> map = fileService.fileDownLoad(fileId);
        File file = (File) map.get("file");
        Resource resource = (Resource) map.get("resource");
        String filename = (String) map.get("fileName");
        logger.info("filename = " + filename);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.builder("attachment").filename(URLEncoder.encode(filename, String.valueOf(StandardCharsets.UTF_8))).build());  // 다운로드 되거나 로컬에 저장되는 용도로 쓰이는지를 알려주는 헤더

        return new ResponseEntity<Object>(resource, headers, HttpStatus.OK);
    }




    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request, HttpServletResponse response) throws Exception {

        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<?> handleFileNotFoundException(FileNotFoundException e, HttpServletRequest request, HttpServletResponse response) throws Exception {

        return ResponseEntity.badRequest().build();
    }
    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> handleIOExceptionException(FileNotFoundException e, HttpServletRequest request, HttpServletResponse response) throws Exception {

        return ResponseEntity.badRequest().build();
    }



}

