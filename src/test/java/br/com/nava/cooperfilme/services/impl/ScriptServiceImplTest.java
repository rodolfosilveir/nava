package br.com.nava.cooperfilme.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.nava.cooperfilme.api.requests.CreateScriptRequest;
import br.com.nava.cooperfilme.api.requests.CreateScriptRequestMock;
import br.com.nava.cooperfilme.api.requests.VoteScriptRequest;
import br.com.nava.cooperfilme.api.responses.ScriptResponse;
import br.com.nava.cooperfilme.dtos.Role;
import br.com.nava.cooperfilme.dtos.ScriptStatus;
import br.com.nava.cooperfilme.dtos.UserDto;
import br.com.nava.cooperfilme.dtos.UserDtoMock;
import br.com.nava.cooperfilme.entities.ScriptControlEntity;
import br.com.nava.cooperfilme.entities.ScriptControlEntityMock;
import br.com.nava.cooperfilme.entities.ScriptEntity;
import br.com.nava.cooperfilme.entities.ScriptEntityMock;
import br.com.nava.cooperfilme.exceptions.ForbiddenVoteException;
import br.com.nava.cooperfilme.exceptions.NotFoundException;
import br.com.nava.cooperfilme.exceptions.ScriptAlreadyExistsException;
import br.com.nava.cooperfilme.exceptions.WithoutReasonException;
import br.com.nava.cooperfilme.repositories.ScriptControlRepository;
import br.com.nava.cooperfilme.repositories.ScriptDetailsMock;
import br.com.nava.cooperfilme.repositories.ScriptRepository;

@ExtendWith(MockitoExtension.class)
class ScriptServiceImplTest {

    @Mock 
    private ScriptRepository scriptRepository;

    @Mock
    private ScriptControlRepository scriptControlRepository;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks 
    private ScriptServiceImpl scriptServiceImpl;

    private static final String SCRIPT_NOT_FOUND = "Roteiro com o ID %s não encontrado";
    private static final Integer ID = 1;

