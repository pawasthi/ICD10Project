package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import dto.ICD10Objects;

public class ICD10DAO {
	
	public ArrayList<ICD10Objects> GetICDDataFromDb(Connection connection) throws Exception {
		ArrayList<ICD10Objects> icd10Data = new ArrayList<ICD10Objects>();
		ArrayList<String> disease = new ArrayList<String>();
		ArrayList<String> stopwords = new ArrayList<String>();
		Map<String, Integer> hm = new HashMap<String, Integer>();
		Map<String, Integer> temphm = new HashMap<String, Integer>();
		String[] splitted;	
		Map.Entry pairs;
		int count = 0;
		
		try {
			
			// Get all the descriptions from the db
			PreparedStatement ps = connection.prepareStatement("SELECT icd_code, disease FROM icd_code_to_disease_mapping_formatted order by icd_code");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				ICD10Objects icd10Object = new ICD10Objects();
				//icd10Object.setIcd_code(rs.getString("icd_code"));
				icd10Object.setDisease(rs.getString("disease"));
				icd10Data.add(icd10Object);
				disease.add(rs.getString("disease")); // Get all diseases
			}
			
			// Get all the stop words from db
			ps = connection.prepareStatement("SELECT word FROM stop_words");
			rs = ps.executeQuery();
			while (rs.next()) {
				stopwords.add(rs.getString("word").trim().toLowerCase()); // Get all diseases
			}
			
			// Create hashmap of words by iterating icd descriptions
			String key ="";
			for (String s : disease){
			    //System.out.println(count++ + ", " + s);
				splitted = s.split("\\s");
				temphm.clear(); 
				for (int i=0; i<splitted.length ; i++) {
					count = 0; 
					key = splitted[i].trim().toLowerCase();
					if(key.matches("[a-zA-Z0-9/\\-+]+")){ // for inserting only text, numbers and few special chars 
			         	if (hm.containsKey(key)) {
			         		if (! temphm.containsKey(key)){ // Do not increment count if keyword re-appears in the same icd10 code's descrip.
				         		count = hm.get(key);
				         		hm.put(key, count + 1);
			         		}
			         	} else if (key != null && !key.isEmpty()){
			         		hm.put(key, count + 1);
			         		temphm.put(key, count + 1);
			         	}
					}else{
						System.out.println("Eliminated from hashmap creation initially - " + key);
					}
				}
			}	
			
			// Print to show dict
		    /*Iterator it = hm.entrySet().iterator();
		      while (it.hasNext()) {
		        Map.Entry pairs = (Map.Entry)it.next();
		        System.out.println(pairs.getKey() + " = " + pairs.getValue());
		    }*/
			
			// Print stop words
			/*count = 0;
			for (String s : stopwords){
				System.out.println(count++ + ", " + s);
			}*/
			
			// Eliminate the stop words from the dictionary
			for (String s : stopwords){
				if (hm.containsKey(s)) {
					System.out.println("Deleted stop word from final dictionary- " + s);
	         		hm.remove(s);
	         	}				
			}
						
			// Insert the final has as the reference dictionary
			Iterator it = hm.entrySet().iterator();
			while (it.hasNext()) {
				pairs = (Map.Entry)it.next();
		        //System.out.println(pairs.getKey() + " = " + pairs.getValue());
		        ps = connection.prepareStatement("INSERT INTO disease_keyword_freq(keyword,frequency) VALUES(?,?)");
				ps.setString(1, String.valueOf(pairs.getKey()));
				ps.setInt(2, (int) (pairs.getValue()));
				ps.executeUpdate();
		    }
			
			// free memory 
			temphm.clear();
			hm.clear();
			
			return icd10Data;	
		} catch (Exception e) {			
			throw e;
		}
	}

}