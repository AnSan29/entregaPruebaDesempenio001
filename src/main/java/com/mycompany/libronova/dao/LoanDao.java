package com.mycompany.libronova.dao;

import com.mycompany.libronova.model.Loan;
import java.util.List;
import java.util.Optional;

public interface LoanDao {

    Optional<Loan> findById(Long id);
    List<Loan> findAll();
    List<Loan> findActivos();
    List<Loan> findVencidos();
    List<Loan> findByMember(Long memberId);

}
