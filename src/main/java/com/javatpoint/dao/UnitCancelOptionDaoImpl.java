package com.javatpoint.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import com.javatpoint.model.UnitCancelOption;
import com.javatpoint.service.UnitService;

public class UnitCancelOptionDaoImpl implements UnitCancelOptionDao {
  
  @Autowired
  JdbcTemplate jdbcTemplate;
  
 
  @Autowired
  UnitService unitService;
    
  public void register(UnitCancelOption unitCancelOption) {

    String sql = "insert into unit_cancel_options ( idUnit, days, devolution, description ) values(?,?,?,?)";

    jdbcTemplate.update(sql, new Object[] { unitCancelOption.getIdUnit(),unitCancelOption.getDays(), unitCancelOption.getDevolution(), unitCancelOption.getDescription() });
    
    
  }
    
  public void update(UnitCancelOption unitCancelOption) {

    String sql = "update unit_cancel_options set days = ?, devolution = ?, description = ? where idUnitCancelOption = ? ";

    jdbcTemplate.update(sql, new Object[] { unitCancelOption.getDays(), unitCancelOption.getDevolution(), unitCancelOption.getDescription(), unitCancelOption.getIdCancelOption() });
        
  }
    
  public UnitCancelOption getUnitCancelOption(int idUnitCancelOption) {

    String sql = "select * from unit_cancel_options where unit_cancel_options = " + idUnitCancelOption; 
        
    List<UnitCancelOption> options = jdbcTemplate.query(sql, new UnitCancelOptionMapper());
        
    return options.size() > 0 ? options.get(0) : null;
  }
  
  public void delete( int id ) {
      
       String sql = "";
      
           
        sql = "delete from unit_cancel_options where idUnitCancelOption = ? ";
      
      
      jdbcTemplate.update(sql, new Object[] { id });
  }
  
  public void deleteAll( int idUnit ) {
      
      String sql = "delete from unit_cancel_options where idUnit = ? ";

      jdbcTemplate.update(sql, new Object[] { idUnit });
  }
  
  
  public List<UnitCancelOption> getUnitCancelOptions(int idUnit) {

    String sql = "select * from  unit_cancel_options where idUnit = " + idUnit + " order by days desc"; 
        
    List<UnitCancelOption> options = jdbcTemplate.query(sql, new UnitCancelOptionMapper());
        
    return options;
  }
  
  
  
  
class UnitCancelOptionMapper implements RowMapper<UnitCancelOption> {
   
    public UnitCancelOptionMapper()
    {}
    
    
  public UnitCancelOption mapRow(ResultSet rs, int arg1) throws SQLException {
    UnitCancelOption unitCancelOption = new UnitCancelOption();
            
    unitCancelOption.setIdCancelOption( rs.getInt("idUnitCancelOption"));
    unitCancelOption.setIdUnit( rs.getInt("idUnit"));    
    unitCancelOption.setDays(rs.getInt("days"));
    unitCancelOption.setDevolution( rs.getFloat("devolution"));
    unitCancelOption.setDescription(rs.getString("description"));
                
    
                
    return unitCancelOption;
  }}

   
}


  
  
