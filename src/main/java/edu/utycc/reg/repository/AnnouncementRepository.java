package edu.utycc.reg.repository;

import edu.utycc.reg.entity.Announcement;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.time.LocalDateTime;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    List<Announcement> findByActiveTrueAndPublishDateBeforeAndExpiryDateAfter(
            LocalDateTime now1, LocalDateTime now2);

    Page<Announcement> findByTitleContainingIgnoreCaseOrPdfFileNameContainingIgnoreCase(String title,
            String pdfFileName, Pageable pageable);

    @Query("""
            SELECT a FROM Announcement a
            WHERE a.active = TRUE
              AND a.publishDate <= :now
              AND (a.expiryDate IS NULL OR a.expiryDate >= :now)
            ORDER BY a.publishDate DESC
            """)
    List<Announcement> findActivePublished(@Param("now") LocalDateTime now, Pageable pageable);

    @Query("SELECT a FROM Announcement a " +
            "WHERE a.active = true " +
            "AND a.publishDate <= :now " +
            "AND (a.expiryDate IS NULL OR a.expiryDate >= :now) " +
            "ORDER BY a.publishDate DESC")
    Page<Announcement> findActiveAnnouncements(Pageable pageable, @Param("now") LocalDateTime now);

}
