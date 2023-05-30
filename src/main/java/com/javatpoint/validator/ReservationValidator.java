/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javatpoint.validator;

import com.javatpoint.model.Disponibility;
import com.javatpoint.model.Reservation;
import com.javatpoint.service.DisponibilityService;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ReservationValidator implements Validator {

    @Autowired
  DisponibilityService disponibilityService;
    
    public boolean supports(Class clazz) {
      return Reservation.class.equals(clazz);
    }

    
    public void validate(Object target, Errors errors) {
      Reservation reservation = (Reservation) target;

//      ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username",
//			"required.userName", "Field name is required.");
      
      Disponibility disponibility = disponibilityService.getDisponibility( reservation.getIdDisponibility() );

      if( reservation.getStartDate() == null ) 
      {
          errors.rejectValue("startDate", "required.startDate", "Campo Data de Início  é obrigatório!");
      }
      else if (!( reservation.getStartDate().compareTo( disponibility.getStartDate()) >= 0 && reservation.getStartDate().compareTo(disponibility.getEndDate()) <= 0)) 
        {
            errors.rejectValue("startDate", "interval.startDate", "Data de Início está fora do período da disponibilidade da unidade!");
        } 
      
      
      if( reservation.getEndDate() == null ) 
      {
          errors.rejectValue("endDate", "required.endDate", "Campo Data de Fim  é obrigatório!");
      }
      else if (!( reservation.getEndDate().compareTo( disponibility.getStartDate()) >= 0 && reservation.getEndDate().compareTo(disponibility.getEndDate()) <= 0)) 
      {
        errors.rejectValue("endDate", "interval.endDate", "Data de Fim está fora do período da disponibilidade da unidade!");
      } 
      
      if ( reservation.getStartDate() != null && reservation.getStartDate().after( reservation.getEndDate() ) )
      {
          errors.rejectValue("startDate", "interval.startDate", "Data de Início deve ser inferior a Data de Fim!");
      }
      
      if ( reservation.getStartDate() != null && reservation.getEndDate() != null )
      {
      
        long diff = reservation.getEndDate().getTime() - reservation.getStartDate().getTime();

        long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

        if ( days > disponibility.getNights() )
        {
            errors.rejectValue("endDate", "interval.endDate", "Intervalo entre datas não pode exceder " + disponibility.getNights() + " dias!" );
        }
      }
      
      
      
      
      
      
      
      
    }

}
