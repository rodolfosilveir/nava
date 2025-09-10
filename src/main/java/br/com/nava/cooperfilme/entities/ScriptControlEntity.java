package br.com.nava.cooperfilme.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@Table(name = "script_control")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ScriptControlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_script", nullable = false)
    private Integer idScript;

    @Column(name = "in_analysis_status")
    private Boolean inAnalysisStatus;

    @Column(name = "in_analysis_date")
    private LocalDateTime inAnalysisDate;

    @Column(name = "in_analysis_by_id")
    private UUID inAnalysisById;

    @Column(name = "in_analysis_reason")
    private String inAnalysisReason;

    @Column(name = "pending_review_status")
    private Boolean pendingReviewStatus;

    @Column(name = "pending_review_date")
    private LocalDateTime pendingReviewDate;

    @Column(name = "pending_review_by_id")
    private UUID pendingReviewById;

    @Column(name = "reviewing_status")
    private Boolean reviewingStatus;

    @Column(name = "reviewing_date")
    private LocalDateTime reviewingDate;

    @Column(name = "reviewing_reason")
    private String reviewingReason;

    @Column(name = "pending_approval_status")
    private Boolean pendingApprovalStatus;

    @Column(name = "pending_approval_date")
    private LocalDateTime pendingApprovalDate;

    @Column(name = "approved_vote_1")
    private Boolean approvedVote1;

    @Column(name = "approved_vote_1_by_id")
    private UUID approvedVote1ById;

    @Column(name = "approving_status")
    private Boolean approvingStatus;

    @Column(name = "approving_date")
    private LocalDateTime approvingDate;    

    @Column(name = "approved_vote_2")
    private Boolean approvedVote2;

    @Column(name = "approved_vote_2_by_id")
    private UUID approvedVote2ById;

    @Column(name = "approved_vote_3")
    private Boolean approvedVote3;

    @Column(name = "approved_vote_3_by_id")
    private UUID approvedVote3ById;
    
}
