package br.com.nava.cooperfilme.repositories;

import br.com.nava.cooperfilme.repositories.ScriptRepository.ScriptDetails;

import java.time.LocalDateTime;

public class ScriptDetailsMock implements ScriptDetails {

    private Integer id;
    private String name;
    private String content;
    private String status;
    private LocalDateTime creationDate;

    private String emailCreator;
    private String nameCreator;
    private String phoneCreator;

    private String emailOwner;
    private String nameOwner;
    private String phoneOwner;

    private String emailAnalist;
    private String nameAnalist;
    private String phoneAnalist;
    private String analysisReason;

    private String emailReviewer;
    private String nameReviewer;
    private String phoneReviewer;
    private String reviewerReason;

    private String emailApprovedVote1By;
    private String nameApprovedVote1By;
    private String phoneApprovedVote1By;
    private Boolean approveVote1By;

    private String emailApprovedVote2By;
    private String nameApprovedVote2By;
    private String phoneApprovedVote2By;
    private Boolean approveVote2By;

    private String emailApprovedVote3By;
    private String nameApprovedVote3By;
    private String phoneApprovedVote3By;
    private Boolean approveVote3By;

    @Override public Integer getId() { return id; }
    @Override public String getName() { return name; }
    @Override public String getContent() { return content; }
    @Override public String getStatus() { return status; }
    @Override public LocalDateTime getCreationDate() { return creationDate; }

    @Override public String getEmailCreator() { return emailCreator; }
    @Override public String getNameCreator() { return nameCreator; }
    @Override public String getPhoneCreator() { return phoneCreator; }

    @Override public String getEmailOwner() { return emailOwner; }
    @Override public String getNameOwner() { return nameOwner; }
    @Override public String getPhoneOwner() { return phoneOwner; }

    @Override public String getEmailAnalist() { return emailAnalist; }
    @Override public String getNameAnalist() { return nameAnalist; }
    @Override public String getPhoneAnalist() { return phoneAnalist; }
    @Override public String getAnalysisReason() { return analysisReason; }

    @Override public String getEmailReviewer() { return emailReviewer; }
    @Override public String getNameReviewer() { return nameReviewer; }
    @Override public String getPhoneReviewer() { return phoneReviewer; }
    @Override public String getReviewerReason() { return reviewerReason; }

    @Override public String getEmailApprovedVote1By() { return emailApprovedVote1By; }
    @Override public String getNameApprovedVote1By() { return nameApprovedVote1By; }
    @Override public String getPhoneApprovedVote1By() { return phoneApprovedVote1By; }
    @Override public Boolean getApproveVote1By() { return approveVote1By; }

    @Override public String getEmailApprovedVote2By() { return emailApprovedVote2By; }
    @Override public String getNameApprovedVote2By() { return nameApprovedVote2By; }
    @Override public String getPhoneApprovedVote2By() { return phoneApprovedVote2By; }
    @Override public Boolean getApproveVote2By() { return approveVote2By; }

    @Override public String getEmailApprovedVote3By() { return emailApprovedVote3By; }
    @Override public String getNameApprovedVote3By() { return nameApprovedVote3By; }
    @Override public String getPhoneApprovedVote3By() { return phoneApprovedVote3By; }
    @Override public Boolean getApproveVote3By() { return approveVote3By; }

    public static ScriptDetailsMock create() {
        ScriptDetailsMock mock = new ScriptDetailsMock();
        mock.id = 1;
        mock.name = "Mock Script";
        mock.content = "Conteúdo fictício";
        mock.status = "em_analise";
        mock.creationDate = LocalDateTime.now();

        mock.emailCreator = "creator@email.com";
        mock.nameCreator = "Creator";
        mock.phoneCreator = "1111-1111";

        mock.emailOwner = "owner@email.com";
        mock.nameOwner = "Owner";
        mock.phoneOwner = "2222-2222";

        mock.emailAnalist = "analist@email.com";
        mock.nameAnalist = "Analist";
        mock.phoneAnalist = "3333-3333";
        mock.analysisReason = "Razão fictícia";

        mock.emailReviewer = "reviewer@email.com";
        mock.nameReviewer = "Reviewer";
        mock.phoneReviewer = "4444-4444";
        mock.reviewerReason = "Comentário fictício";

        mock.emailApprovedVote1By = "vote1@email.com";
        mock.nameApprovedVote1By = "Vote1";
        mock.phoneApprovedVote1By = "5555-5555";
        mock.approveVote1By = true;

        mock.emailApprovedVote2By = "vote2@email.com";
        mock.nameApprovedVote2By = "Vote2";
        mock.phoneApprovedVote2By = "6666-6666";
        mock.approveVote2By = false;

        mock.emailApprovedVote3By = "vote3@email.com";
        mock.nameApprovedVote3By = "Vote3";
        mock.phoneApprovedVote3By = "7777-7777";
        mock.approveVote3By = true;

        return mock;
    }
}

