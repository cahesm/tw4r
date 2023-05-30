package com.javatpoint.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import com.javatpoint.model.Login;
import com.javatpoint.model.ManagerUsersSearch;
import com.javatpoint.model.Media;
import com.javatpoint.model.User;
import com.javatpoint.service.MediaService;
import com.javatpoint.service.SecurityService;
import java.util.Date;
import java.util.UUID;
import org.apache.commons.lang.RandomStringUtils;


public class UserDaoImpl implements UserDao {

  @Autowired
  JdbcTemplate jdbcTemplate;
  
  @Autowired
  SecurityService securityService;
  
  @Autowired
  MediaService mediaService;

  public void register(User user) {
            
    String sql = "insert into users (cod,username,password,name,email,phone,owner,creation, verificationToken, comment) values(?,?,?,?,?,?,?,?,?,?)";

    jdbcTemplate.update(sql, new Object[] { "C" + RandomStringUtils.randomAlphanumeric(8).toUpperCase() , user.getUsername(), user.getPassword(), user.getName(),
       user.getEmail(), user.getPhone(), user.isOwner(), new Date(), UUID.randomUUID().toString(), user.getComment() });
    
    
  }
    
  public void update(User user) {

    String sql = "update users set password = ?, name = ?, email = ?, phone = ?, owner = ?, validationStatus = ?, comment = ? where username = ? ";

    jdbcTemplate.update(sql, new Object[] { user.getPassword(), user.getName(),
       user.getEmail(), user.getPhone(), user.isOwner(),user.getValidationStatus(), user.getComment(), user.getUsername() });
        
  }
  
  public User validateUser(Login login) {

    String pass = securityService.encrypt( login.getPassword() );
            
    String sql = "select * from users where ( username='" + login.getUsername() + "' or email='"+login.getUsername()+"') and password='" + pass + "'";

    List<User> users = jdbcTemplate.query(sql, new UserMapper());

    return users.size() > 0 ? users.get(0) : null;
  }
  
  public User getUser(int id) {

    String sql = "select * from users where idUser = ?"; 
        
    List<User> users = jdbcTemplate.query(sql, new UserMapper(), new Object[]{id});
        
    return users.size() > 0 ? users.get(0) : null;
  }
  
  public User getUser(String userName) {

    String sql = "select * from users where username='" + userName + "'"; 
        
    List<User> users = jdbcTemplate.query(sql, new UserMapper());
        
    return users.size() > 0 ? users.get(0) : null;
  }
  
  public User getUserByEmail(String email) {

    String sql = "select * from users where email='" + email + "'"; 
        
    List<User> users = jdbcTemplate.query(sql, new UserMapper());
        
    return users.size() > 0 ? users.get(0) : null;
  }
  
  
  public List<User> getUsersByFilter( ManagerUsersSearch filter ) {

    String sql = "";
      
    String f = "";
      
      f +=  !filter.getCod().isEmpty() ? f.isEmpty() ? " u.cod = '" + filter.getCod() + "'" : " and u.cod = '" + filter.getCod() + "'" : ""; 
      
      if ( filter.getOption() >= 0 )
      {
          f +=  f.isEmpty() ? " u.validationStatus = " + filter.getOption()  : " and u.validationStatus = " + filter.getOption(); 
      }
      
      if ( sql != null && !sql.isEmpty() )
      {
          sql += f.isEmpty() ? "" : " and " + f;
      }      
      else
      {
          sql = "from users u " + (f.isEmpty() ? "" : " where " + f);
      }
      
      String countSQL = "select count(*) " + sql;
      
      
      int count = jdbcTemplate.queryForObject( countSQL, Integer.class);      
      int pages = (int)((count + filter.getPageSize() - 1) / filter.getPageSize());
      int page = filter.getPage();
      
      if ( page > pages || page < 1 )
      {
          page = 1;
      }
      
      filter.setPages( pages );
      filter.setPage( page );
      filter.setTotal( count );
      
      int offset = (page - 1) * filter.getPageSize();
      
      String fullSQL = "select * " + sql + " limit " + offset + "," + filter.getPageSize() ;
        
    List<User> users = jdbcTemplate.query( fullSQL, new UserMapper());
                    
    return users;
  }
  

class UserMapper implements RowMapper<User> {

  public User mapRow(ResultSet rs, int arg1) throws SQLException {
    User user = new User();
            
    user.setIdUser( rs.getInt("idUser"));
    user.setCod(rs.getString("cod"));
    user.setUsername(rs.getString("username"));
    user.setPassword(rs.getString("password"));
    user.setName( rs.getString("name"));    
    user.setEmail(rs.getString("email"));
    //user.setAddress(rs.getString("address"));
    user.setPhone(rs.getString("phone"));
    user.setOwner( rs.getBoolean("owner"));
    user.setAdmin(rs.getBoolean("admin"));
    user.setCreation( rs.getTimestamp("creation"));
    user.setVerificationToken(rs.getString("verificationToken"));
    user.setComment(rs.getString("comment"));
    user.setActive( rs.getBoolean("active"));
    
    user.setValidationStatus( rs.getInt("validationStatus"));
    
    List<Media> validations = mediaService.getMedias( Media.TYPE_USER_DOCUMENT, user.getIdUser());
    
    user.setValidationDoc( validations != null && !validations.isEmpty() ? validations.get(0) : null );

    return user;
  }}
}
  
  
