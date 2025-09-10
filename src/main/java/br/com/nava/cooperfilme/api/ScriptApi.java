package br.com.nava.cooperfilme.api;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.nava.cooperfilme.api.requests.CreateScriptRequest;
import br.com.nava.cooperfilme.api.requests.VoteScriptRequest;
import br.com.nava.cooperfilme.api.responses.DefaultResponse;
import br.com.nava.cooperfilme.api.responses.ScriptIdResponse;
import br.com.nava.cooperfilme.api.responses.ScriptResponse;
import br.com.nava.cooperfilme.api.responses.ScriptStatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Validated
@Tag(name = "Roteiros", description = "Operações relacionadas a roteiros")
public interface ScriptApi {

    @PostMapping("/script")
    @Operation(
        summary = "Script",
        description = "Operação para criar um novo roteiro"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Roteiro criado com sucesso.",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    value =  """
                            {
                                "httpStatus": 200,
                                "resultData": {
                                    "id": 1
                                }
                            }
                            """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Requisição inválida. Verifique os dados enviados.",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    value =  """
                            {
                                "httpStatus": 400,
                                "erros": [
                                    "name: O nome do roteiro é obrigatório; "
                                ]
                            }
                            """
                )
            )
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "Não permitido.",
            content = @Content(schema = @Schema(hidden = true))
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Erro Interno do Servidor.",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    value =  """
                            {
                                "httpStatus": 500,
                                "erros": [
                                    "Erro interno do servidor"
                                ]
                            }
                            """
                )
            )
        )
    })
    public ResponseEntity<DefaultResponse<ScriptIdResponse>> createScript(@RequestBody @Valid CreateScriptRequest request);

    @GetMapping("/script/{id}/status")
    @Operation(
        summary = "Script",
        description = "Operação para obter o status de um roteiro"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Status do roteiro obtido com sucesso.",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    value =  """
                            {
                                "httpStatus": 200,
                                "resultData": {
                                    "status": "aguardando_analise"
                                }
                            }
                            """
                )
            )
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "Não permitido.",
            content = @Content(schema = @Schema(hidden = true))
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Roteiro não encontrado.",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    value =  """
                            {
                                "httpStatus": 404,
                                "erros": [
                                    "Roteiro com o ID 1 não encontrado "
                                ]
                            }
                            """
                )
            )
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Erro Interno do Servidor.",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    value =  """
                            {
                                "httpStatus": 500,
                                "erros": [
                                    "Erro interno do servidor"
                                ]
                            }
                            """
                )
            )
        )
    })
    public ResponseEntity<DefaultResponse<ScriptStatusResponse>> getScriptStatus(@PathVariable Integer id);

    @GetMapping("/script/{id}")
    @Operation(
        summary = "Script",
        description = "Operação para obter os dados de um roteiro"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Dados do roteiro obtidos com sucesso.",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    value =  """
                            {
                                "httpStatus": 200,
                                "resultData": {
                                    "id": 1,
                                    "name": "O velho oeste",
                                    "content": "Então os xerifes apontaram a arma e atiraram.",
                                    "status": "aguardando_analise",
                                    "creationDate": "2023-10-01T12:00:00",
                                    "owner": {
                                        "email": "owner@cooperfilme.com",
                                        "name": "Owner Name",
                                        "phone": "123456789"
                                    },
                                    "creator": {
                                        "email": "creator@client.com",
                                        "name": "Creator Name",
                                        "phone": "987654321"
                                    }
                                }
                            }
                            """
                )
            )
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "Não permitido.",
            content = @Content(schema = @Schema(hidden = true))
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Roteiro não encontrado.",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    value =  """
                            {
                                "httpStatus": 404,
                                "erros": [
                                    "Roteiro com o ID 1 não encontrado "
                                ]
                            }
                            """
                )
            )
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Erro Interno do Servidor.",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    value =  """
                            {
                                "httpStatus": 500,
                                "erros": [
                                    "Erro interno do servidor"
                                ]
                            }
                            """
                )
            )
        )
    })
    public ResponseEntity<DefaultResponse<ScriptResponse>> getScript(@PathVariable Integer id);

    @GetMapping("/script")
    @Operation(
        summary = "Script",
        description = "Operação para obter os dados de um roteiro"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Dados do roteiro obtidos com sucesso.",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    value =  """
                            [
                                {
                                    "httpStatus": 200,
                                    "resultData": {
                                        "id": 1,
                                        "name": "O velho oeste",
                                        "content": "Então os xerifes apontaram a arma e atiraram.",
                                        "status": "aguardando_analise",
                                        "creationDate": "2023-10-01T12:00:00",
                                        "owner": {
                                            "email": "owner@cooperfilme.com",
                                            "name": "Owner Name",
                                            "phone": "123456789"
                                        },
                                        "creator": {
                                            "email": "creator@client.com",
                                            "name": "Creator Name",
                                            "phone": "987654321"
                                        }
                                    }
                                }
                            ]
                            """
                )
            )
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "Não permitido.",
            content = @Content(schema = @Schema(hidden = true))
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Erro Interno do Servidor.",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    value =  """
                            {
                                "httpStatus": 500,
                                "erros": [
                                    "Erro interno do servidor"
                                ]
                            }
                            """
                )
            )
        )
    })
    public ResponseEntity<DefaultResponse<List<ScriptResponse>>> getAllScripts();

    @PostMapping("/script/{id}/vote")
    @Operation(
        summary = "Script",
        description = "Operação para votar em um roteiro e avanço de status"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Voto registrado com sucesso.",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    value =  """
                            [
                                {
                                    "httpStatus": 200,
                                    "resultData": {
                                        "id": 1,
                                        "name": "O velho oeste",
                                        "content": "Então os xerifes apontaram a arma e atiraram.",
                                        "status": "aguardando_analise",
                                        "creationDate": "2023-10-01T12:00:00",
                                        "owner": {
                                            "email": "owner@cooperfilme.com",
                                            "name": "Owner Name",
                                            "phone": "123456789"
                                        },
                                        "creator": {
                                            "email": "creator@client.com",
                                            "name": "Creator Name",
                                            "phone": "987654321"
                                        }
                                    }
                                }
                            ]
                            """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Requisição inválida. Verifique os dados enviados.",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    value =  """
                            {
                                "httpStatus": 400,
                                "erros": [
                                    "vote: O voto é obrigatório;"
                                ]
                            }
                            """
                )
            )
        ),
        @ApiResponse(
            responseCode = "403", 
            description = "Não permitido.",
            content = @Content(schema = @Schema(hidden = true))
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Roteiro não encontrado.",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    value =  """
                            {
                                "httpStatus": 404,
                                "erros": [
                                    "Roteiro com o ID 1 não encontrado "
                                ]
                            }
                            """
                )
            )
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Erro Interno do Servidor.",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(
                    value =  """
                            {
                                "httpStatus": 500,
                                "erros": [
                                    "Erro interno do servidor"
                                ]
                            }
                            """
                )
            )
        )
    })
    public ResponseEntity<DefaultResponse<Void>> voteScript(@PathVariable Integer id, @RequestBody @Valid VoteScriptRequest request);

}
