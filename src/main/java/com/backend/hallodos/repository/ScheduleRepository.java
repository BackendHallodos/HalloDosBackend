package com.backend.hallodos.repository;

import com.backend.hallodos.model.Schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query(value = "INSERT INTO `schedule`( `dosen_id`, `mhs_id`) VALUES ('[?1]','[?2]')", nativeQuery = true)
    public Schedule insertDataIncludeForeign(Long dosenId, long mhsId);

    // @Query(value = "select * from schedule where id = 1", nativeQuery = true)
    // public Schedule findById();

}
