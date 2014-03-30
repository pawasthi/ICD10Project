package model;

import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.Database;
import dao.Project;
import dto.FeedObjects;

public class ProjectManager {

	public ArrayList<FeedObjects> GetFeeds() throws Exception {
		ArrayList<FeedObjects> feeds = null;
		try {
			Database database = new Database();
			Connection connection = database.Get_Connection();
			Project project = new Project();
			feeds = project.GetFeeds(connection);
		} catch (Exception e) {
			throw e;
		}
		return feeds;
	}

	public String InsertMessage(Connection connection, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String message = null;
		try {
			message = request.getParameter("message");
			if (message != null && message != "" && message.length() > 0) {
				Project project = new Project();
				message = project.InsertMessage(connection, request, response);
			}
		} catch (Exception e) {
			throw e;
		}
		return message;
	}

}