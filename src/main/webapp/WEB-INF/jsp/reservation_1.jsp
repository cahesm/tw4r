<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.util.*" %>

<html>
    <head>
       
        
<style>
    
html,
body {
  height: 100%;
}

body {
  display: flex;
  flex-direction: column;
}

.hd_center_container {
  flex-grow: 1;
}    
    

.tabs-body { padding: 0.5rem; }

.media{display: flex; padding-bottom: 10px;}



.form-control{ margin: 0px !important; border-radius: 0.25rem !important;; border-color: #6c757d !important;; }

          



.documents-heading {
    background-color: #dee2e6;
    border: 1px;   
    text-align: left;
    font-size: 14px;
    padding: 5px;
}


.documents-title {
    margin-bottom: 0px !important;
    
}





.emptyList { text-align: center; padding:20px !important; display: block; }

.status{ padding: 10px; font-size: 13px;}

.text-danger{
    margin-bottom: 0px !important;
}

.text-success{
    margin-bottom: 0px !important;
}



.font-14{ font-size: 14px;}

.fa-close, .fa-plus{ cursor: pointer !important;}

</style>

<script>
        
        let onFileSelected = function (index)
        {
            var spinner = $('.loader');
            
            $(".fa fa-plus").prop('disabled',true);
                        
            var file = document.getElementById('fileInput'+index).files[0];
            var idReservation = document.getElementById('idReservation'+index).value;
            
            var fd = new FormData();
            fd.append("multipartFile", file);
            fd.append("idReservation", idReservation);
            
            spinner.show();
            
            //var dt = {multipartFile: file};
            
            // Ajax call for file uploaling
            var ajaxReq = $.ajax(
            {
              url : 'uploadReservationPayment.html',
              type : 'POST',
              data: fd,
              dataType: 'json',
              cache : false,
              contentType : false,
              processData : false,
              xhr: function()
              {
                //Get XmlHttpRequest object
                 var xhr = $.ajaxSettings.xhr() ;

                //Set onprogress event handler 
                 xhr.upload.onprogress = function(event)
                 {
                        var perc = Math.round((event.loaded / event.total) * 100);
                        $('#progressBar').text(perc + '%');
                        $('#progressBar').css('width',perc + '%');
                 };
                 return xhr ;
                },
                beforeSend: function( xhr ) 
                {
                        //Reset alert message and progress bar
                        $('#alertMsg').text('');
                        $('#progressBar').text('');
                        $('#progressBar').css('width','0%');
                }
            });

            // Called on success of file upload
            ajaxReq.done(function(media) 
            {
              addPayment( media, index );
              $('#alertMsg').text("Upload realizado com sucesso");
              $('#fileInput').val('');
              $('.fa fa-plus').prop('disabled',false);
              spinner.hide();
            });

            // Called on failure of file upload
            ajaxReq.fail(function(jqXHR) {
              $('#alertMsg').text('('+jqXHR.status+
                        ' - '+jqXHR.statusText+')');
              $('.fa fa-plus').prop('disabled',false);
              spinner.hide();
            });
        }
        
        let addPayment = function ( media, index ) {
            
         //let fields = ['name:text','uplaodDate:date', 'status:text'];                  
         let table = document.getElementById( 'reservationPayments'+index);
         let tbody = table.getElementsByTagName('tbody')[0];
                  
         //let rowIndex = tbody.getElementsByTagName('tr').length;

         let tr = document.createElement('tr');
         
         let td = document.createElement('td');
         
         var content = "<a href=\"javascript:download('${pageContext.request.contextPath}/documents/base/payments/"+media.name+"', '"+media.alias+"');\" > "+media.alias+" </a>";
         
         td.innerHTML = content ;
         
         tr.appendChild(td);
         
         /*fields.forEach((field) => {
            let td = document.createElement('td');
                     
            td.appendChild(input);
            tr.appendChild(td);
        });*/
        
        //let td = document.createElement('td');
        //td.innerHTML = "<i class='fa fa-close' style='font-size:20px; float:right' onclick='removeDisponibility(this)'></i>";
        //tr.appendChild(td);
        
        tbody.appendChild(tr);  
        
        }
        
        let removePayment = function ( el, index ) {
                       
            $( el ).prop('disabled',true);
                                                           
            var idMedia = document.getElementById('payments'+index+".idMedia").value;
            
            var fd = new FormData();            
            fd.append("idMedia", idMedia);
           
            // Ajax call for file uploaling
            var ajaxReq = $.ajax(
            {
              url : 'removePayment.html',
              type : 'POST',
              data: fd,
              dataType: 'json',
              cache : false,
              contentType : false,
              processData : false,
              xhr: function()
              {
                //Get XmlHttpRequest object
                 var xhr = $.ajaxSettings.xhr() ;

                //Set onprogress event handler 
                 xhr.upload.onprogress = function(event)
                 {
                        //var perc = Math.round((event.loaded / event.total) * 100);
                        //$('#progressBar').text(perc + '%');
                        //$('#progressBar').css('width',perc + '%');
                 };
                 return xhr ;
                },
                beforeSend: function( xhr ) 
                {
                        //Reset alert message and progress bar
                        //$('#alertMsg').text('');
                        //$('#progressBar').text('');
                        //$('#progressBar').css('width','0%');
                }
            });

            // Called on success of file upload
            ajaxReq.done(function(media) 
            {                          
                let tr = el.closest("tr");
                let tbody = tr.closest("tbody");                                        
                tbody.removeChild( tr );  
                
                $(el).prop('disabled',false);
            });

            // Called on failure of file upload
            ajaxReq.fail(function(jqXHR) {
              $('#alertMsg').text('('+jqXHR.status+
                        ' - '+jqXHR.statusText+')');
              $(el).prop('disabled',false);
            });
        }
        
        function download(dataurl, filename) {
            
        var a = document.createElement("a");
        a.href = dataurl;
        a.setAttribute("download", filename);
        a.click();
        }
        
</script>

</head>

<body>
<div class="hd_center_container">
    <div class="center" id="hd_dashboard_horzmenu">   
<div class = "panel-group">
            <div class = "panel panel-default">
                <div class = "panel-heading">
                    <h4 class = "panel-title">
                        <a data-toggle = "collapse" href = "#reservationList">Minhas Reservas</a>                         
                    </h4>
                </div>
                <div id = "reservationList" class = "panel-collapse expand">
                    <div class = "panel-body">    
                        <c:choose>
                        <c:when test="${not empty reservationList}">
                        <c:forEach var="reservationList" items="${reservationList}" varStatus="status">	
                        <section class="section-box">
                            <div class="container-fluid">
                                <div class="row" >
                                    <div class="col-md-12 listing-block">
                                        <div class="item-heading  text-center">
                                             
                                             <div class = "item-cod">
                                                 ${reservationList.cod}                 
                                             </div>
                                             <div class = "item-title">
                                                     <a>  ${reservationList.hotel.name} </a>                  
                                             </div>
                                            <div class = "item-buttons">
                                                <i class="fa fa-chevron-down float-right" data-toggle="collapse" data-target="#reservation${status.index}"></i>                                                
                                            </div>                                                                                                                                                                                                                                                                                                                              
                                        </div>     
                                        <div class="item text-center status">
                                            <c:choose> 
                                            <c:when test="${reservationList.status eq 0}">
                                              <p class="text-danger">   
                                             <strong>Aguardando Aprovação do Proprietário</strong>
                                             </p>
                                             </c:when>  
                                             <c:when test="${reservationList.status eq 1}">
                                              <p class="text-danger">   
                                             <strong>Aguardando Pagamento</strong>
                                             </p>
                                             </c:when>
                                             <c:when test="${reservationList.status eq 2 && not empty reservationList.voucher && ( reservationList.voucher.status eq 0 or reservationList.voucher.status eq 1)}">
                                                <p class="text-danger">   
                                               <strong>Aguardando Aprovação de Voucher</strong>
                                               </p>
                                               </c:when>  
                                             <c:when test="${reservationList.status eq 2}">
                                              <p class="text-danger">   
                                             <strong>Aguardando Envio do Voucher</strong>
                                             </p>
                                             </c:when>  
                                             <c:when test="${reservationList.status eq 3}">
                                              <p class="text-success">   
                                             <strong>Concluída</strong>
                                             </p>
                                             </c:when>  
                                             <c:when test="${reservationList.status eq 4}">
                                              <p class="text-danger">   
                                             <strong>Cancelada</strong>
                                             </p>
                                             </c:when>  
                                             <c:when test="${reservationList.status eq 5}">
                                              <p class="text-danger">   
                                             <strong>Reprovado</strong>                                             
                                             </p>
                                             </c:when>  
                                             </c:choose>
                                        </div> 
                                        <div id="reservation${status.index}" class="item panel-collapse collapse">   
                                            
                                            <div class="media">   
                                                
                                                <table>
                                                <tr>
                                                    <td colspan="2" style="padding: 0px">
                                                        <div class="stepProgressBox">
                                                        <div class="stepProgress">
                                                            <div class="circle ${reservationList.status > 0 ? 'done' : 'active' }"> <span class="label">1</span>
                                                              <span class="title">Em aprovação</span>

                                                            </div> <span class="bar ${reservationList.status > 0 ? 'done' : 'active' }"></span>

                                                            <div class="circle ${reservationList.status > 1 ? 'done' : reservationList.status eq 1 ? 'active' : '' }"> <span class="label">2</span>
                                                              <span class="title">Aguardando Pagamento</span>

                                                            </div> <span class="bar ${reservationList.status > 1 ? 'done' : reservationList.status eq 1 ? 'active' : '' }"></span>

                                                            <div class="circle ${reservationList.status > 2 ? 'done' : reservationList.status eq 2 ? 'active' : '' }"> <span class="label">3</span>
                                                              <span class="title">Aguardando Voucher</span>

                                                            </div> <span class="bar ${reservationList.status > 2 ? 'done' : reservationList.status eq 2 ? 'active' : '' }"></span>

                                                            <div class="circle ${reservationList.status > 3 ? 'done' : reservationList.status eq 3 ? 'active' : '' }"> <span class="label">4</span>
                                                              <span class="title">Completada</span>
                                                                                                                      
                                                          </div>
                                                      </div>
                                                    </div>
                                                           
                                                            
                                                    </td>    
                                                </tr>    
                                                <tr>
                                                        <td>Unidade</td>
                                                        <td>
                                                            <a href="showUnit.html?c=${Base64.getEncoder().encodeToString(reservationList.unit.cod.getBytes())}>&back=reservation.html" >                                        
                                                                ${reservationList.unit.cod}    
                                                            </a>
                                                                                                                        
                                                        </td>     
                                                </tr>    
                                                <tr>
                                                        <td>Criação</td>
                                                        <td ><fmt:formatDate value="${reservationList.creation}" pattern="dd/MM/yyyy"/> </td>   
                                                </tr>    
                                                <tr>
                                                        <td>CheckIn</td>
                                                        <td ><fmt:formatDate value="${reservationList.startDate}" pattern="dd/MM/yyyy"/> </td>   
                                                </tr>    
                                                <tr>
                                                        <td>Checkout</td>
                                                        <td > <fmt:formatDate value="${reservationList.endDate}" pattern="dd/MM/yyyy"/></td>   
                                                </tr>    
                                                <tr>
                                                        <td>Valor</td>
                                                        <td> 
                                                            R$<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${reservationList.disponibility.finalPrice}" />
                                                        </td>   
                                                </tr>                                                    
                                                <tr>
                                                        <td>Voucher</td>
                                                        <td>
                                                            
                                                            <c:choose>
                                                            <c:when test="${not empty reservationList.voucher}">
                                                                                        
                                                            <a href="javascript:download('${pageContext.request.contextPath}/documents/base/vouchers/${reservationList.voucher.name}', '${reservationList.voucher.alias}');" > ${reservationList.voucher.alias} </a>
                                            
                                                            </c:when>
                                                            <c:otherwise>
                                                                n/d
                                                            </c:otherwise> 
                                                            </c:choose>
                                                               
                                                        </td>   
                                                </tr>  
                                                <tr>
                                                        <td>Comentário</td>
                                                        <td>
                                                           <c:choose> 
                                                           <c:when test="${not empty reservationList.comment}">
                                                             ${reservationList.comment}
                                                            </c:when>  
                                                            <c:otherwise>
                                                             n/d                                                            
                                                            </c:otherwise>
                                                           </c:choose>
                                                        </td>   
                                                </tr>    
                                                </table>	
                                                
                                                
                                            </div>
                                            
                                            
                                             
                                            <div class="tabs-body">
                                                <div class="card text-center">
                                                    <div class="card-header">
                                                        <ul class="nav nav-tabs card-header-tabs" role="tablist">
                                                          <li class="nav-item">
                                                            <a class="nav-link active" href="#payments-${status.index}" role="tab" aria-controls="payments-${status.index}" aria-selected="true">Pagamento</a>
                                                          </li>                                                         
                                                          
                                                        </ul>
                                                      </div>
                                                      <div class="card-body">                                                        
                                                         <div class="tab-content">                                                                                                                   
                                                          <div class="tab-pane active" id="payments-${status.index}" role="tabpanel" aria-labelledby="payments-${status.index}-tab">                                                            
                                                                <input id="idReservation${status.index}" type="hidden"  value="${reservationList.idReservation}" >
                                                                <input class="form-control" type="file" name="file" id="fileInput${status.index}" style="display:none;" onchange="onFileSelected('${status.index}');">
                                                            
                                                            ${reservationList.status eq 1 ? "<p class='font-14'>Faça upload do(s) comprovante(s) de pagamento</p>" : ''}
                                                            
                                                                <div class = "documents-heading">    
                                                                    <c:choose>
                                                                    <c:when test="${not empty reservationList.doc}">
                                                                        Boleto:
                                                                        <a href="javascript:download('${pageContext.request.contextPath}/documents/base/docs/${reservationList.doc.name}', '${reservationList.doc.alias}');" > ${reservationList.doc.alias} </a>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        Boleto ainda não disponível! 
                                                                    </c:otherwise> 
                                                                    </c:choose>    
                                                                </div>
                                                                <div id="paymentsListContainer" class="table-responsive" style="display:block">
                                                                <table id="reservationPayments${status.index}" class="table" style="border-radius: 0px;" >
                                                                <thead>    
                                                                <tr>
                                                                    <th>Comprovante</th>      
                                                                    <th style="width: 22px; min-width: 22px"> 
                                                                        <c:if test="${reservationList.status eq 1}">
                                                                            <i class="fa fa-plus" style="font-size: 20px;" onclick="document.getElementById('fileInput${status.index}').click();"></i> 
                                                                        </c:if>
                                                                    </th>
                                                                </tr>                                  
                                                                </thead>
                                                                <tbody> 
                                                                  <c:forEach items="${reservationList.payments}" var="payments" varStatus="status2">
                                                                    <tr>
                                                                    <input type="hidden" id="payments${status.index}_${status2.index}.idMedia" value="${payments.idMedia}"/>  
                                                                    
                                                                    <td> <a href="javascript:download('${pageContext.request.contextPath}/documents/base/payments/${payments.name}', '${payments.alias}');" > ${payments.alias} </a> </td>
                                                                    <td class="middle">
                                                                        <c:if test="${reservationList.status eq 1}">
                                                                        <i class="fa fa-close" style="font-size:20px; float:right" onclick="removePayment(this, '${status.index}_${status2.index}' )"></i>
                                                                        </c:if>
                                                                    </td>                                                                    
                                                                    </tr>
                                                                </c:forEach>           
                                                                </tbody>
                                                                </table>
                                                            </div>
                                                          </div>  
                                                        </div>
                                                      </div>
                                                    
                                                </div>
                                            </div>     
                                        </div>                      
                                    </div>
                                </div>
                            </div>
                        </section>
                        </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="emptyList"> Nenhuma unidade registrada!</div>
                        </c:otherwise> 
                        </c:choose>
                    </div>
                                       
                </div>
            </div>
        </div>   
        <div id="modalMessageDialog" class="modal fade" role="dialog">
            <div class="modal-dialog modal-lg modal-dialog-centered" role="document">
              <div class="modal-content">

                <div class="modal-header">
                    <h5 class="modal-title">Message</h5>
                    <input type="hidden" id="index"/>                             
                </div>                                                                    
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Fechar</button>                                        
                </div>
                </div>
                </div>

       </div>   
       <div class="loader"></div> 
        
        <% if(  request.getAttribute("modalMessage") != null ) { %>
       <script>
           
            $("#modalMessageDialog .modal-title").text( "${modalMessage}") ;
        
            $("#modalMessageDialog").modal();  
       </script>
       <%}%>
        
       <script>
           $('.item-heading i').click(function() { 
    
            $(this).toggleClass('fa fa-chevron-down fa fa-chevron-up'); 
        });    
       </script>    
   
</div>
</div>
</body>
</html>
