package com.javatpoint.service;

import com.javatpoint.model.UnitCancelOption;
import java.util.List;

public interface UnitCancelOptionService {

  void register(UnitCancelOption value);
  void update(UnitCancelOption value );
  void delete(int id);
  void deleteAll(int idUnit);
  
  //UnitCancelOption getDisponibility( int idDisponibility );
  List<UnitCancelOption> getUnitCancelOptions( int idUnit );
  //List<Disponibility> getDisponibilitiesByFilter(UnitsSearch filter);
  //List<Disponibility> getNewestDisponibilities();
    
}
