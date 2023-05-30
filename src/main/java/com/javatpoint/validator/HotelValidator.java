/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javatpoint.validator;

import com.javatpoint.model.Hotel;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class HotelValidator implements Validator {

    
    public boolean supports(Class clazz) {
      return Hotel.class.equals(clazz);
    }

    
    public void validate(Object target, Errors errors) {
      Hotel hotel = (Hotel) target;

//      ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username",
//			"required.userName", "Field name is required.");
      
      if( hotel.getName().isEmpty() ) 
      {
          errors.rejectValue("name", "required.name", "Campo nome é obrigatório!");
      }
      
      if( hotel.getAddress().getIdCountry() == -1 ) 
      {
          errors.rejectValue("address.idCountry", "required.country", "Nenhum país selecionado!");
      }
      
      if( hotel.getAddress().getIdState()== -1 ) 
      {
          errors.rejectValue("address.idState", "required.state", "Nenhum estado selecionado!");
      }
      
      if( hotel.getAddress().getIdCity() == -1 ) 
      {
          errors.rejectValue("address.idCity", "required.city", "Nenhuma cidade selecionada!");
      }
      
      if( hotel.getAddress().getAddress().isEmpty() ) 
      {
          errors.rejectValue("address.address", "required.address", "Campo endereço é obrigatório!");
      }
      
      if( hotel.getAddress().getNumber().isEmpty() ) 
      {
          errors.rejectValue("address.number", "required.number", "Campo número é obrigatório!");
      }
                  
      if( hotel.getMedias() == null || hotel.getMedias().isEmpty() ) 
      {
          errors.rejectValue("medias", "empty.medias", "Nenhuma foto adicionada!");
      }
      
      
      
      
    }

}
