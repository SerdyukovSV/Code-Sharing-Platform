package platform.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import platform.model.Snippet;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SnippetRepository extends JpaRepository<Snippet, Long> {

    Optional<Snippet> findByUuid(UUID uuid);

    Page<Snippet> findAllByRestrictTimeAndRestrictView(Pageable pageable, boolean restrictTime, boolean restrictView);
}
