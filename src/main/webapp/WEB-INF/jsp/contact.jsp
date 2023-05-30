<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
<title>Contato</title>
<style>
           

html,
body {
  height: 100%;
}

body {
  display: flex;
  flex-direction: column;
}


</style>
        
        
</head>

    
<div class="center-box box-column" style="padding-top: 20px;">         
    <% if(  request.getAttribute("success")!=null ) { %>
    <table>       
        <tr>
            <td>
                <div class="alert alert-success">
                    <strong>Mensagem enviada com sucesso!</strong>                    
                </div>                       
            </td>
        </tr>   
    </table>        
    <%} else { %> 
    <div class="contact-box">
        <div>
            <p class="contact-title"> Contato </p>
        </div>
        
        <form:form method="post" id="contactForm" action="processContact.html" modelAttribute="contact">
             <div class="form-group">
                   <p>Utilize esse formulário para nos enviar qualquer dúvida a respeito da TW4R. Ficaremos felizes em lhe responder.</p>
             </div>  
             <div class="form-group">
                    <form:label path="name">Nome</form:label>
                    <form:input path="name" /><form:errors path="name" cssClass="error"/>
             </div>  
             <div class="form-group">
                    <form:label path="email">Email</form:label>
                    <form:input path="email" /><form:errors path="email" cssClass="error"/>
             </div>                       
             <div class="form-group">
                    <form:label path="phone">Telefone</form:label>
                    <form:input path="phone" />
             </div>                       
             <div class="form-group">
                    <form:label path="message">Mensagem</form:label>
                    <form:textarea path="message" rows="10" cols="30" /><form:errors path="message" cssClass="error"/>
             </div>  
             <div class="buttonGroup">
                  <center>
                      <button type="button" class="submit-button" onClick="submitMainForm();">Enviar</button>
                  </center>
             </div>    

             <% if(  request.getAttribute("error") != null ) { %>
             <div class="form-group">  
                    <div class="error">                       
                        <%=request.getAttribute("error").toString()%>
                    </div>
             </div>                 

                  <%}%>

        </form:form>
    </div>
            
</div>      
<%}%> 
<div class="loader"></div>



<script>
    function submitMainForm()
    {                   
        var spinner = $('.loader');
            
        spinner.show();
        
        document.getElementById("contactForm").submit();
    }
</script>

</html>