<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.javatpoint.model.User" %>


<div id="mySidebar" class="sidebar sidemenu">
  
  <div class="topbar"> 
      <div>
        <img src="resources/img/icon-w-user.svg" width="40" height="40" class="rounded-circle">  
        <%=(request.getSession().getAttribute("username") != null) ? request.getSession().getAttribute("username").toString() : "Visitante"%> 
      </div>
       <i class="fa fa-close close-bt" onclick="closeNav()"></i>
      
  </div>
  
  <div class="container">
  <div class="row">
    <% if(  request.getSession().getAttribute("username")!=null ) { %>  
    <div class="col-6 text-center squareBt">        
        <div class="outsquareBt  loginBt" onclick="location.href = 'account.html'">                      
              <i class="fa fa-user"></i>              
        </div>
        <p> Conta </p>                       
    </div>
    <div class="col-6 text-center squareBt">        
        <div class="outsquareBt  loginBt" onclick="location.href = 'logout.html'">                     
              <i class="fa fa-sign-out"></i>                                      
        </div>
         <p> Sair </p>
    </div>
    <%} else { %>
    <div class="col-6 text-center squareBt">        
        <div class="outsquareBt  loginBt" onclick="location.href = 'login.html'">                      
              <i class="fa fa-sign-in"></i>                                       
        </div>
         <p> Entrar </p>
    </div>
    <div class="col-6 text-center squareBt">        
        <div class="outsquareBt  loginBt" onclick="location.href = 'register.html'">                      
              <i class="fa fa fa-user-plus"></i>                                      
        </div>
        <p> Registrar </p>
    </div>
    <%}%>  
  </div>
</div>    
  
<hr class="solid">   
  
<div class="container">
  <div class="row">
     
    <div class="col-6 text-center squareBt">        
        <div class="outsquareBt userBt" onclick="location.href = 'hello.html'">                      
              <i class="fa fa-home"></i>                                    
        </div>
         <p> Home </p>
    </div>
    <div class="col-6 text-center squareBt">        
        <div class="outsquareBt userBt" onclick="location.href = 'offers.html'">                      
              <i class="fa fa-search"></i>                                      
        </div>
        <p> Busca </p>
    </div>
    
    <div class="col-6 text-center squareBt">        
        <div class="outsquareBt userBt" onclick="location.href = 'reservation.html'">                      
              <i class="fa fa-calendar"></i>                                    
        </div>
         <p> Minhas Reservas </p>
    </div>
    <div class="col-6 text-center squareBt">        
        <div class="outsquareBt userBt" onclick="location.href = 'unit.html'">                      
              <i class="fa fa fa-suitcase"></i>                                       
        </div>
        <p> Minhas Unidades </p>
    </div>
   
  </div>
</div>      

<% if(  request.getSession().getAttribute("admin")!=null ) { %>
<hr class="solid">

<div class="container">
  <div class="row">
     
    <div class="col-6 text-center squareBt">        
        <div class="outsquareBt adminBt" onclick="location.href = 'hotel.html'">                      
              <i class="fa fa-building-o"></i>                                    
        </div>
         <p> Hotéis </p>
    </div>
    <div class="col-6 text-center squareBt">        
        <div class="outsquareBt adminBt" onclick="location.href = 'managerUnit.html'">                      
              <i class="fa fa-bed"></i>                                      
        </div>
        <p> Unidades </p>
    </div>
    
    <div class="col-6 text-center squareBt">        
        <div class="outsquareBt adminBt" onclick="location.href = 'managerReservation.html'">                      
              <i class="fa fa-calendar-check-o"></i>                                    
        </div>
         <p> Reservas </p>
    </div>
    <div class="col-6 text-center squareBt">        
        <div class="outsquareBt adminBt" onclick="location.href = 'managerUser.html'">                      
              <i class="fa fa-group"></i>                                       
        </div>
        <p> Usuários </p>
    </div>
    <div class="col-6 text-center squareBt">        
        <div class="outsquareBt adminBt" onclick="location.href = 'managerFaq.html'">                      
              <i class="fa fa-question"></i>                                       
        </div>
        <p> Dúvidas </p>
    </div>
   
  </div>
</div>   

<%}%>
  
      
  
  
</div>



<div class="center-box top-header">
    <div class="top-header-logo">
        <a class="navbar-logo" href="hello.html">
        <img src="resources/img/logo.png" width="50px" alt="logo">
        <p class="brandLabel"> TopWeeksForRent </p>
        </a>
    </div>
    <div class="top-header-links">
        <ul class="navbar-nav mx-auto" id="appLinks">
             
            <li class="nav-item">
                <a class="nav-link" href="hello.html"> Home </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="offers.html"></i> Busca </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="reservation.html"></i> Minhas Reservas </a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="unit.html"></i> Minhas Unidades </a>
            </li>            
            <% if(  request.getSession().getAttribute("admin")!=null ) { %>            
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">Gerenciador</a>
                <div class="dropdown-menu">
                  <a class="dropdown-item" href="hotel.html">Hotéis</a>
                  <a class="dropdown-item" href="managerUnit.html">Unidades</a>                  
                  <a class="dropdown-item" href="managerReservation.html">Reservas</a>                  
                  <a class="dropdown-item" href="managerUser.html">Usuários</a>                  
                  <a class="dropdown-item" href="managerFaq.html">Dúvidas</a>                  
                </div>
            </li>
             <%}%>
             
        </ul>  
    </div>
    <div class="top-header-login">
        <ul class="navbar-nav">
        
            <% if(  request.getSession().getAttribute("username")!=null ) { %>
            <li class="nav-user-item">
                <a class="nav-link" href="account.html">Conta</a>
            </li>    
            <li class="nav-user-item filled">
                <a class="nav-link" href="logout.html">Sair</a>
            </li>
            <%} else { %>   
            <li class="nav-user-item">
                <a class="nav-link" href="login.html">Entrar</a>
            </li>
            <li class="nav-user-item filled">
                <a class="nav-link" href="register.html">Registrar</a>          
            </li>
            <%}%>    
        
            <li class="nav-item">
            <a class="nav-link" href="#" id="navbarDropdownMenuLink" style="text-align:center;" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
              <img src="resources/img/icon-w-user.svg" width="40" height="40" class="rounded-circle"> 
              <%=(request.getSession().getAttribute("username") != null) ? request.getSession().getAttribute("username").toString() : "Visitante"%>
            </a>

            </li>   
        </ul>
    </div>
    <div class="top-header-menu">
        <i class="fa fa-bars openSideMenuBtn" onclick="openNav()"></i>
    </div>    
    
</div>      
            
            <script>
      /*
      $( '.top-header-links .navbar-nav a' ).on( 'click', function () {
          alert('teste');
	$( '.top-header-links .navbar-nav' ).find( 'li.active' ).removeClass( 'active' );
	$( this ).parent( 'li' ).addClass( 'active' );
});*/
  </script>   


        