package com.javatpoint.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javatpoint.model.GeoName;
import com.javatpoint.model.Hotel;
import com.javatpoint.model.HotelRequest;
import com.javatpoint.model.HotelsSearch;
import com.javatpoint.model.Media;
import com.javatpoint.model.SearchResultItem;
import com.javatpoint.model.User;
import com.javatpoint.service.EmailService;
import com.javatpoint.service.GeoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.javatpoint.service.HotelService;
import com.javatpoint.service.UnitService;
import com.javatpoint.service.UserService;
import com.javatpoint.util.AppUtilities;
import com.javatpoint.validator.HotelValidator;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@SessionAttributes
public class HotelController {

  @Autowired
  ServletContext context;   
    
  @Autowired
  HotelService hotelService;
  
  @Autowired
  UserService userService;
  
  @Autowired
  UnitService unitService;
  
  @Autowired
  GeoService geoService;
  
  @Autowired  
  private EmailService emailService;
  
  @Autowired
  HotelValidator hotelValidator;
              
  @InitBinder("hotel")
   protected void initHotelBinder(WebDataBinder binder) {
      binder.addValidators(hotelValidator);
   }
  

        
    @RequestMapping("/hotel")
	public String listHotel( Model m, HttpServletRequest request, HttpSession session) {
                        
            int userId = (Integer)request.getSession().getAttribute("userId");

            User user = userService.getUser( userId );

            if ( user.isAdmin())
            {  
            
                HotelsSearch filter = null; //new HotelsSearch();
                
                if ( session.getAttribute("hotelSearch") != null )
                {
                    filter = (HotelsSearch)session.getAttribute("hotelSearch");

                    session.removeAttribute("hotelSearch");
                }
                else
                {
                    filter = new HotelsSearch();
                }
                                                
                List<Hotel> hotels = hotelService.getHotelsByFilter( filter );
                
                HashMap ids = new HashMap();
                
                for( Hotel hotel : hotels )
                {
                    String token = AppUtilities.generateNewToken();
                    
                    hotel.setToken( token );
                    
                    ids.put( token , hotel.getIdHotel() );
                }
                
                session.setAttribute( "ids", ids );
                
                if ( session.getAttribute("modalMessage") != null )
                {
                    m.addAttribute("modalMessage", session.getAttribute("modalMessage") );
                    session.removeAttribute("modalMessage");
                }
                                
		
                m.addAttribute("hotels", filter);
                
                m.addAttribute("hotelList", hotels );
		            
		return "hotel"; 
            }
            else
            {
                return "redirect:noAdminPermission.html";
            }
	}    
  
    @RequestMapping("/addHotel")
	public String registerHotel( Model m, HttpServletRequest request, HttpSession session ) {
            
            String target = AppUtilities.redirectToLogin( m, "addHotel", request);
            
            if ( target == "addHotel")
            {
                int userId = (Integer)request.getSession().getAttribute("userId");

                User user = userService.getUser( userId );

                if ( user.isAdmin())
                {  
                    Hotel hotel = null;

                    if ( session.getAttribute("hotel") != null )
                    {
                        hotel = (Hotel)session.getAttribute("hotel");

                        session.removeAttribute("hotel");
                    }
                    else
                    {                                
                        hotel = new Hotel(); 
                    }

                    if ( session.getAttribute("binding") != null )
                    {
                        m.addAttribute("org.springframework.validation.BindingResult.hotel", session.getAttribute("binding") );
                        session.removeAttribute("binding");
                    }


                    ArrayList<GeoName> countries = new ArrayList<GeoName>();
                    ArrayList<GeoName> states = new ArrayList<GeoName>();
                    ArrayList<GeoName> cities = new ArrayList<GeoName>();

                    //continents.add( new GeoName(-1, "selecione um Continente"));
                    countries.add( new GeoName(-1, "selecione um País"));
                    states.add( new GeoName(-1, "selecione um Estado"));
                    cities.add( new GeoName(-1, "selecione uma Cidade"));

                    try
                    {                    
                        //continents.addAll( geoService.getNames( 6295630 ) );
                        countries.addAll( geoService.getCountries() );
                        
                        if ( hotel.getAddress() != null )
                        {                        
                            states.addAll( geoService.getStates( hotel.getAddress().getIdCountry() ) ); 
                            cities.addAll( geoService.getCities( hotel.getAddress().getIdState() ) );
                        }
                        
                    }
                    catch( Exception e)
                    {}

                    m.addAttribute("hotel", hotel );   

                    m.addAttribute("countryList", countries);
                    m.addAttribute("stateList", states);
                    m.addAttribute("cityList", cities);
                }
                else
                {
                    target=  "redirect:noAdminPermission.html";
                }
                

            }
            return target; 
	}    
        
