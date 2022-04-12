package com.hy.demo.Domain.User.Service;

import com.hy.demo.Domain.Course.Dto.CourseDto;
import com.hy.demo.Domain.Course.Entity.Course;
import com.hy.demo.Domain.Course.Repository.CourseRepository;
import com.hy.demo.Domain.Course.Service.CourseService;
import com.hy.demo.Domain.User.Dto.UserDto;
import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Entity.UserCourse;
import com.hy.demo.Domain.User.Repository.UserCourseRepository;
import com.hy.demo.Domain.User.Repository.UserRepository;
import com.hy.demo.Utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static org.springframework.data.util.Optionals.ifPresentOrElse;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserCourseRepository userCourseRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CourseRepository courseRepository;


    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Transactional
    public void register(User user, User provider) throws DuplicateKeyException {
        User byUsername = userRepository.findByUsername(user.getUsername());
        boolean empty = ObjectUtils.isEmpty(byUsername);
        Long count = userRepository.countByNickname(user.getNickname());

        if (count != 0) {
            throw new DuplicateKeyException("닉네임");
        }
        if (!empty) {
            throw new DuplicateKeyException("아이디");
        }
        if (provider == null) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            logger.info("user.getPassword() = " + user.getPassword());
            userRepository.save(user);
        } else {
            User user2 = User.builder()
                    .username(provider.getUsername())
                    .password(provider.getPassword())
                    .email(provider.getEmail())
                    .role(user.getRole())
                    .provider(provider.getProvider())
                    .providerId(provider.getProviderId())
                    .selfIntroduction(user.getSelfIntroduction())
                    .nickname(user.getNickname())
                    .build();
            logger.info("user.getPassword() = " + user2.getPassword());
            userRepository.save(user2);
        }

    }


    public Boolean loginForm(User user) {


        String username = user.getUsername();
        User findUser = userRepository.findByUsername(username);
        if (findUser != null) {
            logger.info("회원");
            return true;
        } else {
            logger.info("비회원");
            return false;
        }
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    public User findProfileImageByUser(User user) {
        User findUser = Optional.ofNullable(userRepository.findByUsername(user.getUsername())).orElseThrow(() -> new EntityNotFoundException("이미지없음"));

        return findUser;
    }

    public void updateUserProfileImage(String profileImage, User user) {
        User findUser = userRepository.findByUsername(user.getUsername());
        findUser.updateProfileImage(profileImage);
        userRepository.save(findUser);
    }

    public Map findByUserCourse(String course, User user) {
        Long Lid = Long.parseLong(course);
        logger.info("idss= " + Lid);
        CourseDto courseDto = courseService.findDetailCourse(Lid);
        User findUser = findByUsername(user.getUsername());
        UserCourse findUserCourse = userCourseRepository.findByUserAndCourse(findUser, courseDto.returnEntity());
        Map map = new HashMap();
        map.put("course", courseDto);
        map.put("userCourse", findUserCourse);
        return map;

    }

    public UserDto findUserInfo(String username) {
        User findUser = Optional.ofNullable(userRepository.findByUsername(username)).orElseThrow(() -> new EntityNotFoundException("권한없음"));

        logger.info("findUser.changeDto().toString() = " + findUser.changeDto().toString());
        return findUser.changeDto();

    }


    public void application(Long id, String usernames) {


        User username = userRepository.findByUsername(usernames);
        Optional<Course> course = courseRepository.findById(id);
        UserCourse userCourse = UserCourse.builder()
                .user(username)
                .course(course.orElseThrow(IllegalArgumentException::new))
                .build();
        userCourseRepository.save(userCourse);

    }

    public UserDto userUpdate(User user, UserDto update) {
        User findUser = Optional.ofNullable(userRepository.findByUsername(user.getUsername())).orElseThrow(() -> new EntityNotFoundException("권한없음"));
        Long count = userRepository.countByNicknameIsAndUsernameIsNot(update.getNickname(), user.getUsername());
        if (count != 0) {
            throw new EntityNotFoundException("닉네임중복");
        }
        findUser.updateEmail(update.getEmail());
        findUser.updateSelfIntroduction(update.getSelfIntroduction());
        findUser.updateNickname(update.getNickname());
        User updateUser = userRepository.save(findUser);
        return updateUser.changeDto();
    }


    public UserDto passwordUpdate(User user, String password, String newPassword) {
        User findUser = Optional.ofNullable(userRepository.findByUsername(user.getUsername())).orElseThrow(() -> new EntityNotFoundException("권한없음"));


        if (!bCryptPasswordEncoder.matches(password, findUser.getPassword())) {
            throw new AccessDeniedException("권한없음");
        }
        findUser.updatePassword(bCryptPasswordEncoder.encode(newPassword));
        User updateUser = userRepository.save(findUser);
        return updateUser.changeDto();

    }


    public Long countDateRegisteredUserCount(Long courseId, String date) {
        return userCourseRepository.countDateRegisteredUserCountByCourseId(courseId, date);
    }

    public Map countMonthlyToDayRegisteredUser(Long courseId, String date) {
        return userCourseRepository.countMonthlyToDayRegisteredUserByCourseId(courseId, date);
    }

    public Map countThisYearToMonthlyRegisteredUser(Long courseId, String date) {
        return userCourseRepository.countThisYearToMonthlyRegisteredUserByCourseId(courseId, date);
    }

    public Map countTenYearToYearRegisteredUser(Long courseId, String date) {
        return userCourseRepository.countTenYearToYearRegisteredUserByCourseId(courseId, date);
    }

    public List<UserDto> RankRandomUser(int amount) {

        return userCourseRepository.findRankRandomUserById(amount);

    }


    public List<UserDto> rankRandomUser(int amount) {

        return userCourseRepository.findRankRandomUserById(amount);

    }


}
