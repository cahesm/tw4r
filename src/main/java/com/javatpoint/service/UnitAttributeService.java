package com.javatpoint.service;

import com.javatpoint.model.UnitAttribute;
import java.util.List;

public interface UnitAttributeService {

  void register( UnitAttribute value );  
  void update( UnitAttribute value);  
  void delete( int idUnit );
  void delete( int idUnit, int idAttribute );
  //void deleteAll(int type, int idSource);
  
  UnitAttribute getUnitAttribute( int idUnit, int idAttribute );
  List<UnitAttribute> getUnitAttributes( int idUnit );
      
}
