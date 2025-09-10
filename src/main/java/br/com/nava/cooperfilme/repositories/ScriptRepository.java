package br.com.nava.cooperfilme.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.nava.cooperfilme.entities.ScriptEntity;

@Repository
public interface ScriptRepository extends JpaRepository<ScriptEntity, String>{

    Optional<ScriptEntity> findById(Integer id);

    interface ScriptDetails {
        Integer getId();
        String getName();
        String getContent();
        String getStatus();
        LocalDateTime getCreationDate();
        String getEmailCreator();
        String getNameCreator();
        String getPhoneCreator();
        String getEmailOwner();
        String getNameOwner();
        String getPhoneOwner();
        String getEmailAnalist();
        String getNameAnalist();
        String getPhoneAnalist();
        String getAnalysisReason();
        String getEmailReviewer();
        String getNameReviewer();
        String getPhoneReviewer();
        String getReviewerReason();
        String getEmailApprovedVote1By();
        String getNameApprovedVote1By();
        String getPhoneApprovedVote1By();
        Boolean getApproveVote1By();
        String getEmailApprovedVote2By();
        String getNameApprovedVote2By();
        String getPhoneApprovedVote2By();
        Boolean getApproveVote2By();
        String getEmailApprovedVote3By();
        String getNameApprovedVote3By();
        String getPhoneApprovedVote3By();
        Boolean getApproveVote3By();
    }
 
    @Query(value = """
            SELECT 
                s.id,
                s.name,
                s.content,
                s.status,
                s.creation_date AS creationDate,

                uc.login AS emailCreator,
                uc.name  AS nameCreator, 
                uc.phone AS phoneCreator,

                uo.login AS emailOwner,
                uo.name  AS nameOwner, 
                uo.phone AS phoneOwner,

                analist.login AS emailAnalist,
                analist.name AS nameAnalist,
                analist.phone AS phoneAnalist,
                sc.in_analysis_reason AS analysisReason,

                reviewer.login AS emailReviewer,
                reviewer.name AS nameReviewer,
                reviewer.phone AS phoneReviewer,
                sc.reviewing_reason AS reviewerReason,

                ua1.login AS emailApprovedVote1By,
                ua1.name  AS nameApprovedVote1By,
                ua1.phone AS phoneApprovedVote1By,
                sc.approved_vote_1 AS approveVote1By,

                ua2.login AS emailApprovedVote2By,
                ua2.name  AS nameApprovedVote2By,
                ua2.phone AS phoneApprovedVote2By,
                sc.approved_vote_2 AS approveVote2By,

                ua3.login AS emailApprovedVote3By,
                ua3.name  AS nameApprovedVote3By,
                ua3.phone AS phoneApprovedVote3By,
                sc.approved_vote_3 AS approveVote3By

            FROM script s

            LEFT JOIN users uc ON uc.id = s.id_user_creator
            LEFT JOIN users uo ON uo.id = s.id_user_owner

            LEFT JOIN script_control sc ON sc.id_script = s.id

            LEFT JOIN users analist ON analist.id = sc.in_analysis_by_id
            LEFT JOIN users reviewer ON reviewer.id = sc.pending_review_by_id
            LEFT JOIN users ua1 ON ua1.id = sc.approved_vote_1_by_id
            LEFT JOIN users ua2 ON ua2.id = sc.approved_vote_2_by_id
            LEFT JOIN users ua3 ON ua3.id = sc.approved_vote_3_by_id
            WHERE 
                s.id = :id
            """, nativeQuery = true)
    Optional<ScriptDetails> findScriptWithDetailsById(@Param("id") Integer id);

    @Query(value = """
            SELECT 
                s.id,
                s.name,
                s.content,
                s.status,
                s.creation_date AS creationDate,

                uc.login AS emailCreator,
                uc.name  AS nameCreator, 
                uc.phone AS phoneCreator,

                uo.login AS emailOwner,
                uo.name  AS nameOwner, 
                uo.phone AS phoneOwner,

                analist.login AS emailAnalist,
                analist.name AS nameAnalist,
                analist.phone AS phoneAnalist,
                sc.in_analysis_reason AS analysisReason,

                reviewer.login AS emailReviewer,
                reviewer.name AS nameReviewer,
                reviewer.phone AS phoneReviewer,
                sc.reviewing_reason AS reviewerReason,

                ua1.login AS emailApprovedVote1By,
                ua1.name  AS nameApprovedVote1By,
                ua1.phone AS phoneApprovedVote1By,
                sc.approved_vote_1 AS approveVote1By,

                ua2.login AS emailApprovedVote2By,
                ua2.name  AS nameApprovedVote2By,
                ua2.phone AS phoneApprovedVote2By,
                sc.approved_vote_2 AS approveVote2By,

                ua3.login AS emailApprovedVote3By,
                ua3.name  AS nameApprovedVote3By,
                ua3.phone AS phoneApprovedVote3By,
                sc.approved_vote_3 AS approveVote3By

            FROM script s

            LEFT JOIN users uc ON uc.id = s.id_user_creator
            LEFT JOIN users uo ON uo.id = s.id_user_owner

            LEFT JOIN script_control sc ON sc.id_script = s.id

            LEFT JOIN users analist ON analist.id = sc.in_analysis_by_id
            LEFT JOIN users reviewer ON reviewer.id = sc.pending_review_by_id
            LEFT JOIN users ua1 ON ua1.id = sc.approved_vote_1_by_id
            LEFT JOIN users ua2 ON ua2.id = sc.approved_vote_2_by_id
            LEFT JOIN users ua3 ON ua3.id = sc.approved_vote_3_by_id
            """, nativeQuery = true)
    List<ScriptDetails> findScriptsWithDetails();

}
