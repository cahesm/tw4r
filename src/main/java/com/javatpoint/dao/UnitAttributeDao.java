package com.javatpoint.dao;

import com.javatpoint.model.UnitAttribute;
import java.util.List;

public interface UnitAttributeDao {

  void register(UnitAttribute category);  
  void update(UnitAttribute category);  
  
  void delete( int idUnit );
  void delete( int idUnit, int idAttribute );
    
  UnitAttribute getUnitAttribute( int idUnit, int idAttribute );
  List<UnitAttribute> getUnitAttributes( int idUnit );
  
}
