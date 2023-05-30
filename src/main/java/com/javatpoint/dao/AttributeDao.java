package com.javatpoint.dao;

import com.javatpoint.model.Attribute;
import java.util.List;

public interface AttributeDao {

  void register(Attribute attribute);  
  void update(Attribute attribute);  
  
  void delete( int id );
    
  Attribute getAttribute( int id );
  List<Attribute> getAttributes( int category );
  List<Attribute> getAttributes();
  
}
