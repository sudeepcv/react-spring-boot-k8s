package com.sudeepcv.SpringbootApiServer;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserTable, Long> {

    // Custom query methods can be added here
    UserTable findByUsername(String username);

    UserTable findByMobileNumber(String mobileNumber);
}