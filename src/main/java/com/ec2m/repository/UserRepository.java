package com.ec2m.repository;

import com.ec2m.model.EntityUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
@Repository
public interface UserRepository extends JpaRepository<EntityUser,Long> {


    Optional<EntityUser> findByUsernameAndDeleted(String username,boolean deleted);

    Optional<EntityUser> findByEmailAndDeleted(String email,boolean deleted);

    List<EntityUser> findAll();

    Optional<EntityUser> findByIdAndDeleted(Long userId, boolean deleted);
}
