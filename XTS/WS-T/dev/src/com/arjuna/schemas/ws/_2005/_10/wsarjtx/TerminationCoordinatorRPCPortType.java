package com.arjuna.schemas.ws._2005._10.wsarjtx;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.2.9-patch-01
 * Thu Aug 26 17:20:43 BST 2010
 * Generated source version: 2.2.9-patch-01
 *
 */

@WebService(targetNamespace = "http://schemas.arjuna.com/ws/2005/10/wsarjtx", name = "TerminationCoordinatorRPCPortType")
@XmlSeeAlso({ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface TerminationCoordinatorRPCPortType {

    @WebResult(name = "Cancelled", targetNamespace = "http://schemas.arjuna.com/ws/2005/10/wsarjtx", partName = "parameters")
    @WebMethod(operationName = "CancelOperation", action = "http://schemas.arjuna.com/ws/2005/10/wsarjtx/Cancel")
    public NotificationType cancelOperation(
        @WebParam(partName = "parameters", name = "Cancel", targetNamespace = "http://schemas.arjuna.com/ws/2005/10/wsarjtx")
        NotificationType parameters
    ) ;

    @WebResult(name = "Closed", targetNamespace = "http://schemas.arjuna.com/ws/2005/10/wsarjtx", partName = "parameters")
    @WebMethod(operationName = "CloseOperation", action = "http://schemas.arjuna.com/ws/2005/10/wsarjtx/Close")
    public NotificationType closeOperation(
        @WebParam(partName = "parameters", name = "Close", targetNamespace = "http://schemas.arjuna.com/ws/2005/10/wsarjtx")
        NotificationType parameters
    ) ;

    @WebResult(name = "Completed", targetNamespace = "http://schemas.arjuna.com/ws/2005/10/wsarjtx", partName = "parameters")
    @WebMethod(operationName = "CompleteOperation", action = "http://schemas.arjuna.com/ws/2005/10/wsarjtx/Complete")
    public NotificationType completeOperation(
        @WebParam(partName = "parameters", name = "Complete", targetNamespace = "http://schemas.arjuna.com/ws/2005/10/wsarjtx")
        NotificationType parameters
    ) ;
}
