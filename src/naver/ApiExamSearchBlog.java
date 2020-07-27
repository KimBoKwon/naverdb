package naver;

//네이버 검색 API 예제 - blog 검색
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

import dao.SearchDao;
import dao.SearchDaoImpl;
import dto.SearchDto;

public class ApiExamSearchBlog {

 public static void main(String[] args) {
	 
	 Scanner sc = new Scanner(System.in);
	 SearchDaoImpl userDaoImpl = new SearchDaoImpl();
	 SearchDao searchDao = new SearchDaoImpl();
	 SearchDto dto = new SearchDto();
	 System.out.print("검색어를 입력하시오 : ");
	 String search = sc.next();
	 
     String clientId = "Hw4e4GUKIeJoaeYDitTd"; //애플리케이션 클라이언트 아이디값"
     String clientSecret = "yH8wUi2Jlw"; //애플리케이션 클라이언트 시크릿값"

     String text = null;
     try {
         text = URLEncoder.encode(search, "UTF-8");
     } catch (UnsupportedEncodingException e) {
         throw new RuntimeException("검색어 인코딩 실패",e);
     }

     String apiURL = "https://openapi.naver.com/v1/search/news?query=" + text;    // json 결과
     //String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text; // xml 결과

     Map<String, String> requestHeaders = new HashMap<>();
     requestHeaders.put("X-Naver-Client-Id", clientId);
     requestHeaders.put("X-Naver-Client-Secret", clientSecret);
     String responseBody = get(apiURL,requestHeaders);

     System.out.println(responseBody);
     
  // 가장 큰 JSONObject를 가져옵니다.
     JSONObject jObject = new JSONObject(responseBody);
     // 배열을 가져옵니다.
     JSONArray jArray = jObject.getJSONArray("items");

     // 배열의 모든 아이템을 출력합니다.
     for (int i = 0; i < jArray.length(); i++) {
         JSONObject obj = jArray.getJSONObject(i);
         dto.setTitle(obj.getString("title"));
         dto.setOriginallink(obj.getString("originallink"));
         dto.setLink(obj.getString("link"));
         dto.setDescription(obj.getString("description"));
         dto.setPubDate(obj.getString("pubDate"));
         searchDao.insert(dto);
     }
     
     ArrayList<SearchDto> list = searchDao.select();
		for (SearchDto dti : list) {
			System.out.println("title : " + dti.getTitle());
			System.out.println("originallink : " + dti.getOriginallink());
			System.out.println("link : " + dti.getLink());
			System.out.println("description : " + dti.getDescription());
			System.out.println("pubDate : " + dti.getPubDate());
		}
     
 }

 private static String get(String apiUrl, Map<String, String> requestHeaders){
     HttpURLConnection con = connect(apiUrl);
     try {
         con.setRequestMethod("GET");
         for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
             con.setRequestProperty(header.getKey(), header.getValue());
         }

         int responseCode = con.getResponseCode();
         if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
             return readBody(con.getInputStream());
         } else { // 에러 발생
             return readBody(con.getErrorStream());
         }
     } catch (IOException e) {
         throw new RuntimeException("API 요청과 응답 실패", e);
     } finally {
         con.disconnect();
     }
 }

 private static HttpURLConnection connect(String apiUrl){
     try {
         URL url = new URL(apiUrl);
         return (HttpURLConnection)url.openConnection();
     } catch (MalformedURLException e) {
         throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
     } catch (IOException e) {
         throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
     }
 }

 private static String readBody(InputStream body){
     InputStreamReader streamReader = new InputStreamReader(body);

     try (BufferedReader lineReader = new BufferedReader(streamReader)) {
         StringBuilder responseBody = new StringBuilder();

         String line;
         while ((line = lineReader.readLine()) != null) {
             responseBody.append(line);
         }

         return responseBody.toString();
     } catch (IOException e) {
         throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
     }
 }
}
