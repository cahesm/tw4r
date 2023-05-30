package com.javatpoint.service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import com.javatpoint.dao.UnitAttributeDao;
import com.javatpoint.model.UnitAttribute;

public class UnitAttributeServiceImpl implements UnitAttributeService {

  @Autowired
  public UnitAttributeDao unitAttributeDao;

  public void register( UnitAttribute unitAttribute ) {
    unitAttributeDao.register( unitAttribute );
  }
  
  public void update(UnitAttribute unitAttribute) {
    unitAttributeDao.update(unitAttribute);
  }
      
  public void delete(int idUnit) {
    unitAttributeDao.delete(idUnit);
  }
  
  public void delete(int idUnit, int idAttribute) {
    unitAttributeDao.delete(idUnit, idAttribute);
  }
          
  public UnitAttribute getUnitAttribute(int idUnit, int idAttribute) {
    return unitAttributeDao.getUnitAttribute(idUnit, idAttribute);
  }
  
  public List<UnitAttribute> getUnitAttributes( int idUnit ) {
    return unitAttributeDao.getUnitAttributes( idUnit );
  }
  
  
}
