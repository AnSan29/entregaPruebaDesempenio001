package com.mycompany.libronova.dao;

import com.mycompany.libronova.model.Member;
import java.util.List;
import java.util.Optional;

public interface MemberDao {

    
    Long create(Member m);
    boolean update(Member m);
    boolean deleteById(Long id);
    Optional<Member> findById(Long id);
    Optional<Member> findByEmail(String email);
    List<Member> findAll();
    List<Member> findByNameLike(String name);


}
