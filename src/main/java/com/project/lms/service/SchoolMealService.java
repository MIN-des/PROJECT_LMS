package com.project.lms.service;

import java.util.Map;

public interface SchoolMealService {
	Map<String, Object> getSchoolMeal(String regionCode, String schoolCode, String date);
}
