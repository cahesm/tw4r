package com.javatpoint.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javatpoint.model.Attribute;
import com.javatpoint.model.Disponibility;
import com.javatpoint.model.Hotel;
import com.javatpoint.model.ManagerUnitsSearch;
import com.javatpoint.model.Media;
import com.javatpoint.model.Option;
import com.javatpoint.model.Reservation;
import com.javatpoint.model.SearchResultItem;
import com.javatpoint.model.Unit;
import com.javatpoint.model.UnitsSearch;
import com.javatpoint.model.User;
import com.javatpoint.service.AttributeService;
import com.javatpoint.service.DisponibilityService;
import com.javatpoint.service.EmailService;
import com.javatpoint.service.HotelService;
import com.javatpoint.service.MediaService;
import com.javatpoint.service.OptionService;
import com.javatpoint.service.ReservationService;
import com.javatpoint.service.UnitService;
import com.javatpoint.service.UserService;
import com.javatpoint.util.AppUtilities;
import com.javatpoint.util.FormCustomNumberEditor;
import com.javatpoint.validator.UnitValidator;
import java.beans.PropertyEditorSupport;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@SessionAttributes
public class UnitController {

  @Autowired
  UnitService unitService;
  
  @Autowired
  MediaService mediaService;
  
  @Autowired
  AttributeService attributeService;
  
  @Autowired
  DisponibilityService disponibilityService;
  
  @Autowired
  ReservationService reservationService;
  
  @Autowired
  HotelService hotelService;
  
  @Autowired
  UserService userService;
  
  @Autowired
  UnitValidator unitValidator;
  
  @Autowired  
  EmailService emailService;
  
  @Autowired  
  OptionService optionService;
  
  @Autowired
  ServletContext context; 
  
  private static final String AJAX_HEADER_NAME = "X-Requested-With";
  private static final String AJAX_HEADER_VALUE = "XMLHttpRequest";
  

   @InitBinder("unit")
   protected void initUnitBinder(WebDataBinder binder) {
      binder.addValidators(unitValidator);
   }
  
    @InitBinder
    public void bindingPreparation(WebDataBinder binder) 
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        CustomDateEditor orderDateEditor = new CustomDateEditor(dateFormat, true);
        binder.registerCustomEditor(Date.class, orderDateEditor);
                                
        binder.registerCustomEditor(List.class, "attributes", new PropertyEditorSupport() {
        @Override
        public void setAsText(String text) {
            String [] ids = text.split(",");
            List attributes = null;
            for(String id:ids){
                if(attributes == null)
                    attributes = new ArrayList();
                 Attribute att = attributeService.getAttribute( Integer.parseInt(id) );
                if(att != null)
                    attributes.add(att);
            }

            setValue(attributes);
            
        }
    });
        

