package com.javatpoint.dao;

import com.javatpoint.model.GeoName;
import java.util.List;

public interface GeoDao {

    GeoName getGeoName( int id );
    
    List<GeoName> getCountries();
    List<GeoName> getStates( int idParent );
    List<GeoName> getCities( int idParent );
}
