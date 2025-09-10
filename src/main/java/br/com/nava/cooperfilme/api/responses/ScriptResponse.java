package br.com.nava.cooperfilme.api.responses;

import java.time.LocalDateTime;
import java.util.List;

import br.com.nava.cooperfilme.repositories.ScriptRepository.ScriptDetails;
import lombok.Builder;

@Builder
public record ScriptResponse(
    Integer id,
    String name,
    String content,
    String status,
    LocalDateTime creationDate,
    UserData owner,
    UserData creator,
    ReasonData analistReason,
    ReasonData reviewerReason,
    List<ApproveData> approvers

) {
    public record UserData(
        String email,
        String name,
        String phone
    ){} 

    public record ReasonData(
        UserData userReason,
        String reason
    ){}

    public record ApproveData(
        UserData userReason,
        Boolean approve
    ){}

    public static ScriptResponse fromDetails(ScriptDetails details){
        return ScriptResponse.builder()
            .id(details.getId())
            .name(details.getName())
            .content(details.getContent())
            .status(details.getStatus())
            .creationDate(details.getCreationDate())
            .owner(new ScriptResponse.UserData(
                details.getEmailOwner(),
                details.getNameOwner(),
                details.getPhoneOwner()
            ))
            .creator(new ScriptResponse.UserData(
                details.getEmailCreator(),
                details.getNameCreator(),
                details.getPhoneCreator()
            ))
            .analistReason(new ScriptResponse.ReasonData(
                new ScriptResponse.UserData(
                    details.getEmailAnalist(),
                    details.getNameAnalist(),
                    details.getPhoneAnalist()
            ),
            details.getAnalysisReason()
            ))
            .reviewerReason(new ScriptResponse.ReasonData(
                new ScriptResponse.UserData(
                    details.getEmailReviewer(),
                    details.getNameReviewer(),
                    details.getPhoneReviewer()
            ),
            details.getReviewerReason()
            ))

            .approvers(List.of(
                new ScriptResponse.ApproveData(
                    new ScriptResponse.UserData(
                    details.getEmailApprovedVote1By(),
                    details.getNameApprovedVote1By(),
                    details.getPhoneApprovedVote1By()), details.getApproveVote1By()
                ),

                new ScriptResponse.ApproveData(
                    new ScriptResponse.UserData(
                    details.getEmailApprovedVote2By(),
                    details.getNameApprovedVote2By(),
                    details.getPhoneApprovedVote2By()), details.getApproveVote2By()
                ),

                new ScriptResponse.ApproveData(
                    new ScriptResponse.UserData(
                    details.getEmailApprovedVote3By(),
                    details.getNameApprovedVote3By(),
                    details.getPhoneApprovedVote3By()), details.getApproveVote3By()
                )
            ))
        .build();
    }
}