        binder.registerCustomEditor(double.class, new FormCustomNumberEditor(Double.class));
        binder.registerCustomEditor(float.class, new FormCustomNumberEditor(Float.class));
        binder.registerCustomEditor(long.class, new FormCustomNumberEditor(Long.class));
        binder.registerCustomEditor(int.class, new FormCustomNumberEditor(Integer.class));

                                
    }
  
   @RequestMapping(value = "/offers" , method = RequestMethod.GET)
	public String offers( Model m, HttpSession session ) {
		
                
                UnitsSearch filter = null; //new HotelsSearch();
                
                if ( session.getAttribute("unitsSearch") != null )
                {
                    filter = (UnitsSearch)session.getAttribute("unitsSearch");

                    session.removeAttribute("unitsSearch");
                    
                    m.addAttribute("dispList", disponibilityService.getDisponibilitiesByFilter( filter ) );
                    
                }
                else
                {
                    filter = new UnitsSearch();
                }
                
                m.addAttribute("offers", filter);
		              
		return "offers"; 
	}
    
    
   @RequestMapping(value = "/unit", method = RequestMethod.GET)
	public String listUnit( Model m, HttpServletRequest request, HttpSession session ) {
            
            String target = AppUtilities.redirectToLogin( m, "unit", request);
            
                                  
            if ( target == "unit")
            {             
                int userId = (Integer)request.getSession().getAttribute("userId");

                User user = userService.getUser( userId );

                if ( user.isOwner() )
                {  
                    List<Unit> units = unitService.getUnitsByUser( userId );

                    HashMap ids = new HashMap();

                    for( Unit unit : units )
                    {
                        String token = AppUtilities.generateNewToken();

                        unit.setToken( token );

                        ids.put( token , unit.getIdUnit() );
                    }

                    session.setAttribute( "ids", ids );

                    if ( session.getAttribute("modalMessage") != null )
                    {
                        m.addAttribute("modalMessage", session.getAttribute("modalMessage") );
                        session.removeAttribute("modalMessage");
                    }


                    m.addAttribute("unitList", units);
                }                     
                else
                {
                    target = "redirect:noOwnerPermission.html";
                }
            }
           
                
            return target;
	}    
        
   @RequestMapping("/managerUnit")
	public String managerUnit( Model m, HttpServletRequest request, HttpSession session, @RequestParam( required = false ) Integer option, @RequestParam( required = false ) String c ) {
            
            
            HashMap atts = new HashMap();
            atts.put( "option", option);
            
            String target = AppUtilities.redirectToLogin( m, "managerUnit", request, atts);
            
            int op = option != null ? option : -1;
            
            if ( target == "managerUnit")
            {        
                int userId = (Integer)request.getSession().getAttribute("userId");

                User user = userService.getUser( userId );

                if ( user.isAdmin())
                {  
                    ManagerUnitsSearch search = new ManagerUnitsSearch();
                    
                    if ( session.getAttribute("managerUnitsSearch") != null )
                    {
                        search = (ManagerUnitsSearch)session.getAttribute("managerUnitsSearch");
                        session.removeAttribute("managerUnitsSearch");
                    }
                                                            
                    search.setOption( op );
                    
                    if ( c != null && !c.isEmpty() )
                    {
                        byte[] decodedBytes = Base64.getMimeDecoder().decode(c);
                        String cod = new String(decodedBytes);
                        search.setCod( cod );
                        
                        search.setOption( -1 );
                    }
                    
                    m.addAttribute("unitList", unitService.getUnitsByFilter( search ));
                    m.addAttribute("managerUnitsSearch", search);
                   
                    m.addAttribute("option", op );
                }
                else
                {
                    target=  "redirect:noAdminPermission.html";
                }
            }
                
            return target;
	}    
                  
        @RequestMapping(value = "/showUnit" )
	public String showUnit(Model m, HttpServletRequest request, @RequestParam String c, @RequestParam( required = false ) String back, HttpSession session) 
        {

            back = back != null ? back : "hello.html";

            String message = "Unidade";

            byte[] decodedBytes = Base64.getMimeDecoder().decode(c);
            String cod = new String(decodedBytes);

            Unit unit = unitService.getUnit( cod );

            HashMap ids = new HashMap();

            for( Disponibility disp : unit.getDisponibilities() )
            {
                String token = AppUtilities.generateNewToken();

                disp.setToken( token );

                ids.put( token , disp.getIdDisponibility() );
            }

            session.setAttribute( "ids", ids );

            m.addAttribute("message", message);
            m.addAttribute("back", back);
            m.addAttribute("c", c);

            m.addAttribute("unit", unit );                
                                                                                     
            return "showUnit";
	}
        
        
        

        
    @RequestMapping("/addUnit")
	public String registerUnit( Model m, HttpServletRequest request, HttpSession session ) 
        {
            String target = AppUtilities.redirectToLogin( m, "addUnit", request);
            
            if ( target == "addUnit")
            {
                int userId = (Integer)request.getSession().getAttribute("userId");

                User user = userService.getUser( userId );

                if ( user.isOwner() )
                {  
                
                    Unit unit = null;

                    if ( session.getAttribute("unit") != null )
                    {
                        unit = (Unit)session.getAttribute("unit");
                        
                        session.removeAttribute("unit");
                    }
                    else
                    {
                        unit = new Unit();
                        unit.setDisponibilities( new ArrayList());
                        unit.setCancelOptions( new ArrayList());
                    }
                    
                    if ( session.getAttribute("hotelSearch") != null )
                    {
                        m.addAttribute("hotelSearch", session.getAttribute("hotelSearch") );
                        session.removeAttribute("hotelSearch");
                    }
                    
                    if ( session.getAttribute("binding") != null )
                    {
                        m.addAttribute("org.springframework.validation.BindingResult.unit", session.getAttribute("binding") );
                        session.removeAttribute("binding");
                    }
                
                
                    String message = "Unit";
                    m.addAttribute("unit", unit );   

                    m.addAttribute("message", message); 
                }
                else
                {
                    target = "redirect:noOwnerPermission.html";
                }
                
                
		             
            }
	
            return target; 
	}    
        
   @RequestMapping(value = "/deleteUnit", method = RequestMethod.POST)
