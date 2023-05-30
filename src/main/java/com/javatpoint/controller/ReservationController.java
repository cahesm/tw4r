package com.javatpoint.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javatpoint.model.Disponibility;
import com.javatpoint.model.Hotel;
import com.javatpoint.model.ManagerReservationsSearch;
import com.javatpoint.model.Media;
import com.javatpoint.model.Reservation;
import com.javatpoint.model.Unit;
import com.javatpoint.model.User;
import com.javatpoint.service.DisponibilityService;
import com.javatpoint.service.EmailService;
import com.javatpoint.service.HotelService;
import com.javatpoint.service.MediaService;
import com.javatpoint.service.ReservationService;
import com.javatpoint.service.UnitService;
import com.javatpoint.service.UserService;
import com.javatpoint.util.AppUtilities;
import com.javatpoint.validator.ReservationValidator;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

@Controller
@SessionAttributes
public class ReservationController {

  @Autowired
  ReservationService reservationService;
  
  @Autowired
  DisponibilityService disponibilityService;
  
  @Autowired
  UnitService unitService;
  
  @Autowired
  HotelService hotelService;
  
  @Autowired
  MediaService mediaService;
  
  @Autowired  
  EmailService emailService;
  
  @Autowired
  UserService userService;
  
  @Autowired
  ReservationValidator reservationValidator;
  
  
  
  @Autowired
  ServletContext context; 
  
  private static final String AJAX_HEADER_NAME = "X-Requested-With";
  private static final String AJAX_HEADER_VALUE = "XMLHttpRequest";
  
  @InitBinder("reservation")
   protected void initUnitBinder(WebDataBinder binder) {
      binder.addValidators(reservationValidator);
   }
   
