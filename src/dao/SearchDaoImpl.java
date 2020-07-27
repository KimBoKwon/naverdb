package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import db.DB;
import dto.SearchDto;

public class SearchDaoImpl implements SearchDao{

	@Override
	public void insert(SearchDto dto) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		//디비 연결
		//쿼리 만들고
		//쿼리 실행
		try {
			conn = DB.condb();
			String sql = "INSERT INTO search_tb VALUES (?, ?, ?, ?, ?);";
			pstmt = conn.prepareStatement(sql);
			// 4. 데이터 binding
			pstmt.setString(1, dto.getTitle());
			pstmt.setString(2, dto.getOriginallink());
			pstmt.setString(3, dto.getLink());
			pstmt.setString(4, dto.getDescription());
			pstmt.setString(5, dto.getPubDate());
			
			int count = pstmt.executeUpdate();
			if (count == 0) {
				System.out.println("데이터 입력 실패");
			} else {
				System.out.println("데이터 입력 성공");
			}

		} catch (Exception e) {
			System.out.println("에러: " + e);
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
				if( pstmt != null && !pstmt.isClosed()){
                    pstmt.close();
                }
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public SearchDto select(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<SearchDto> select() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		// 전달 변수(dto 담을 그릇)
		ArrayList<SearchDto> list = new ArrayList<SearchDto>();
		try {
			conn = DB.condb();
			String sql = "SELECT * FROM search_tb";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				SearchDto dto = new SearchDto();
				dto.setTitle(rs.getString("title"));
				dto.setOriginallink(rs.getString("originallink"));
				dto.setLink(rs.getString("link"));
				dto.setDescription(rs.getString("description"));
				dto.setPubDate(rs.getString("pubDate"));
				list.add(dto);
			}

		} catch (Exception e) {
			System.out.println("에러: " + e);
		} finally {
			try {
				if( rs != null && !rs.isClosed()){
                    rs.close();
                }
				if( pstmt != null && !pstmt.isClosed()){
                    pstmt.close();
                }
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

}
