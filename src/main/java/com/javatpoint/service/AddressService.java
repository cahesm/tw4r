package com.javatpoint.service;

import com.javatpoint.model.Address;
import com.javatpoint.model.Hotel;

public interface AddressService {

  int register(Address address);
  void update(Address address);  
    
  Address getAddress(int id);
}
