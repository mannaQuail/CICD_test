package com.hacker.hacker.repository;

import com.hacker.hacker.model.Transcript;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface TranscriptRepository extends JpaRepository<Transcript, Long> {
}
