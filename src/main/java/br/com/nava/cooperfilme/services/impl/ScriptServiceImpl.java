package br.com.nava.cooperfilme.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.nava.cooperfilme.api.requests.CreateScriptRequest;
import br.com.nava.cooperfilme.api.requests.VoteScriptRequest;
import br.com.nava.cooperfilme.api.responses.ScriptResponse;
import br.com.nava.cooperfilme.dtos.Role;
import br.com.nava.cooperfilme.dtos.ScriptStatus;
import br.com.nava.cooperfilme.dtos.UserDto;
import br.com.nava.cooperfilme.entities.ScriptControlEntity;
import br.com.nava.cooperfilme.entities.ScriptEntity;
import br.com.nava.cooperfilme.exceptions.ForbiddenVoteException;
import br.com.nava.cooperfilme.exceptions.NotFoundException;
import br.com.nava.cooperfilme.exceptions.ScriptAlreadyExistsException;
import br.com.nava.cooperfilme.exceptions.WithoutReasonException;
import br.com.nava.cooperfilme.repositories.ScriptControlRepository;
import br.com.nava.cooperfilme.repositories.ScriptRepository;
import br.com.nava.cooperfilme.repositories.ScriptRepository.ScriptDetails;
import br.com.nava.cooperfilme.services.ScriptControlService;
import br.com.nava.cooperfilme.services.ScriptService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScriptServiceImpl implements ScriptService, ScriptControlService {

    private final ScriptRepository scriptRepository;
    private final ScriptControlRepository scriptControlRepository;

    private static final String SCRIPT_NOT_FOUND = "Roteiro com o ID %s não encontrado";
    private static final String FORBIDDEN_VOTE_GENERAL_MESSAGE = "Only %s can vote at this stage. ID: %s, Status: %s";
    private static final String FORBIDDEN_VOTE_NO_MORE_VOTES_MESSAGE = "You have already voted on this script and cannot vote again. ID: %s, Status: %s";
    private static final String SCRIPT_ALREADY_FINISHED_MESSAGE = "Script with ID %s is already finished and cannot be voted on. Status: %s";

    @Override
    @Transactional
    public Integer createScript(CreateScriptRequest request) {
        UserDto user = (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try{
             ScriptEntity script = scriptRepository.save(ScriptEntity.builder()
                .idUserCreator(user.getId())
                .name(request.name())
                .content(request.content())
                .status(ScriptStatus.AGUARDANDO_ANALISE)
                .creationDate(LocalDateTime.now())
                .build());

            scriptControlRepository.save(ScriptControlEntity.builder()
                .idScript(script.getId())
                .build());

            return script.getId();
        }catch(DataIntegrityViolationException e){
            throw new ScriptAlreadyExistsException(request.name());
        }
        
    }

    @Override
    public String getScriptStatus(Integer id) {
        UserDto user = (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ScriptEntity script = this.getScriptById(id);

        if(!script.getIdUserCreator().equals(user.getId())){
            throw new NotFoundException(String.format(SCRIPT_NOT_FOUND, id.toString()));
        }

        return script.getStatus().getText();
    }

    @Override
    public ScriptResponse getScriptDetails(Integer id) {
        
        ScriptDetails details = scriptRepository.findScriptWithDetailsById(id)
            .orElseThrow(() -> new NotFoundException(String.format(SCRIPT_NOT_FOUND, id.toString())));

        return ScriptResponse.fromDetails(details);
    }

    @Override
    public List<ScriptResponse> getAllScripts() {
        List<ScriptDetails> allDetails = scriptRepository.findScriptsWithDetails();

        return allDetails.stream().map(ScriptResponse::fromDetails).toList();
    }

    @Override
    public void voteScript(Integer id, VoteScriptRequest request) {

        UserDto user = (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ScriptEntity script = this.getScriptById(id);
        ScriptControlEntity scriptControl = scriptControlRepository.findByScriptId(id);

        switch (script.getStatus()) {
            case AGUARDANDO_ANALISE:
                    script.setIdUserOwner(user.getId());
                    script.setStatus(ScriptStatus.EM_ANALISE);

                break;
            case EM_ANALISE:
                    validateRole(user, Role.ANALYST, script);
                    validReason(request, script);

                    script.setStatus(request.approve() ? ScriptStatus.AGUARDANDO_REVISAO : ScriptStatus.RECUSADO);

                    scriptControl.setInAnalysisStatus(true);
                    scriptControl.setInAnalysisDate(LocalDateTime.now());
                    scriptControl.setInAnalysisById(user.getId());
                    scriptControl.setInAnalysisReason(request.reason());
                    
                break;
            case AGUARDANDO_REVISAO:
                    validateRole(user, Role.REVIEWER, script);

                    script.setStatus(ScriptStatus.EM_REVISAO);

                    scriptControl.setPendingReviewStatus(true);
                    scriptControl.setPendingReviewDate(LocalDateTime.now());
                    scriptControl.setPendingReviewById(user.getId());

                break;
            case EM_REVISAO:
                    validateRole(user, Role.REVIEWER, script);
                    validReason(request, script);

                    script.setStatus(ScriptStatus.AGUARDANDO_APROVACAO);

                    scriptControl.setReviewingStatus(true);
                    scriptControl.setReviewingDate(LocalDateTime.now());
                    scriptControl.setReviewingReason(request.reason());

                break;
            case AGUARDANDO_APROVACAO:
                    validateRole(user, Role.APPROVER, script);

                    script.setStatus(ScriptStatus.EM_APROVACAO);
                    scriptControl.setPendingApprovalStatus(true);
                    scriptControl.setPendingApprovalDate(LocalDateTime.now());
                    scriptControl.setApprovedVote1(request.approve());
                    scriptControl.setApprovedVote1ById(user.getId());

                break;
            case EM_APROVACAO:
                    validateRole(user, Role.APPROVER, script);

                    if(scriptControl.getApprovedVote1ById().equals(user.getId())){
                        throw new ForbiddenVoteException(FORBIDDEN_VOTE_NO_MORE_VOTES_MESSAGE.formatted(script.getId(), script.getStatus()));
                    }

                    if(Objects.isNull(scriptControl.getApprovedVote2ById())){
                        scriptControl.setApprovedVote2(request.approve());
                        scriptControl.setApprovedVote2ById(user.getId());
                    }else{
                        if(scriptControl.getApprovedVote2ById().equals(user.getId())){
                            throw new ForbiddenVoteException(FORBIDDEN_VOTE_NO_MORE_VOTES_MESSAGE.formatted(script.getId(), script.getStatus()));
                        }
                        scriptControl.setApprovedVote3(request.approve());
                        scriptControl.setApprovedVote3ById(user.getId());

                        scriptControl.setApprovingStatus(true);
                        scriptControl.setApprovingDate(LocalDateTime.now());

                        if(scriptControl.getApprovedVote1() && scriptControl.getApprovedVote2() && scriptControl.getApprovedVote3()){
                            script.setStatus(ScriptStatus.APROVADO);
                        }else{
                            script.setStatus(ScriptStatus.RECUSADO);
                        }
                    }
                break;
            case APROVADO, RECUSADO, ERRO:
                    throw new ForbiddenVoteException(SCRIPT_ALREADY_FINISHED_MESSAGE.formatted(script.getId(), script.getStatus()));
        }

        scriptRepository.save(script);
        scriptControlRepository.save(scriptControl);
    }

    private ScriptEntity getScriptById(Integer id) {
        return scriptRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(SCRIPT_NOT_FOUND, id.toString())));
    }

    private void validateRole(UserDto user, Role validRole, ScriptEntity script){
        if(!user.getRole().equals(validRole)){
            throw new ForbiddenVoteException(FORBIDDEN_VOTE_GENERAL_MESSAGE.formatted(validRole.name().toLowerCase() + "s", script.getId(), script.getStatus()));
        }
    }

    private void validReason(VoteScriptRequest request, ScriptEntity script){
        if(Objects.isNull(request.reason())){
            throw new WithoutReasonException(script.getStatus());
        }
    }

}
