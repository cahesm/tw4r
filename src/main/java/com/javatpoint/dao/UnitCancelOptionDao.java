package com.javatpoint.dao;

import com.javatpoint.model.UnitCancelOption;
import java.util.List;

public interface UnitCancelOptionDao {

  void register(UnitCancelOption value);  
  void update(UnitCancelOption value);
  void delete( int id );
  void deleteAll( int idUnit );
  
  UnitCancelOption getUnitCancelOption( int id );
  List<UnitCancelOption> getUnitCancelOptions( int idUnit );
  
}
  