   @RequestMapping(value = "/deleteHotel", method = RequestMethod.POST)
public String deleteHotel(Model m, RedirectAttributes rm, HttpServletRequest request, @RequestParam int id){
    
    String target = AppUtilities.redirectToLogin( m, "redirect:hotel.html", request);
    
    if ( target == "redirect:hotel.html")
    {
        int userId = (Integer)request.getSession().getAttribute("userId");

        User user = userService.getUser( userId );

        if ( user.isAdmin())
        {    
            if ( unitService.getUnitsByHotel(id).isEmpty() )
            {                
                hotelService.delete( id );

                rm.addFlashAttribute("modalMessage", "Hotel excluído com sucesso. ");
            }
            else
            {
                rm.addFlashAttribute("modalMessage", "Hotel não pode ser excluído pois existem unidades vinculadas a ele. ");    
            }
        }
        else
        {
            rm.addFlashAttribute("modalMessage", "Sem permissão para excluir hotel. ");
        }
        
    }
    
    return target;
    
    
}

    @RequestMapping(value = "/editHotel" )
	public String editHotel(Model m, HttpServletRequest request, @RequestParam String token, HttpSession session) {
            
            String target = AppUtilities.redirectToLogin( m, "redirect:editHotel.html?token="+token, request);
            
            HashMap ids = (HashMap)session.getAttribute("ids");
            
            if ( ids.containsKey( token ))
            {
                int id = (Integer)ids.get( token );
                
                if ( target.contains( "editHotel") )
                {
                    int userId = (Integer)request.getSession().getAttribute("userId");

                    User user = userService.getUser( userId );

                    if ( user.isAdmin())
                    {  

                        target = "editHotel";

                        Hotel hotel = null;

                        if ( session.getAttribute("hotel") != null )
                        {
                            hotel = (Hotel)session.getAttribute("hotel");

                            session.removeAttribute("hotel");
                        }
                        else
                        {
                            hotel = hotelService.getHotel( id );
                        }

                        if ( session.getAttribute("binding") != null )
                        {
                            m.addAttribute("org.springframework.validation.BindingResult.hotel", session.getAttribute("binding") );
                            session.removeAttribute("binding");
                        }

                        hotel.setToken( token );


                        ArrayList<GeoName> countries = new ArrayList<GeoName>();
                        ArrayList<GeoName> states = new ArrayList<GeoName>();
                        ArrayList<GeoName> cities = new ArrayList<GeoName>();

                        countries.add( new GeoName(-1, "selecione um País"));
                        states.add( new GeoName(-1, "selecione um Estado"));
                        cities.add( new GeoName(-1, "selecione uma Cidade"));

                        try
                        {                        
                            countries.addAll( geoService.getCountries() );
                            states.addAll( geoService.getStates( hotel.getAddress().getIdCountry() ) ); 
                            cities.addAll( geoService.getCities( hotel.getAddress().getIdState() ) );

                        }
                        catch( Exception e)
                        {}


                        m.addAttribute("countryList", countries);
                        m.addAttribute("stateList", states);
                        m.addAttribute("cityList", cities);

                        m.addAttribute("hotel", hotel );
                    }
                    else
                    {
                        target=  "redirect:noAdminPermission.html";
                    }
                }
            }            
            else
            {
                m.addAttribute("message", "Hotel não encontrado. Por favor atualize a página.");
            }
            
                                                                                    
            return target;
	}
        
        
    @RequestMapping(value = "/processHotel", method = RequestMethod.POST)
    public String processRegister(@ModelAttribute("hotel") @Validated Hotel hotel, BindingResult result, Model m, HttpServletRequest request, HttpSession session) 
    {
       
        if ( result.hasErrors() )
        {

            session.setAttribute("hotel", hotel);
            session.setAttribute("binding",result);  
            
            
            return "redirect:addHotel.html";
        }
        else
        {
            String target = AppUtilities.redirectToLogin( m, "redirect:hotel.html", request);

            if ( target == "redirect:hotel.html")
            {                                
                String tempFolder = System.getProperty("user.home").concat( File.separator ).concat("images").concat( File.separator ).concat("temp").concat( File.separator ).concat("hotels");
                
                File baseFolder = new File(System.getProperty("user.home").concat( File.separator ).concat("images").concat( File.separator ).concat("base").concat( File.separator ).concat("hotels"));

                if ( hotel.getMedias() != null )
                {
                    for ( Media media : hotel.getMedias() )
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

                hotelService.register( hotel );
                
                session.setAttribute("modalMessage","Hotel adicionado com sucesso!");
            }

            return target;
        }        
    }
    
    @RequestMapping(value = "/updateHotel", method = RequestMethod.POST)
    public String updateHotel(@ModelAttribute("hotel") @Validated Hotel hotel, BindingResult result, Model m, HttpServletRequest request, HttpSession session) 
    {
        
        if ( result.hasErrors() )
        {            
            session.setAttribute("hotel",hotel);
            session.setAttribute("binding",result);
            
            return "redirect:editHotel.html?token="+hotel.getToken();
        }        
        else
        {
            String target = AppUtilities.redirectToLogin( m, "redirect:hotel.html", request);

            if ( target == "redirect:hotel.html")
            {                                      
            
                String tempFolder = System.getProperty("user.home").concat( File.separator ).concat("images").concat( File.separator ).concat("temp").concat( File.separator ).concat("hotels");
                
                File baseFolder = new File(System.getProperty("user.home").concat( File.separator ).concat("images").concat( File.separator ).concat("base").concat( File.separator ).concat("hotels"));

                if ( hotel.getMedias() != null )
                {
                    for ( Media media : hotel.getMedias() )
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

                hotelService.update( hotel );
                session.setAttribute("modalMessage","Hotel atualizado com sucesso!");
            }

            return target;
        }
    }
    
    @RequestMapping(value = "/processRequestHotel", method = RequestMethod.POST)
    public ModelAndView processRequestHotel(@ModelAttribute("hotelRequest") HotelRequest  hotelRequest, BindingResult result, HttpServletRequest request) {
            //write the code here to add contact
            
            ModelAndView mav = new ModelAndView("forward:requestHotel.html");
            
            int userId = (Integer)request.getSession().getAttribute("userId");
            
            User user = userService.getUser( userId );
            
            emailService.sendMessage( "contato@tw4r.com", "Requisição para cadastro de Hotel", "Usuário: "+ user.getName() + "\nEmail: "+ user.getEmail()+ "\nInformação do Hotel:" + hotelRequest.getInfo());
            mav.addObject("success", "Requisição enviada com sucesso!");
            
            
        return mav;
    }
    
    @RequestMapping(value = "/searchHotels", method = RequestMethod.POST)
    public String searchOffers(@ModelAttribute("hotels") HotelsSearch hotelSearch, BindingResult result, Model m, HttpServletRequest request, HttpSession session) 
    {
          //session.setAttribute("hotelList",hotelService.getHotelsByFilter( hotelSearch ));
          session.setAttribute("hotelSearch",hotelSearch);
          
//          m.addAttribute("hotelList", hotelService.getHotelsByFilter( hotelSearch ) );
//          
//          m.addAttribute("hotels",hotelSearch);
         
         
       return "redirect:hotel.html";
    }
    
    
    @RequestMapping(value = "/getHotels", method = RequestMethod.POST, headers="Accept=*/*", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String getHotels(@RequestParam String tagName) throws JsonProcessingException
    {
        ObjectMapper m = new ObjectMapper();
        
        return m.writeValueAsString( hotelService.search( tagName )  );
        

    }
        
    @RequestMapping(value = "/getGroupedHotels", method = RequestMethod.POST, headers="Accept=*/*", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String getGroupedHotels(@RequestParam String tagName) throws JsonProcessingException
    {
        ObjectMapper m = new ObjectMapper();
        return m.writeValueAsString( hotelService.searchGrouped( tagName ) );

    }    
           

    
    @RequestMapping(value = "/getCountries", method = RequestMethod.POST, headers="Accept=*/*", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String getCountries(@RequestParam(value = "continent") int continent) throws JsonProcessingException
    {
        ObjectMapper m = new ObjectMapper();
        ArrayList<GeoName> countries = new ArrayList<GeoName>(); 
        countries.add( new GeoName(-1, "selecione um País"));

        try
        {
            countries.addAll(geoService.getNames( continent ));
        }
        catch( Exception e)
        {}

        return m.writeValueAsString( countries );


    }
    
    
    @RequestMapping(value = "/getStates", method = RequestMethod.POST, headers="Accept=*/*", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String getStates(@RequestParam(value = "country") int country) throws JsonProcessingException
    {
        ObjectMapper m = new ObjectMapper();
        ArrayList<GeoName> states = new ArrayList<GeoName>(); 
        states.add( new GeoName(-1, "selecione um Estado"));

        try
        {

            states.addAll(geoService.getStates(country) );
        }
        catch( Exception e)
        {}

        return m.writeValueAsString( states );

    }
    
    @RequestMapping(value = "/getCities", method = RequestMethod.POST, headers="Accept=*/*", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String getCities(@RequestParam(value = "state") int state) throws JsonProcessingException
    {
        ObjectMapper m = new ObjectMapper();
        ArrayList<GeoName> cities = new ArrayList<GeoName>(); 
        cities.add( new GeoName(-1, "selecione uma Cidade"));

        try
        {

            cities.addAll(geoService.getCities( state ));
        }
        catch( Exception e)
        {}

        return m.writeValueAsString( cities );

    }

    @RequestMapping(value = "/uploadHotelMedia", method = RequestMethod.POST, headers="Accept=*/*", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String uploadUnitMedia(HttpServletRequest servletRequest, @RequestParam("multipartFile") MultipartFile multipartFile ) throws JsonProcessingException
    {
        ObjectMapper m = new ObjectMapper();
        Media media = new Media();

        try {
            String fileName = UUID.randomUUID().toString().replace("-", "").concat(".png");

            File folder = new File(System.getProperty("user.home").concat( File.separator ).concat("images").concat( File.separator ).concat("temp").concat( File.separator ).concat("hotels"));
            folder.mkdirs();
            File file = new File(folder.getAbsolutePath(), fileName);
            file.getParentFile().mkdirs();
            multipartFile.transferTo(file);
            media.setName( fileName );
        } catch (IOException e) {
            e.printStackTrace();
        }

        return m.writeValueAsString( media );
    }



}
  