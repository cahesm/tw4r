package com.javatpoint.service;

import com.javatpoint.dao.HotelDao;
import org.springframework.beans.factory.annotation.Autowired;

import com.javatpoint.model.Hotel;
import com.javatpoint.model.HotelsSearch;
import com.javatpoint.model.SearchResultItem;
import java.util.List;

public class HotelServiceImpl implements HotelService {

  @Autowired
  public HotelDao hotelDao;

  public void register(Hotel hotel) {
    hotelDao.register(hotel);
  }
  
  public void update(Hotel hotel) {
    hotelDao.update(hotel);
  }
  
  public void delete(int id) {
    hotelDao.delete(id);
  }
  
  public Hotel getHotel(String name) {
    return hotelDao.getHotel(name);
  }
  
  public Hotel getHotel(int id) {
    return hotelDao.getHotel(id);
  }
  
  public List<Hotel> getHotels() {
    return hotelDao.getHotels();
  }
  
  public List<Hotel> getHotelsByFilter( HotelsSearch filter ) {
    return hotelDao.getHotelsByFilter( filter );
  }
  
  public List<SearchResultItem> search( String name ) {
    return hotelDao.search( name );
  }
  
  public List<SearchResultItem> searchGrouped( String name ) {
    return hotelDao.searchGrouped( name );
  }

}
