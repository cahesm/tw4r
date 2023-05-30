package com.javatpoint.service;

import com.javatpoint.model.Disponibility;
import com.javatpoint.model.UnitsSearch;
import java.util.List;

public interface DisponibilityService {

  void register(Disponibility disponibility);
  void update(Disponibility disponibility);
  void delete(int id);
  void deleteAll(int idUnit);
  
  Disponibility getDisponibility( int idDisponibility );
  List<Disponibility> getDisponibilitiesByUnit( int idUnit );
  List<Disponibility> getDisponibilitiesByFilter(UnitsSearch filter);
  List<Disponibility> getNewestDisponibilities();
    
}
