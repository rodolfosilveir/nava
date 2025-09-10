package br.com.nava.cooperfilme.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.nava.cooperfilme.entities.ScriptControlEntity;

@Repository
public interface ScriptControlRepository  extends JpaRepository<ScriptControlEntity, String>{

    @Query(value = """
            SELECT 
                *
            FROM 
                script_control sc 
            WHERE 
                sc.id_script = :id
            """, nativeQuery = true)
    ScriptControlEntity findByScriptId(Integer id);
    
}
