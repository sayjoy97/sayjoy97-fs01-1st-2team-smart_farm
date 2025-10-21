package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dto.MemberDTO;

import util.DBUtil;

public class MemberDAOImpl implements MemberDAO {

	@Override
	public int register(MemberDTO user) {
		String sql = "insert into members values(null,?,?,?,?,?,now())";
		Connection con = null;
		PreparedStatement ptmt = null;
		int result = 0;
		try {
			con = DBUtil.getConnect();
			ptmt = con.prepareStatement(sql);
			ptmt.setString(1, user.getUserId());
			ptmt.setString(2, user.getPass());
			ptmt.setString(3, user.getName());
			ptmt.setString(4, user.getEmail());
			ptmt.setString(5, user.getAddress());
			result = ptmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(null, ptmt, con);
		}
		return result;
		
	}

	@Override
	public MemberDTO login(String id, String pass) {
		Connection con = null;
		PreparedStatement ptmt =null;
		ResultSet rs = null;
		MemberDTO loginSuccessUser = null;
		String sql = "select * from members where user_id=? and pass=?";
		try {
			con = DBUtil.getConnect();
			ptmt =  con.prepareStatement(sql);
			ptmt.setString(1, id);
			ptmt.setString(2, pass);
			rs =  ptmt.executeQuery();
			if(rs.next()) {
				loginSuccessUser = new MemberDTO(rs.getInt("id"), 
						rs.getString("user_id"),rs.getString("pass"),
						rs.getString("name"), rs.getString("email")
						,rs.getString(6),rs.getTimestamp(7));
				
			}
		}  catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBUtil.close(rs, ptmt, con);
		}
		return loginSuccessUser;
	}

}
