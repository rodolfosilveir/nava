CREATE TABLE script_control (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_script INT NOT NULL,

    -- em análise
    in_analysis_status BOOLEAN DEFAULT false,
    in_analysis_date   TIMESTAMP,
    in_analysis_by_id  UUID,
    in_analysis_reason TEXT,

    -- aguardando revisão
    pending_review_status BOOLEAN DEFAULT false,
    pending_review_date   TIMESTAMP,
    pending_review_by_id UUID,

    -- em revisão
    reviewing_status BOOLEAN DEFAULT false,
    reviewing_date   TIMESTAMP,
    reviewing_reason TEXT,

    -- aguardando aprovação
    pending_approval_status BOOLEAN,
    pending_approval_date   TIMESTAMP,
    approved_vote_1 BOOLEAN,
    approved_vote_1_by_id UUID,

    -- em aprovação
    approving_status BOOLEAN,
    approving_date   TIMESTAMP,
    approved_vote_2 BOOLEAN,
    approved_vote_2_by_id UUID,
    approved_vote_3 BOOLEAN,
    approved_vote_3_by_id UUID,

    CONSTRAINT fk_script FOREIGN KEY (id_script) REFERENCES script(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_in_analysis FOREIGN KEY (in_analysis_by_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_pending_review FOREIGN KEY (pending_review_by_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_approved_vote_1 FOREIGN KEY (approved_vote_1_by_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_approved_vote_2 FOREIGN KEY (approved_vote_2_by_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_approved_vote_3 FOREIGN KEY (approved_vote_3_by_id) REFERENCES users(id) ON DELETE CASCADE
);
