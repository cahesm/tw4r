package com.javatpoint.service;

import com.javatpoint.dao.AttributeDao;
import com.javatpoint.model.Attribute;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AttributeServiceImpl implements AttributeService {

  @Autowired
  public AttributeDao attributeDao;

  public void register( Attribute attribute ) {
    attributeDao.register( attribute );
  }
  
  public void update( Attribute attribute) {
    attributeDao.update(attribute);
  }
      
  public void delete( int idAttribute) {
    attributeDao.delete( idAttribute );
  }
          
  public Attribute getAttribute(int idAttribute) {
    return attributeDao.getAttribute(idAttribute);
  }
  
  public List<Attribute> getAttributes() {
    return attributeDao.getAttributes();
  }
  
  public List<Attribute> getAttributes( int idCategory ) {
    return attributeDao.getAttributes( idCategory );
  }
  
  
}
