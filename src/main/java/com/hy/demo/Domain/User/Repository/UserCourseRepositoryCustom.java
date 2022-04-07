package com.hy.demo.Domain.User.Repository;

import java.util.Map;

public interface UserCourseRepositoryCustom {
    Long countDateRegisteredUserCountByCourseId(Long courseId, String date);

    Map countMonthlyToDayRegisteredUserByCourseId(Long courseId, String date);

    Map countThisYearToMonthlyRegisteredUserByCourseId(Long courseId, String date);

    Map countTenYearToYearRegisteredUserByCourseId(Long courseId, String date);

    void nativeQuery(Long courseId);
}
