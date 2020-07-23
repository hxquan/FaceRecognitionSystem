package com.whut.dao;


import com.whut.dao.entity.PunchRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PunchRecordRepository extends JpaRepository<PunchRecord,Integer> {

    PunchRecord findById(int id);
    List<PunchRecord> findAllByUsername(String username);
    List<PunchRecord> findAllByUsernameAndDate(String username, Date date);

}
