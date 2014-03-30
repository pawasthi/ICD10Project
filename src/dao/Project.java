package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dto.FeedObjects;

public class Project {
	
	public ArrayList<FeedObjects> GetFeeds(Connection connection) throws Exception {
		ArrayList<FeedObjects> feedData = new ArrayList<FeedObjects>();
		try {
			PreparedStatement ps = connection
					.prepareStatement("SELECT title,description,url FROM website ORDER BY id DESC");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				FeedObjects feedObject = new FeedObjects();
				feedObject.setTitle(rs.getString("title"));
				feedObject.setDescription(rs.getString("description"));
				feedObject.setUrl(rs.getString("url"));
				feedData.add(feedObject);
			}
			return feedData;
		} catch (Exception e) {
			throw e;
		}
	}

	public String InsertMessage(Connection connection, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String message = null;
		try {
			message = request.getParameter("message");
			PreparedStatement ps = connection
					.prepareStatement("INSERT INTO messages(message) VALUES(?)");
			ps.setString(1, message);
			int rs = ps.executeUpdate();
			if (rs > 0) {
				return message;
			} else {
				return null;
			}

		} catch (Exception e) {
			throw e;
		}
	}

}