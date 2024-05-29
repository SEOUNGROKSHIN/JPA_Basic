package org.zerock.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.zerock.domain.Board;

import java.util.Collection;
import java.util.List;

public interface BoardRepository extends CrudRepository<Board, Long>, QuerydslPredicateExecutor<Board> {

     List<Board> findBoardByTitle(String title);

     List<Board> findBoardByWriter(String writer);

    // 작성자에 대한 like % 키워드 %
    Collection<Board> findByWriterContaining(String writer);

    // OR 조건의 처리
    Collection<Board> findByTitleOrContentContaining(String title, String content);

    // Title Like % ? % AND BNO > ?
    Collection<Board> findByTitleContainingAndBnoGreaterThan(String keyword , Long num);

    // bno > ? ORDER BY Bno Desc
    Collection<Board> findByBnoGreaterThanOrderByBnoDesc(Long bno);

    // 기본적인 페이징 처리
    List<Board> findByBnoGreaterThanOrderByBnoDesc(Long bno, Pageable paging);

    // List<T> 타입
//    List<Board> findByBnoGreaterThan(Long bno, Pageable paging);

    //Page<T> 타입
    Page<Board> findByBnoGreaterThan(Long bno, Pageable paging);

    @Query("SELECT b FROM Board b WHERE b.title LIKE %?1% AND b.bno > 0 ORDER BY b.bno DESC")
    List<Board> findByTitle(String title);

    @Query("SELECT b FROM Board b WHERE b.content LIKE %:content% AND b.bno > 0 ORDER BY b.bno DESC")
    List<Board> findByContent(@Param("content") String content);

    @Query("SELECT b FROM #{#entityName} b WHERE b.writer LIKE %?1% AND b.bno > 0 ORDER BY b.bno DESC")
    List<Board> findByWriter(String writer);

    @Query("SELECT b.bno, b.title, b.writer, b.regdate " +
            "FROM Board b WHERE b.title LIKE %?1% and b.bno > 0 ORDER BY b.bno DESC")
    List<Object[]> findByTitle2(String title);

    @Query(value = "select bno, title, writer " +
            "from tbl_boards " +
            "where title like concat('%', ?1, '%') " +
            "and bno > 0 " +
            "order by bno desc", nativeQuery = true)
    List<Object[]> findByTitle3(String title);

    // @Query와 Paging 처리/정렬
    @Query("SELECT B FROM Board B WHERE B.bno > 0 ORDER BY B.bno DESC")
    Page<Board> findByPage(Pageable pageable);
}
