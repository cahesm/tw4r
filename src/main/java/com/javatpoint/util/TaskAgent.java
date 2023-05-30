/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javatpoint.util;

import com.javatpoint.model.Reservation;
import com.javatpoint.service.ReservationService;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 *
 * @author cm
 */
public class TaskAgent {
    
    @Autowired
    ReservationService reservationService;
            
    @Scheduled(cron="0 0 1 * * ?")
    public void execute()
    {        
        deleteTempFiles();
        cancelDelayedReservations();
    }
    
    private void deleteTempFiles()
    {
        File tempFolder = new File(System.getProperty("user.home").concat( File.separator ).concat("images").concat( File.separator ).concat("temp") );
        
        List<File> files = new ArrayList();
        
        files = addFiles( files, tempFolder );
        
        for( File file : files )
        {
            long diff = new Date().getTime() - file.lastModified();

            if (diff > 24 * 60 * 60 * 1000) 
            {
                file.delete();
            }
        }
    }
    
    private List<File> addFiles(List<File> files, File dir)
    {
        if (files == null)
            files = new LinkedList<File>();

        if (!dir.isDirectory())
        {            
            files.add(dir);
            return files;
        }

        for (File file : dir.listFiles())
            addFiles(files, file);
        return files;
    }
    
    private void cancelDelayedReservations()
    {
        List<Reservation> reservations = reservationService.getDelayedReservations();
        
        for( Reservation reservation: reservations )
        {
            reservation.setStatus( Reservation.STATUS_CANCELED );
            reservation.setComment("Reserva cancelada automaticamente por atraso.");
             
            reservationService.update(reservation);
           
        }
    }
    
}
