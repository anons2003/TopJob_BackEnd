package com.SWP.WebServer.repository;

import com.SWP.WebServer.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {
    Optional<Template> findByName(String name);
}
