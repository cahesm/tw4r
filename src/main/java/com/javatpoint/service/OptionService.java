package com.javatpoint.service;

import com.javatpoint.model.Option;
import java.util.List;

public interface OptionService {

  void register( Option attribute );  
  void update( Option attribute );  
  void delete(int id);
  
  Option getOption( int idOption );
  
  List<Option> getOptions( int type );
      
}
