package webService;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import model.ICD10Model;

import com.google.gson.Gson;

import dto.FeedObjects;
import dto.ICD10Objects;

@Path("/ICDWebService")
public class ICD10WebService {

	@GET
	@Path("/GetICDCodeAndDescriptions")
	@Produces("application/json")
	public String feed() {
		String feeds = null;
		try {
			ArrayList<ICD10Objects> icd10Data = null;			
			ICD10Model icdModel = new ICD10Model();
			Gson gson = new Gson();
			
			// Get all the icd code and descp
			icd10Data = icdModel.GetICDData();
			
			// Convert to json and display
			//System.out.println(gson.toJson(icd10Data));
			//feeds = gson.toJson(icd10Data);
			
			System.out.println("<div> Check logs for results !!! </div>");
		}
		catch (Exception e) {
			System.out.println("Exception Error"); // Console
		}
		return feeds;
	}

}