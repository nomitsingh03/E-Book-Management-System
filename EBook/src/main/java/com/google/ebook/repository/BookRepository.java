package com.google.ebook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.google.ebook.entity.BookDetails;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface BookRepository extends JpaRepository<BookDetails, Integer>{
	
	@Query("select b from BookDetails b where b.bookStatus=:bookStatus order by b.book_id DESC")
	List<BookDetails> getRecentBooks(@Param("bookStatus")String bookStatus);
	
	@Query("select b from BookDetails b where b.bookStatus=:bookStatus and b.bookType=:bookType")
	List<BookDetails> getNewBooks(@Param("bookStatus") String bookStatus , @Param("bookType") String bookType);
	
	@Query("select b from BookDetails b where b.bookType=:bookType")
	List<BookDetails> getOldBooks( @Param("bookType") String bookType);
	
	List<BookDetails> findByBookCategory(String bookCategory);
	
}
