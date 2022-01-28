package com.backend.hallodos.repository;

import java.util.List;

import com.backend.hallodos.model.Schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query(value = "INSERT INTO `schedule`( `dosen_id`, `mhs_id`) VALUES ('[?1]','[?2]')", nativeQuery = true)
    public Schedule insertDataIncludeForeign(Long dosenId, long mhsId);

    @Query(value = "Select * from schedule where dosen_id=?1 AND status='Waiting'", nativeQuery = true)
	public List<Schedule> findByDosenId(long idDosennya);

    @Query(value = "Select * from schedule where dosen_id=?1 AND status='Accepted'", nativeQuery = true)
	public List<Schedule> findByDosenIdAccepted(long idMahanya);

    @Query(value = "Select * from schedule where mhs_id=?1 AND status='Waiting'", nativeQuery = true)
	public List<Schedule> findByMahaIdWaiting(long idMahanya);

    @Query(value = "Select * from schedule where mhs_id=?1 AND status='Accepted'", nativeQuery = true)
	public List<Schedule> findByMahaIdAccepted(long idMahanya);

    @Query(value = "Select * from schedule where dosen_id=?1 AND mhs_id=?2", nativeQuery = true)
    public Schedule findByForeignId(long idDosenTsb, long idMhsTsb);

    @Query(value = "Select * from schedule where dosen_id=?1 AND mhs_id=?2", nativeQuery = true)
    public List<Schedule> findByDsnIdAndMhsId(long idDsn, long mhsId);

    // @Query(value = "select * from schedule where id = 1", nativeQuery = true)
    // public Schedule findById();

}
