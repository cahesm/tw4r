package com.javatpoint.service;

import com.javatpoint.model.ManagerUnitsSearch;
import com.javatpoint.model.SearchResultItem;
import com.javatpoint.model.Unit;
import com.javatpoint.model.UnitsSearch;
import java.util.List;

public interface UnitService {

  void register(Unit unit);
  void update(Unit unit);
  void delete(int id);
  
  Unit getUnit( int idUnit );
  Unit getUnit( String cod );
  Unit getUnitWithoutDependencies( int idUnit );
  Unit getUnitWithoutDependencies( int idUnit, boolean loadHotel, boolean loadUser, boolean loadDisponibilities, boolean loadMedias, boolean loadDocuments, boolean loadReservations, boolean loadAttributes, boolean loadCancelOption );
  List<Unit> getUnitsByUser( int idUser );
  List<Unit> getUnitsByHotel( int idHotel );
  List<Unit> getUnitsByFilter( UnitsSearch filter );
  List<Unit> getUnitsByFilter( ManagerUnitsSearch filter );
  List<Unit> getNotValidatedUnits();
  List<Unit> getUnits();
  
  List<SearchResultItem>search( String name );
}
