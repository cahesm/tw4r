package com.javatpoint.service;

import com.javatpoint.dao.UnitDao;
import com.javatpoint.model.ManagerUnitsSearch;
import org.springframework.beans.factory.annotation.Autowired;

import com.javatpoint.model.SearchResultItem;
import com.javatpoint.model.Unit;
import com.javatpoint.model.UnitsSearch;
import java.util.List;

public class UnitServiceImpl implements UnitService {

  @Autowired
  public UnitDao unitDao;

  public void register(Unit unit) {
    unitDao.register(unit);
  }
  
  public void update(Unit unit) {
    unitDao.update(unit);
  }
  
  public void delete(int id) {
    unitDao.delete(id);
  }
      
  public Unit getUnit(int id) {
    return unitDao.getUnit(id);
  }
  
  public Unit getUnit(String cod) {
    return unitDao.getUnit(cod);
  }
  
  public Unit getUnitWithoutDependencies(int id) {
    return unitDao.getUnitWithoutDependencies(id);
  }
  
  public Unit getUnitWithoutDependencies( int idUnit, boolean loadHotel, boolean loadUser, boolean loadDisponibilities, boolean loadMedias, boolean loadDocuments, boolean loadReservations, boolean loadAttributes, boolean loadCancelOption )
  {
      return unitDao.getUnitWithoutDependencies( idUnit, loadHotel, loadUser, loadDisponibilities, loadMedias, loadDocuments, loadReservations, loadAttributes, loadCancelOption);
  }
  
  public List<Unit> getUnitsByUser( int idUser ) {
    return unitDao.getUnitsByUser( idUser );
  }
  
  public List<Unit> getUnitsByHotel( int idHotel ) {
    return unitDao.getUnitsByHotel( idHotel );
  }
  
  public List<Unit> getUnitsByFilter( UnitsSearch filter ) {
    return unitDao.getUnitsByFilter( filter );
  }
  
  public List<Unit> getUnitsByFilter( ManagerUnitsSearch filter ) {
    return unitDao.getUnitsByFilter( filter );
  }
  
  public List<Unit> getNotValidatedUnits(  ) {
    return unitDao.getNotValidatedUnits();
  }
  
  public List<Unit> getUnits(  ) {
    return unitDao.getUnits();
  }
  
  public List<SearchResultItem> search( String name ) {
    return unitDao.search( name );
  }

}
