package com.javatpoint.dao;

import com.javatpoint.model.Disponibility;
import com.javatpoint.model.UnitsSearch;
import java.util.List;

public interface DisponibilityDao {

  void register(Disponibility disponibility);  
  void update(Disponibility disponibility);
  void delete( int id );
  void deleteAll( int idUnit );
  
  Disponibility getDisponibility( int id );
  List<Disponibility> getDisponibilitiesByUnit( int idUnit );
  List<Disponibility> getDisponibilitiesByFilter(UnitsSearch filter);
  List<Disponibility> getNewestDisponibilities();
}
  

