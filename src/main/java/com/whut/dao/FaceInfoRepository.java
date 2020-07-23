package com.whut.dao;


import com.whut.dao.entity.FaceInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FaceInfoRepository extends JpaRepository<FaceInfo,Integer> {

    FaceInfo findByUsername(String username);
    List<FaceInfo> findAll();

}
