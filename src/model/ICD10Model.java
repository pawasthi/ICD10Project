package model;

import java.sql.Connection;
import java.util.ArrayList;

//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;

import dao.Database;
import dao.ICD10DAO;
import dto.ICD10Objects;

public class ICD10Model {

	public ArrayList<ICD10Objects> GetICDData() throws Exception {
		ArrayList<ICD10Objects> icd10Data = null;
		try {
			// Create db connection
			Database database = new Database();
			Connection connection = database.Get_Connection();
			
			ICD10DAO icdDao = new ICD10DAO();
			icd10Data = icdDao.GetICDDataFromDb(connection);
			
		} catch (Exception e) {
			throw e;
		}
		return icd10Data;
	}
}