package com.arjuna.webservices11.wsat.sei;

import org.oasis_open.docs.ws_tx.wsat._2006._06.Notification;
import org.oasis_open.docs.ws_tx.wsat._2006._06.CompletionCoordinatorPortType;
import org.jboss.ws.api.addressing.MAP;

import javax.jws.*;
import javax.jws.soap.SOAPBinding;
import javax.annotation.Resource;
import javax.xml.ws.Action;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.soap.Addressing;

import com.arjuna.webservices11.wsarj.ArjunaContext;
import com.arjuna.webservices11.wsat.processors.CompletionCoordinatorProcessor;
import com.arjuna.webservices11.wsaddr.AddressingHelper;
import com.arjuna.services.framework.task.TaskManager;
import com.arjuna.services.framework.task.Task;

/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.1-b03-
 * Generated source version: 2.0
 *
 */
@WebService(name = "CompletionCoordinatorPortType", targetNamespace = "http://docs.oasis-open.org/ws-tx/wsat/2006/06",
        // wsdlLocation = "/WEB-INF/wsdl/wsat-completion-coordinator-binding.wsdl",
        serviceName = "CompletionCoordinatorService",
        portName = "CompletionCoordinatorPortType"
)
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@HandlerChain(file="/ws-t_handlers.xml")
@Addressing(required=true)
public class CompletionCoordinatorPortTypeImpl // implements CompletionCoordinatorPortType
{
    @Resource
    private WebServiceContext webServiceCtx;

    /**
     *
     * @param parameters
     */
    @WebMethod(operationName = "CommitOperation", action = "http://docs.oasis-open.org/ws-tx/wsat/2006/06/Commit")
    @Oneway
    @Action(input="http://docs.oasis-open.org/ws-tx/wsat/2006/06/Commit")
    public void commitOperation(
        @WebParam(name = "Commit", targetNamespace = "http://docs.oasis-open.org/ws-tx/wsat/2006/06", partName = "parameters")
        Notification parameters)
    {
        MessageContext ctx = webServiceCtx.getMessageContext();
        final Notification commit = parameters;
        final MAP inboundMap = AddressingHelper.inboundMap(ctx);
        final ArjunaContext arjunaContext = ArjunaContext.getCurrentContext(ctx);

        TaskManager.getManager().queueTask(new Task() {
            public void executeTask() {
                CompletionCoordinatorProcessor.getProcessor().commit(commit, inboundMap, arjunaContext) ;
            }
        }) ;
    }

    /**
     *
     * @param parameters
     */
    @WebMethod(operationName = "RollbackOperation", action = "http://docs.oasis-open.org/ws-tx/wsat/2006/06/Rollback")
    @Oneway
    @Action(input="http://docs.oasis-open.org/ws-tx/wsat/2006/06/Rollback")
    public void rollbackOperation(
        @WebParam(name = "Rollback", targetNamespace = "http://docs.oasis-open.org/ws-tx/wsat/2006/06", partName = "parameters")
        Notification parameters)
    {
        MessageContext ctx = webServiceCtx.getMessageContext();
        final Notification rollback = parameters;
        final MAP inboundMap = AddressingHelper.inboundMap(ctx);
        final ArjunaContext arjunaContext = ArjunaContext.getCurrentContext(ctx);

        TaskManager.getManager().queueTask(new Task() {
            public void executeTask() {
                CompletionCoordinatorProcessor.getProcessor().rollback(rollback, inboundMap, arjunaContext) ;
            }
        }) ;
    }

}
