package com.javatpoint.service;

import com.javatpoint.dao.OptionDao;
import com.javatpoint.model.Option;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class OptionServiceImpl implements OptionService {

  @Autowired
  public OptionDao optionDao;

  public void register( Option option ) {
    optionDao.register( option );
  }
  
  public void update( Option option) {
    optionDao.update( option );
  }
      
  public void delete( int idOption ) {
    optionDao.delete( idOption );
  }
          
  public Option getOption(int type ) {
    return optionDao.getOption(type);
  }
  
  public List<Option> getOptions( int type ) {
    return optionDao.getOptions( type );
  }
  
  
  
  
}
