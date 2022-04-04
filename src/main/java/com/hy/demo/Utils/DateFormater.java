package com.hy.demo.Utils;

import lombok.Getter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
public class DateFormater {
    private LocalDate searchDate;

    public DateFormater(String currentDate,String flag) {

        this.searchDate = LocalDate.parse(currentDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    }

    public Timestamp startDate() {
        return Timestamp.valueOf(LocalDateTime.of(this.searchDate, LocalTime.of(0,0,0)));

    }
    public Timestamp endDate() {
        return Timestamp.valueOf(LocalDateTime.of(this.searchDate, LocalTime.of(23,59,59)));
    }

    public Timestamp startMonth() {
        return Timestamp.valueOf(LocalDateTime.of(this.searchDate.getYear(),this.searchDate.getMonth(),1,0,0,0));
    }
    public Timestamp endMonth() {
        return Timestamp.valueOf(LocalDateTime.of(this.searchDate.getYear(),this.searchDate.getMonth(),this.searchDate.lengthOfMonth(),23,59,59));
    }
}