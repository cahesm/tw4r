package com.javatpoint.dao;

import com.javatpoint.model.AttributeCategory;
import java.util.List;

public interface AttributeCategoryDao {

  void register(AttributeCategory category);  
  void update(AttributeCategory category);  
  
  void delete( int id );
    
  AttributeCategory getCategory( int id );
  List<AttributeCategory> getCategories( int category );
  
}
