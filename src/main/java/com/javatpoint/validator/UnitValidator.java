/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javatpoint.validator;

import com.javatpoint.model.Disponibility;
import com.javatpoint.model.Unit;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UnitValidator implements Validator {

    
    public boolean supports(Class clazz) {
      return Unit.class.equals(clazz);
    }

    
    public void validate(Object target, Errors errors) {
      Unit unit = (Unit) target;


      
      if( unit.getIdHotel() == 0 ) 
      {
          errors.rejectValue("idHotel", "required.idHotel", "Campo hotel é obrigatório!");
      }
      
      if( unit.getBathRoom() == 0 ) 
      {
          errors.rejectValue("bathRoom", "value.bathRoom", "Valor deve ser superior a zero!");
      }
      
      if( unit.getBedRoom() == 0 ) 
      {
          errors.rejectValue("bedRoom", "value.bedRoom", "Valor deve ser superior a zero!");
      }
      
      if( unit.getRoom() == 0 ) 
      {
          errors.rejectValue("room", "value.room", "Valor deve ser superior a zero!");
      }
      
      if( unit.getDisponibilities() == null || unit.getDisponibilities().isEmpty() ) 
      {
          errors.rejectValue("disponibilities", "empty.disponibilities", "Nenhuma disponibilidade adicionada!");
      }
      else
      {
          int index = 0;
          for(  Disponibility disp : unit.getDisponibilities() )
          {
              if ( disp.getStartDate() == null || disp.getEndDate() == null  )
              {
                if ( disp.getStartDate() == null )
                {
                     errors.rejectValue("disponibilities["+index +"].startDate", "empty.disponibilities.startDate","Campo sem valor!");
                }
                
                if ( disp.getEndDate() == null )
                {
                     errors.rejectValue("disponibilities["+index +"].endDate", "empty.disponibilities.endDate","Campo sem valor!");
                }
              }
              else
              {              
                if ( disp.getEndDate().before( disp.getStartDate()) )
                {
                    errors.rejectValue("disponibilities["+index +"].startDate", "value.disponibilities.lessThen", "Data de fim inferior a data de início!");
                }
                
                if ( disp.getStartDate().before( new Date() ) )
                {
                    errors.rejectValue("disponibilities["+index +"].startDate", "value.disponibilities.lessThenToday", "Data de início deve ser superior a data atual!");
                }
                
//                long millisInDay = 60 * 60 * 24 * 1000;
//                long currentTime = new Date().getTime();
//                long dateOnly = (currentTime / millisInDay) * millisInDay;
                //Date clearDate = new Date(dateOnly);
                
                
//                if ( disp.getStartDate().before( clearDate ) )
//                {
//                    errors.rejectValue("disponibilities["+index +"].startDate", "value.disponibilities.lessThen", "Data de início inferior a data atual!");
//                }
                
                long diff = disp.getEndDate().getTime() - disp.getStartDate().getTime();

                long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

                if ( days < disp.getNights() )
                {
                    errors.rejectValue("disponibilities["+index +"].startDate", "value.disponibilities.wrongInterval", "Intervalo inferior ao número de noites!");
                }
               
                
              }
              
              if ( disp.getPrice() <= 0 )
              {                                                    
                  errors.rejectValue("disponibilities["+index +"].price", "value.disponibilities.price", "Valor deve ser superior a zero!");
              }
              
              if ( disp.getNights() <= 0 )
              {                  
                  errors.rejectValue("disponibilities["+index +"].nights", "value.disponibilities.nights", "Valor deve ser superior a zero!");
              }
              
              index++;
          }
      }
      
      
      
      if( unit.getMedias() == null || unit.getMedias().isEmpty() ) 
      {
          errors.rejectValue("medias", "empty.medias", "Nenhuma foto adicionada!");
      }
      
      
      
      
    }

}
