package dao;

import java.util.ArrayList;

import dto.SearchDto;

public interface SearchDao {
	// 데이터 넣고
		public void insert(SearchDto dto);
		// ID로 검색하고
		public SearchDto select(String id);
		// 전체 데이터 가져오기
		public ArrayList<SearchDto> select();
}