public String deleteUnit(Model m, RedirectAttributes rm, HttpServletRequest request, @RequestParam int id)
{
    String target = AppUtilities.redirectToLogin( m, "redirect:unit.html", request);
            
    if ( target == "redirect:unit.html")
    {
        int userId = (Integer)request.getSession().getAttribute("userId");

        User user = userService.getUser( userId );
        //List<Reservation> reservations = reservationService.getReservationsByUnit( id );
        List<Reservation> activeReservations = reservationService.getActiveReservationsByUnit( id );
        
        if ( user != null && ( user.isAdmin() || user.isOwner() ) )
        {
            if ( activeReservations.isEmpty()  )
            {                
                unitService.delete( id );

                rm.addFlashAttribute("modalMessage", "Unidade excluída com sucesso.");
            }        
            else 
            {
                rm.addFlashAttribute("modalMessage", "Não é possível excluir unidade com reservas ativas.");
            }
        }
        else
        {
            rm.addFlashAttribute("modalMessage", "Sem permissão para excluir unidade.");
        }
    }
        
    return target;
}

    @RequestMapping(value = "/editUnit" )
	public String editUnit(Model m, HttpServletRequest request, @RequestParam String token, HttpSession session) 
        {
            String target = AppUtilities.redirectToLogin( m, "redirect:editUnit.html?token="+token, request);
                        
            HashMap ids = (HashMap)session.getAttribute("ids");
            
            if ( ids.containsKey( token ))
            {            
                int id = (Integer)ids.get( token );

                if ( target.contains( "editUnit" ) )
                {
                    int userId = (Integer)request.getSession().getAttribute("userId");

                    User user = userService.getUser( userId );

                    if ( user.isOwner() )
                    {  
                        target = "editUnit";
                        
                        Unit unit = null;

                        if ( session.getAttribute("unit") != null )
                        {
                            unit = (Unit)session.getAttribute("unit");

                            session.removeAttribute("unit");
                        }
                        else
                        {
                            unit = unitService.getUnit( id );

                            m.addAttribute("hotelSearch", unit.getHotel().getName() + " - " + unit.getHotel().getAddress().getCity() + " - " + unit.getHotel().getAddress().getState() + " - " + unit.getHotel().getAddress().getCountry() );
                        }

                        if ( session.getAttribute("hotelSearch") != null )
                        {
                            m.addAttribute("hotelSearch", session.getAttribute("hotelSearch") );
                            session.removeAttribute("hotelSearch");
                        }

                        if ( session.getAttribute("binding") != null )
                        {
                            m.addAttribute("org.springframework.validation.BindingResult.unit", session.getAttribute("binding") );
                            session.removeAttribute("binding");
                        }

                        unit.setToken( token );



                        m.addAttribute("unit", unit );
                    }
                    else
                    {
                        target = "redirect:noOwnerPermission.html";
                    }
                    
                }
            }
            else
            {
                m.addAttribute("message", "Unidade não encontrada. Por favor atualize a página.");
            }
                                                                                                          
            return target;
	}
        
        
    @RequestMapping(value = "/processUnit", method = RequestMethod.POST)
    public String processRegister(@ModelAttribute("unit") @Validated Unit unit, BindingResult result, Model m, HttpServletRequest request, HttpSession session) 
    {
                                    
        if ( result.hasErrors() )
        {                 
            session.setAttribute("unit",unit);
            session.setAttribute("binding",result);            
                                    
            if ( unit.getIdHotel() > 0 )
            {
                Hotel hotel = hotelService.getHotel( unit.getIdHotel() );

                if ( hotel != null )
                {
                    String hotelSearch = hotel.getName() + " - " + hotel.getAddress().getCity()+ " - " + hotel.getAddress().getState()+ " - " + hotel.getAddress().getCountry();
                    session.setAttribute("hotelSearch",hotelSearch);
                }
            }
            
                            
            return "redirect:addUnit.html";
        }
        else
        {
        
            String target = AppUtilities.redirectToLogin( m, "redirect:unit.html", request);
            
            if ( target == "redirect:unit.html")
            {        
                int userId = (Integer)request.getSession().getAttribute("userId");
                unit.setIdUser(userId);

                String tempFolder = System.getProperty("user.home").concat( File.separator ).concat("images").concat( File.separator ).concat("temp").concat( File.separator ).concat("units");
                
                File baseFolder = new File(System.getProperty("user.home").concat( File.separator ).concat("images").concat( File.separator ).concat("base").concat( File.separator ).concat("units"));

                if ( unit.getMedias() != null )
                {
                    for ( Media media : unit.getMedias() )
                    {
                        
                        File image = new File( tempFolder, media.getName() );

                        if ( image.exists() )
                        {
                            File file = new File(baseFolder.getAbsolutePath(), image.getName());
                            file.getParentFile().mkdirs();

                            image.renameTo( file );
                            
                        }
                       
                    }
                }
                
//                if ( unit.getIdCancelOption() != -1 )
//                {
//                    unit.setCancelDays( 0 );
//                    unit.setCancelInfo( "" );
//                }

                unitService.register( unit );
                                            
                User user = userService.getUser( unit.getIdUser() );
            
                emailService.sendMessage( user.getEmail(), "Validação da Unidade de Timesharing", 
               "A fim de manter a integridade das informações cadastradas por nossos proprietários associados, pedimos para que o mesmo nos enviei uma cópia do contrato de sua unidade no hotel/resort para validação. \n" +
               "É uma segurança que precisamos oferecer a nossos usuários.\n" +
               "Obrigado pela compreensão.\n" +
               "\n" +
               "Para isto, vá em “Minhas Unidades”  e selecione a aba Validação para fazer o upload (carregamento) de sua cópia de contrato.\n\n" +
               "Acesse sua unidade: http://tw4r.com/unit.html" );  
                
                session.setAttribute("modalMessage","Unidade adicionada com sucesso!");  
                                                              
            }

            return target;
        }
    }
    
    @RequestMapping(value = "/updateUnit", method = RequestMethod.POST)
    public String updateUnit(@ModelAttribute("unit") @Validated Unit unit, BindingResult result, Model m, HttpServletRequest request, HttpSession session) {
           
        if ( result.hasErrors() )
        {            
            session.setAttribute("unit",unit);
            session.setAttribute("binding",result);
            
            if ( unit.getIdHotel() > 0 )
            {
                Hotel hotel = hotelService.getHotel( unit.getIdHotel() );

                if ( hotel != null )
                {
                    String hotelSearch = hotel.getName() + " - " + hotel.getAddress().getCity()+ " - " + hotel.getAddress().getState()+ " - " + hotel.getAddress().getCountry();
                    session.setAttribute("hotelSearch",hotelSearch);
                }
            }
           
            return "redirect:editUnit.html?token="+unit.getToken();
        }
        else
        {    
            
            String target = AppUtilities.redirectToLogin( m, "redirect:unit.html", request);
            
            if ( target == "redirect:unit.html")
            {                        

                String tempFolder = System.getProperty("user.home").concat( File.separator ).concat("images").concat( File.separator ).concat("temp").concat( File.separator ).concat("units");

                File baseFolder = new File(System.getProperty("user.home").concat( File.separator ).concat("images").concat( File.separator ).concat("base").concat( File.separator ).concat("units"));

                if ( unit.getMedias() != null )
                {
                    for ( Media media : unit.getMedias() )
                    {
                        if ( media.getIdMedia() == 0 )
                        {
                            File image = new File( tempFolder, media.getName() );

                            if ( image.exists() )
                            {
                                File file = new File(baseFolder.getAbsolutePath(), image.getName());
                                file.getParentFile().mkdirs();

                                image.renameTo( file );                               
                            }
                        }
                    }
                }
                
//                if ( unit.getIdCancelOption() != -1 )
//                {
//                    unit.setCancelDays( 0 );
//                    unit.setCancelInfo( "" );
//                }

                unitService.update( unit );

                session.setAttribute("modalMessage","Unidade atualizada com sucesso!");  
            }
            return target;
        }
        
        
    }
    

    @RequestMapping(value = "/searchOffers", method = RequestMethod.POST)
    public String searchOffers(@ModelAttribute("offers") UnitsSearch unitsSearch, BindingResult result, Model m, HttpServletRequest request, HttpSession session) 
    {
        session.setAttribute("unitsSearch",unitsSearch); 
           
//        m.addAttribute("dispList", disponibilityService.getDisponibilitiesByFilter( unitsSearch ) );
//
//        m.addAttribute("offers",unitsSearch);
//         
        
       return "redirect:offers.html";
    }
    
    @RequestMapping(value = "/searchManagerUnits", method = RequestMethod.POST)
    public String searchOffers(@ModelAttribute("managerUnitsSearch") ManagerUnitsSearch unitsSearch, BindingResult result, Model m, HttpServletRequest request, HttpSession session) 
    {
         
        session.setAttribute("managerUnitsSearch",unitsSearch);
        
       return "redirect:managerUnit.html";
    }
    
       
    @RequestMapping(value = "/getOffers", method = RequestMethod.POST, headers="Accept=*/*", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
	public @ResponseBody String getOffers(@RequestParam String tagName) throws JsonProcessingException
        {
            ObjectMapper m = new ObjectMapper();
            
            List<SearchResultItem> items = unitService.search( tagName );
            
            return m.writeValueAsString( items  );
            //return unitService.search( tagName );
	}
      
        
    @RequestMapping(value = "/uploadUnitMedia", method = RequestMethod.POST,headers="Accept=*/*", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String uploadUnitMedia(HttpServletRequest servletRequest, @RequestParam("multipartFile") MultipartFile multipartFile ) throws JsonProcessingException
    {
        ObjectMapper m = new ObjectMapper();
         
        Media media = new Media();
 
        try {
            String fileName = UUID.randomUUID().toString().replace("-", "").concat(".png");
//            File folder = new File(context.getRealPath("").concat( File.separator ).concat("images").concat( File.separator ).concat("temp").concat( File.separator ).concat("units"));
            File folder = new File(System.getProperty("user.home").concat( File.separator ).concat("images").concat( File.separator ).concat("temp").concat( File.separator ).concat("units"));
            folder.mkdirs();
            File file = new File(folder.getAbsolutePath(), fileName);
            file.getParentFile().mkdirs();
            multipartFile.transferTo(file);
            media.setName( fileName );
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return m.writeValueAsString( media  );
    }
    
    @RequestMapping(value = "/uploadUnitDocument", method = RequestMethod.POST, headers="Accept=*/*", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String uploadUnitDocument(HttpServletRequest servletRequest, @RequestParam("multipartFile") MultipartFile multipartFile,@RequestParam("idUnit") int idUnit ) throws JsonProcessingException 
    {
        ObjectMapper m = new ObjectMapper();
        
        Media media = new Media();
        
        media.setIdSource( idUnit );
        media.setType( Media.TYPE_UNIT_DOCUMENT );
        media.setAlias( multipartFile.getOriginalFilename() );
        media.setStatus( Media.STATUS_PENDING_APPROVAL);
        String extension = FilenameUtils.getExtension( media.getAlias() );
        try {
            String fileName = UUID.randomUUID().toString().replace("-", "").concat( extension != null && !extension.isEmpty() ? "." + extension : "" );
            //File folder = new File(context.getRealPath("").concat( File.separator ).concat("documents").concat( File.separator ).concat("base").concat( File.separator ).concat("units"));
            File folder = new File(System.getProperty("user.home").concat( File.separator ).concat("documents").concat( File.separator ).concat("base").concat( File.separator ).concat("units"));
            folder.mkdirs();
            File file = new File(folder.getAbsolutePath(), fileName);
            file.getParentFile().mkdirs();
            multipartFile.transferTo(file);
            media.setName( fileName );
            
            mediaService.deleteAll( Media.TYPE_UNIT_DOCUMENT, idUnit );
            
            mediaService.register( media );
            
            Unit unit = unitService.getUnit( idUnit );
            
            String cod = Base64.getEncoder().encodeToString(unit.getCod().getBytes());
            
            emailService.sendMessage( "contato@tw4r.com", "Contrato da unidade emitido pelo proprietário", 
                "Contrato da unidade foi emitido pelo proprietário, por favor acesse a aplicação e valide a unidade \n" + 
                "Código da unidade: " + unit.getCod() + "\n\n" +
                "Acesse a unidade pelo gerenciador: http://tw4r.com/managerUnit.html?c=" + cod );      
                
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return m.writeValueAsString( media  );
    }
    

    
    @RequestMapping(value = "/removeDocument", method = RequestMethod.POST,  headers="Accept=*/*", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String removeDocument(HttpServletRequest servletRequest, @RequestParam("idMedia") int idMedia ) throws JsonProcessingException 
    {
        ObjectMapper m = new ObjectMapper();
        
        Media media = mediaService.getMedia( idMedia );
                
        mediaService.delete( media.getIdMedia() );
        
        return m.writeValueAsString( media  );
    }
    
    @RequestMapping(value = "/approveDocument", method = RequestMethod.POST, headers="Accept=*/*", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String approveReservation(HttpServletRequest servletRequest, @RequestParam("idMedia") int idMedia ) throws JsonProcessingException 
    {
        ObjectMapper m = new ObjectMapper();
        
        Media document = mediaService.getMedia( idMedia );
        
        document.setStatus( Media.STATUS_APPROVED );
        mediaService.update(document);
        
        return m.writeValueAsString( document  );
    }
    
    @RequestMapping(value = "/reproveDocument", method = RequestMethod.POST, headers="Accept=*/*", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String reproveDocument(HttpServletRequest servletRequest, @RequestParam("idMedia") int idMedia, @RequestParam("comment") String comment ) throws JsonProcessingException 
    {
        ObjectMapper m = new ObjectMapper();
        
        Media document = mediaService.getMedia( idMedia );
        
        document.setStatus( Media.STATUS_REPROVED );
        document.setComment( comment );
        mediaService.update(document);
        
        return m.writeValueAsString( document  );
    }
    
    @RequestMapping(value = "/approveUnit", method = RequestMethod.POST )
    public String approveUnit(HttpServletRequest servletRequest, @RequestParam("idUnit") int idUnit ) 
    {
        Unit unit = unitService.getUnit( idUnit );
                
        unit.setValidationStatus( Unit.STATUS_VALIDATED);

        unitService.update( unit );
        
        if ( !unit.getDocuments().isEmpty() ) 
        {
            Media media = unit.getDocuments().get(0);
            media.setStatus( Media.STATUS_APPROVED );            
            mediaService.update( media );
            
                        
             User user = userService.getUser( unit.getIdUser() );
            
             emailService.sendMessage( user.getEmail(), "Unidade aprovada", 
             "Caro proprietário, sua unidade foi validada e aprovada por nossa equipe.\n" +
            "A partir de agora, sua unidade já está disponível para consulta por nossos usuários e clientes.\n\n" +
            "Acesse sua unidade: http://tw4r.com/unit.html \n\n" +
            "É muito importante que você divulgue para sua rede de relacionamento sobre a oportunidade de utilização da sua unidade.\n" +
            "Compartilhe em suas redes sociais a página da Top Weeks, isso ajudará que mais pessoas conheçam essa oportunidade.");
        }
        
        
        return "redirect:managerUnit.html";
    }
    
    @RequestMapping(value = "/reproveUnit", method = RequestMethod.POST )
    public String reproveUnit(HttpServletRequest servletRequest, @RequestParam("idUnit") int idUnit, @RequestParam("comment") String comment ) 
    {
        Unit unit = unitService.getUnit( idUnit );
                                
        if ( !unit.getDocuments().isEmpty() ) 
        {
            Media media = unit.getDocuments().get(0);
            media.setStatus( Media.STATUS_REPROVED );            
            media.setComment( comment );            
            mediaService.update( media );
        }
        
        
        return "redirect:managerUnit.html";
    }
    
    
   @ModelAttribute("generalAttributes")
   public List<Attribute> getGeneralAttributes() {
       
       List<Attribute> attributes = attributeService.getAttributes();       
     
      return attributes;
   }
   
   @ModelAttribute("cancelOptions")
   public List<Option> getCancelOptions() {
       
       List<Option> options = optionService.getOptions( Option.OPTION_CANCEL );       
     
      return options;
   }
   
       
}
  