    private void mockSecurityContext(UserDto user) {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void shouldThrowScriptAlreadyExistsException() {
        UserDto userMock = UserDtoMock.create("test_user", UUID.randomUUID(), Role.ADMIN);
        mockSecurityContext(userMock);

        CreateScriptRequest request = CreateScriptRequestMock.create();
            
        when(scriptRepository.save(any(ScriptEntity.class))).thenThrow(DataIntegrityViolationException.class);

        ScriptAlreadyExistsException e = assertThrows(ScriptAlreadyExistsException.class, () -> 
        scriptServiceImpl.createScript(request));

        assertEquals(new ScriptAlreadyExistsException(request.name()).getMessage(), e.getMessage());

        verify(scriptRepository, times(1)).save(any(ScriptEntity.class));
        verify(scriptControlRepository, never()).save(any(ScriptControlEntity.class));
        verify(scriptRepository, never()).findById(ID);
        verify(scriptRepository, never()).findScriptWithDetailsById(ID);
        verify(scriptRepository, never()).findScriptsWithDetails();
        verify(scriptControlRepository, never()).findByScriptId(ID);
    }

    @Test
    void shouldCreateScriptSuccessfully() {
        UserDto userMock = UserDtoMock.create("test_user", UUID.randomUUID(), Role.ADMIN);
        mockSecurityContext(userMock);

        CreateScriptRequest request = CreateScriptRequestMock.create();
        ScriptEntity scriptEntity = ScriptEntityMock.create(UUID.randomUUID(), ScriptStatus.EM_ANALISE);

        when(scriptRepository.save(any(ScriptEntity.class))).thenReturn(scriptEntity);
        when(scriptControlRepository.save(any(ScriptControlEntity.class))).thenReturn(ScriptControlEntityMock.create());

        Integer response = scriptServiceImpl.createScript(request);

        assertEquals(scriptEntity.getId(), response);
        
        verify(scriptRepository, times(1)).save(any(ScriptEntity.class));
        verify(scriptControlRepository, times(1)).save(any(ScriptControlEntity.class));
        verify(scriptRepository, never()).findById(ID);
        verify(scriptRepository, never()).findScriptWithDetailsById(ID);
        verify(scriptRepository, never()).findScriptsWithDetails();
        verify(scriptControlRepository, never()).findByScriptId(ID);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenScriptNotFound() {
        UserDto userMock = UserDtoMock.create("test_user", UUID.randomUUID(), Role.ADMIN);
        mockSecurityContext(userMock);

        when(scriptRepository.findById(ID)).thenReturn(Optional.empty());

        NotFoundException e = assertThrows(NotFoundException.class, () -> 
        scriptServiceImpl.getScriptStatus(ID));

        assertEquals(String.format(SCRIPT_NOT_FOUND, ID.toString()), e.getMessage());

        verify(scriptRepository, never()).save(any(ScriptEntity.class));
        verify(scriptControlRepository, never()).save(any(ScriptControlEntity.class));
        verify(scriptRepository, times(1)).findById(ID);
        verify(scriptRepository, never()).findScriptWithDetailsById(ID);
        verify(scriptRepository, never()).findScriptsWithDetails();
        verify(scriptControlRepository, never()).findByScriptId(ID);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenScriptFoundDoesNotBelongToLoggedUser() {
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        UserDto userMock = UserDtoMock.create("test_user", uuid1, Role.ADMIN);
        mockSecurityContext(userMock);

        when(scriptRepository.findById(ID)).thenReturn(Optional.of(ScriptEntityMock.create(uuid2, ScriptStatus.EM_ANALISE)));

        NotFoundException e = assertThrows(NotFoundException.class, () -> 
        scriptServiceImpl.getScriptStatus(ID));

        assertEquals(String.format(SCRIPT_NOT_FOUND, ID.toString()), e.getMessage());

        verify(scriptRepository, never()).save(any(ScriptEntity.class));
        verify(scriptControlRepository, never()).save(any(ScriptControlEntity.class));
        verify(scriptRepository, times(1)).findById(ID);
        verify(scriptRepository, never()).findScriptWithDetailsById(ID);
        verify(scriptRepository, never()).findScriptsWithDetails();  
        verify(scriptControlRepository, never()).findByScriptId(ID);  
    }

    @Test
    void shouldReturnScriptStatusWhenScriptFound() {
        UUID uuid = UUID.randomUUID();
        UserDto userMock = UserDtoMock.create("test_user", uuid, Role.ADMIN);
        mockSecurityContext(userMock);

        ScriptEntity scriptEntity = ScriptEntityMock.create(uuid, ScriptStatus.EM_ANALISE);
        
        when(scriptRepository.findById(ID)).thenReturn(Optional.of(scriptEntity));

        String response = scriptServiceImpl.getScriptStatus(ID);

        assertEquals(scriptEntity.getStatus().getText(), response);

        verify(scriptRepository, never()).save(any(ScriptEntity.class));
        verify(scriptControlRepository, never()).save(any(ScriptControlEntity.class));
        verify(scriptRepository, times(1)).findById(ID);
        verify(scriptRepository, never()).findScriptWithDetailsById(ID);
        verify(scriptRepository, never()).findScriptsWithDetails();
        verify(scriptControlRepository, never()).findByScriptId(ID);
    
    }

    @Test
    void shouldThrowNotFoundExceptionWhenScriptDetailsNotFound() {
        when(scriptRepository.findScriptWithDetailsById(ID)).thenReturn(Optional.empty());

        NotFoundException e = assertThrows(NotFoundException.class, () -> 
        scriptServiceImpl.getScriptDetails(ID));

        assertEquals(String.format(SCRIPT_NOT_FOUND, ID.toString()), e.getMessage());

        verify(scriptRepository, never()).save(any(ScriptEntity.class));
        verify(scriptControlRepository, never()).save(any(ScriptControlEntity.class));
        verify(scriptRepository, never()).findById(ID);
        verify(scriptRepository, times(1)).findScriptWithDetailsById(ID);
        verify(scriptRepository, never()).findScriptsWithDetails();
        verify(scriptControlRepository, never()).findByScriptId(ID);
    }

    @Test
    void shouldReturnScriptDetails() {
        ScriptDetailsMock detailsMock = ScriptDetailsMock.create();
        when(scriptRepository.findScriptWithDetailsById(ID)).thenReturn(Optional.of(detailsMock));

        ScriptResponse response = scriptServiceImpl.getScriptDetails(ID);

        assertEquals(detailsMock.getName(), response.name());

        verify(scriptRepository, never()).save(any(ScriptEntity.class));
        verify(scriptControlRepository, never()).save(any(ScriptControlEntity.class));
        verify(scriptRepository, never()).findById(ID);
        verify(scriptRepository, times(1)).findScriptWithDetailsById(ID);
        verify(scriptRepository, never()).findScriptsWithDetails();
        verify(scriptControlRepository, never()).findByScriptId(ID);
    }

    @Test
    void shouldReturnAllScripts() {
        ScriptDetailsMock detailsMock = ScriptDetailsMock.create();
        
        when(scriptRepository.findScriptsWithDetails()).thenReturn(List.of(detailsMock));

        List<ScriptResponse> response = scriptServiceImpl.getAllScripts();

        assertEquals(detailsMock.getName(), response.get(0).name());

        verify(scriptRepository, never()).save(any(ScriptEntity.class));
        verify(scriptControlRepository, never()).save(any(ScriptControlEntity.class));
        verify(scriptRepository, never()).findById(ID);
        verify(scriptRepository, never()).findScriptWithDetailsById(ID);
        verify(scriptRepository, times(1)).findScriptsWithDetails();
        verify(scriptControlRepository, never()).findByScriptId(ID);
    
    }

    @ParameterizedTest
    @MethodSource("successCases")
    void shouldUpdateStatusSuccessfully(ScriptStatus status, Role userRole, boolean approve, String reason, 
            UUID uuidUser, UUID uuidVote1, UUID uuidVote2, boolean vote1, boolean vote2) {

        UserDto userMock = UserDtoMock.create("test_user", uuidUser, userRole);
        mockSecurityContext(userMock);

        ScriptEntity scriptEntity = ScriptEntityMock.create(uuidUser, status);
        ScriptControlEntity scriptControl = ScriptControlEntityMock.create(uuidVote1, uuidVote2, vote1, vote2);

        VoteScriptRequest request = new VoteScriptRequest(approve, reason);

        when(scriptRepository.findById(ID)).thenReturn(Optional.of(scriptEntity));
        when(scriptControlRepository.findByScriptId(ID)).thenReturn(scriptControl);
        when(scriptRepository.save(any(ScriptEntity.class))).thenReturn(scriptEntity);
        when(scriptControlRepository.save(scriptControl)).thenReturn(scriptControl);

        scriptServiceImpl.voteScript(ID, request);

        verify(scriptRepository, times(1)).save(any(ScriptEntity.class));
        verify(scriptControlRepository, times(1)).save(any(ScriptControlEntity.class));
        verify(scriptRepository, times(1)).findById(ID);
        verify(scriptRepository, never()).findScriptWithDetailsById(ID);
        verify(scriptRepository, never()).findScriptsWithDetails();
        verify(scriptControlRepository, times(1)).findByScriptId(ID);

    }

    @ParameterizedTest
    @MethodSource("forbiddenVoteCases")
    void shouldThrowForbiddenVoteException(ScriptStatus status, Role userRole, boolean approve, String reason, 
            UUID uuidUser, UUID uuidVote1, UUID uuidVote2, boolean vote1, boolean vote2) {

        UserDto userMock = UserDtoMock.create("test_user", uuidUser, userRole);
        mockSecurityContext(userMock);

        ScriptEntity scriptEntity = ScriptEntityMock.create(uuidUser, status);
        ScriptControlEntity scriptControl = ScriptControlEntityMock.create(uuidVote1, uuidVote2, vote1, vote2);

        VoteScriptRequest request = new VoteScriptRequest(true, "reason");

        when(scriptRepository.findById(ID)).thenReturn(Optional.of(scriptEntity));
        when(scriptControlRepository.findByScriptId(ID)).thenReturn(scriptControl);

        assertThrows(ForbiddenVoteException.class, () -> 
        scriptServiceImpl.voteScript(ID, request));

        verify(scriptRepository, never()).save(any(ScriptEntity.class));
        verify(scriptControlRepository, never()).save(any(ScriptControlEntity.class));
        verify(scriptRepository, times(1)).findById(ID);
        verify(scriptRepository, never()).findScriptWithDetailsById(ID);
        verify(scriptRepository, never()).findScriptsWithDetails();
        verify(scriptControlRepository, times(1)).findByScriptId(ID);

    }

    @Test
    void shouldThrowWithoutReasonException() {

        UUID uuid = UUID.randomUUID();
        UserDto userMock = UserDtoMock.create("test_user", uuid, Role.ANALYST);
        mockSecurityContext(userMock);

        ScriptEntity scriptEntity = ScriptEntityMock.create(uuid, ScriptStatus.EM_ANALISE);
        ScriptControlEntity scriptControl = ScriptControlEntityMock.create();

        VoteScriptRequest request = new VoteScriptRequest(true, null);

        when(scriptRepository.findById(ID)).thenReturn(Optional.of(scriptEntity));
        when(scriptControlRepository.findByScriptId(ID)).thenReturn(scriptControl);

        assertThrows(WithoutReasonException.class, () -> 
        scriptServiceImpl.voteScript(ID, request));

        verify(scriptRepository, never()).save(any(ScriptEntity.class));
        verify(scriptControlRepository, never()).save(any(ScriptControlEntity.class));
        verify(scriptRepository, times(1)).findById(ID);
        verify(scriptRepository, never()).findScriptWithDetailsById(ID);
        verify(scriptRepository, never()).findScriptsWithDetails();
        verify(scriptControlRepository, times(1)).findByScriptId(ID);

    }

    @ParameterizedTest
    @MethodSource("finishedStatus")
    void shouldThrowForbiddenVoteExceptionBecauseScriptHasFinished(ScriptStatus scriptStatus) {

        UUID uuid = UUID.randomUUID();
        UserDto userMock = UserDtoMock.create("test_user", uuid, Role.ANALYST);
        mockSecurityContext(userMock);

        ScriptEntity scriptEntity = ScriptEntityMock.create(uuid, scriptStatus);
        ScriptControlEntity scriptControl = ScriptControlEntityMock.create();

        VoteScriptRequest request = new VoteScriptRequest(true, "reason");

        when(scriptRepository.findById(ID)).thenReturn(Optional.of(scriptEntity));
        when(scriptControlRepository.findByScriptId(ID)).thenReturn(scriptControl);

        assertThrows(ForbiddenVoteException.class, () -> 
        scriptServiceImpl.voteScript(ID, request));

        verify(scriptRepository, never()).save(any(ScriptEntity.class));
        verify(scriptControlRepository, never()).save(any(ScriptControlEntity.class));
        verify(scriptRepository, times(1)).findById(ID);
        verify(scriptRepository, never()).findScriptWithDetailsById(ID);
        verify(scriptRepository, never()).findScriptsWithDetails();
        verify(scriptControlRepository, times(1)).findByScriptId(ID);

    }

    private static Stream<Arguments> successCases() {
        UUID uuidUser = UUID.randomUUID();
        return Stream.of(
                Arguments.of(ScriptStatus.AGUARDANDO_ANALISE, Role.ADMIN, true, "reason", uuidUser, null, null, true, true),
                Arguments.of(ScriptStatus.EM_ANALISE, Role.ANALYST, true, "reason", uuidUser, null, null, true, true),
                Arguments.of(ScriptStatus.EM_ANALISE, Role.ANALYST, false, "reason", uuidUser, null, null, true, true),
                Arguments.of(ScriptStatus.AGUARDANDO_REVISAO, Role.REVIEWER, true, "reason", uuidUser, null, null, true, true),
                Arguments.of(ScriptStatus.EM_REVISAO, Role.REVIEWER, true, "reason", uuidUser, null, null, true, true),
                Arguments.of(ScriptStatus.AGUARDANDO_APROVACAO, Role.APPROVER, true, "reason", uuidUser, null, null, true, true),
                Arguments.of(ScriptStatus.EM_APROVACAO, Role.APPROVER, true, "reason", uuidUser, UUID.randomUUID(), null, true, true),
                Arguments.of(ScriptStatus.EM_APROVACAO, Role.APPROVER, true, "reason", uuidUser, UUID.randomUUID(), UUID.randomUUID(), true, true),
                Arguments.of(ScriptStatus.EM_APROVACAO, Role.APPROVER, false, "reason", uuidUser, UUID.randomUUID(), UUID.randomUUID(), true, true)
        );
    }

    private static Stream<Arguments> forbiddenVoteCases() {
        UUID uuidUser = UUID.randomUUID();
        return Stream.of(
                Arguments.of(ScriptStatus.EM_ANALISE, Role.ADMIN, true, "reason", uuidUser, null, null, true, true),
                Arguments.of(ScriptStatus.EM_APROVACAO, Role.APPROVER, true, "reason", uuidUser, uuidUser, null, true, true),
                Arguments.of(ScriptStatus.EM_APROVACAO, Role.APPROVER, true, "reason", uuidUser, UUID.randomUUID(), uuidUser, true, true)
        );
    }

    private static Stream<ScriptStatus> finishedStatus() {
        return Stream.of(
                ScriptStatus.APROVADO,
                ScriptStatus.RECUSADO,
                ScriptStatus.ERRO
        );
    }
}

