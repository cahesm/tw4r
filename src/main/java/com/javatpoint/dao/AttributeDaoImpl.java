package com.javatpoint.dao;

import com.javatpoint.model.Attribute;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;


public class AttributeDaoImpl implements AttributeDao {
  
  @Autowired
  JdbcTemplate jdbcTemplate;
    
  public void register( Attribute attribute ) {

    String sql = "insert into attributes ( id_category, name ) values(?,?)";

    jdbcTemplate.update(sql, new Object[] { attribute.getIdCategory(),attribute.getName() });        
  }
  
  public void update( Attribute attribute ) {

    String sql = "update attributes set id_category = ?, name = ? where  idAttribute = ?  ";

    jdbcTemplate.update(sql, new Object[] { attribute.getIdCategory(),attribute.getName(),attribute.getIdAttribute() });
          
  }
            
  public void delete( int idAttribute ) {
      
      String sql = "delete from attributes where idAttribute = ? ";

      jdbcTemplate.update(sql, new Object[] { idAttribute });
  }
  
 
  public Attribute getAttribute( int idAttribute) {

    String sql = "select * from attributes where idAttribute = ? ";  ; 
        
    List<Attribute> attributes = jdbcTemplate.query(sql, new AttributeMapper(),new Object[] { idAttribute });
        
    return attributes.size() > 0 ? attributes.get(0) : null;
  }
  
  public List<Attribute> getAttributes(int idCategory) {

    String sql = "select * from attributes where idCategory = ? " ; 
        
    List<Attribute> attributes = jdbcTemplate.query(sql, new AttributeMapper(),new Object[] { idCategory });
        
    return attributes;
  }
  
  public List<Attribute> getAttributes() {

    String sql = "select * from attributes" ; 
        
    List<Attribute> attributes = jdbcTemplate.query(sql, new AttributeMapper());
        
    return attributes;
  }
      
}

class AttributeMapper implements RowMapper<Attribute> {

  public Attribute mapRow(ResultSet rs, int arg1) throws SQLException {
    Attribute attribute = new Attribute();
            
    attribute.setIdAttribute(rs.getInt("idAttribute"));
    attribute.setIdCategory(rs.getInt("idCategory"));    
    attribute.setName(rs.getString("name"));
        
    return attribute;
  }
}
  
  
  
