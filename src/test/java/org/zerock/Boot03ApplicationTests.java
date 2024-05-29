package org.zerock;

import com.querydsl.core.BooleanBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.domain.Board;
import org.zerock.domain.QBoard;
import org.zerock.persistence.BoardRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@SpringBootTest
class Boot03ApplicationTests {

    @Autowired
    private BoardRepository repo;

    @Test
    void contextLoads() {
    }

    @Test
    void testInsert200() {

        for (int i = 0; i <= 200; i++) {
            Board board = new Board();
            board.setTitle("제목..." + i);
            board.setContent("내용 ...." + i + " 채우기 ");
            board.setWriter("user0" + (i % 10));
            repo.save(board);
        }
    }
    @Test
    public void testByTitle() {

        // Java 8
        repo.findBoardByTitle("제목...177")
                .forEach(board -> System.out.println(board));

    }

    @Test
    void testByWriter() {
        Collection<Board> results = repo.findBoardByWriter("user00");

        results.forEach(
                board -> System.out.println(board));
    }
    @Test
    void testByWriterContaining() {
        Collection<Board> results = repo.findByWriterContaining("05");
        // for 루프 대신 forEach
        results.forEach(board -> System.out.println("board = " + board));
    }
    @Test
    void testByTitleAndBno() {

        Collection<Board> results = repo.findByTitleContainingAndBnoGreaterThan("5", 50L);
        results.forEach(board -> System.out.println("board = " + board));
    }
    @Test
    void testBnoOrderBy() {
        
        Collection<Board> results = repo.findByBnoGreaterThanOrderByBnoDesc(90L);
        results.forEach(board -> System.out.println("board = " + board));
    }
    @Test
    void testBnoOrderByPaging() {

        Pageable paging = PageRequest.of(0, 10);

        Collection<Board> results = repo.findByBnoGreaterThanOrderByBnoDesc(1L , paging);

        results.forEach(board -> System.out.println("board = " + board));
    }
   // List<T>
   /* @Test
    void testBnoPagingSortGreater() {

        Pageable paging = PageRequest.of(0, 10, Sort.Direction.ASC, "bno");

        Collection<Board> results = repo.findByBnoGreaterThanOrderByBnoDesc(0L, paging);
        results.forEach(board -> System.out.println("board = " + board));
    }*/

    // Page<T>
    @Test
    void testBnoPagingSortGreater() {

        Pageable paging = PageRequest.of(0, 10, Sort.Direction.ASC, "bno");

        Page<Board> result = repo.findByBnoGreaterThan(0L, paging);

        System.out.println("PAGE SIZE :: " + result.getSize());
        System.out.println("TOTAL PAGES :: " + result.getTotalPages());
        System.out.println("TOTAL COUNT :: " + result.getTotalElements());
        System.out.println("NEXT :: " + result.nextPageable());
        
        List<Board> list = result.getContent();
        list.forEach(board -> System.out.println("board = " + board));
    }
    @Test
    void testByTitle2() {

        repo.findByTitle("17")
                .forEach(board -> System.out.println("board = " + board));
    }

    @Test
    void testByContent() {
        repo.findByContent("170")
                .forEach(board -> System.out.println("board = " + board));
    }
    @Test
    void testByWriter2() {
        repo.findByWriter("user00")
                .forEach(board -> System.out.println("board = " + board));
    }
    @Test
    void testByTitle17() {
        repo.findByTitle2("17")
                .forEach(arr -> System.out.println(Arrays.toString(arr)));
    }
    @Test
    void testByTitle3() {
        repo.findByTitle3("17")
                .forEach(arr -> System.out.println(Arrays.toString(arr)));
    }
    @Test
    void testByPaging() {

        Pageable pageable = PageRequest.of(0 , 10);

        repo.findByPage(pageable).forEach(board -> System.out.println("board = " + board));
    }
    @Test
    void testPredicate() {

        String type = "t";
        String keyword = "17";

        BooleanBuilder builder = new BooleanBuilder();

        QBoard board = QBoard.board;

        if(type.equals("t")){
            builder.and(board.title.like("%" + keyword + "%"));
        }

        // bno > 0
        builder.and(board.bno.gt(0L));

        Pageable pageable = PageRequest.of(0, 10);

        Page<Board> result = repo.findAll(builder , pageable);

        System.out.println("PAGE SIZE : " + result.getSize());
        System.out.println("TOTAL PAGES : " + result.getTotalPages());
        System.out.println("TOTAL COUNT : " + result.nextPageable());

       List<Board> list = result.getContent();

       list.forEach(b -> System.out.println(b));
    }
}
