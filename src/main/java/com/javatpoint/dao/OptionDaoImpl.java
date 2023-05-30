package com.javatpoint.dao;

import com.javatpoint.model.Attribute;
import com.javatpoint.model.Option;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;


public class OptionDaoImpl implements OptionDao {
  
  @Autowired
  JdbcTemplate jdbcTemplate;
    
  public void register( Option option ) {

    String sql = "insert into options ( type, name, description ) values(?,?,?)";

    jdbcTemplate.update(sql, new Object[] { option.getType(),option.getName(), option.getDescription() });        
  }
  
  public void update( Option option ) {

    String sql = "update options set name = ?, description = ? where  idOption = ?  ";

    jdbcTemplate.update(sql, new Object[] { option.getName(), option.getDescription(), option.getIdOption() });
          
  }
            
  public void delete( int idOption ) {
      
      String sql = "delete from options where idOption = ? ";

      jdbcTemplate.update(sql, new Object[] { idOption });
  }
  
 
  public Option getOption( int idOption) {

    String sql = "select * from options where idOption = ? ";  ; 
        
    List<Option> options = jdbcTemplate.query(sql, new OptionMapper(),new Object[] { idOption });
        
    return options.size() > 0 ? options.get(0) : null;
  }
  
  public List<Option> getOptions(int type) {

    String sql = "select * from options where type = ? " ; 
        
    List<Option> options = jdbcTemplate.query(sql, new OptionMapper(),new Object[] { type });
        
    return options;
  }
  
  
      
}

class OptionMapper implements RowMapper<Option> {

  public Option mapRow(ResultSet rs, int arg1) throws SQLException {
    Option option = new Option();
            
    option.setIdOption( rs.getInt("idOption") );
    option.setType( rs.getInt("type"));    
    option.setName(rs.getString("name"));
    option.setDescription(rs.getString("description"));
        
    return option;
  }
}
  
  
  
