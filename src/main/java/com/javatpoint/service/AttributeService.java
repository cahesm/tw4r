package com.javatpoint.service;

import com.javatpoint.model.Attribute;
import java.util.List;

public interface AttributeService {

  void register( Attribute attribute );  
  void update(Attribute attribute);  
  void delete(int id);
  //void deleteAll(int type, int idSource);
  
  Attribute getAttribute( int idAttribute );
  List<Attribute> getAttributes( int idCategory);
  List<Attribute> getAttributes();
      
}
