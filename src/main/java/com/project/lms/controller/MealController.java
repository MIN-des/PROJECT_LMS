package com.project.lms.controller;

import com.project.lms.service.SchoolMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MealController {

  @Autowired
  private SchoolMealService schoolMealService;

  @GetMapping("/meals")
  public ResponseEntity<Map<String, Object>> getMeal(@RequestParam(value = "date", required = false) String date) {
    // 기본값: 오늘 날짜
    LocalDate currentDate = (date != null) ? LocalDate.parse(date) : LocalDate.now();

    // API 호출
    String regionCode = "N10"; // 서울특별시교육청
    String schoolCode = "8140482"; // 특정 학교 코드
    String formattedDate = currentDate.toString().replaceAll("-", ""); // yyyyMMdd 포맷
    Map<String, Object> mealInfo = schoolMealService.getSchoolMeal(regionCode, schoolCode, formattedDate);

    // 데이터를 가공하여 템플릿에 전달
    List<Map<String, String>> mealMenus = new ArrayList<>();
    if (mealInfo != null && mealInfo.containsKey("mealServiceDietInfo")) {
      try {
        List<?> dietInfoList = (List<?>) mealInfo.get("mealServiceDietInfo");
        Map<String, Object> dietInfo = (Map<String, Object>) dietInfoList.get(1); // "row" 포함
        List<?> rows = (List<?>) dietInfo.get("row");
        for (Object row : rows) {
          Map<String, Object> rowMap = (Map<String, Object>) row;
          Map<String, String> meal = new HashMap<>();
          meal.put("mealType", (String) rowMap.get("MMEAL_SC_NM")); // 조식/중식/석식
          meal.put("menu", (String) rowMap.get("DDISH_NM"));        // 메뉴
          meal.put("calories", (String) rowMap.get("CAL_INFO"));   // 칼로리 정보
          mealMenus.add(meal);
        }
      } catch (Exception e) {
        System.err.println("학식 데이터 처리 중 오류 발생: " + e.getMessage());
      }
    }
    // 전날과 다음날 계산
    LocalDate previousDate = currentDate.minusDays(1);
    LocalDate nextDate = currentDate.plusDays(1);

    // JSON 데이터 생성
    Map<String, Object> response = new HashMap<>();
    // 모델 추가
    response.put("mealMenus", mealMenus);
    response.put("currentDate", currentDate.toString());
    response.put("previousDate", currentDate.minusDays(1).toString());
    response.put("nextDate", currentDate.plusDays(1).toString());
    return ResponseEntity.ok(response);
  }
}
