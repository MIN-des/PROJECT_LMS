package com.project.lms.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class SchoolMealServiceImpl implements SchoolMealService{
	private static final String BASE_URL = "https://open.neis.go.kr/hub/mealServiceDietInfo";
	private static final String API_KEY = "0bff5fc6d15649de951888f66285a922"; // 발급받은 API Key

	public Map<String, Object> getSchoolMeal(String regionCode, String schoolCode, String date) {
		RestTemplate restTemplate = new RestTemplate();

		String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
				.queryParam("KEY", API_KEY)
				.queryParam("ATPT_OFCDC_SC_CODE", regionCode)
				.queryParam("SD_SCHUL_CODE", schoolCode)
				.queryParam("MLSV_YMD", date)
				.queryParam("Type", "json")
				.toUriString();

		HttpHeaders headers = new HttpHeaders();
		headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.110 Safari/537.36");

		HttpEntity<String> entity = new HttpEntity<>(headers);

		try {
//			System.out.println("Request URL: " + url); // 테스트용 / 로그에 apikey 찍힘

			// HTTP GET 요청
			ResponseEntity<String> responseEntity = restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, entity, String.class);
			String responseBody = responseEntity.getBody();
			System.out.println("Raw Response: " + responseBody);

			// JSON 파싱
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(responseBody);
			System.out.println("Parsed JSON: " + rootNode.toPrettyString());

			// Map 형태로 반환
			return objectMapper.convertValue(rootNode, Map.class);
		} catch (HttpClientErrorException e) {
			System.err.println("HTTP 클라이언트 오류: " + e.getStatusCode());
			System.err.println("응답 바디: " + e.getResponseBodyAsString());
			return null;
		} catch (HttpServerErrorException e) {
			System.err.println("HTTP 서버 오류: " + e.getStatusCode());
			System.err.println("응답 바디: " + e.getResponseBodyAsString());
			return null;
		} catch (Exception e) {
			System.err.println("알 수 없는 오류: " + e.getMessage());
			return null;
		}
	}

	public Map<String, Object> getSchoolMealsForRange(String regionCode, String schoolCode, LocalDate startDate, LocalDate endDate) {
		Map<String, Object> meals = new HashMap<>();
		LocalDate date = startDate;

		while (!date.isAfter(endDate)) {
			String formattedDate = date.toString().replaceAll("-", ""); // yyyyMMdd 포맷
			meals.put(formattedDate, getSchoolMeal(regionCode, schoolCode, formattedDate));
			date = date.plusDays(1);
		}
		return meals;
	}
}
