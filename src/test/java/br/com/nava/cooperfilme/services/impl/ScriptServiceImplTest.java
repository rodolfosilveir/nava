package br.com.nava.cooperfilme.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.nava.cooperfilme.api.requests.CreateScriptRequest;
import br.com.nava.cooperfilme.api.requests.CreateScriptRequestMock;
import br.com.nava.cooperfilme.dtos.ScriptStatus;
import br.com.nava.cooperfilme.dtos.UserDto;
import br.com.nava.cooperfilme.dtos.UserDtoMock;
import br.com.nava.cooperfilme.entities.ScriptEntity;
import br.com.nava.cooperfilme.entities.ScriptEntityMock;
import br.com.nava.cooperfilme.exceptions.NotFoundException;
import br.com.nava.cooperfilme.exceptions.ScriptAlreadyExistsException;
import br.com.nava.cooperfilme.repositories.ScriptControlRepository;
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

    private void mockSecurityContext(UserDto user) {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void shouldCreateScriptSuccessfully() {
        UserDto userMock = UserDtoMock.create("test_user", UUID.randomUUID());
        mockSecurityContext(userMock);

        CreateScriptRequest request = CreateScriptRequestMock.create();
        ScriptEntity scriptEntity = ScriptEntityMock.create(UUID.randomUUID(), ScriptStatus.EM_ANALISE);

        when(scriptRepository.save(any(ScriptEntity.class))).thenReturn(scriptEntity);

        Integer response = scriptServiceImpl.createScript(request);

        assertEquals(scriptEntity.getId(), response);
        verify(scriptRepository, times(1)).save(any(ScriptEntity.class));
        verify(scriptRepository, never()).findById(anyInt());
        verify(scriptRepository, never()).findScriptWithDetailsById(anyInt());
    }

    @Test
    void shouldThrowScriptAlreadyExistsException() {
        UserDto userMock = UserDtoMock.create("test_user", UUID.randomUUID());
        mockSecurityContext(userMock);

        CreateScriptRequest request = CreateScriptRequestMock.create();
            
        when(scriptRepository.save(any(ScriptEntity.class))).thenThrow(DataIntegrityViolationException.class);

        ScriptAlreadyExistsException e = assertThrows(ScriptAlreadyExistsException.class, () -> 
        scriptServiceImpl.createScript(request));

        assertEquals(new ScriptAlreadyExistsException(request.name()).getMessage(), e.getMessage());
        verify(scriptRepository, times(1)).save(any(ScriptEntity.class));
        verify(scriptRepository, never()).findById(anyInt());
        verify(scriptRepository, never()).findScriptWithDetailsById(anyInt());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenScriptNotFound() {
        UserDto userMock = UserDtoMock.create("test_user", UUID.randomUUID());
        mockSecurityContext(userMock);

        Integer id = 1;
        when(scriptRepository.findById(id)).thenReturn(Optional.empty());

        NotFoundException e = assertThrows(NotFoundException.class, () -> 
        scriptServiceImpl.getScriptStatus(id));

        assertEquals(String.format(SCRIPT_NOT_FOUND, id.toString()), e.getMessage());
        verify(scriptRepository, never()).save(any(ScriptEntity.class));
        verify(scriptRepository, times(1)).findById(id);
        verify(scriptRepository, never()).findScriptWithDetailsById(id);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenScriptFoundDoesNotBelongToLoggedUser() {
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        UserDto userMock = UserDtoMock.create("test_user", uuid1);
        mockSecurityContext(userMock);

        Integer id = 1;
        when(scriptRepository.findById(id)).thenReturn(Optional.of(ScriptEntityMock.create(uuid2, ScriptStatus.EM_ANALISE)));

        NotFoundException e = assertThrows(NotFoundException.class, () -> 
        scriptServiceImpl.getScriptStatus(id));

        assertEquals(String.format(SCRIPT_NOT_FOUND, id.toString()), e.getMessage());
        verify(scriptRepository, never()).save(any(ScriptEntity.class));
        verify(scriptRepository, times(1)).findById(id);
        verify(scriptRepository, never()).findScriptWithDetailsById(id);
    
    }

    @Test
    void shouldReturnScriptStatusWhenScriptFound() {
        UUID uuid = UUID.randomUUID();
        UserDto userMock = UserDtoMock.create("test_user", uuid);
        mockSecurityContext(userMock);

        ScriptEntity scriptEntity = ScriptEntityMock.create(uuid, ScriptStatus.EM_ANALISE);
        
        Integer id = 1;
        when(scriptRepository.findById(id)).thenReturn(Optional.of(scriptEntity));

        String response = scriptServiceImpl.getScriptStatus(id);

        assertEquals(scriptEntity.getStatus().getText(), response);
        verify(scriptRepository, never()).save(any(ScriptEntity.class));
        verify(scriptRepository, times(1)).findById(id);
        verify(scriptRepository, never()).findScriptWithDetailsById(id);
    
    }

    @Test
    void shouldThrowNotFoundExceptionWhenScriptDetailsNotFound() {
        Integer id = 1;
        when(scriptRepository.findScriptWithDetailsById(id)).thenReturn(Optional.empty());

        NotFoundException e = assertThrows(NotFoundException.class, () -> 
        scriptServiceImpl.getScriptDetails(id));

        assertEquals(String.format(SCRIPT_NOT_FOUND, id.toString()), e.getMessage());
        verify(scriptRepository, never()).save(any(ScriptEntity.class));
        verify(scriptRepository, never()).findById(id);
        verify(scriptRepository, times(1)).findScriptWithDetailsById(id);
    }
}

