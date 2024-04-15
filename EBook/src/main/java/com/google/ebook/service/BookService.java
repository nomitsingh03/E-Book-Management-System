package com.google.ebook.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.ebook.entity.BookDetails;
import com.google.ebook.repository.BookRepository;


@Service
public class BookService {
	
	@Autowired
	private BookRepository bookRepo;
	

	public String uploadImage(String path, MultipartFile file, String bname) throws IOException {
		// File name
		String name = bname+ file.getOriginalFilename();

		// full path

		String filePath = path + File.separator + name;
		// create folder
		System.out.println(path);
		File f = new File(path);
		if (!f.exists()) {
			f.mkdir();
		}
		// file copy
		Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
		System.out.println(file.getInputStream());

		return name;
	}
	
public void deleteImage(String path, BookDetails bookDetails) {
		
	if(bookDetails.getBookImageName()!=null) {
		File delete = new File(path,bookDetails.getBookImageName());
		delete.delete();
	}
	}
	
	public List<BookDetails> getAllBooks(){
		return bookRepo.findAll();
	}
	
	public BookDetails getBookById(int id) {
		// TODO Auto-generated method stub
		return bookRepo.findById(id).get();
	}
	

	public boolean deleteBooks(int id) {
		// TODO Auto-generated method stub
		
		BookDetails book = bookRepo.findById(id).get();
		if(book!=null) 
		{
			bookRepo.delete(book);
			return true;
		}
		return false;
	}
	
	
	public List<BookDetails> getRecentBooks() {
		// TODO Auto-generated method stub
		List<BookDetails> list = new ArrayList<>();
		List<BookDetails> bookDetails = new ArrayList<>();
		list = bookRepo.getRecentBooks("Active");
		Iterator<BookDetails> it = list.iterator();
		int i=1;
		while(it.hasNext() && i<=4)
		{
			bookDetails.add(it.next());
			i++;
		}
		return bookDetails;
 
	}
	
	public List<BookDetails> getNewBooks() {
		// TODO Auto-generated method stub
		List<BookDetails> list = new ArrayList<>();
		List<BookDetails> bookDetails = new ArrayList<>();
		list = bookRepo.getNewBooks("Active", "New");
		Iterator<BookDetails> it = list.iterator();
		int i=1;
		while(it.hasNext() && i<=4)
		{
			bookDetails.add(it.next());
			i++;
		}
		return bookDetails;
 
	}
	
	public List<BookDetails> getOldBooks() {
		// TODO Auto-generated method stub
		List<BookDetails> list = new ArrayList<>();
		List<BookDetails> bookDetails = new ArrayList<>();
		list = bookRepo.getOldBooks("Old");
		Iterator<BookDetails> it = list.iterator();
		int i=1;
		while(it.hasNext() && i<=4)
		{
			bookDetails.add(it.next());
			i++;
		}
		return bookDetails;
 
	}
	
	public List<BookDetails> getComputerBooks() {
		// TODO Auto-generated method stub
		List<BookDetails> list = new ArrayList<>();
		List<BookDetails> bookDetails = new ArrayList<>();
		list = bookRepo.findByBookCategory("ComputerFundamentals");
		Iterator<BookDetails> it = list.iterator();
		int i=1;
		while(it.hasNext() && i<=4)
		{
			bookDetails.add(it.next());
			i++;
		}
		return bookDetails;
 
	}
	
	public List<BookDetails> getCommunicationBooks() {
		// TODO Auto-generated method stub
		List<BookDetails> list = new ArrayList<>();
		List<BookDetails> bookDetails = new ArrayList<>();
		list = bookRepo.findByBookCategory("communication");
		Iterator<BookDetails> it = list.iterator();
		int i=1;
		while(it.hasNext() && i<=4)
		{
			bookDetails.add(it.next());
			i++;
		}
		return bookDetails;
 
	}
	
	public List<BookDetails> getProgrammingBooks() {
		// TODO Auto-generated method stub
		List<BookDetails> list = new ArrayList<>();
		List<BookDetails> bookDetails = new ArrayList<>();
		list = bookRepo.findByBookCategory("programming");
		Iterator<BookDetails> it = list.iterator();
		int i=1;
		while(it.hasNext() && i<=4)
		{
			bookDetails.add(it.next());
			i++;
		}
		return bookDetails;
 
	}
	
	public List<BookDetails> getAllRecentBooks() {
		// TODO Auto-generated method stub
		return bookRepo.getRecentBooks("Active");
	}
	
	public List<BookDetails> getAllNewBooks(){
		return bookRepo.getNewBooks("Active", "New");
	}

	public List<BookDetails> getAllOldBooks() {
		return bookRepo.getOldBooks("Old");
	}
}
