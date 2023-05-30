package com.javatpoint.service;

import com.javatpoint.model.Hotel;
import com.javatpoint.model.HotelsSearch;
import com.javatpoint.model.SearchResultItem;
import java.util.List;

public interface HotelService {

  void register(Hotel hotel);
  void update(Hotel hotel);
  void delete(int id);
  
  Hotel getHotel(String name);
  Hotel getHotel(int id);
  
  List<Hotel> getHotels();
  List<Hotel> getHotelsByFilter( HotelsSearch filter);
  
  List<SearchResultItem>search( String name );
  List<SearchResultItem>searchGrouped( String name );
}
