package com.javatpoint.service;

import com.javatpoint.dao.GeoDao;
import com.javatpoint.model.GeoName;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.http.HttpHeaders.USER_AGENT;

public class GeoServiceImpl implements GeoService 
{
 
    @Autowired
    public GeoDao geoDao;
    
    public List<GeoName> getNames( int code ) throws Exception
    {
        String url = "http://www.geonames.org/childrenJSON?geonameId="+code+"&style=long";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
        }
        in.close();
        
        ArrayList<GeoName> geoNames = new ArrayList<GeoName>();
        
        JSONObject myResponse = new JSONObject(response.toString());        
        JSONArray list = myResponse.getJSONArray("geonames");
        
        for( int x = 0;x < list.length(); x++ )
        {
            JSONObject item = list.getJSONObject( x );
            String name = item.getString("name");
            int id = item.getInt("geonameId");
            
            GeoName geoName = new GeoName( id, name);
            geoNames.add(geoName);
        }
                        
        return geoNames;
    }
    
    public GeoName getGeoName( int id )
    {
        return geoDao.getGeoName( id );
    }
        
     public List<GeoName> getCountries()
     {
         return geoDao.getCountries();
     }
     
     public List<GeoName> getStates( int idParent )
     {
         return geoDao.getStates( idParent );
     }
     
     public List<GeoName> getCities( int idParent )
     {
         return geoDao.getCities( idParent );
     }
  
}