   @InitBinder
    public void bindingPreparation(WebDataBinder binder) 
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        //DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        CustomDateEditor orderDateEditor = new CustomDateEditor(dateFormat, true);
        binder.registerCustomEditor(Date.class, orderDateEditor);
    }

  @RequestMapping(value = "/showReservation" )
	public String showUnit(Model m, HttpServletRequest request, @RequestParam String c, @RequestParam( required = false ) String back, HttpSession session) 
        {
            back = back != null ? back : "hello.html";

            byte[] decodedBytes = Base64.getMimeDecoder().decode(c);
            String cod = new String(decodedBytes);

            Reservation reservation = reservationService.getReservation( cod );

                       
            m.addAttribute("back", back);

            m.addAttribute("reservation", reservation );                
                                                                                     
            return "showReservation";
	}  
    
  @RequestMapping("/addReservation")
	public String registerReservation( Model m, HttpServletRequest request, @RequestParam String token,@RequestParam( required = false ) String back, HttpSession session ) 
        {
//            HashMap atts = new HashMap();
//            atts.put( "idDisponibility", idDisponibility);
            back = back != null ? back : "hello.html";
            
            String target = AppUtilities.redirectToLogin( m, "redirect:addReservation.html?token="+token+"%26back="+back, request );
            
            HashMap ids = (HashMap)session.getAttribute("ids");
            
            if ( ids.containsKey( token ))
            {
                int idDisponibility = (Integer)ids.get( token );        
                
                if ( target.contains("addReservation"))
                {
                    target = "addReservation";
                                        
                    Disponibility disponibility = disponibilityService.getDisponibility( idDisponibility );                
                    Unit unit = unitService.getUnitWithoutDependencies( disponibility.getIdUnit() );
                    Hotel hotel = hotelService.getHotel( unit.getIdHotel() );

                    disponibility.setToken( token );

                    Reservation reservation = null;
                    
                    if ( session.getAttribute("reservation") != null )
                    {
                        reservation = (Reservation)session.getAttribute("reservation");
                        session.removeAttribute("reservation");
                        
                        reservation.setIdDisponibility(idDisponibility );
                        reservation.setDisponibility( disponibility );
                    }
                    else
                    {
                        reservation = new Reservation();  
                        reservation.setIdDisponibility(idDisponibility );

                        reservation.setDisponibility( disponibility );
                    }
                    
                                                            
                    if ( session.getAttribute("binding") != null )
                    {
                        m.addAttribute("org.springframework.validation.BindingResult.reservation", session.getAttribute("binding") );
                        session.removeAttribute("binding");
                    }
                    
                    String message = "Reservation";
                    m.addAttribute("reservation", reservation );   
                    m.addAttribute("hotel", hotel );   
                    m.addAttribute("back", back );   
                    
                    m.addAttribute("message", message);    
                               
                }
	
                
            }
            else
            {
                m.addAttribute("message", "Disponibilidade de reserva não encontrada. Por favor atualize a página.");
            }
            
            return target; 
	}    
        
    
    
  
    @RequestMapping(value = "/approvePayment", method = RequestMethod.POST )
    public String approvePayment(HttpServletRequest servletRequest, @RequestParam("idReservation") int idReservation ) 
    {
        Reservation reservation = reservationService.getReservation( idReservation );
        
        reservation.setStatus( Reservation.STATUS_WAITING_VOUCHER );
        reservationService.update(reservation);
                
        Disponibility disponibility = disponibilityService.getDisponibility( reservation.getIdDisponibility() );
        Unit unit = unitService.getUnit( disponibility.getIdUnit() );

        User user = userService.getUser( unit.getIdUser() );
        
        emailService.sendMessage( user.getEmail(), "Pagamento de reserva confirmado", 
        "Caro Proprietário,\n" +
        "temos a satisfação de comunicá-lo que sua reserva foi paga pelo cliente. \n" +
        "Estamos no momento em que será necessário a emissão do voucher em seu hotel/resort de acordo com as datas solicitadas pelo cliente.\n" +
        "Enquanto isso, a fim de dar segurança a toda a transação, tanto para você como para o cliente, entra uma questão importante da nossa missão como empresa. Vamos fazer a custódia do pagamento de sua reserva  até que o cliente faça uso de seu voucher de viagem. Caso aconteça algum imprevisto com o voucher, nós devolveremos os valores para o cliente.\n" +
        "Esse pagamento será repassado a você no momento da viagem de seu cliente, ou após o período do possível cancelamento.\n" +
        "O voucher deve ser carregado no site na opção “Minhas Unidades”\n" +
        "Qualquer dúvida entre em contato com nosso suporte.\n\n" +  
        "Acesse sua unidade: http://tw4r.com/unit.html" );        
        
        return "redirect:managerReservation.html?option=1";
    }
    

    
    @RequestMapping(value = "/approveVoucher", method = RequestMethod.POST )
    public String approveVoucher(HttpServletRequest servletRequest, @RequestParam("idReservation") int idReservation ) 
    {
        Reservation reservation = reservationService.getReservation( idReservation );
        
        reservation.setStatus( Reservation.STATUS_COMPLETED );
        reservation.setComment("");
        reservationService.update(reservation);
        
        User user = userService.getUser( reservation.getIdUser() );
            
        emailService.sendMessage( user.getEmail(), "Voucher da unidade emitido pelo proprietário",
                "Caro Cliente,\n" +
                "temos a satisfação de comunicá-lo que seu voucher para a viagem foi gerado e está disponível na nossa plataforma.\n" +
                "Desejamos a você uma experiência inesquecível nesta viagem.\n" +
                "Entre em contato com qualquer dúvida ou problema em relação ao uso deste voucher.\n" +
                "Até lá, a fim de dar segurança a toda a transação, tanto para você como para o proprietário, entra uma questão importante da nossa missão como empresa. Vamos fazer a custódia do pagamento de sua reserva  até que você faça sua viagem ou que o período para um possível cancelamento seja alcançado. \n" +
                "Qualquer dúvida entre em contato com nosso suporte.\n\n" + 
                "Acesse sua reserva: http://tw4r.com/reservation.html" );           
        
        return "redirect:managerReservation.html?option=0";
    }
    
     @RequestMapping(value = "/reproveVoucher", method = RequestMethod.POST )
    public String reproveVoucher(HttpServletRequest servletRequest, @RequestParam("idReservation") int idReservation, @RequestParam("comment") String comment )     
    {
        
        Reservation reservation = reservationService.getReservation( idReservation );
        
        if ( reservation.getVoucher() != null ) 
        {
            Media media = reservation.getVoucher();
            media.setStatus( Media.STATUS_REPROVED );            
            media.setComment( comment );            
            mediaService.update( media );
            
            reservation.setComment( comment );
            reservationService.update(reservation);
            
            Disponibility disponibility = disponibilityService.getDisponibility( reservation.getIdDisponibility() );
            Unit unit = unitService.getUnit( disponibility.getIdUnit() );

            User user = userService.getUser( unit.getIdUser() );
            
            emailService.sendMessage( user.getEmail(), "Voucher reprovado", 
                "Caro proprietário,\n" +
                "o voucher emitido para uma reserva de sua unidade foi reprovado pela nossa equipe.\n\n" +
                "Motivo:\n" + comment + "\n\n" +                
                "Qualquer dúvida entre em contato com nosso suporte.\n\n" +
                "Acesse sua unidade: http://tw4r.com/unit.html" );
            
        }
        
        return "redirect:managerReservation.html?option=0";
        
    }
    
        @RequestMapping(value = "/processReservation", method = RequestMethod.POST)
    public String processRegister(@ModelAttribute("reservation") @Validated Reservation reservation, BindingResult result, Model m, HttpServletRequest request, HttpSession session) 
    {
           
        if ( result.hasErrors() )
        {            
            
            Disponibility disponibility = disponibilityService.getDisponibility( reservation.getIdDisponibility() );                
            Unit unit = unitService.getUnitWithoutDependencies( disponibility.getIdUnit() );
            Hotel hotel = hotelService.getHotel( unit.getIdHotel() );
            
            session.setAttribute("hotel",hotel);
            session.setAttribute("reservation",reservation);
            session.setAttribute("binding",result);       
            
            String back = "hello.html";
                        
            return "redirect:addReservation.html?token="+reservation.getDisponibility().getToken()+"&back="+back;
        }
        else
        {
                       
            String target = AppUtilities.redirectToLogin( m, "redirect:reservation.html", request);

            if ( target == "redirect:reservation.html")
            {        
                int userId = (Integer)request.getSession().getAttribute("userId");
                
                reservation.setIdUser( userId );
                reservationService.register( reservation );
                
                Disponibility disponibility = disponibilityService.getDisponibility( reservation.getIdDisponibility() );
                Unit unit = unitService.getUnit( disponibility.getIdUnit() );
                                            
                User user = userService.getUser( unit.getIdUser() );
            
                emailService.sendMessage( user.getEmail(), "Unidade reservada", 
                "Caro proprietário,\n" +
                "Temos a satisfação de comunicá-lo que existe uma reserva cadastrada com interesse em sua Unidade. \n" +
                "Precisamos que você entre no nosso site em “Minhas Unidades” e avalie as datas solicitadas na reserva. Você pode aprovar ou não a reserva de acordo com a disponibilidade em seu hotel/resort.\n\n" +
                "Acesse sua unidade: http://tw4r.com/unit.html" );        

                session.setAttribute("modalMessage","Reserva registrada com sucesso!");
               
            }

            return target;
        }
    }
  
    @RequestMapping("/managerReservation")
	public String managerReservation( Model m, HttpServletRequest request,  HttpSession session, @RequestParam( required = false ) Integer option, @RequestParam( required = false ) String c ) {
            
            HashMap atts = new HashMap();
            atts.put( "option", option);
            
            int op = option != null ? option : -1;
            
            String target = AppUtilities.redirectToLogin( m, "managerReservation", request, atts);
            
            if ( target == "managerReservation")
            {           
                int userId = (Integer)request.getSession().getAttribute("userId");

                User user = userService.getUser( userId );

                if ( user.isAdmin())
                {  
                    ManagerReservationsSearch search = new ManagerReservationsSearch();
                    
                    if ( session.getAttribute("managerReservationsSearch") != null )
                    {
                        search = (ManagerReservationsSearch)session.getAttribute("managerReservationsSearch");
                        session.removeAttribute("managerReservationsSearch");
                    }
                                                            
                    search.setOption( op );
                    
                    if ( c != null && !c.isEmpty() )
                    {
                        byte[] decodedBytes = Base64.getMimeDecoder().decode(c);
                        String cod = new String(decodedBytes);
                        search.setCod( cod );
                        
                        search.setOption( -1 );
                    }
                    
                    m.addAttribute("reservationList", reservationService.getReservationsByFilter( search ));
                    m.addAttribute("managerReservationsSearch", search);
                   
                    m.addAttribute("option", op );                                        
                }
                else
                {
                    target=  "redirect:noAdminPermission.html";
                }
            }
                
            return target;
	}    
        
    @RequestMapping("/unitReservation")
	public String unitReservations( Model m, HttpServletRequest request,  HttpSession session, @RequestParam( required = true ) String token, @RequestParam( required = false ) Integer option, @RequestParam( required = false ) String c ) {
            
            HashMap atts = new HashMap();
            atts.put( "token", token);
            atts.put( "option", option);
            
            int op = option != null ? option : -1;
            
            String target = AppUtilities.redirectToLogin( m, "unitReservation", request, atts);
            //String target = AppUtilities.redirectToLogin( m, "unitReservation.html?token="+token, request, atts );
            
            if ( target.contains( "unitReservation") )
            {     
                HashMap ids = (HashMap)session.getAttribute("ids");
            
                if ( ids.containsKey( token ))
                {
                
                    int idUnit = (Integer)ids.get( token );  

                    ManagerReservationsSearch search = new ManagerReservationsSearch();

                    if ( session.getAttribute("unitReservationsSearch") != null )
                    {
                        search = (ManagerReservationsSearch)session.getAttribute("unitReservationsSearch");
                        session.removeAttribute("unitReservationsSearch");
                    }

                    search.setIdUnit( idUnit );
                    search.setOption( op );
                    search.setToken( token );

                    if ( c != null && !c.isEmpty() )
                    {
                        byte[] decodedBytes = Base64.getMimeDecoder().decode(c);
                        String cod = new String(decodedBytes);
                        search.setCod( cod );

                        search.setOption( -1 );
                    }

                    m.addAttribute("reservationList", reservationService.getReservationsByFilter( search ));
                    m.addAttribute("unitReservationsSearch", search);

                    m.addAttribute("option", op );   
                    m.addAttribute("token", token );   
                }
                else
                {
                    m.addAttribute("message", "Unidade não encontrada. Por favor atualize a página.");
                }
                
            }
                
            return target;
	}    
    
    
   @RequestMapping("/reservation")
	public String listReservation( Model m, HttpServletRequest request, HttpSession session ) {
            
            String target = AppUtilities.redirectToLogin( m, "reservation", request);
            
            if ( target == "reservation")
            {
                int userId = (Integer)request.getSession().getAttribute("userId");
                List<Reservation> reservations =  reservationService.getReservationsByUser( userId );
                                
                if ( session.getAttribute("modalMessage") != null )
                {
                    m.addAttribute("modalMessage", session.getAttribute("modalMessage") );
                    session.removeAttribute("modalMessage");
                }
                
                m.addAttribute("reservationList", reservations);
            }
                
            return target;
	}    
  
   
    
    @RequestMapping(value = "/searchManagerReservations", method = RequestMethod.POST)
    public String searchManagerReservations(@ModelAttribute("managerReservationsSearch") ManagerReservationsSearch reservationsSearch, BindingResult result, Model m, HttpServletRequest request, HttpSession session) 
    {         
       session.setAttribute("managerReservationsSearch",reservationsSearch);
        
       return "redirect:managerReservation.html";
    }
    
    @RequestMapping(value = "/searchUnitReservations", method = RequestMethod.POST)
    public String searchUnitReservations(@ModelAttribute("unitReservationsSearch") ManagerReservationsSearch reservationsSearch, BindingResult result, Model m, HttpServletRequest request, HttpSession session) 
    {         
        session.setAttribute("unitReservationsSearch",reservationsSearch);
        
       return "redirect:unitReservation.html?token="+reservationsSearch.getToken();
    }
    
    
    @RequestMapping(value = "/approveReservation", method = RequestMethod.POST, headers="Accept=*/*", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String approveReservation(HttpServletRequest servletRequest, @RequestParam("idReservation") int idReservation ) throws JsonProcessingException 
    {
        ObjectMapper m = new ObjectMapper();
        Reservation reservation = reservationService.getReservation( idReservation );
        
        reservation.setStatus( Reservation.STATUS_WAITING_PAYMENT );
        reservationService.update(reservation);
        
        User user = userService.getUser( reservation.getIdUser() );
        
        String cod = Base64.getEncoder().encodeToString( reservation.getCod().getBytes() );
            
        emailService.sendMessage( user.getEmail(), "Reserva aprovada", 
                "Caro cliente,\n" +
                "temos a satisfação de comunicá-lo que sua reserva foi aprovada pelo proprietário.\n" +
                "Logo será disponibilizado em sua reserva o boleto para pagamento.\n" +
                "Após o pagamento do boleto não esqueça de fazer upload do comprovante de pagamento em sua reserva.\n" +
                "A fim de dar segurança a toda a transação, tanto para você como para o proprietário, entra uma questão importante da nossa missão como empresa. Vamos fazer a custódia do seu pagamento até que o proprietário possa gerar o seu voucher de viagem. Caso aconteça algum imprevisto com o voucher, nós devolveremos o seu pagamento. \n" +
                "Esse pagamento só será repassado ao proprietário no momento da sua viagem, ou após o período do possível cancelamento.\n" +
                "O pagamento deve ser realizado via boleto bancário e o comprovante deve ser carregado no site na opção “Minha Reserva”\n" +
                "Qualquer dúvida entre em contato com nosso suporte.\n\n" +
                "Acesse sua reserva: http://tw4r.com/reservation.html" );  
        
        emailService.sendMessage( "contato@tw4r.com", "Gerar boleto para pagamento de reserva",
                "Reserva aprovada pelo proprietário, por favor acesse o gerenciador e gere um boleto para o pagamento. \n" + 
                "Código da reserva: " + reservation.getCod() + "\n\n" +
                "Acesse a reserva pelo gerenciador: http://tw4r.com/managerReservation.html?c=" + cod  );         
        
        
        return m.writeValueAsString( reservation  );
    }
    
    @RequestMapping(value = "/reproveReservation", method = RequestMethod.POST, headers="Accept=*/*", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String reproveReservation(HttpServletRequest servletRequest, @RequestParam("idReservation") int idReservation, @RequestParam("comment") String comment ) throws JsonProcessingException
    {
        ObjectMapper m = new ObjectMapper();
        Reservation reservation = reservationService.getReservation( idReservation );
        
        reservation.setStatus( Reservation.STATUS_REPROVED );
        reservation.setComment( comment );
        reservationService.update(reservation);
        
         User user = userService.getUser( reservation.getIdUser() );
            
        emailService.sendMessage( user.getEmail(), "Reserva reprovada", 
                "Caro cliente,\n" +
                "Sua reserva foi reprovada pelo proprietário.\n\n" +
                "Motivo:\n" + comment + "\n\n" +                
                "Qualquer dúvida entre em contato com nosso suporte.\n\n" +
                "Acesse sua reserva: http://tw4r.com/reservation.html" );
        
        
        return m.writeValueAsString( reservation  );
    }
    
    @RequestMapping(value = "/cancelReservation", method = RequestMethod.POST, headers="Accept=*/*", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String cancelReservation(HttpServletRequest servletRequest, @RequestParam("idReservation") int idReservation ) throws JsonProcessingException
    {
        ObjectMapper m = new ObjectMapper();
        Reservation reservation = reservationService.getReservation( idReservation );
        
        reservation.setStatus( Reservation.STATUS_CANCELED );
        
        reservationService.update(reservation);
        
        return m.writeValueAsString( reservation  );
    }
    
    @RequestMapping(value = "/uploadReservationPayment", method = RequestMethod.POST, headers="Accept=*/*", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String uploadReservationPayment(HttpServletRequest servletRequest, @RequestParam("multipartFile") MultipartFile multipartFile,@RequestParam("idReservation") int idReservation ) throws JsonProcessingException
    {
        ObjectMapper m = new ObjectMapper();
        Media media = new Media();
        
        media.setIdSource( idReservation );
        media.setType( Media.TYPE_RESERVATION_PAYMENT );
        media.setAlias( multipartFile.getOriginalFilename() );
        media.setStatus( Media.STATUS_PENDING_APPROVAL );
        String extension = FilenameUtils.getExtension( media.getAlias() );
        try {
            String fileName = UUID.randomUUID().toString().replace("-", "").concat( extension != null && !extension.isEmpty() ? "." + extension : "" );
            //File folder = new File(context.getRealPath("").concat( File.separator ).concat("documents").concat( File.separator ).concat("base").concat( File.separator ).concat("payments"));
            File folder = new File(System.getProperty("user.home").concat( File.separator ).concat("documents").concat( File.separator ).concat("base").concat( File.separator ).concat("payments"));
            folder.mkdirs();
            File file = new File(folder.getAbsolutePath(), fileName);
            file.getParentFile().mkdirs();
            multipartFile.transferTo(file);
            media.setName( fileName );
            
            mediaService.register( media );
            
            Reservation reservation = reservationService.getReservation( idReservation );
            
            String cod = Base64.getEncoder().encodeToString( reservation.getCod().getBytes() );

            emailService.sendMessage( "contato@tw4r.com", "Pagamento de reserva efetuado", 
                "Pagamento de reserva foi efetuado pelo cliente, por favor acesse o gerenciador e valide o pagamento. \n" + 
                "Código da reserva: " + reservation.getCod() + "\n\n" +
                "Acesse a reserva pelo gerenciador: http://tw4r.com/managerReservation.html?c=" + cod );  
            
            
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return m.writeValueAsString( media  );
    }
    
   @RequestMapping(value = "/uploadReservationVoucher", method = RequestMethod.POST, headers="Accept=*/*", produces = MediaType.APPLICATION_JSON_VALUE)
   @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String uploadReservationVoucher(HttpServletRequest servletRequest, @RequestParam("multipartFile") MultipartFile multipartFile,@RequestParam("idReservation") int idReservation ) throws JsonProcessingException
    {
        ObjectMapper m = new ObjectMapper();
        Media media = new Media();
        
        media.setIdSource( idReservation );
        media.setType( Media.TYPE_RESERVATION_VOUCHER );
        media.setAlias( multipartFile.getOriginalFilename() );
        media.setStatus( Media.STATUS_CREATED );
        String extension = FilenameUtils.getExtension( media.getAlias() );
        try {
            String fileName = UUID.randomUUID().toString().replace("-", "").concat( extension != null && !extension.isEmpty() ? "." + extension : "" );
            //File folder = new File(context.getRealPath("").concat( File.separator ).concat("documents").concat( File.separator ).concat("base").concat( File.separator ).concat("vouchers"));
            File folder = new File(System.getProperty("user.home").concat( File.separator ).concat("documents").concat( File.separator ).concat("base").concat( File.separator ).concat("vouchers"));
            folder.mkdirs();
            File file = new File(folder.getAbsolutePath(), fileName);
            file.getParentFile().mkdirs();
            multipartFile.transferTo(file);
            media.setName( fileName );
            
            mediaService.deleteAll( Media.TYPE_RESERVATION_VOUCHER, idReservation );
            
            mediaService.register( media );
            
            Reservation reservation = reservationService.getReservation( idReservation );
            reservation.setComment("");
            reservationService.update( reservation );
            
            String cod = Base64.getEncoder().encodeToString( reservation.getCod().getBytes() );

            emailService.sendMessage( "contato@tw4r.com", "Voucher de reserva emitido", 
                "Voucher de reserva foi emitido pelo proprietário, por favor acesse o gerenciador e valide o voucher. \n" + 
                "Código da reserva: " + reservation.getCod() + "\n\n" +
                "Acesse a reserva pelo gerenciador: http://tw4r.com/managerReservation.html?c=" + cod );  
                                    
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return m.writeValueAsString( media  );
    }
    
   @RequestMapping(value = "/uploadReservationDoc", method = RequestMethod.POST, headers="Accept=*/*", produces = MediaType.APPLICATION_JSON_VALUE)
   @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String uploadReservationDoc(HttpServletRequest servletRequest, @RequestParam("multipartFile") MultipartFile multipartFile,@RequestParam("idReservation") int idReservation ) throws JsonProcessingException
    {
        ObjectMapper m = new ObjectMapper();
        Media media = new Media();
        
        media.setIdSource( idReservation );
        media.setType( Media.TYPE_RESERVATION_DOC );
        media.setAlias( multipartFile.getOriginalFilename() );
        media.setStatus( Media.STATUS_CREATED );
        String extension = FilenameUtils.getExtension( media.getAlias() );
        try {
            String fileName = UUID.randomUUID().toString().replace("-", "").concat( extension != null && !extension.isEmpty() ? "." + extension : "" );
            //File folder = new File(context.getRealPath("").concat( File.separator ).concat("documents").concat( File.separator ).concat("base").concat( File.separator ).concat("vouchers"));
            File folder = new File(System.getProperty("user.home").concat( File.separator ).concat("documents").concat( File.separator ).concat("base").concat( File.separator ).concat("docs"));
            folder.mkdirs();
            File file = new File(folder.getAbsolutePath(), fileName);
            file.getParentFile().mkdirs();
            multipartFile.transferTo(file);
            media.setName( fileName );
            
            mediaService.deleteAll( Media.TYPE_RESERVATION_DOC, idReservation );
            
            mediaService.register( media );
            
//            Reservation reservation = reservationService.getReservation( idReservation );
//            
//            String cod = Base64.getEncoder().encodeToString( reservation.getCod().getBytes() );
//
//            emailService.sendMessage( "contato@tw4r.com", "Voucher de reserva emitido", 
//                "Voucher de reserva foi emitido pelo proprietário, por favor acesse o gerenciador e valide o voucher. \n" + 
//                "Código da reserva: " + reservation.getCod() + "\n\n" +
//                "Acesse a reserva pelo gerenciador: http://tw4r.com/managerReservation.html?c=" + cod );  
                                    
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return m.writeValueAsString( media  );
    }
    
    @RequestMapping(value = "/removePayment", method = RequestMethod.POST, headers="Accept=*/*", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String removeDocument(HttpServletRequest servletRequest, @RequestParam("idMedia") int idMedia ) throws JsonProcessingException
    {
        ObjectMapper m = new ObjectMapper();
        Media media = mediaService.getMedia( idMedia );
                
        mediaService.delete( media.getIdMedia() );
        
        return m.writeValueAsString( media  );
    }
       
}
  