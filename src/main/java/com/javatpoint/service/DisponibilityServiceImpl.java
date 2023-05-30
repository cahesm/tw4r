package com.javatpoint.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.javatpoint.model.Disponibility;
import java.util.List;
import com.javatpoint.dao.DisponibilityDao;
import com.javatpoint.model.UnitsSearch;

public class DisponibilityServiceImpl implements DisponibilityService {

  @Autowired
  public DisponibilityDao disponibilityDao;

  public void register(Disponibility disponibility) {
    disponibilityDao.register(disponibility);
  }
  
  public void update(Disponibility disponibility) {
    disponibilityDao.update(disponibility);
  }
  
  public void delete(int id) {
    disponibilityDao.delete(id);
  }
  
  public void deleteAll(int idUnit) {
    disponibilityDao.delete(idUnit);
  }
      
  public Disponibility getDisponibility(int id) {
    return disponibilityDao.getDisponibility(id);
  }
  
  public List<Disponibility> getDisponibilitiesByUnit( int idUnit ) {
    return disponibilityDao.getDisponibilitiesByUnit( idUnit );
  }
  
  public List<Disponibility> getDisponibilitiesByFilter(UnitsSearch filter)
  {
      return disponibilityDao.getDisponibilitiesByFilter( filter );
  }
  
  public List<Disponibility> getNewestDisponibilities()
  {
      return disponibilityDao.getNewestDisponibilities( );
  }
  
}
