package com.javatpoint.service;

import com.javatpoint.model.GeoName;
import java.util.List;

public interface GeoService {

    List<GeoName> getNames( int code ) throws Exception;
    
    //List<Location> getContinents();
    GeoName getGeoName( int id );
    
    List<GeoName> getCountries();
    List<GeoName> getStates( int idParent );
    List<GeoName> getCities( int idParent );
    
    
}
