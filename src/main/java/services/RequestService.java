package services;

import domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import repositories.RequestRepository;

import java.util.Collection;
import java.util.Date;

@Service
@Transactional
public class RequestService {
    // Repositories
    @Autowired
    private RequestRepository requestRepository;
    // Services
    @Autowired
    private ActorService actorService;
    @Autowired
    private ServantService servantService;

    @Autowired
    private Validator validator;

    // CRUD -----------------------------------------------------
    public Request finOne(int id){
        return requestRepository.findOne(id);
    }


    public Request save(Request request) {
        final Actor actor = this.actorService.findByPrincipal();
        Assert.notNull(actor, "msg.not.logged.block");
        Assert.isTrue(actor instanceof Responsible, "msg.not.owned.block");
        Request bdObject = requestRepository.findOne(request.getId());
        if (request.getId() == 0) {
            Assert.isTrue(request.getResponsible().equals((Responsible) actor), "msg.not.owned.block");
            request.setStatus(Request.PENDING);
            request.setStartingDay(null);
            request.setEndingDay(null);
        } else {
            Assert.isNull(bdObject.getEndingDay(), "msg.ended.request.block");
            Assert.isTrue(!bdObject.getServant().isCancelled(), "msg.not.available.item.block");
            Assert.isTrue(!bdObject.getServant().isDraft(), "msg.not.available.item.block");
            Assert.isTrue(bdObject.getResponsible().equals((Responsible) actor) || actor instanceof Manager, "msg.not.owned.block");
            String status = bdObject.getStatus();
            if (status.equals(Request.PENDING)) { // Aqui impedimos el cambio de estado una vez aceptado o rechazado
                if (request.getStatus().equals(Request.ACCEPTED)) {
                    request.setStartingDay(new Date(System.currentTimeMillis() - 500));

                }else if (request.getStatus().equals(Request.REJECTED)) {
                    Assert.isTrue(request.getRejectionReason()!=null && !request.getRejectionReason().isEmpty(), "msg.rejected.must.have.reason");
                }
            }else{
                Assert.isTrue(status.equals(Request.ACCEPTED), "msg.rejected.state.block");
                Assert.isTrue(bdObject.getStartingDay()!=null, "msg.incidence.bad.moments");
                Assert.isNull(bdObject.getEndingDay(), "msg.ended.request.block");
            }

        }
        request = requestRepository.save(request);
        this.flush();
        return request;

    }

    public Request create(int servantId) {
        final Actor actor = this.actorService.findByPrincipal();
        Assert.notNull(actor, "msg.not.logged.block");
        Assert.isTrue(actor instanceof Responsible, "msg.not.user.block");
        Servant servant = servantService.findOne(servantId);
        Assert.isTrue(!servant.isDraft(), "msg.not.available.item.block");
        Assert.isTrue(!servant.isCancelled(), "msg.not.available.item.block");
        Request request = new Request();
        request.setResponsible((Responsible) actor);
        request.setCreationMoment(new Date());
        request.setServant(servant);
        request.setStatus(Constant.requestStatus.PENDING.toString());
        return request;
    }

    public Collection<Request> findCreatedRequests() {
        final Actor actor = this.actorService.findByPrincipal();
        Assert.notNull(actor, "msg.not.logged.block");
        Assert.isTrue(actor instanceof Responsible, "msg.not.user.block");
        return this.requestRepository.findByResponsibleId(actor.getId());
    }

    public Collection<Request> findAllAcceptedRequests() {

        final Actor actor = this.actorService.findByPrincipal();
        Assert.notNull(actor, "msg.not.logged.block");
        Assert.isTrue(actor instanceof Manager, "msg.not.user.block");
        return this.requestRepository.findAllAcceptedRequests();
    }


    public Request reconstruct(Request request, BindingResult binding) {
        final Actor actor = this.actorService.findByPrincipal();
        Assert.notNull(actor, "msg.not.logged.block");
        Servant servant = this.servantService.findOne(request.getServant().getId());
        request.setServant(servant);
        Actor responsible = this.actorService.findOne(request.getResponsible().getId());
        request.setResponsible((Responsible) responsible);
        this.validator.validate(request, binding);
        Assert.isTrue(responsible.equals((Responsible) actor) || actor instanceof Manager,
                "msg.not.owned.block");
        return request;
    }

    public Collection<Request> findAllRequestsByServantId(int id) {

        return this.requestRepository.findAllRequestsByServantId(id);
    }


    public Collection<Request> findAll() {
        return requestRepository.findAll();
    }

    public Collection<Request> findActiveRequessToServant(Servant servant) {
        return requestRepository.findActiveRequestsByServantId(servant.getId());
    }
    public void flush() {
        requestRepository.flush();
    }

    public Request findOne(int requestId) {
        return requestRepository.findOne(requestId);
    }
}
