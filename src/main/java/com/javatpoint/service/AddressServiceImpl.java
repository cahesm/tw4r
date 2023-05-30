package com.javatpoint.service;

import com.javatpoint.dao.AddressDao;
import org.springframework.beans.factory.annotation.Autowired;

import com.javatpoint.model.Address;

public class AddressServiceImpl implements AddressService {

  @Autowired
  public AddressDao addressDao;

  public int register(Address address) {
    return addressDao.register(address);
  }
  
  public void update(Address address) {
    addressDao.update(address);
  }
    
  public Address getAddress(int id) {
    return addressDao.getAddress(id);
  }

}
