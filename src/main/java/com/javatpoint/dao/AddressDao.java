package com.javatpoint.dao;

import com.javatpoint.model.Address;

public interface AddressDao {

  int register(Address address);  
  void update(Address address);
  
  Address getAddress( int id );
}
