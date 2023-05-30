package com.javatpoint.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import com.javatpoint.model.UnitAttribute;

public class UnitAttributeDaoImpl implements UnitAttributeDao {

  @Autowired
  JdbcTemplate jdbcTemplate;
    
  public void register(UnitAttribute unitAttribute) {

    String sql = "insert into unit_attributes ( idUnit, idAttribute, comment ) values(?,?,?)";

    jdbcTemplate.update(sql, new Object[] { unitAttribute.getIdUnit(),unitAttribute.getIdAttribute(), unitAttribute.getComment() });        
  }
  
  public void update(UnitAttribute unitAttribute) {

    String sql = "update unit_attributes set comment = ? where idUnit = ? and idAttribute = ?  ";

    jdbcTemplate.update(sql, new Object[] { unitAttribute.getComment(),unitAttribute.getIdUnit(),unitAttribute.getIdAttribute() });
          
  }
            
  public void delete( int idUnit ) {
      
      String sql = "delete from unit_attributes where idUnit = ? ";

      jdbcTemplate.update(sql, new Object[] { idUnit });
  }
  
  public void delete( int idUnit, int idAttribute ) {
      
      String sql = "delete from unit_attributes where idUnit = ?  and idAttribute = ? ";

      jdbcTemplate.update(sql, new Object[] { idUnit, idAttribute });
  }
  
//  public void deleteAll( int type, int idSource ) {
//      
//      String sql = "delete from medias where type = ? and idSource = ? ";
//
//      jdbcTemplate.update(sql, new Object[] { type, idSource });
//  }
  
  public UnitAttribute getUnitAttribute(int idUnit, int idAttribute) {

    String sql = "select * from unit_attributes where idUnit = " + idUnit + " and idAttribute = " + idAttribute  ; 
        
    List<UnitAttribute> unitAttributes = jdbcTemplate.query(sql, new UnitAttributeMapper());
        
    return unitAttributes.size() > 0 ? unitAttributes.get(0) : null;
  }
  
  public List<UnitAttribute> getUnitAttributes(int idUnit) {

    String sql = "select * from unit_attributes where idUnit = ? " ; 
        
    List<UnitAttribute> unitAttributes = jdbcTemplate.query(sql, new UnitAttributeMapper(),new Object[] { idUnit });
        
    return unitAttributes;
  }
      
}

class UnitAttributeMapper implements RowMapper<UnitAttribute> {

  public UnitAttribute mapRow(ResultSet rs, int arg1) throws SQLException {
    UnitAttribute unitAttribute = new UnitAttribute();
            
    unitAttribute.setIdUnit(rs.getInt("idUnit"));
    unitAttribute.setIdAttribute(rs.getInt("idAttribute"));    
    unitAttribute.setComment(rs.getString("comment"));
        
    return unitAttribute;
  }
}
  
  
  
