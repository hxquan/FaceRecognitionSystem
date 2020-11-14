package com.whut.dao;


import com.whut.dao.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    User findById(int id);
    User findByPhone(String phone);
    User findByUsername(String username);

    
}
