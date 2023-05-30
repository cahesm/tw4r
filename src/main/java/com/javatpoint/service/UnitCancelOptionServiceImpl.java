package com.javatpoint.service;

import com.javatpoint.dao.UnitCancelOptionDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import com.javatpoint.model.UnitCancelOption;

public class UnitCancelOptionServiceImpl implements UnitCancelOptionService {

  @Autowired
  public UnitCancelOptionDao unitCancelOptionDao;

  public void register( UnitCancelOption unitCancelOption ) {
    unitCancelOptionDao.register( unitCancelOption );
  }
  
  public void update(UnitCancelOption unitCancelOption) {
    unitCancelOptionDao.update( unitCancelOption );
  }
  
  public void delete(int id) {
    unitCancelOptionDao.delete(id);
  }
  
  public void deleteAll(int idUnit) {
    unitCancelOptionDao.delete(idUnit);
  }
      
  public UnitCancelOption getUnitCancelOption(int id) {
    return unitCancelOptionDao.getUnitCancelOption(id);
  }
  
  public List<UnitCancelOption> getUnitCancelOptions( int idUnit ) {
    return unitCancelOptionDao.getUnitCancelOptions( idUnit );
  }
  
  
}
