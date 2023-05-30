package com.javatpoint.dao;

import com.javatpoint.model.Option;
import java.util.List;

public interface OptionDao {

  void register(Option option);  
  void update(Option option);  
  
  void delete( int id );
    
  Option getOption( int id );
  List<Option> getOptions( int type );
  
  
}
